package smartgrid.simcontrol;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.interfaces.*;
import smartgrid.helper.ScenarioHelper;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.Scenario;
import smartgrid.simcontrol.ui.*;
import smartgrid.simcontroller.baselibary.Constants;
import smartgrid.simcontroller.baselibary.GenerationStyle;

public enum SimulationController {
	;

	/* Generator Fields */

	private static List<IInputGenerator> inputGeneratorList = new  LinkedList<IInputGenerator>();
	private static IInputGenerator usedInputGenerator;
	private static GenerationStyle desiredStyle;
	private static int SmartMeterCount;
	private static int ControlCenterCount;

	/* Analysis Fields */

	// Init Lists here necessary / not possible in the Constructor (Variant
	// seperate initList() Method)
	private static List<IPowerLoadSimulation> powerLoadSimulationList = new LinkedList<IPowerLoadSimulation>();
	private static List<IImpactAnalysis> impactAnalsisList = new LinkedList<IImpactAnalysis>();
	private static List<IAttackerSimulation> attackerSimulationList = new LinkedList<IAttackerSimulation>();


	//Used Analysis from Selection
	private static IPowerLoadSimulation usedPowerLoadSimulation;
	private static IImpactAnalysis usedImpactAnalsis;
	private static IAttackerSimulation usedAttackerSimulation;

	
	
	/* Workflow Fields */

	private static List<ITerminationCondition> terminationConditionList = new LinkedList<ITerminationCondition>(); 
	private static List<ITimeProgressor> timeProgressorList = new LinkedList<ITimeProgressor>();

	//Use Workflow Fields from Selection
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

	// misc
	private static Scenario topo;
	private static ScenarioState initialState;

	// Private Constructor
	private SimulationController() {

	}

	// /**
	// * Constructs the SimController with registered ExtensionPoints
	// *
	// * @param myConfig
	// * The "Data Transfer Object" from UI
	// */
	// public SimulationController(ILaunchConfiguration myConfig) {
	//
	// this.myConfiguration = myConfig;
	//
	// init();
	//
	// }

	// /**
	// * Constructor for Interface based hooked in
	// *
	// *
	// * @deprecated Use Extension Points instead
	// *
	// * @param powerLoadSimulation
	// * @param impactAnalsis
	// * @param attackerSimulation
	// * @param terminationCondition
	// * @param timeProgressor
	// * @param configuration
	// * The "Data Transfer Object" from UI
	// */
	// public SimulationController(IPowerLoadSimulation powerLoadSimulation,
	// IImpactAnalysis impactAnalsis,
	// IAttackerSimulation attackerSimulation,
	// ITerminationCondition terminationCondition,
	// ITimeProgressor timeProgressor, ILaunchConfiguration configuration) {
	//
	// this.powerLoadSimulation = powerLoadSimulation;
	// this.impactAnalsis = impactAnalsis;
	// this.attackerSimulation = attackerSimulation;
	// this.terminationCondition = terminationCondition;
	// this.timeProgressor = timeProgressor;
	//
	// this.myConfiguration = configuration;
	//
	// // Reading Config
	//
	// String tempPath = "C:\\Temp\\";
	//
	// // Read ILaunchConfiguration
	// try {
	// tempPath = myConfiguration.getAttribute(Constants.OUTPUT_PATH_KEY,
	// "");
	// // Gets String from Config and compares whether it is the same
	// // String as Constants.TRUE
	// ignoreLogicalCon = myConfiguration.getAttribute(
	// Constants.IGNORE_LOC_CON_KEY, Constants.FALSE).contains(
	// Constants.TRUE);
	//
	// } catch (CoreException e) {
	//
	// e.printStackTrace();
	// System.out
	// .println("LaunchConfig had no valid Path using: C:\\Temp\\");
	// }
	//
	// this.fileSystemPath = new StringBuilder(tempPath);
	//
	// }

	// TODO JavaDoc
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

