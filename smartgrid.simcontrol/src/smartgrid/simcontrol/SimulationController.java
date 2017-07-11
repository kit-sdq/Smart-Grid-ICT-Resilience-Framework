package smartgrid.simcontrol;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.helper.SimulationExtensionPointHelper;
import smartgrid.log4j.LoggingInitializer;
import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.ITimeProgressor;
import smartgrid.simcontrol.coupling.PowerSpec;
import smartgrid.simcontrol.coupling.Exceptions.SimcontrolException;

public final class SimulationController {

    private static final Logger LOG = Logger.getLogger(SimulationController.class);

    // TODO delete once active remote communication is implemented
//    /*
//     * This is not properly synchronized regarding protocol. In practice, multiple remote calls to
//     * initActive and/or multiple starts from the SimControl launch config can overwrite this
//     * singleton.
//     */
//    private static SimulationController instance;
//
//    public static synchronized SimulationController getInstance() {
//        while (instance == null)
//            try {
//                SimulationController.class.wait();
//            } catch (InterruptedException e) {
//                LOG.warn("Interrupted while waiting for SimulationController singleton. This is unexpected.", e);
//            }
//
//        return instance;
//    }
//
//    public static synchronized void setInstance(SimulationController newInstance) {
//        instance = newInstance;
//        SimulationController.class.notifyAll();
//    }

    private ReactiveSimulationController reactiveSimControl;
    private IKritisSimulationWrapper kritisSimulation;
//    private IPowerLoadSimulationWrapper powerLoadSimulation;
//    private IImpactAnalysis impactAnalsis;
//    private IAttackerSimulation attackerSimulation;
//    private ITerminationCondition terminationCondition;
    private ITimeProgressor timeProgressor;

//    private String workingDirPath;
    private int maxTimeSteps;
//    private SmartGridTopology topo; // TODO get this to reactive simcontrol
//    private ScenarioState initialState; // TODO get this to reactive simcontrol

//    private FileAppender fileAppender;

    /**
     * @param topo
     * @param initialState
     * @param maxTimeSteps
     */
    public void run() {
        Map<String, Map<String, PowerSpec>> kritisPowerDemand;
        Map<String, Map<String, Double>> powerSupply = null;

//        // Compute Initial Impact Analysis Result
//        ScenarioResult impactResult = impactAnalsis.run(topo, impactInput);

        // one iteration computes one timestep
        for (int timeStep = 0; timeStep < maxTimeSteps; timeStep++) {

//            // Generates Path with default System separators
//            final String timeStepPath = new File(workingDirPath + "\\Zeitschritt " + timeStep).getPath();

            if (timeStep == 0) {
                kritisPowerDemand = kritisSimulation.getDefaultDemand();
            } else {
                kritisPowerDemand = kritisSimulation.run(powerSupply);
            }

            powerSupply = reactiveSimControl.run(kritisPowerDemand);

//            impactResult = attackerSimulation.run(topo, impactResult);
//            if (impactResult == null) {
//                // Attacker simulation failed due to rootNodeID not found -->
//                // Abort the simulation
//                //TODO: improve reporting
//                break;
//            }

//            // save attack result to file
//            final String attackResultFile = new File(timeStepPath + "\\AttackerSimulationResult.smartgridoutput").getPath();
//            FileSystemHelper.saveToFileSystem(impactResult, attackResultFile);

//            // impact and power may iterate several times
//            int innerLoopIterationCount = 0;
//            do {
//                final String iterationPath = new File(timeStepPath + "\\Iteration " + innerLoopIterationCount).getPath();
//
//                // run power load simulation
//                final Map<String, Map<String, ISmartMeterState>> smartMeterStates = ReactiveSimulationController.convertToPowerLoadInput(impactResult);
//                powerSupply = powerLoadSimulation.run(kritisPowerDemand, smartMeterStates);
//
//                // copy the input
//                impactInputOld = EcoreUtil.copy(impactInput);
//
//                // convert input for impact analysis
//                ReactiveSimulationController.updateImactAnalysisInput(impactInput, impactResult, powerSupply);
//
//                // Save input to file
//                final String inputFile = new File(iterationPath + "\\PowerLoadResult.smartgridinput").getPath();
//                FileSystemHelper.saveToFileSystem(impactInput, inputFile);
//
//                impactResultOld = impactResult;
//                impactResult = impactAnalsis.run(topo, impactInput);
//
//                // Save Result
//                final String resultFile = new File(iterationPath + "\\ImpactResult.smartgridoutput").getPath();
//                FileSystemHelper.saveToFileSystem(impactResult, resultFile);
//
//                //generate report
//                final File resultReportPath = new File(iterationPath + "\\ResultReport.csv");
//                ReportGenerator.saveScenarioResult(resultReportPath, impactResult);
//
//                innerLoopIterationCount++;
//            } while (terminationCondition.evaluate(innerLoopIterationCount, impactInput, impactInputOld, impactResult, impactResultOld));
//
//            LOG.info("Time step " + timeStep + " terminated after " + innerLoopIterationCount + " power/impact iterations");

            // modify the scenario between time steps
            timeProgressor.progress();
        }

        LOG.info("Coupled simulation terminated");

        reactiveSimControl.shutDown();

//        // remove file appender of this run
//        Logger.getRootLogger().removeAppender(fileAppender);
//        fileAppender.close();
    }

