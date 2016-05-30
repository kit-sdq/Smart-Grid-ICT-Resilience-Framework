package smartgrid.simcontrol;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.emf.ecore.util.EcoreUtil;

import smartgrid.helper.SimulationExtensionPointHelper;
import smartgrid.helper.ScenarioHelper;
import smartgrid.inputgenerator.IInputGenerator;
import smartgrid.log4j.InitializeLogger;
import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.GenerationStyle;
import smartgrid.simcontrol.baselib.coupling.AbstractCostFunction;
import smartgrid.simcontrol.baselib.coupling.IAttackerSimulation;
import smartgrid.simcontrol.baselib.coupling.IImpactAnalysis;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.IPowerLoadSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.ITerminationCondition;
import smartgrid.simcontrol.baselib.coupling.ITimeProgressor;
import smartgrid.simcontrol.baselib.coupling.PowerPerNode;
import smartgrid.simcontrol.baselib.coupling.SmartMeterState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridoutput.EntityState;
import smartgridoutput.On;
import smartgridoutput.ScenarioResult;
import smartgridtopo.NetworkEntity;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;

public final class SimulationController {

	private static final Logger LOG = Logger.getLogger(SimulationController.class);

	/* Generator Fields (not yet properly implemented) */
	private static Boolean generateInput = false;
	private static List<IInputGenerator> inputGeneratorList = new LinkedList<IInputGenerator>();
	@SuppressWarnings("unused")
    private static IInputGenerator inputGenerator;
	@SuppressWarnings("unused")
	private static GenerationStyle desiredStyle;
	@SuppressWarnings("unused")
	private static int smartMeterCount;
	@SuppressWarnings("unused")
	private static int controlCenterCount;

	private static IKritisSimulationWrapper kritisSimulation;
	private static IPowerLoadSimulationWrapper powerLoadSimulation;
	private static IImpactAnalysis impactAnalsis;
	private static IAttackerSimulation attackerSimulation;
	private static ITerminationCondition terminationCondition;
	private static ITimeProgressor timeProgressor;

	private static StringBuilder fileSystemPath;
	private static int timeSteps;
	private static SmartGridTopology topo;
	private static ScenarioState initialState;

	private SimulationController() {
	}

	/**
	 * @param topo
	 * @param initialState
	 * @param timeSteps
	 */
	public static void run() {
		ScenarioState impactInputOld;
		ScenarioState impactInput = initialState;
		ScenarioResult impactResultOld;
		
		List<PowerPerNode> powerPerNodes = null; // TODO how to initialize?
		List<AbstractCostFunction> costFunctions;

		// Compute Initial Impact Analysis Result
		ScenarioResult impactResult = impactAnalsis.run(topo, impactInput);
		
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

			costFunctions = kritisSimulation.run(powerPerNodes);
			
			impactResult = attackerSimulation.run(topo, impactResult);
			if (impactResult == null) {
				// Attacker simulation failed due to rootNodeID not found -->
				// Abort the simulation
				break;
			}
			
			// save attack result to file
			String attackResultFile = new File(timeStepPath + "\\AttackerSimulationResult.smartgridoutput").getPath();
			smartgrid.helper.FileSystem.saveToFileSystem(impactResult, attackResultFile);

			// impact and power may iterate several times
			int innerLoopIterationCount = 0;
			do {
			    String iterationPath = new File(timeStepPath + "\\Iteration " + innerLoopIterationCount).getPath();
			    
			    // run power load simulation
		        List<SmartMeterState> smartMeterStates = convertToPowerLoadInput(impactResult);
		        powerPerNodes = powerLoadSimulation.run(costFunctions, smartMeterStates);
		        
		        // copy the input
		        impactInputOld = EcoreUtil.copy(impactInput);
		        
		        // convert input for impact analysis
				updateImactAnalysisInput(impactInput, impactResult, powerPerNodes);
				
				// Save input to file
                String inputFile = new File(iterationPath + "\\PowerLoadResult.smartgridinput").getPath();
                smartgrid.helper.FileSystem.saveToFileSystem(impactInput, inputFile);
				
				impactResultOld = impactResult;
				impactResult = impactAnalsis.run(topo, impactInput);
				
				// Save Result
				String resultFile = new File(iterationPath + "\\ImpactResult.smartgridoutput").getPath();
				smartgrid.helper.FileSystem.saveToFileSystem(impactResult, resultFile);

				innerLoopIterationCount++;
			} while (terminationCondition.evaluate(innerLoopIterationCount, impactInput, impactInputOld,
					impactResult, impactResultOld));

			// modify the scenario between time steps
			timeProgressor.progress();
		}
		