		// one iteration computes one timestep
		for (int i = 0; i < timeSteps; i++) {
			impactResult = usedAttackerSimulation.run(topo, impactResult);

			// Generates Path with default System separators
			String timeStepPath = new File(fileSystemPath.toString()
					+ "\\Zeitschritt " + i).getPath();

			// impact and power may iterate several times until the state of one
			// timestep is determined
			int innerLoopIterationCount = 0;
			do {
				impactInputOld = impactInput;
				impactInput = usedPowerLoadSimulation.run(topo, impactInput,
						impactResult);
				impactResultOld = impactResult;
				impactResult = usedImpactAnalsis.run(topo, impactInput);
				innerLoopIterationCount++;

				// Save Iteration to file system

				// Exampel Zeitschritt 1\Iteration 1\
				String iterationPath = new File(timeStepPath + "\\Iteration "
						+ innerLoopIterationCount).getPath();
				// TODO Externalize Strings

				// Save Input
				String inputFile = new File(iterationPath
						+ "\\Input.smartgridinput").getPath();

				smartgrid.helper.FileSystem.saveToFileSystem(impactInputOld,
						inputFile);

				// Save Result
				String resultFile = new File(iterationPath
						+ "\\Result.smartgridoutput").getPath();

				smartgrid.helper.FileSystem.saveToFileSystem(impactResult,
						resultFile);

				// End 1 Iteration
			} while (usedTerminationCondition.evaluate(innerLoopIterationCount,
					impactInput, impactInputOld, impactResult, impactResultOld));

			// modify the scenario between time steps
			usedTimeProgressor.progress(); // TODO: parameters
		}// End 1 TimeStep
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

			String inputPath = myConfiguration.getAttribute(
					Constants.INPUT_PATH_KEY, ""); // smartgrid.simcontrol.ui.Constants

			String topoPath = myConfiguration.getAttribute(
					Constants.TOPOLOGY_PATH_KEY, "");
			/*
			 * Check whether this is really an Integer already done in
			 * smartgrid.simcontrol.ui.SimControlLaunchConfigurationTab. So just
			 * parsing to Int
			 */
			SimulationController.timeSteps = Integer
					.parseUnsignedInt(myConfiguration.getAttribute(
							Constants.TIMESTEPS_KEY, ""));

			System.out.println("Input : " + inputPath);
			System.out.println("Topology : " + topoPath);

			//TODO Activate Generator here
			
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

			tempPath = myConfiguration.getAttribute(Constants.OUTPUT_PATH_KEY,
					"");
			// Gets String from Config and compares whether it is the same
			// String as Constants.TRUE
			ignoreLogicalCon = myConfiguration.getAttribute(
					Constants.IGNORE_LOC_CON_KEY, Constants.FALSE).contains(
					Constants.TRUE);

			generateInput = myConfiguration.getAttribute(
					Constants.GEN_SYNTHETIC_INPUT_KEY, Constants.FALSE)
					.contains(Constants.TRUE);

