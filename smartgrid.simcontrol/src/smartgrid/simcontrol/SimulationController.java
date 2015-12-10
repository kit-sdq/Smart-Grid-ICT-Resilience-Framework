package smartgrid.simcontrol;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.helper.ReceiveAnalysesHelper;
import smartgrid.helper.ScenarioHelper;
import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.GenerationStyle;
import smartgrid.simcontrol.interfaces.ErrorCodeEnum;
import smartgrid.simcontrol.interfaces.IAttackerSimulation;
import smartgrid.simcontrol.interfaces.IImpactAnalysis;
import smartgrid.simcontrol.interfaces.IInputGenerator;
import smartgrid.simcontrol.interfaces.IPowerLoadSimulation;
import smartgrid.simcontrol.interfaces.ITerminationCondition;
import smartgrid.simcontrol.interfaces.ITimeProgressor;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

public final class SimulationController {

	/* Generator Fields */

	private static List<IInputGenerator> inputGeneratorList = new LinkedList<IInputGenerator>();
	private static IInputGenerator usedInputGenerator;
	private static GenerationStyle desiredStyle;
	private static int SmartMeterCount;
	private static int ControlCenterCount;

	/* Analysis Fields */

	// Used Analysis from Selection
	private static IPowerLoadSimulation usedPowerLoadSimulation;
	private static IImpactAnalysis usedImpactAnalsis;
	private static IAttackerSimulation usedAttackerSimulation;

	/* Workflow Fields */

	// Use Workflow Fields from Selection
	private static ITerminationCondition usedTerminationCondition;
	private static ITimeProgressor usedTimeProgressor;

	/* Organisation Fields */

	private static ILaunchConfiguration myConfiguration;
	private static StringBuilder fileSystemPath;

	private static IExtensionRegistry registry;

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
		ScenarioResult impactResult = usedImpactAnalsis.run(topo, initialState);

		if (impactResult == null) {
			return;
		}

		// one iteration computes one timestep
		for (int i = 0; i < timeSteps; i++) {
			// Generates Path with default System separators
			String timeStepPath = new File(fileSystemPath.toString() + "\\Zeitschritt " + i).getPath();

			impactResult = usedAttackerSimulation.run(topo, impactResult);

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
				impactInput = usedPowerLoadSimulation.run(topo, impactInput, impactResult);
				impactResultOld = impactResult;
				impactResult = usedImpactAnalsis.run(topo, impactInput);
				innerLoopIterationCount++;

				// Save Iteration to file system

				// Exampel Zeitschritt 1\Iteration 1\
				String iterationPath = new File(timeStepPath + "\\Iteration " + innerLoopIterationCount).getPath();
				// TODO Externalize Strings

				// Save Input
				String inputFile = new File(iterationPath + "\\PowerLoadResult.smartgridinput").getPath();

				smartgrid.helper.FileSystem.saveToFileSystem(impactInputOld, inputFile);

				// Save Result
				String resultFile = new File(iterationPath + "\\ImpactResult.smartgridoutput").getPath();

				smartgrid.helper.FileSystem.saveToFileSystem(impactResult, resultFile);

				// End 1 Iteration
			} while (usedTerminationCondition.evaluate(innerLoopIterationCount, impactInput, impactInputOld,
					impactResult, impactResultOld));

