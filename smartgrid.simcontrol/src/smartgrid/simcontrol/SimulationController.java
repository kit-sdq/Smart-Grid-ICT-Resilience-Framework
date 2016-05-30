package smartgrid.simcontrol;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.helper.ReceiveAnalysesHelper;
import smartgrid.helper.ScenarioHelper;
import smartgrid.inputgenerator.IInputGenerator;
import smartgrid.log4j.InitializeLogger;
import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.GenerationStyle;
import smartgrid.simcontrol.baselib.coupling.IAttackerSimulation;
import smartgrid.simcontrol.baselib.coupling.IImpactAnalysis;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.IPowerLoadSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.ITerminationCondition;
import smartgrid.simcontrol.baselib.coupling.ITimeProgressor;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

public final class SimulationController {

	private static final Logger LOG = Logger.getLogger(SimulationController.class);

	/* Generator Fields (not yet properly implemented) */
	private static List<IInputGenerator> inputGeneratorList = new LinkedList<IInputGenerator>();
	@SuppressWarnings("unused")
    private static IInputGenerator inputGenerator;
	@SuppressWarnings("unused")
	private static GenerationStyle desiredStyle;
	@SuppressWarnings("unused")
	private static int smartMeterCount;
	@SuppressWarnings("unused")
	private static int controlCenterCount;

	/* Analysis Fields */

	// Used Analysis from Selection
	private static IKritisSimulationWrapper kritisSimulation;
	private static IPowerLoadSimulationWrapper powerLoadSimulation;
	private static IImpactAnalysis impactAnalsis;
	private static IAttackerSimulation attackerSimulation;

	/* Workflow Fields */

	// Use Workflow Fields from Selection
	private static ITerminationCondition terminationCondition;
	private static ITimeProgressor timeProgressor;

	/* Organisation Fields */
	private static ILaunchConfiguration myConfiguration;
	private static StringBuilder fileSystemPath;

	@SuppressWarnings("unused")
	private static Boolean ignoreLogicalCon; // TODO Perhaps here not needed
												// depends on Further changes

	private static Boolean generateInput = false; // TODO Read Value from Config

	private static Boolean evalutionDone = false;

	private static int timeSteps;

	/**
	 * @return the evalutionDone
	 */
	public Boolean getEvalutionDone() {
		return evalutionDone;
	}

	private static SmartGridTopology topo;
	private static ScenarioState initialState;

	// Private Constructor
	private SimulationController() {

	}

	/**
	 * 
	 * 
	 * @param topo
	 * @param initialState
	 * @param timeSteps
	 */
	public static void run() {
		ScenarioState impactInputOld;
		ScenarioState impactInput = initialState;
		ScenarioResult impactResultOld;

		// Compute Initial Impact Analysis Result
		ScenarioResult impactResult = impactAnalsis.run(topo, initialState);

		if (impactResult == null) {
			return;
		}
		
		//add fileappender for local logs
		Logger rootLogger = Logger.getRootLogger();
		FileAppender fileAppender = null;
		try {
            Layout layout = ((Appender)rootLogger.getAllAppenders().nextElement()).getLayout();
            fileAppender = new FileAppender(layout, fileSystemPath.toString()+"\\log.log");
            rootLogger.addAppender(fileAppender);
        } catch (IOException e) {
            e.printStackTrace();
        }

		// one iteration computes one timestep
		for (int i = 0; i < timeSteps; i++) {
			// Generates Path with default System separators
			String timeStepPath = new File(fileSystemPath.toString() + "\\Zeitschritt " + i).getPath();

			kritisSimulation.run(topo, impactInput);
			
			impactResult = attackerSimulation.run(topo, impactResult);

			if (impactResult == null) {
				// Attacker simulation failed due to rootNodeID not found -->
				// Abort the simulation
				break;
			}
			// Save Input
			String attackResultFile = new File(timeStepPath + "\\AttackerSimulationResult.smartgridoutput").getPath();
			smartgrid.helper.FileSystem.saveToFileSystem(impactResult, attackResultFile);

			// impact and power may iterate several times until the state of one
			// timestep is determined
			int innerLoopIterationCount = 0;
			do {
				impactInputOld = impactInput;
				impactInput = powerLoadSimulation.run(topo, impactInput, impactResult);
				
				impactResultOld = impactResult;
				impactResult = impactAnalsis.run(topo, impactInput);
				
				innerLoopIterationCount++;

				// Save Iteration to file system

				// Exampel Zeitschritt 1\Iteration 1\
				String iterationPath = new File(timeStepPath + "\\Iteration " + innerLoopIterationCount).getPath();

				// Save Input
				String inputFile = new File(iterationPath + "\\PowerLoadResult.smartgridinput").getPath();
				smartgrid.helper.FileSystem.saveToFileSystem(impactInputOld, inputFile);

				// Save Result
				String resultFile = new File(iterationPath + "\\ImpactResult.smartgridoutput").getPath();
				smartgrid.helper.FileSystem.saveToFileSystem(impactResult, resultFile);

				// End 1 Iteration
			} while (terminationCondition.evaluate(innerLoopIterationCount, impactInput, impactInputOld,
					impactResult, impactResultOld));

			// modify the scenario between time steps
			timeProgressor.progress();
		} // End 1 TimeStep
		
		//remove file appender of this run
		rootLogger.removeAppender(fileAppender);
	}

