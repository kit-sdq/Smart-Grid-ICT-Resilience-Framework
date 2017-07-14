package smartgrid.simcontrol;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.emf.ecore.util.EcoreUtil;

import smartgrid.helper.FileSystemHelper;
import smartgrid.helper.ScenarioModelHelper;
import smartgrid.helper.SimulationExtensionPointHelper;
import smartgrid.log4j.LoggingInitializer;
import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.coupling.IAttackerSimulation;
import smartgrid.simcontrol.baselib.coupling.IImpactAnalysis;
import smartgrid.simcontrol.baselib.coupling.IPowerLoadSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.ITerminationCondition;
import smartgrid.simcontrol.baselib.coupling.ITimeProgressor;
import smartgrid.simcontrol.coupling.ISmartMeterState;
import smartgrid.simcontrol.coupling.PowerSpec;
import smartgrid.simcontrol.coupling.SmartMeterState;
import smartgrid.simcontrol.coupling.Exceptions.SimcontrolException;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridoutput.Defect;
import smartgridoutput.EntityState;
import smartgridoutput.NoPower;
import smartgridoutput.NoUplink;
import smartgridoutput.On;
import smartgridoutput.Online;
import smartgridoutput.ScenarioResult;
import smartgridtopo.NetworkEntity;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;

public final class ReactiveSimulationController {

    private static final Logger LOG = Logger.getLogger(ReactiveSimulationController.class);

    private IPowerLoadSimulationWrapper powerLoadSimulation;
    private IImpactAnalysis impactAnalsis;
    private IAttackerSimulation attackerSimulation;
    private ITerminationCondition terminationCondition;
    private ITimeProgressor timeProgressor;

    private String workingDirPath;
    private SmartGridTopology topo;
    private ScenarioState initialState;

    private FileAppender fileAppender;

    // Simulation State
    private int timeStep;
    private ScenarioState impactInputOld;
    private ScenarioState impactInput;
    private ScenarioResult impactResultOld;
    private Map<String, Map<String, Double>> powerSupply;

    private Map<String, Map<String, ISmartMeterState>> emptyPowerLoadInput;

    public ReactiveSimulationController() {
        timeStep = 0;
    }

    public Map<String, Map<String, Double>> run(Map<String, Map<String, PowerSpec>> kritisPowerDemand) {

        ensureEmptyPowerInput(kritisPowerDemand);

        // Compute Initial Impact Analysis Result
        ScenarioResult impactResult = impactAnalsis.run(topo, impactInput);

        // Generates Path with default System separators
        final String timeStepPath = new File(workingDirPath + "\\Zeitschritt " + timeStep).getPath();

        impactResult = attackerSimulation.run(topo, impactResult);

        // save attack result to file
        final String attackResultFile = new File(timeStepPath + "\\AttackerSimulationResult.smartgridoutput").getPath();
        FileSystemHelper.saveToFileSystem(impactResult, attackResultFile);

        // impact and power may iterate several times
        int innerLoopIterationCount = 0;
        do {
            final String iterationPath = new File(timeStepPath + "\\Iteration " + innerLoopIterationCount).getPath();

            // run power load simulation
            final Map<String, Map<String, ISmartMeterState>> smartMeterStates = convertToPowerLoadInput(impactResult, kritisPowerDemand);
            powerSupply = powerLoadSimulation.run(kritisPowerDemand, smartMeterStates);

            // copy the input
            impactInputOld = EcoreUtil.copy(impactInput);

            // convert input for impact analysis
            updateImactAnalysisInput(impactInput, impactResult, powerSupply);

            // Save input to file
            final String inputFile = new File(iterationPath + "\\PowerLoadResult.smartgridinput").getPath();
            FileSystemHelper.saveToFileSystem(impactInput, inputFile);

            impactResultOld = impactResult;
            impactResult = impactAnalsis.run(topo, impactInput);

            // Save Result
            final String resultFile = new File(iterationPath + "\\ImpactResult.smartgridoutput").getPath();
            FileSystemHelper.saveToFileSystem(impactResult, resultFile);

            //generate report
            final File resultReportPath = new File(iterationPath + "\\ResultReport.csv");
            ReportGenerator.saveScenarioResult(resultReportPath, impactResult);

            innerLoopIterationCount++;
        } while (terminationCondition.evaluate(innerLoopIterationCount, impactInput, impactInputOld, impactResult, impactResultOld));

        LOG.info("Time step " + timeStep + " terminated after " + innerLoopIterationCount + " power/impact iterations");

        // modify the scenario between time steps
        timeProgressor.progress();

        return powerSupply;
    }