			// modify the scenario between time steps
			usedTimeProgressor.progress();
		} // End 1 TimeStep
	}

	// Private Methods

	/*
	 * Inits some things for all runs. Not for Interface based hook in !
	 * 
	 * Does: # Generates Output Path String # Inits the Simulations
	 */
	public static void init(ILaunchConfiguration myConfig) {

		// Do Contructor things
		SimulationController.myConfiguration = myConfig;

		String tempPath = "C:\\Temp\\";

		// Read ILaunchConfiguration
		try {

			/*
			 * Read from @see ILaunchConfiguration
			 */

			// TODO Change to in case of Debug Only executing/compiling ?
			System.out.println("GELADEN");
			System.out.println("Finde Parameter");

			String inputPath = myConfiguration.getAttribute(Constants.INPUT_PATH_KEY, ""); // smartgrid.simcontrol.ui.Constants

			String topoPath = myConfiguration.getAttribute(Constants.TOPOLOGY_PATH_KEY, "");
			/*
			 * Check whether this is really an Integer already done in
			 * smartgrid.simcontrol.ui.SimControlLaunchConfigurationTab. So just
			 * parsing to Int
			 */
			SimulationController.timeSteps = Integer
					.parseUnsignedInt(myConfiguration.getAttribute(Constants.TIMESTEPS_KEY, ""));

			System.out.println("Input : " + inputPath);
			System.out.println("Topology : " + topoPath);

			// TODO Activate Generator here

			// if (generateInput) { // Generate Input
			//
			// SimulationController.inputGenerator.init();
			//
			// InputModelDTO myDTO =
			// SimulationController.inputGenerator.generate(
			// desiredStyle, SmartMeterCount, ControlCenterCount);
			//
			// initialState = myDTO.getMyScenarioStates();
			// topo = myDTO.getMyScenarioTopo();
			//
			// } else {// Read Input from FileSystem

			initialState = ScenarioHelper.loadInput(inputPath);
			topo = ScenarioHelper.loadScenario(topoPath);

			// }

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

			e.printStackTrace();
			System.out.println("[SimController] LaunchConfig not valid; using Path: C:\\Temp\\");
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//

		try { // Inits the Simulations
			powerError = SimulationController.usedPowerLoadSimulation.init(myConfiguration);
			impactError = SimulationController.usedImpactAnalsis.init(myConfiguration);
			attackerError = SimulationController.usedAttackerSimulation.init(myConfiguration);
			terminationError = SimulationController.usedTerminationCondition.init(myConfiguration);
			timeError = SimulationController.usedTimeProgressor.init(myConfiguration);

		} catch (CoreException e) {

			e.printStackTrace();

			final String newline = System.getProperty("line.separator");

			String printError = "[SimController] init failed with these Errors " + newline + "powerLoad Simulation: "
					+ powerError.toString() + newline + "impactAnalysis: " + impactError.toString() + newline
					+ "attacker Simulation: " + attackerError.toString() + newline + "terminationCondition: "
					+ terminationError.toString() + newline + "timeProgressor: " + timeError.toString();

			System.out.println(printError);

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
				usedAttackerSimulation = e;
			}

		}

		List<IPowerLoadSimulation> power = helper.getPowerLoadElements();
		for (IPowerLoadSimulation e : power) {

			if (myConfiguration.getAttribute(Constants.POWER_LOAD_SIMULATION_CONFIG, "").equals(e.getName())) {
				usedPowerLoadSimulation = e;
			}
		}

		List<ITerminationCondition> termination = helper.getTerminationConditionElements();
		for (ITerminationCondition e : termination) {

			if (myConfiguration.getAttribute(Constants.TERMINATION_CONDITION_SIMULATION_CONFIG, "")
					.equals(e.getName())) {
				usedTerminationCondition = e;
			}
		}

		List<ITimeProgressor> time = helper.getProgressorElements();
		for (ITimeProgressor e : time) {

			if (myConfiguration.getAttribute(Constants.TIME_PROGRESSOR_SIMULATION_CONFIG, "").equals(e.getName())) {
				usedTimeProgressor = e;
			}
		}

		List<IImpactAnalysis> impact = helper.getImpactAnalysisElements();
		for (IImpactAnalysis e : impact) {

			if (myConfiguration.getAttribute(Constants.IMPACT_ANALYSIS_SIMULATION_CONFIG, "").equals(e.getName())) {
				usedImpactAnalsis = e;
			}
		}

		if (generateInput) {
			// #pragma parallel section
			for (IInputGenerator e : inputGeneratorList) {

				// TODO Add User Selection from UI here

				// if(e.getName().equals("User Selection")){
				usedInputGenerator = e;
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
			System.out.println("[Simcontroller] Default Generation Style used");
		}

		SimulationController.desiredStyle = GenerationStyle.valueOf(genStyleString);

		String smartString = myConfiguration.getAttribute(Constants.SMART_METER_COUNT_KEY, Constants.FAIL);

		if (smartString.equals(Constants.FAIL)) {
			smartString = Constants.DEFAULT_SMART_METER_COUNT;
			System.out.println("[Simcontroller] Default SmartMeterCount used");

		}

		SimulationController.SmartMeterCount = Integer.parseInt(smartString);

		String controlCenterString = myConfiguration.getAttribute(Constants.CONTROL_CENTER_COUNT_KEY, Constants.FAIL);

		if (controlCenterString.equals(Constants.FAIL)) {
			controlCenterString = Constants.DEFAULT_CONTROL_CENTER_COUNT;
			System.out.println("[Simcontroller] Default ControlCenterCount used");

		}

		SimulationController.ControlCenterCount = Integer.parseInt(controlCenterString);

	}
}