	// Private Methods

	/*
	 * Inits some things for all runs. Not for Interface based hook in !
	 * 
	 * Does: # Generates Output Path String # Inits the Simulations
	 */
	public static void init(ILaunchConfiguration myConfig) {
		InitializeLogger.initialize();
		
		// Do Contructor things
		SimulationController.myConfiguration = myConfig;

		String tempPath = "C:\\Temp\\";

		// Read ILaunchConfiguration
		try {
			LOG.debug("Loaded");
			LOG.debug("Find parameters");

			String inputPath = myConfiguration.getAttribute(Constants.INPUT_PATH_KEY, ""); // smartgrid.simcontrol.ui.Constants

			String topoPath = myConfiguration.getAttribute(Constants.TOPOLOGY_PATH_KEY, "");
			/*
			 * Check whether this is really an Integer already done in
			 * smartgrid.simcontrol.ui.SimControlLaunchConfigurationTab. So just
			 * parsing to Int
			 */
			SimulationController.timeSteps = Integer
					.parseUnsignedInt(myConfiguration.getAttribute(Constants.TIMESTEPS_KEY, ""));

			LOG.info("Input : " + inputPath);
			LOG.info("Topology : " + topoPath);

			initialState = ScenarioHelper.loadInput(inputPath);
			topo = ScenarioHelper.loadScenario(topoPath);

			tempPath = myConfiguration.getAttribute(Constants.OUTPUT_PATH_KEY, "");
			// Gets String from Config and compares whether it is the same
			// String as Constants.TRUE
			ignoreLogicalCon = myConfiguration.getAttribute(Constants.IGNORE_LOC_CON_KEY, Constants.FALSE)
					.contains(Constants.TRUE);

			generateInput = myConfiguration.getAttribute(Constants.GEN_SYNTHETIC_INPUT_KEY, Constants.FALSE)
					.contains(Constants.TRUE);

			if (generateInput) {
				initGeneratorInput();
			}

		} catch (CoreException e) {
			LOG.error("[SimulationController] LaunchConfig not valid, using Path: C:\\Temp\\");
			e.printStackTrace();
		}

		// Init needed Variables

		SimulationController.fileSystemPath = new StringBuilder(tempPath);

		// Init Simulations Errorcodes
		ErrorCodeEnum powerError = ErrorCodeEnum.NOT_SET;
		ErrorCodeEnum impactError = ErrorCodeEnum.NOT_SET;
		ErrorCodeEnum attackerError = ErrorCodeEnum.NOT_SET;
		ErrorCodeEnum terminationError = ErrorCodeEnum.NOT_SET;
		ErrorCodeEnum timeError = ErrorCodeEnum.NOT_SET;

		//
		// evaluateExtensionPoints();
		//
		// Add usedAnalysis' according to custom user selection
		try {
			loadCustomUserAnalysis();
		} catch (CoreException e1) {
			LOG.error("Exception occured while loading custom user analysis");
			e1.printStackTrace();
		}
		//

		try { // Inits the Simulations
			powerError = SimulationController.powerLoadSimulation.init(myConfiguration);
			impactError = SimulationController.impactAnalsis.init(myConfiguration);
			attackerError = SimulationController.attackerSimulation.init(myConfiguration);
			terminationError = SimulationController.terminationCondition.init(myConfiguration);
			timeError = SimulationController.timeProgressor.init(myConfiguration);

		} catch (Exception e) {

			e.printStackTrace();

			final String newline = System.getProperty("line.separator");

			String printError = "[SimulationController] init failed with these Errors " + newline
					+ "powerLoad Simulation: " + powerError.toString() + newline + "impactAnalysis: "
					+ impactError.toString() + newline + "attacker Simulation: " + attackerError.toString() + newline
					+ "terminationCondition: " + terminationError.toString() + newline + "timeProgressor: "
					+ timeError.toString();

			LOG.error(printError);

		}

	}