    private void ensureEmptyPowerInput(Map<String, Map<String, PowerSpec>> kritisPowerDemand) {

        // do only once
        if (emptyPowerLoadInput != null) {
            return;
        }

        emptyPowerLoadInput = new HashMap<>();

        // copy the structure of the input
        for (Entry<String, Map<String, PowerSpec>> nodeEntry : kritisPowerDemand.entrySet()) {
            HashMap<String, ISmartMeterState> prosumerMap = new HashMap<>();
            emptyPowerLoadInput.put(nodeEntry.getKey(), prosumerMap);

            for (Entry<String, PowerSpec> prosumerEntry : nodeEntry.getValue().entrySet()) {
                // do not yet insert smartmeter states
                prosumerMap.put(prosumerEntry.getKey(), null);
            }
        }
    }

    public void shutDown() {
        // remove file appender of this run
        Logger.getRootLogger().removeAppender(fileAppender);
        fileAppender.close();
    }

    private Map<String, Map<String, ISmartMeterState>> convertToPowerLoadInput(final ScenarioResult impactResult, Map<String, Map<String, PowerSpec>> kritisPowerDemand) {

        // copy empty map
        final Map<String, Map<String, ISmartMeterState>> powerLoadInput = new HashMap<>(emptyPowerLoadInput);

        // iterate over states and convert them into nested map
        for (final EntityState state : impactResult.getStates()) {
            final NetworkEntity stateOwner = state.getOwner();
            if (stateOwner instanceof SmartMeter) {
                final String prosumerId = Integer.toString(stateOwner.getId());
                String nodeId = findNodeId(prosumerId);
                Map<String, ISmartMeterState> statesForNode = powerLoadInput.get(nodeId);
                if (statesForNode == null) {
                    LOG.error("Could not find node ID (" + nodeId + ") from the ICT Topo in KRITIS data.");
                } else {
                    statesForNode.put(prosumerId, stateToEnum(state));
                }
            }
        }
        return powerLoadInput;
    }

    private String findNodeId(String prosumerId) {
        for (Entry<String, Map<String, ISmartMeterState>> nodeEntry : emptyPowerLoadInput.entrySet()) {
            Map<String, ISmartMeterState> mapOfProsumersInNode = nodeEntry.getValue();
            if (mapOfProsumersInNode.containsKey(prosumerId)) {
                String nodeId = nodeEntry.getKey();
                return nodeId;
            }
        }
        return null;
    }

    private static SmartMeterState stateToEnum(EntityState state) {
        if (state instanceof Online) {
            if (((Online) state).isIsHacked()) {
                return SmartMeterState.ONLINE_HACKED;
            } else {
                return SmartMeterState.ONLINE;
            }
        } else if (state instanceof NoUplink) {
            if (((NoUplink) state).isIsHacked()) {
                return SmartMeterState.NO_UPLINK_HACKED;
            } else {
                return SmartMeterState.NO_UPLINK;
            }
        } else if (state instanceof NoPower) {
            return SmartMeterState.NO_POWER;
        } else if (state instanceof Defect) {
            return SmartMeterState.DEFECT;
        }
        throw new RuntimeException("Unknown EntityState");
    }

    /**
     * 
     * @param impactInput
     *            The outdated ImpactAnalysis input data that should be updated
     * @param impactResult
     *            The result from the last iteration. This also contains the hacked states, which
     *            have to be transfered to impactInput
     * @param powerSupply
     *            The output of the PowerLoad analysis, which has to be interpreted and transfered
     *            to impactInput
     */
    private static void updateImactAnalysisInput(final ScenarioState impactInput, final ScenarioResult impactResult, Map<String, Map<String, Double>> powerSupply) {

        //Transfer hacked state into next input
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

        //Transfer power supply state into next input
        for (final PowerState inputPowerState : impactInput.getPowerStates()) {
            final String prosumerId = Integer.toString(inputPowerState.getOwner().getId());

            // iterate over node entries
            for (final Map<String, Double> powerForProsumersOfNode : powerSupply.values()) {
                Double supply = powerForProsumersOfNode.get(prosumerId);
                assert supply != null;
                inputPowerState.setPowerOutage(isOutage(supply));
            }
        }
    }

    public static boolean isOutage(double supply) {
        // TODO is this really a power outage?
        return supply == 0.0d;
    }

    public void init(String outputPath, String topoPath, String inputStatePath) throws SimcontrolException {

        LoggingInitializer.initialize();

        LOG.debug("init reactive launch config");

        LOG.info("Output: " + outputPath);
        workingDirPath = determineWorkingDirPath(outputPath);
        LOG.info("Working directory: " + workingDirPath);

        // add fileappender for local logs
        final Logger rootLogger = Logger.getRootLogger();
        try {
            Layout layout = ((Appender) rootLogger.getAllAppenders().nextElement()).getLayout();
            fileAppender = new FileAppender(layout, workingDirPath + "\\log.log");
            rootLogger.addAppender(fileAppender);
        } catch (final IOException e) {
            throw new RuntimeException("Error creating local log appender in the working directory. Most likely there are problems with access rights.");
        }

        // load models
        initialState = ScenarioModelHelper.loadInput(inputStatePath);
        impactInput = initialState;
        topo = ScenarioModelHelper.loadScenario(topoPath);
        LOG.info("Scenario input state: " + inputStatePath);
        LOG.info("Topology: " + topoPath);

        // Retrieve simulations from extension points
        try {
            loadDefaultAnalyses();
        } catch (CoreException e) {
            throw new SimcontrolException("Individual simulations could not be created", e);
        }
    }

