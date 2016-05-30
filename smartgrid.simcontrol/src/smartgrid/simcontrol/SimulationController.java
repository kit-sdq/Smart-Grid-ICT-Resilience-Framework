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

import smartgrid.helper.ScenarioModelHelper;
import smartgrid.helper.SimulationExtensionPointHelper;
import smartgrid.inputgenerator.IInputGenerator;
import smartgrid.log4j.InitializeLogger;
import smartgrid.simcontrol.baselib.Constants;
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

    private static String fileSystemPath;
    private static int maxTimeSteps;
    private static SmartGridTopology topo;
    private static ScenarioState initialState;

    private static FileAppender fileAppender;

    private SimulationController() {
    }

    /**
     * @param topo
     * @param initialState
     * @param maxTimeSteps
     */
    public static void run() {
        ScenarioState impactInputOld;
        final ScenarioState impactInput = initialState;
        ScenarioResult impactResultOld;

        List<PowerPerNode> powerPerNodes = null; // TODO how to initialize?
        List<AbstractCostFunction> costFunctions;

        // Compute Initial Impact Analysis Result
        ScenarioResult impactResult = impactAnalsis.run(topo, impactInput);

        // one iteration computes one timestep
        for (int timeStep = 0; timeStep < maxTimeSteps; timeStep++) {
            // Generates Path with default System separators
            final String timeStepPath = new File(fileSystemPath + "\\Zeitschritt " + timeStep).getPath();

            costFunctions = kritisSimulation.run(powerPerNodes);

            impactResult = attackerSimulation.run(topo, impactResult);
            if (impactResult == null) {
                // Attacker simulation failed due to rootNodeID not found -->
                // Abort the simulation
                break;
            }

            // save attack result to file
            final String attackResultFile = new File(timeStepPath + "\\AttackerSimulationResult.smartgridoutput")
                    .getPath();
            smartgrid.helper.FileSystem.saveToFileSystem(impactResult, attackResultFile);

            // impact and power may iterate several times
            int innerLoopIterationCount = 0;
            do {
                final String iterationPath = new File(timeStepPath + "\\Iteration " + innerLoopIterationCount)
                        .getPath();

                // run power load simulation
                final List<SmartMeterState> smartMeterStates = convertToPowerLoadInput(impactResult);
                powerPerNodes = powerLoadSimulation.run(costFunctions, smartMeterStates);

                // copy the input
                impactInputOld = EcoreUtil.copy(impactInput);

                // convert input for impact analysis
                updateImactAnalysisInput(impactInput, impactResult, powerPerNodes);

                // Save input to file
                final String inputFile = new File(iterationPath + "\\PowerLoadResult.smartgridinput").getPath();
                smartgrid.helper.FileSystem.saveToFileSystem(impactInput, inputFile);

                impactResultOld = impactResult;
                impactResult = impactAnalsis.run(topo, impactInput);

                // Save Result
                final String resultFile = new File(iterationPath + "\\ImpactResult.smartgridoutput").getPath();
                smartgrid.helper.FileSystem.saveToFileSystem(impactResult, resultFile);

                innerLoopIterationCount++;
            } while (terminationCondition.evaluate(innerLoopIterationCount, impactInput, impactInputOld, impactResult,
                    impactResultOld));

            LOG.info("Time step " + timeStep + " terminated after " + innerLoopIterationCount
                    + " power/impact iterations");

            // modify the scenario between time steps
            timeProgressor.progress();
        }

        LOG.info("Coupled simulation terminated");

        // remove file appender of this run
        Logger.getRootLogger().removeAppender(fileAppender);
    }

    // Private Methods

    private static List<SmartMeterState> convertToPowerLoadInput(final ScenarioResult impactResult) {
        final List<SmartMeterState> smartMeterStates = new ArrayList<SmartMeterState>();
        for (final EntityState state : impactResult.getStates()) {
            final NetworkEntity stateOwner = state.getOwner();
            if (stateOwner instanceof SmartMeter) {
                final String id = Integer.toString(stateOwner.getId());
                final SmartMeterState smartMeterState = new SmartMeterState(id, state);
                smartMeterStates.add(smartMeterState);
            }
        }
        return smartMeterStates;
    }

    private static ScenarioState updateImactAnalysisInput(final ScenarioState impactInput,
            final ScenarioResult impactResult, final List<PowerPerNode> powerPerNodes) {

        for (final EntityState state : impactResult.getStates()) {
            final boolean hackedState = state instanceof On && ((On) state).isIsHacked();
            final NetworkEntity owner = state.getOwner();
            for (final smartgridinput.EntityState input : impactInput.getEntityStates()) {
                if (input.getOwner().equals(owner)) {
                    input.setIsHacked(hackedState);
                    break;
                }
            }
        }

        for (final PowerState powerState : impactInput.getPowerStates()) {
            final String id = Integer.toString(powerState.getOwner().getId());
            for (final PowerPerNode powerPerNode : powerPerNodes) {
                if (id.equals(powerPerNode.getPowerNodeID())) {
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
    public static void init(final ILaunchConfiguration launchConfig) throws CoreException {

        InitializeLogger.initialize();

        LOG.debug("loading launch config");

        fileSystemPath = launchConfig.getAttribute(Constants.OUTPUT_PATH_KEY, "");

        // add fileappender for local logs
        final Logger rootLogger = Logger.getRootLogger();
        try {
            final Layout layout = ((Appender) rootLogger.getAllAppenders().nextElement()).getLayout();
            fileAppender = new FileAppender(layout, fileSystemPath + "\\log.log");
            rootLogger.addAppender(fileAppender);
        } catch (final IOException e) {
            throw new RuntimeException(
                    "Error creating local log appender in the working directory. Most likely there are problems with access rights.");
        }

        // load models
        final String inputPath = launchConfig.getAttribute(Constants.INPUT_PATH_KEY, "");
        final String topoPath = launchConfig.getAttribute(Constants.TOPOLOGY_PATH_KEY, "");
        initialState = ScenarioModelHelper.loadInput(inputPath);
        topo = ScenarioModelHelper.loadScenario(topoPath);
        LOG.info("Scenario input state: " + inputPath);
        LOG.info("Topology: " + topoPath);

        /*
         * Check whether this is really an Integer already done in
         * smartgrid.simcontrol.ui.SimControlLaunchConfigurationTab. So just parsing to Int
         */
        maxTimeSteps = Integer.parseUnsignedInt(launchConfig.getAttribute(Constants.TIMESTEPS_KEY, ""));
        LOG.info("Running for " + maxTimeSteps + " time steps");

        // Gets String from Config and compares whether it is the same String as Constants.TRUE
        generateInput = launchConfig.getAttribute(Constants.GEN_SYNTHETIC_INPUT_KEY, Constants.FALSE)
                .contains(Constants.TRUE);
        if (generateInput) {
            initGeneratorInput(launchConfig);
        }

        // Retrieve simulations from extension points
        loadCustomUserAnalysis(launchConfig);

        // Init all simulations
        powerLoadSimulation.init(launchConfig);
        kritisSimulation.init(launchConfig);
        impactAnalsis.init(launchConfig);
        attackerSimulation.init(launchConfig);
        terminationCondition.init(launchConfig);
        timeProgressor.init(launchConfig);

        LOG.info("Using power load simulation: " + powerLoadSimulation.getName());
        LOG.info("Using KRITIS simulation: " + kritisSimulation.getName());
        LOG.info("Using impact analysis: " + impactAnalsis.getName());
        LOG.info("Using attacker simulation: " + attackerSimulation.getName());
        LOG.info("Using termination condition: " + terminationCondition.getName());
        LOG.info("Using time progressor: " + timeProgressor.getName());
    }

    /**
     * @throws CoreException
     */
    private static void loadCustomUserAnalysis(final ILaunchConfiguration launchConfig) throws CoreException {
        final SimulationExtensionPointHelper helper = new SimulationExtensionPointHelper();

        final List<IAttackerSimulation> attack = helper.getAttackerSimulationExtensions();
        for (final IAttackerSimulation e : attack) {

            if (launchConfig.getAttribute(Constants.ATTACKER_SIMULATION_CONFIG, "").equals(e.getName())) {
                attackerSimulation = e;
            }
        }

        final List<IPowerLoadSimulationWrapper> power = helper.getPowerLoadSimulationExtensions();
        for (final IPowerLoadSimulationWrapper e : power) {

            if (launchConfig.getAttribute(Constants.POWER_LOAD_SIMULATION_CONFIG, "").equals(e.getName())) {
                powerLoadSimulation = e;
            }
        }

        final List<IKritisSimulationWrapper> kritis = helper.getKritisSimulationExtensions();
        for (final IKritisSimulationWrapper e : kritis) {

            if (launchConfig.getAttribute(Constants.KRITIS_SIMULATION_CONFIG, "").equals(e.getName())) {
                kritisSimulation = e;
            }
        }

        final List<ITerminationCondition> termination = helper.getTerminationConditionExtensions();
        for (final ITerminationCondition e : termination) {

            if (launchConfig.getAttribute(Constants.TERMINATION_CONDITION_SIMULATION_CONFIG, "").equals(e.getName())) {
                terminationCondition = e;
            }
        }

        final List<ITimeProgressor> time = helper.getProgressorExtensions();
        for (final ITimeProgressor e : time) {

            if (launchConfig.getAttribute(Constants.TIME_PROGRESSOR_SIMULATION_CONFIG, "").equals(e.getName())) {
                timeProgressor = e;
            }
        }

        final List<IImpactAnalysis> impact = helper.getImpactAnalysisExtensions();
        for (final IImpactAnalysis e : impact) {

            if (launchConfig.getAttribute(Constants.IMPACT_ANALYSIS_SIMULATION_CONFIG, "").equals(e.getName())) {
                impactAnalsis = e;
            }
        }

        if (generateInput) {
            // #pragma parallel section
            for (final IInputGenerator e : inputGeneratorList) {

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
    private static void initGeneratorInput(final ILaunchConfiguration launchConfig)
            throws CoreException, NumberFormatException {

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
