package smartgrid.newsimcontrol.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SmartComponentStateContainer;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.initializer.InitializationMapKeys;
import couplingToICT.initializer.PowerSpecsModificationTypes;
import smartgrid.attackersimulation.psm.DoublePSM;
import smartgrid.attackersimulation.psm.MaxPSM;
import smartgrid.attackersimulation.psm.PowerSpecsModifier;
import smartgrid.attackersimulation.psm.ZeroPSM;
import smartgrid.helper.FileSystemHelper;
import smartgrid.helper.ScenarioModelHelper;
import smartgrid.helper.TestSimulationExtensionPointHelper;
import smartgrid.impactanalysis.GraphAnalyzer;
import smartgrid.log4j.LoggingInitializer;
import smartgrid.model.test.generation.DefaultInputGenerator;
import smartgrid.model.test.generation.ITopoGenerator;
import smartgrid.model.test.generation.TrivialTopoGenerator;
import smartgrid.newsimcontrol.ReportGenerator;
import smartgrid.simcontrol.test.baselib.Constants;
import smartgrid.simcontrol.test.baselib.coupling.IAttackerSimulation;
import smartgrid.simcontrol.test.baselib.coupling.IImpactAnalysis;
import smartgrid.simcontrol.test.baselib.coupling.ITimeProgressor;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridoutput.EntityState;
import smartgridoutput.Offline;
import smartgridoutput.On;
import smartgridoutput.ScenarioResult;
import smartgridtopo.NetworkEntity;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;

public final class ReactiveSimulationController {

	private static final Logger LOG = Logger.getLogger(ReactiveSimulationController.class);

	private static String getProsumerIdOfInputPowerState(final PowerState inputPowerState) {
		String id = inputPowerState.getOwner().getId();
		if (id.endsWith("_PGN")) {
			id = id.substring(0, id.length() - 4);
		}
		return id;
	}
	public static boolean isOutage(double supply) {
		// TODO is this really a power outage?
		return supply == 0.0d;
	}
	/**
	 * @param impactInput  The outdated ImpactAnalysis input data that should be
	 *                     updated
	 * @param impactResult The result from the last iteration. This also contains
	 *                     the hacked states, which have to be transfered to
	 *                     impactInput
	 * @param powerSupply  The output of the PowerLoad analysis, which has to be
	 *                     interpreted and transfered to impactInput
	 */
	private static void updateImactAnalysisInput(final ScenarioState impactInput, final ScenarioResult impactResult,
			Map<String, Map<String, Double>> powerSupply) {

		// transfer hacked state into next input
		for (final EntityState state : impactResult.getStates()) {
			final boolean hackedState = state instanceof On && ((On) state).isIsHacked();
			final NetworkEntity owner = state.getOwner();
			for (final smartgridinput.EntityState inputEntityState : impactInput.getEntityStates()) {
				if (inputEntityState.getOwner().equals(owner)) {
					inputEntityState.setIsHacked(hackedState);
					break;
				}
			}
		}

		if (powerSupply == null) {
			LOG.error("Power Load Simulation returned null. Power supply remains unchanged.");
			return;
		}

		// transfer power supply state into next input
		for (final PowerState inputPowerState : impactInput.getPowerStates()) {
			String prosumerId = getProsumerIdOfInputPowerState(inputPowerState);

			boolean foundSupply = false;

			// search this prosumer in the node map
			for (final Map<String, Double> subNodePowerSupply : powerSupply.values()) {
				Double supply = subNodePowerSupply.get(prosumerId);
				if (supply != null) {
					inputPowerState.setPowerOutage(isOutage(supply));
					foundSupply = true;
					break;
				}
			}

			if (!foundSupply) {
				LOG.error("There is no power supply for CI " + prosumerId + ". Power supply remains unchanged.");
			}
		}
	}

	private IAttackerSimulation attackerSimulation;
	private SmartComponentStateContainer dysfunctionalcomponents;
	private FileAppender fileAppender;

	private IImpactAnalysis impactAnalsis;

	// private ScenarioState impactInputOld;
	private ScenarioState impactInput;
	private ScenarioResult impactResult;

	private ScenarioState initialState;

	private PowerSpecsModificationTypes powerDemandModificationType;

	private ITimeProgressor timeProgressor;

	// Simulation State
	private int timeStep;

	private SmartGridTopology topo;

	private String workingDirPath;

	public ReactiveSimulationController() {
		timeStep = 0;
	}

	private String determineWorkingDirPath(String initialPath) {
		initialPath = removeTrailingSeparator(initialPath);
		String currentPath = initialPath;
		int runningNumber = 0;
		while (new File(currentPath).exists()) {
			LOG.debug("Exists already: " + currentPath);

			currentPath = initialPath + runningNumber; // + '\\';
			runningNumber++;
		}
		LOG.info("Working dir is: " + currentPath);
		return currentPath;
	}