		//remove file appender of this run
		rootLogger.removeAppender(fileAppender);
	}

	// Private Methods

    private static List<SmartMeterState> convertToPowerLoadInput(ScenarioResult impactResult) {
        List<SmartMeterState> smartMeterStates = new ArrayList<SmartMeterState>();
        for (EntityState state : impactResult.getStates()) {
            NetworkEntity stateOwner = state.getOwner();
            if (stateOwner instanceof SmartMeter) {
                String id = Integer.toString(stateOwner.getId());
                SmartMeterState smartMeterState = new SmartMeterState(id, state);
                smartMeterStates.add(smartMeterState);
            }
        }
        return smartMeterStates;
    }

    private static ScenarioState updateImactAnalysisInput(ScenarioState impactInput,
            ScenarioResult impactResult, List<PowerPerNode> powerPerNodes) {

        for (EntityState state : impactResult.getStates()) {
            boolean hackedState = state instanceof On && ((On) state).isIsHacked();
            NetworkEntity owner = state.getOwner();
            for (smartgridinput.EntityState input : impactInput.getEntityStates()) {
                if (input.getOwner().equals(owner)) {
                    input.setIsHacked(hackedState);
                    break;
                }
            }
        }
        
        for (PowerState powerState : impactInput.getPowerStates()) {
            String id = Integer.toString(powerState.getOwner().getId());
            for(PowerPerNode powerPerNode : powerPerNodes) {
                if(id.equals(powerPerNode.getPowerNodeID())) {
                    // TODO interpret data correctly
                    powerState.setPowerOutage(powerPerNode.getAbsolutePower() == 0);
                }
            }
        }
        
        return null;
    }

	/*
	 * Inits some things for all runs. Not for Interface based hook in !
	 * 
	 * Does: # Generates Output Path String # Inits the Simulations
	 */
	public static void init(ILaunchConfiguration launchConfig) {
		InitializeLogger.initialize();
		
		String tempPath = "C:\\Temp\\";

		// Read ILaunchConfiguration
		try {
			LOG.debug("Loaded");
			LOG.debug("Find parameters");

			String inputPath = launchConfig.getAttribute(Constants.INPUT_PATH_KEY, ""); // smartgrid.simcontrol.ui.Constants

			String topoPath = launchConfig.getAttribute(Constants.TOPOLOGY_PATH_KEY, "");
			/*
			 * Check whether this is really an Integer already done in
			 * smartgrid.simcontrol.ui.SimControlLaunchConfigurationTab. So just
			 * parsing to Int
			 */
			SimulationController.timeSteps = Integer
					.parseUnsignedInt(launchConfig.getAttribute(Constants.TIMESTEPS_KEY, ""));

			LOG.info("Input : " + inputPath);
			LOG.info("Topology : " + topoPath);

			initialState = ScenarioHelper.loadInput(inputPath);
			topo = ScenarioHelper.loadScenario(topoPath);

			tempPath = launchConfig.getAttribute(Constants.OUTPUT_PATH_KEY, "");
			
			// Gets String from Config and compares whether it is the same
			// String as Constants.TRUE
			generateInput = launchConfig.getAttribute(Constants.GEN_SYNTHETIC_INPUT_KEY, Constants.FALSE)
					.contains(Constants.TRUE);

			if (generateInput) {
				initGeneratorInput(launchConfig);
			}

		} catch (CoreException e) {
			LOG.error("[SimulationController] LaunchConfig not valid, using Path: C:\\Temp\\");
			e.printStackTrace();
		}

		SimulationController.fileSystemPath = new StringBuilder(tempPath);

		// Init Simulations Errorcodes
		ErrorCodeEnum powerError = ErrorCodeEnum.NOT_SET;
		ErrorCodeEnum kritisError = ErrorCodeEnum.NOT_SET;
		ErrorCodeEnum impactError = ErrorCodeEnum.NOT_SET;
		ErrorCodeEnum attackerError = ErrorCodeEnum.NOT_SET;
		ErrorCodeEnum terminationError = ErrorCodeEnum.NOT_SET;
		ErrorCodeEnum timeError = ErrorCodeEnum.NOT_SET;

		// Retrieve simulations from extension points
		try {
			loadCustomUserAnalysis(launchConfig);
		} catch (CoreException e1) {
			LOG.error("Exception occured while loading custom user analysis");
			e1.printStackTrace();
		}

		try {
		    // Init all simulations
			powerError = SimulationController.powerLoadSimulation.init(launchConfig);
			kritisError = SimulationController.kritisSimulation.init(launchConfig);
			impactError = SimulationController.impactAnalsis.init(launchConfig);
			attackerError = SimulationController.attackerSimulation.init(launchConfig);
			terminationError = SimulationController.terminationCondition.init(launchConfig);
			timeError = SimulationController.timeProgressor.init(launchConfig);
			
			LOG.info("Using power load simulation: "+powerLoadSimulation.getName());
			LOG.info("Using KRITIS simulation: "+kritisSimulation.getName());
			LOG.info("Using impact analysis: "+impactAnalsis.getName());
			LOG.info("Using attacker simulation: "+attackerSimulation.getName());
			LOG.info("Using termination condition: "+terminationCondition.getName());
			LOG.info("Using time progressor: "+timeProgressor.getName());

		} catch (Exception e) {

			e.printStackTrace();

			final String newline = System.getProperty("line.separator");

			String printError = "init failed with these Errors " + newline
					+ "power load simulation: " + powerError.toString() + newline + "KRITIS simulation: " +
			        kritisError.toString() + newline + "impact analysis: " + impactError.toString() +
			        newline + "attacker simulation: " + attackerError.toString() + newline +
			        "termination condition: " + terminationError.toString() + newline + "time progressor: "
					+ timeError.toString();

			LOG.error(printError);

		}

	}

	/**
	 * @throws CoreException
	 */
	private static void loadCustomUserAnalysis(ILaunchConfiguration launchConfig) throws CoreException {
		SimulationExtensionPointHelper helper = new SimulationExtensionPointHelper();

		List<IAttackerSimulation> attack = helper.getAttackerSimulationExtensions();
		for (IAttackerSimulation e : attack) {

			if (launchConfig.getAttribute(Constants.ATTACKER_SIMULATION_CONFIG, "").equals(e.getName())) {
				attackerSimulation = e;
			}
		}

		List<IPowerLoadSimulationWrapper> power = helper.getPowerLoadSimulationExtensions();
		for (IPowerLoadSimulationWrapper e : power) {

			if (launchConfig.getAttribute(Constants.POWER_LOAD_SIMULATION_CONFIG, "").equals(e.getName())) {
				powerLoadSimulation = e;
			}
		}
		
        List<IKritisSimulationWrapper> kritis = helper.getKritisSimulationExtensions();
        for (IKritisSimulationWrapper e : kritis) {

            if (launchConfig.getAttribute(Constants.KRITIS_SIMULATION_CONFIG, "").equals(e.getName())) {
                kritisSimulation = e;
            }
        }

		List<ITerminationCondition> termination = helper.getTerminationConditionExtensions();
		for (ITerminationCondition e : termination) {

			if (launchConfig.getAttribute(Constants.TERMINATION_CONDITION_SIMULATION_CONFIG, "")
					.equals(e.getName())) {
				terminationCondition = e;
			}
		}

		List<ITimeProgressor> time = helper.getProgressorExtensions();
		for (ITimeProgressor e : time) {

			if (launchConfig.getAttribute(Constants.TIME_PROGRESSOR_SIMULATION_CONFIG, "").equals(e.getName())) {
				timeProgressor = e;
			}
		}

		List<IImpactAnalysis> impact = helper.getImpactAnalysisExtensions();
		for (IImpactAnalysis e : impact) {

			if (launchConfig.getAttribute(Constants.IMPACT_ANALYSIS_SIMULATION_CONFIG, "").equals(e.getName())) {
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
	private static void initGeneratorInput(ILaunchConfiguration launchConfig) throws CoreException, NumberFormatException {

		assert generateInput : "Only run this Methods if generate Input is desired by user";

		String genStyleString = launchConfig.getAttribute(Constants.GEN_SYNTHETIC_INPUT_KEY, Constants.FAIL);

		if (genStyleString.equals(Constants.FAIL)) {

			genStyleString = Constants.DEFAULT_GEN_SYNTHETIC_INPUT;
			LOG.info("[Simulationcontroller] Default Generation Style used");
		}

		SimulationController.desiredStyle = GenerationStyle.valueOf(genStyleString);

		String smartString = launchConfig.getAttribute(Constants.SMART_METER_COUNT_KEY, Constants.FAIL);

		if (smartString.equals(Constants.FAIL)) {
			smartString = Constants.DEFAULT_SMART_METER_COUNT;
			LOG.info("[SimulationController] Default SmartMeterCount used");

		}

		SimulationController.smartMeterCount = Integer.parseInt(smartString);

		String controlCenterString = launchConfig.getAttribute(Constants.CONTROL_CENTER_COUNT_KEY, Constants.FAIL);

		if (controlCenterString.equals(Constants.FAIL)) {
			controlCenterString = Constants.DEFAULT_CONTROL_CENTER_COUNT;
			LOG.info("[SimulationController] Default ControlCenterCount used");

		}
		SimulationController.controlCenterCount = Integer.parseInt(controlCenterString);
	}
}