			if (generateInput) {
				initGeneratorInput();
			}

		} catch (CoreException e) {

			e.printStackTrace();
			System.out
					.println("[SimController] LaunchConfig not valid; using Path: C:\\Temp\\");
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
		evaluateExtensionPoints();
		//
		//Add usedAnalysis' according to custom user selection
		loadCustomUserAnalysis();
		//
		
		
		try { // Inits the Simulations
			powerError = SimulationController.usedPowerLoadSimulation
					.init(myConfiguration);
			impactError = SimulationController.usedImpactAnalsis
					.init(myConfiguration);
			attackerError = SimulationController.usedAttackerSimulation
					.init(myConfiguration);
			terminationError = SimulationController.usedTerminationCondition
					.init(myConfiguration);
			timeError = SimulationController.usedTimeProgressor
					.init(myConfiguration);

		} catch (CoreException e) {

			e.printStackTrace();

			final String newline = System.getProperty("line.separator");

			String printError = "[SimController] init failed with these Errors "
					+ newline
					+ "powerLoad Simulation: "
					+ powerError.toString()
					+ newline
					+ "impactAnalysis: "
					+ impactError.toString()
					+ newline
					+ "attacker Simulation: "
					+ attackerError.toString()
					+ newline
					+ "terminationCondition: "
					+ terminationError.toString()
					+ newline
					+ "timeProgressor: " + timeError.toString();

			System.out.println(printError);

		}

	}

	/**
	 * 
	 */
	private static void loadCustomUserAnalysis() {
		//TODO the different for loops are possible Point for Parallelization 
		
		//#pragma parallel section
		for(IAttackerSimulation e : attackerSimulationList){
			
			//TODO Add User Selection from UI here

//			if(e.getName().equals("User Selection")){
				usedAttackerSimulation = e;
//			}
			
		}
		
		
		for (IPowerLoadSimulation e : powerLoadSimulationList) {

			//TODO Add User Selection from UI here

			//			if(e.getName().equals("User Selection")){
			usedPowerLoadSimulation = e;
			//			}

		}
		
		for (ITerminationCondition e : terminationConditionList) {

			//TODO Add User Selection from UI here

			//			if(e.getName().equals("User Selection")){
			usedTerminationCondition = e;
			//			}

		}
		
		for (ITimeProgressor e : timeProgressorList) {

			//TODO Add User Selection from UI here

			//			if(e.getName().equals("User Selection")){
			usedTimeProgressor = e;
			//			}

		}
		
		for (IImpactAnalysis e : impactAnalsisList) {

			//TODO Add User Selection from UI here

			//			if(e.getName().equals("User Selection")){
			usedImpactAnalsis = e;
			//			}

		}
		
		
		if (generateInput) {
			//#pragma parallel section
			for (IInputGenerator e : inputGeneratorList) {

				//TODO Add User Selection from UI here

				//			if(e.getName().equals("User Selection")){
				usedInputGenerator = e;
				//			}

			}
		}
	}

	/**
	 * @throws CoreException
	 * @throws NumberFormatException
	 */
	private static void initGeneratorInput() throws CoreException,
			NumberFormatException {

		assert generateInput : "Only run this Methods if generate Input is desired by user";

		String genStyleString = myConfiguration.getAttribute(
				Constants.GEN_SYNTHETIC_INPUT_KEY, Constants.FAIL);

		if (genStyleString.equals(Constants.FAIL)) {

			genStyleString = Constants.DEFAULT_GEN_SYNTHETIC_INPUT;
			System.out.println("[Simcontroller] Default Generation Style used");
		}

		 SimulationController.desiredStyle =
		 GenerationStyle.valueOf(genStyleString);

		String smartString = myConfiguration.getAttribute(
				Constants.SMART_METER_COUNT_KEY, Constants.FAIL);

		if (smartString.equals(Constants.FAIL)) {
			smartString = Constants.DEFAULT_SMART_METER_COUNT;
			System.out.println("[Simcontroller] Default SmartMeterCount used");

		}

		 SimulationController.SmartMeterCount = Integer.parseInt(smartString);

		String controlCenterString = myConfiguration.getAttribute(
				Constants.CONTROL_CENTER_COUNT_KEY, Constants.FAIL);

		if (controlCenterString.equals(Constants.FAIL)) {
			controlCenterString = Constants.DEFAULT_CONTROL_CENTER_COUNT;
			System.out
					.println("[Simcontroller] Default ControlCenterCount used");

		}

		 SimulationController.ControlCenterCount =
		 Integer.parseInt(controlCenterString);

	}

	/*
	 * Evaluates all ExtensionPoints known and inits the Extensions
	 */
	private static void evaluateExtensionPoints() {

		if (!evalutionDone) {
			SimulationController.registry = Platform.getExtensionRegistry();
			final String[] ExtensionPoints_ID = {
					"smartgrid.simcontrol.extensionpoints.attackerSimulation",
					"smartgrid.simcontrol.extensionpoints.impactAnalysis",
					"smartgrid.simcontrol.extensionpoints.powerLoadSimulation",
					"smartgrid.simcontrol.extensionpoints.timeProgressor",
					"smartgrid.simcontrol.extensionpoints.terminationCondition",
					"smartgrid.simcontrol.extensionpoints.inputGenerator" };
			for (int i = 0; i < ExtensionPoints_ID.length; i++) {
				IConfigurationElement[] config = registry
						.getConfigurationElementsFor(ExtensionPoints_ID[i]);
				try {
					for (IConfigurationElement e : config) {
						System.out.println("Evaluating extension "
								+ ExtensionPoints_ID[i].substring(37));
						/*
						 * substring(37) removes
						 * smartgrid.simcontrol.extensionpoints. from the Stings
						 * so that only the Extension Name is displayed in
						 * Console
						 */
						final Object o = e.createExecutableExtension("class");
						if (o instanceof IPowerLoadSimulation) {

							SimulationController.powerLoadSimulationList.add((IPowerLoadSimulation) o);

						} else if (o instanceof IImpactAnalysis) {
							SimulationController.impactAnalsisList.add((IImpactAnalysis) o);
						} else if (o instanceof IAttackerSimulation) {
							SimulationController.attackerSimulationList.add((IAttackerSimulation) o);
						} else if (o instanceof ITerminationCondition) {
							SimulationController.terminationConditionList.add((ITerminationCondition) o);
						} else if (o instanceof ITimeProgressor) {
							SimulationController.timeProgressorList.add((ITimeProgressor) o);
						} else if (generateInput
								&& o instanceof IInputGenerator) {
							SimulationController.inputGeneratorList.add((IInputGenerator) o);
						}
					}
				} catch (CoreException ex) {
					System.out.println(ex.getMessage());
					System.out.println("[Dammit!]");
				}
			}

			SimulationController.evalutionDone = true;
		}

	}

}