	private SmartComponentStateContainer generateSCSC(ScenarioResult impactResult) {
		var smartMeterStates = new ArrayList<String>();
		var iedStates = new ArrayList<String>();

		for (EntityState state : impactResult.getStates()) {
			if (state.getOwner() instanceof SmartMeter && state instanceof Offline) {
				smartMeterStates.add(state.getOwner().getId());
			}
		}
		return new SmartComponentStateContainer(smartMeterStates, iedStates);
	}

	public SmartComponentStateContainer getDysfunctionalcomponents() {
		return dysfunctionalcomponents;
	}

	private Set<String> getHackedSmartMeters() {
		
		var hackedSmartMeters = new HashSet<String>();
		if (impactResult != null) {
			for (EntityState state : impactResult.getStates()) {
				if (state.getOwner() instanceof SmartMeter && state instanceof On && ((On) state).isIsHacked()) {
					hackedSmartMeters.add(state.getOwner().getId());
				}
			}
		}

		return hackedSmartMeters;
	}

	public void init(String outputPath) {
		LoggingInitializer.initialize();
		LOG.debug("init reactive launch config");
		workingDirPath = determineWorkingDirPath(outputPath + File.separator + "Analyse");
		LOG.info("Output: " + outputPath);

		// add fileappender for local logs
		final Logger rootLogger = Logger.getRootLogger();
		try {
			Layout layout = ((Appender) rootLogger.getAllAppenders().nextElement()).getLayout();
			fileAppender = new FileAppender(layout, workingDirPath + File.separator + "log.log");
			rootLogger.addAppender(fileAppender);
		} catch (final IOException e) {
			throw new RuntimeException(
					"Error creating local log appender in the working directory. Most likely there are problems with access rights.");
		}
	}

	public void initModelsFromFiles(String topoPath, String inputStatePath) {

		// load models
		initialState = ScenarioModelHelper.loadInput(inputStatePath);
		impactInput = initialState;
		topo = ScenarioModelHelper.loadScenario(topoPath);
		LOG.info("Scenario input state: " + inputStatePath);
		LOG.info("Topology: " + topoPath);
	}

	public void initTopo(SmartGridTopoContainer topoContainer) {
		// generate and persist topo
		ITopoGenerator generator = new TrivialTopoGenerator();
		topo = generator.generateTopo(topoContainer);
		FileSystemHelper.saveToFileSystem(topo, workingDirPath + File.separatorChar + "generated.smartgridtopo");
		LOG.info("Topo is generated");
		// generate and persist input
		LOG.info("Input will be generated");
		DefaultInputGenerator defaultInputGenerator = new DefaultInputGenerator();
		initialState = defaultInputGenerator.generateInput(topo);
		FileSystemHelper.saveToFileSystem(initialState,
				workingDirPath + File.separatorChar + "generated.smartgridinput");
		impactInput = initialState;
		LOG.info("Input is generated");
	}

	public void loadCustomUserAnalysis(final ILaunchConfiguration launchConfig)
			throws CoreException, InterruptedException {

		attackerSimulation = TestSimulationExtensionPointHelper.findExtension(launchConfig,
				TestSimulationExtensionPointHelper.getAttackerSimulationExtensions(), Constants.ATTACKER_SIMULATION_KEY,
				IAttackerSimulation.class);
		impactAnalsis = TestSimulationExtensionPointHelper.findExtension(launchConfig,
				TestSimulationExtensionPointHelper.getImpactAnalysisExtensions(),
				Constants.IMPACT_ANALYSIS_SIMULATION_KEY, IImpactAnalysis.class);
		timeProgressor = TestSimulationExtensionPointHelper.findExtension(launchConfig,
				TestSimulationExtensionPointHelper.getProgressorExtensions(), Constants.TIME_PROGRESSOR_SIMULATION_KEY,
				ITimeProgressor.class);

		impactAnalsis.init(launchConfig);
		attackerSimulation.init(launchConfig);
		timeProgressor.init(launchConfig);

		LOG.info("Using impact analysis: " + impactAnalsis.getName());
		LOG.info("Using attacker simulation: " + attackerSimulation.getName());
		LOG.info("Using time progressor: " + timeProgressor.getName());

		if (!launchConfig.getAttribute(InitializationMapKeys.POWER_MODIFY_KEY.getDescription(), "").equals("")) {
			String powerModificationString = launchConfig
					.getAttribute(InitializationMapKeys.POWER_MODIFY_KEY.getDescription(), "");
			PowerSpecsModificationTypes powerSpecsModificationType = PowerSpecsModificationTypes
					.valueOf(powerModificationString);
			this.powerDemandModificationType = powerSpecsModificationType;
		}
	}