    private String determineWorkingDirPath(String initialPath) {
        initialPath = removeTrailingSeparator(initialPath);
        String currentPath = initialPath;
        int runningNumber = 0;
        while (new File(currentPath).exists()) {
            LOG.info("Exists already: " + currentPath);

            currentPath = initialPath + runningNumber + '\\';
            runningNumber++;
        }
        LOG.info("Working dir is: " + currentPath);
        return currentPath;
    }

    private String removeTrailingSeparator(String initialPath) {
        if (initialPath.endsWith("\\")) {
            return initialPath.substring(0, initialPath.length() - 1);
        }
        return initialPath;
    }

    private void loadDefaultAnalyses() throws CoreException {
        final SimulationExtensionPointHelper helper = new SimulationExtensionPointHelper();

        // TODO init which attack simulation should be used and initialize it
        final List<IAttackerSimulation> attack = helper.getAttackerSimulationExtensions();
        for (final IAttackerSimulation e : attack) {

            if (e.getName().equals("No Attack Simulation")) {
                attackerSimulation = e;
            }
        }

        // To-do at some point, IIP might want some init data
        final List<IPowerLoadSimulationWrapper> power = helper.getPowerLoadSimulationExtensions();
        for (final IPowerLoadSimulationWrapper e : power) {

            if (e.getName().equals("Power Load Simulation Wrapper")) {
                powerLoadSimulation = e;
            }
        }

        // TODO init! (with number of iterations)
        final List<ITerminationCondition> termination = helper.getTerminationConditionExtensions();
        for (final ITerminationCondition e : termination) {

            if (e.getName().equals("Iteration Count")) {
                terminationCondition = e;
            }
        }

        // TODO init?
        final List<IImpactAnalysis> impact = helper.getImpactAnalysisExtensions();
        for (final IImpactAnalysis e : impact) {

            if (e.getName().equals("Graph Analyzer Impact Analysis")) {
                impactAnalsis = e;
            }
        }

        final List<ITimeProgressor> time = helper.getProgressorExtensions();
        for (final ITimeProgressor e : time) {

            if (e.getName().equals("No Operation")) {
                timeProgressor = e;
            }
        }

        LOG.info("Using power load simulation: " + powerLoadSimulation.getName());
        LOG.info("Using impact analysis: " + impactAnalsis.getName());
        LOG.info("Using attacker simulation: " + attackerSimulation.getName());
        LOG.info("Using termination condition: " + terminationCondition.getName());
        LOG.info("Using time progressor: " + timeProgressor.getName());
    }

    public void loadCustomUserAnalysis(final ILaunchConfiguration launchConfig) throws CoreException {
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

        final List<ITerminationCondition> termination = helper.getTerminationConditionExtensions();
        for (final ITerminationCondition e : termination) {

            if (launchConfig.getAttribute(Constants.TERMINATION_CONDITION_SIMULATION_CONFIG, "").equals(e.getName())) {
                terminationCondition = e;
            }
        }

        final List<IImpactAnalysis> impact = helper.getImpactAnalysisExtensions();
        for (final IImpactAnalysis e : impact) {

            if (launchConfig.getAttribute(Constants.IMPACT_ANALYSIS_SIMULATION_CONFIG, "").equals(e.getName())) {
                impactAnalsis = e;
            }
        }

        final List<ITimeProgressor> time = helper.getProgressorExtensions();
        for (final ITimeProgressor e : time) {

            if (launchConfig.getAttribute(Constants.TIME_PROGRESSOR_SIMULATION_CONFIG, "").equals(e.getName())) {
                timeProgressor = e;
            }
        }

        powerLoadSimulation.init(launchConfig);
        impactAnalsis.init(launchConfig);
        attackerSimulation.init(launchConfig);
        terminationCondition.init(launchConfig);
        timeProgressor.init(launchConfig);

        LOG.info("Using power load simulation: " + powerLoadSimulation.getName());
        LOG.info("Using impact analysis: " + impactAnalsis.getName());
        LOG.info("Using attacker simulation: " + attackerSimulation.getName());
        LOG.info("Using termination condition: " + terminationCondition.getName());
        LOG.info("Using time progressor: " + timeProgressor.getName());
    }
}
