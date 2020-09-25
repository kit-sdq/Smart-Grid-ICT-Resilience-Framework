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
import java.util.stream.Collectors;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.osgi.service.component.annotations.Component;

import couplingToICT.ICTElement;
import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SmartComponentStateContainer;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.initializer.InitializationMapKeys;
import couplingToICT.initializer.PowerSpecsModificationTypes;
import couplingToICT.initializer.TopoGenerationStyle;
import smartgrid.attackersimulation.psm.DoublePSM;
import smartgrid.attackersimulation.psm.MaxPSM;
import smartgrid.attackersimulation.psm.PowerSpecsModifier;
import smartgrid.attackersimulation.psm.ZeroPSM;
import smartgrid.helper.FileSystemHelper;
import smartgrid.helper.HashMapHelper;
import smartgrid.helper.ScenarioModelHelper;
import smartgrid.helper.SimulationExtensionPointHelper;
import smartgrid.log4j.LoggingInitializer;
import smartgrid.model.topo.generator.DefaultInputGenerator;
import smartgrid.model.topo.generator.ITopoGenerator;
import smartgrid.model.topo.generator.ring.RingTopoGenerator;
import smartgrid.model.topo.generator.star.StarTopoGenerator;
import smartgrid.model.topo.generator.starstar.StarStarTopoGenerator;
import smartgrid.model.topo.generator.trivial.TrivialTopoGenerator;
import smartgrid.newsimcontrol.ReportGenerator;
import smartgrid.simcontrol.test.baselib.coupling.IAttackerSimulation;
import smartgrid.simcontrol.test.baselib.coupling.IImpactAnalysis;
import smartgrid.simcontrol.test.baselib.coupling.ITimeProgressor;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridoutput.EntityState;
import smartgridoutput.Offline;
import smartgridoutput.On;
import smartgridoutput.ScenarioResult;
import smartgridtopo.ControlCenter;
import smartgridtopo.GenericController;
import smartgridtopo.InterCom;
import smartgridtopo.NetworkEntity;
import smartgridtopo.NetworkNode;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;
@Component
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
	
	private TopoGenerationStyle topoGenerationStyle;

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

	/**
	 * A method to generate a toplogy from a toplogy container
	 * the style of generation is defined in the initialization map
	 * if no style is defined, so the trivial one will be used.
	 * @param topoContainer the container of the to be generated topology
	 * @return
	 */
	public List<ICTElement> initTopo(SmartGridTopoContainer topoContainer) {
		// generate and persist topo
		ITopoGenerator generator;
		
		if (topoGenerationStyle == null)
			generator = new TrivialTopoGenerator();
		else {
			switch (topoGenerationStyle) {
				case STERN_TOPO:
					generator = new StarTopoGenerator();
					break;
				case RING_TOPO:
					generator = new RingTopoGenerator();
					break;
				case STERN_STERN_TOPO:
					generator = new StarStarTopoGenerator();
					break;
				default:
					generator = new TrivialTopoGenerator();
					break;
			}
		}
		topo = generator.generateTopo(topoContainer);
		//TODO: Wie soll es hier aussehen?
		FileSystemHelper.saveToFileSystem(topo, workingDirPath + File.separatorChar + "generated.smartgridtopo");
		LOG.info("Topo is generated");
		// generate and persist input
		DefaultInputGenerator defaultInputGenerator = new DefaultInputGenerator();
		initialState = defaultInputGenerator.generateInput(topo);
		FileSystemHelper.saveToFileSystem(initialState, workingDirPath + File.separatorChar + "generated.smartgridinput");
		impactInput = initialState;
		LOG.info("Input is generated");
		return topo.getContainsNE().stream().filter(e -> e instanceof NetworkNode || e instanceof ControlCenter || e instanceof InterCom || e instanceof GenericController).map(e-> new ICTElement(e.getId(), e.eClass().toString())).collect(Collectors.toList());
	}

	public void loadCustomUserAnalysis(Map<InitializationMapKeys, String> initMap) throws CoreException{

		//TODO:BEGIN:
		//Mazen:05.06.2020:Ad-hock till solving the problem with the registers
		//Disadvantages: 1. Dummy , 2. Must edited at every new simulation, 3.many not needed imports and dependencies
		
		
		attackerSimulation = SimulationExtensionPointHelper.findExtension(initMap,
				SimulationExtensionPointHelper.getAttackerSimulationExtensions(), InitializationMapKeys.ATTACKER_SIMULATION_KEY,
				IAttackerSimulation.class);
		impactAnalsis = SimulationExtensionPointHelper.findExtension(initMap,
				SimulationExtensionPointHelper.getImpactAnalysisExtensions(),
				InitializationMapKeys.IMPACT_ANALYSIS_SIMULATION_KEY, IImpactAnalysis.class);
		timeProgressor = SimulationExtensionPointHelper.findExtension(initMap,
				SimulationExtensionPointHelper.getProgressorExtensions(), InitializationMapKeys.TIME_PROGRESSOR_SIMULATION_KEY,
				ITimeProgressor.class);
		
//		String attackerSimulationKey = HashMapHelper.getAttribute(initMap, InitializationMapKeys.ATTACKER_SIMULATION_KEY, "");
//		switch(attackerSimulationKey) {
//			case "No Attack Simulation":
//				attackerSimulation = new NoAttackerSimulation();
//				break;
//			case "Local Hacker":
//				attackerSimulation = new LocalHacker();
//				break;
//			case "Viral Hacker":
//				attackerSimulation = new ViralHacker();
//				break;
//			default:
//				attackerSimulation = new NoAttackerSimulation();
//				break;
//		}
		
		//impactAnalsis = new GraphAnalyzer();
		//timeProgressor = new NoOperationTimeProgressor();
		//TODO:END
		
		impactAnalsis.init(initMap);
		attackerSimulation.init(initMap);
		timeProgressor.init(initMap);

		LOG.info("Using impact analysis: " + impactAnalsis.getName());
		LOG.info("Using attacker simulation: " + attackerSimulation.getName());
		LOG.info("Using time progressor: " + timeProgressor.getName());

		if (!HashMapHelper.getAttribute(initMap, InitializationMapKeys.POWER_MODIFY_KEY, "").equals("")) {
			String powerModificationString = HashMapHelper.getAttribute(initMap, InitializationMapKeys.POWER_MODIFY_KEY, "");
			PowerSpecsModificationTypes powerSpecsModificationType = PowerSpecsModificationTypes
					.valueOf(powerModificationString);
			this.powerDemandModificationType = powerSpecsModificationType;
		}
		
		if (!HashMapHelper.getAttribute(initMap, InitializationMapKeys.TOPO_GENERATION_STYLE, "").equals("")) {
			String topoGenerationString = HashMapHelper.getAttribute(initMap, InitializationMapKeys.TOPO_GENERATION_STYLE, "");
			TopoGenerationStyle topoGenerationStyle = TopoGenerationStyle
					.valueOf(topoGenerationString);
			this.topoGenerationStyle = topoGenerationStyle;
		}
	}

	
	public PowerSpecContainer modifyPowerSpecContainer(PowerSpecContainer powerSpecContainer) { 
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
		
		updateImactAnalysisInput(impactInput, impactResult, powerSupply); //update for next timestep

		
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
	
	public ScenarioState getInitialState() {
		return initialState;
	}
	public void setInitialState(ScenarioState initialState) {
		this.initialState = initialState;
	}
	public SmartGridTopology getTopo() {
		return topo;
	}
	public void setTopo(SmartGridTopology topo) {
		this.topo = topo;
	}


	public ScenarioState getImpactInput() {
		return impactInput;
	}
	public void setImpactInput(ScenarioState impactInput) {
		this.impactInput = impactInput;
	}
}