	public void loadDefaultAnalyses() throws CoreException {

		final List<IImpactAnalysis> impact = TestSimulationExtensionPointHelper.getImpactAnalysisExtensions();
		for (final IImpactAnalysis e : impact) {

			if (e.getName().equals("Graph Analyzer Impact Analysis")) {
				impactAnalsis = e;
				((GraphAnalyzer) impactAnalsis).initForTesting(true);
			}
		}

		final List<IAttackerSimulation> attack = TestSimulationExtensionPointHelper.getAttackerSimulationExtensions();
		for (final IAttackerSimulation e : attack) {
			if (e.getName().equals("No Attack Simulation")) {
				attackerSimulation = e;
			}
		}

		final List<ITimeProgressor> time = TestSimulationExtensionPointHelper.getProgressorExtensions();
		for (final ITimeProgressor e : time) {

			if (e.getName().equals("No Operation")) {
				timeProgressor = e;
			}
		}

		assert impactAnalsis != null;
		LOG.info("Using impact analysis: " + impactAnalsis.getName());
		assert timeProgressor != null;
		LOG.info("Using time progressor: " + timeProgressor.getName());
		assert attackerSimulation != null;
		LOG.info("Using attacker simulation: " + attackerSimulation.getName());

		this.powerDemandModificationType = PowerSpecsModificationTypes.NO_CHANGE_MODIFIER;
	}

	public PowerSpecContainer modifyPowerSpecContainer(PowerSpecContainer powerSpecContainer) {
		// TODO:Später wird für PowerDemand und PowerInfeed getrennte Strategien benutzt
		// TODO:Später wird das abhängig von powerDemandModificationType
		var hackedSmartMeters = getHackedSmartMeters();

		// modify the powerSpecs
		PowerSpecsModifier pDemandModifier = null;
		if (powerDemandModificationType.equals(PowerSpecsModificationTypes.MAX_MODIFIER)) {
			pDemandModifier = new MaxPSM();
		} else if (powerDemandModificationType.equals(PowerSpecsModificationTypes.ZERO_MODIFIER)) {
			pDemandModifier = new ZeroPSM();
		} else if (powerDemandModificationType.equals(PowerSpecsModificationTypes.DOUBLE_MODIFIER)) {
			pDemandModifier = new DoublePSM();
		} else if (powerDemandModificationType.equals(PowerSpecsModificationTypes.NO_CHANGE_MODIFIER)) {
			return powerSpecContainer;
		} else {
			throw new IllegalStateException("Unkown PowerDemandModification");
		}
		return pDemandModifier.modifyPowerSpecs(powerSpecContainer, hackedSmartMeters);

	}

	private String removeTrailingSeparator(String initialPath) {
		if (initialPath.endsWith(File.pathSeparator)) {
			return initialPath.substring(0, initialPath.length() - 1);
		}
		return initialPath;
	}

	public SmartComponentStateContainer run(PowerAssigned power) {

		Map<String, Map<String, Double>> powerSupply = null;

		LOG.info("Starting time step " + timeStep);

		topo.setId("testID");
		LOG.info(topo.getId());

		// Generates Path with default System separators
		final String timeStepPath = new File(workingDirPath + File.separator + "Zeitschritt " + timeStep).getPath();

		// get power supply
		if (power != null) {
			LinkedHashMap<String, HashMap<String, Double>> powerAssignedMap = power.getPowerAssigned();
			powerSupply = new LinkedHashMap<String, Map<String, Double>>(powerAssignedMap);
		}

		// copy the input
		// impactInputOld = EcoreUtil.copy(impactInput);

		LOG.info("Starting Impact Analysis");
		ScenarioResult impactResult = impactAnalsis.run(topo, impactInput);

		LOG.info("Converting input for impact analysis (With power supply)");
		// convert input for impact analysis
//		updateImactAnalysisInput(impactInput, impactResult, powerSupply);

		// Save input to file
		final String inputFile = new File(timeStepPath + File.separator + "PowerLoadResult.smartgridinput").getPath();
		FileSystemHelper.saveToFileSystem(impactInput, inputFile);

		LOG.info("Starting Attacker Simulation");
		impactResult = attackerSimulation.run(topo, impactResult);
		this.impactResult = impactResult;

		// save attack result to file
		final String attackResultFile = new File(
				timeStepPath + File.separator + "AttackerSimulationResult.smartgridoutput").getPath();
		FileSystemHelper.saveToFileSystem(impactResult, attackResultFile);

		LOG.info("Collecting dysfunctionalComponents");
		// get smartmeters
		dysfunctionalcomponents = generateSCSC(impactResult);
		// Save Result

		final String resultFile = new File(timeStepPath + File.separator + "ImpactResult.smartgridoutput").getPath();
		FileSystemHelper.saveToFileSystem(impactResult, resultFile);

		// generate report
		final File resultReportPath = new File(timeStepPath + File.separator + "ResultReport.csv");
		ReportGenerator.saveScenarioResult(resultReportPath, impactResult);

		// modify the scenario between time steps
		timeProgressor.progress();
		timeStep++;

		LOG.info("Finished time step " + timeStep);
		return dysfunctionalcomponents;
	}

	public void shutDown() {
		// remove file appender of this run
		if (fileAppender != null) {
			Logger.getRootLogger().removeAppender(fileAppender);
			fileAppender.close();
		}
	}
}