    /*
     * Inits some things for all runs. Not for Interface based hook in !
     *
     * Does: # Generates Output Path String # Inits the Simulations
     */
    public void init(final ILaunchConfiguration launchConfig) throws CoreException, SimcontrolException {

        LoggingInitializer.initialize();

        LOG.debug("init active launch config");

//        determineWorkingDirPath(launchConfig);

//        // add fileappender for local logs
//        final Logger rootLogger = Logger.getRootLogger();
//        try {
//            final Layout layout = ((Appender) rootLogger.getAllAppenders().nextElement()).getLayout();
//            fileAppender = new FileAppender(layout, workingDirPath + "\\log.log");
//            rootLogger.addAppender(fileAppender);
//        } catch (final IOException e) {
//            throw new RuntimeException("Error creating local log appender in the working directory. Most likely there are problems with access rights.");
//        }

        // read paths
        String outputPath = launchConfig.getAttribute(Constants.OUTPUT_PATH_KEY, "");
        String inputPath = launchConfig.getAttribute(Constants.INPUT_PATH_KEY, "");
        String topoPath = launchConfig.getAttribute(Constants.TOPOLOGY_PATH_KEY, "");
//        initialState = ScenarioModelHelper.loadInput(inputPath);
//        topo = ScenarioModelHelper.loadScenario(topoPath);
//        LOG.info("Scenario input state: " + inputPath);
//        LOG.info("Topology: " + topoPath);

        /*
         * Check whether this is really an Integer already done in
         * smartgrid.simcontrol.ui.SimControlLaunchConfigurationTab. So just parsing to Int
         */
        maxTimeSteps = Integer.parseUnsignedInt(launchConfig.getAttribute(Constants.TIMESTEPS_KEY, ""));
        LOG.info("Running for " + maxTimeSteps + " time steps");

        // Retrieve simulations from extension points
        loadCustomUserAnalysis(launchConfig);

        // Init all simulations
//        powerLoadSimulation.init(launchConfig);
//        kritisSimulation.init(launchConfig);
//        impactAnalsis.init(launchConfig);
//        attackerSimulation.init(launchConfig);
//        terminationCondition.init(launchConfig);
        timeProgressor.init(launchConfig);

//        LOG.info("Using power load simulation: " + powerLoadSimulation.getName());
//        LOG.info("Using KRITIS simulation: " + kritisSimulation.getName());
//        LOG.info("Using impact analysis: " + impactAnalsis.getName());
//        LOG.info("Using attacker simulation: " + attackerSimulation.getName());
//        LOG.info("Using termination condition: " + terminationCondition.getName());
        LOG.info("Using time progressor: " + timeProgressor.getName());

        reactiveSimControl = new ReactiveSimulationController();
        reactiveSimControl.init(outputPath, topoPath, inputPath);
    }

//    private void determineWorkingDirPath(final ILaunchConfiguration launchConfig) throws CoreException {
//        String initialPath = launchConfig.getAttribute(Constants.OUTPUT_PATH_KEY, "");
//        String currentPath = initialPath;
//        int runningNumber = 0;
//        while (new File(currentPath).exists()) {
//            LOG.info("Exists already: " + currentPath);
//
//            currentPath = removeTrailingSeparator(initialPath) + runningNumber + '\\';
//            runningNumber++;
//        }
//        workingDirPath = currentPath;
//        LOG.info("Working dir is: " + workingDirPath);
//    }

//    private String removeTrailingSeparator(String initialPath) {
//        if (initialPath.endsWith("\\")) {
//            return initialPath.substring(0, initialPath.length() - 1);
//        }
//        return initialPath;
//    }

    /**
     * @throws CoreException
     */
    private void loadCustomUserAnalysis(final ILaunchConfiguration launchConfig) throws CoreException {
        final SimulationExtensionPointHelper helper = new SimulationExtensionPointHelper();
//
//        final List<IAttackerSimulation> attack = helper.getAttackerSimulationExtensions();
//        for (final IAttackerSimulation e : attack) {
//
//            if (launchConfig.getAttribute(Constants.ATTACKER_SIMULATION_CONFIG, "").equals(e.getName())) {
//                attackerSimulation = e;
//            }
//        }
//
//        final List<IPowerLoadSimulationWrapper> power = helper.getPowerLoadSimulationExtensions();
//        for (final IPowerLoadSimulationWrapper e : power) {
//
//            if (launchConfig.getAttribute(Constants.POWER_LOAD_SIMULATION_CONFIG, "").equals(e.getName())) {
//                powerLoadSimulation = e;
//            }
//        }
//
//        final List<IKritisSimulationWrapper> kritis = helper.getKritisSimulationExtensions();
//        for (final IKritisSimulationWrapper e : kritis) {
//
//            if (launchConfig.getAttribute(Constants.KRITIS_SIMULATION_CONFIG, "").equals(e.getName())) {
//                kritisSimulation = e;
//            }
//        }
//
//        final List<ITerminationCondition> termination = helper.getTerminationConditionExtensions();
//        for (final ITerminationCondition e : termination) {
//
//            if (launchConfig.getAttribute(Constants.TERMINATION_CONDITION_SIMULATION_CONFIG, "").equals(e.getName())) {
//                terminationCondition = e;
//            }
//        }
//
        final List<ITimeProgressor> time = helper.getProgressorExtensions();
        for (final ITimeProgressor e : time) {

            if (launchConfig.getAttribute(Constants.TIME_PROGRESSOR_SIMULATION_CONFIG, "").equals(e.getName())) {
                timeProgressor = e;
            }
        }
//
//        final List<IImpactAnalysis> impact = helper.getImpactAnalysisExtensions();
//        for (final IImpactAnalysis e : impact) {
//
//            if (launchConfig.getAttribute(Constants.IMPACT_ANALYSIS_SIMULATION_CONFIG, "").equals(e.getName())) {
//                impactAnalsis = e;
//            }
//        }
    }
}