	/**
	 * @throws CoreException
	 * 
	 */
	private static void loadCustomUserAnalysis() throws CoreException {
		ReceiveAnalysesHelper helper = new ReceiveAnalysesHelper();

		List<IAttackerSimulation> attack = helper.getAttackerSimulationElements();
		for (IAttackerSimulation e : attack) {

			if (myConfiguration.getAttribute(Constants.ATTACKER_SIMULATION_CONFIG, "").equals(e.getName())) {
				attackerSimulation = e;
			}
		}

		List<IPowerLoadSimulationWrapper> power = helper.getPowerLoadElements();
		for (IPowerLoadSimulationWrapper e : power) {

			if (myConfiguration.getAttribute(Constants.POWER_LOAD_SIMULATION_CONFIG, "").equals(e.getName())) {
				powerLoadSimulation = e;
			}
		}
		
        List<IKritisSimulationWrapper> kritis = helper.getKritisSimulationElements();
        for (IKritisSimulationWrapper e : kritis) {

            if (myConfiguration.getAttribute(Constants.KRITIS_SIMULATION_CONFIG, "").equals(e.getName())) {
                kritisSimulation = e;
            }
        }

		List<ITerminationCondition> termination = helper.getTerminationConditionElements();
		for (ITerminationCondition e : termination) {

			if (myConfiguration.getAttribute(Constants.TERMINATION_CONDITION_SIMULATION_CONFIG, "")
					.equals(e.getName())) {
				terminationCondition = e;
			}
		}

		List<ITimeProgressor> time = helper.getProgressorElements();
		for (ITimeProgressor e : time) {

			if (myConfiguration.getAttribute(Constants.TIME_PROGRESSOR_SIMULATION_CONFIG, "").equals(e.getName())) {
				timeProgressor = e;
			}
		}

		List<IImpactAnalysis> impact = helper.getImpactAnalysisElements();
		for (IImpactAnalysis e : impact) {

			if (myConfiguration.getAttribute(Constants.IMPACT_ANALYSIS_SIMULATION_CONFIG, "").equals(e.getName())) {
				impactAnalsis = e;
			}
		}

		if (generateInput) {
			// #pragma parallel section
			for (IInputGenerator e : inputGeneratorList) {

				// TODO Add User Selection from UI here

				// if(e.getName().equals("User Selection")){
				inputGenerator = e;
				// }
			}
		}
	}

	/**
	 * @throws CoreException
	 * @throws NumberFormatException
	 */
	private static void initGeneratorInput() throws CoreException, NumberFormatException {

		assert generateInput : "Only run this Methods if generate Input is desired by user";

		String genStyleString = myConfiguration.getAttribute(Constants.GEN_SYNTHETIC_INPUT_KEY, Constants.FAIL);

		if (genStyleString.equals(Constants.FAIL)) {

			genStyleString = Constants.DEFAULT_GEN_SYNTHETIC_INPUT;
			LOG.info("[Simulationcontroller] Default Generation Style used");
		}

		SimulationController.desiredStyle = GenerationStyle.valueOf(genStyleString);

		String smartString = myConfiguration.getAttribute(Constants.SMART_METER_COUNT_KEY, Constants.FAIL);

		if (smartString.equals(Constants.FAIL)) {
			smartString = Constants.DEFAULT_SMART_METER_COUNT;
			LOG.info("[SimulationController] Default SmartMeterCount used");

		}

		SimulationController.smartMeterCount = Integer.parseInt(smartString);

		String controlCenterString = myConfiguration.getAttribute(Constants.CONTROL_CENTER_COUNT_KEY, Constants.FAIL);

		if (controlCenterString.equals(Constants.FAIL)) {
			controlCenterString = Constants.DEFAULT_CONTROL_CENTER_COUNT;
			LOG.info("[SimulationController] Default ControlCenterCount used");

		}
		SimulationController.controlCenterCount = Integer.parseInt(controlCenterString);
	}
}
