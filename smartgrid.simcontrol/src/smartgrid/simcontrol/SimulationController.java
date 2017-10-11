package smartgrid.simcontrol;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.helper.SimulationExtensionPointHelper;
import smartgrid.log4j.LoggingInitializer;
import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;
import smartgrid.simcontrol.coupling.PowerSpec;

public final class SimulationController {

    private static final Logger LOG = Logger.getLogger(SimulationController.class);

    private ReactiveSimulationController reactiveSimControl;
    private IKritisSimulationWrapper kritisSimulation;

    private int maxTimeSteps;

    public void run() throws InterruptedException {
        Map<String, Map<String, PowerSpec>> kritisPowerDemand;
        Map<String, Map<String, Double>> powerSupply = null;

        // one iteration computes one timestep
        for (int timeStep = 0; timeStep < maxTimeSteps; timeStep++) {

            if (timeStep == 0) {
                kritisPowerDemand = kritisSimulation.getDefaultDemand();
            } else {
                kritisPowerDemand = kritisSimulation.run(powerSupply);
            }

            powerSupply = reactiveSimControl.run(kritisPowerDemand);
        }

        LOG.info("Coupled simulation terminated internally");
        reactiveSimControl.shutDown();
    }

    public void init(final ILaunchConfiguration launchConfig) throws CoreException {

        LoggingInitializer.initialize();

        LOG.debug("init active launch config");

        // read paths
        String outputPath = launchConfig.getAttribute(Constants.OUTPUT_PATH_KEY, "");
        String inputPath = launchConfig.getAttribute(Constants.INPUT_PATH_KEY, "");
        String topoPath = launchConfig.getAttribute(Constants.TOPOLOGY_PATH_KEY, "");

        /*
         * Check whether this is really an Integer already done in
         * smartgrid.simcontrol.ui.SimControlLaunchConfigurationTab. So just parsing to Int
         */
        maxTimeSteps = Integer.parseUnsignedInt(launchConfig.getAttribute(Constants.TIMESTEPS_KEY, ""));
        LOG.info("Running for " + maxTimeSteps + " time steps");

        reactiveSimControl = new ReactiveSimulationController();
        reactiveSimControl.init(outputPath, topoPath, inputPath);
        reactiveSimControl.loadCustomUserAnalysis(launchConfig);

        kritisSimulation = SimulationExtensionPointHelper.findExtension(launchConfig, SimulationExtensionPointHelper.getKritisSimulationExtensions(), Constants.KRITIS_SIMULATION_CONFIG,
                IKritisSimulationWrapper.class);
        kritisSimulation.init(launchConfig);
        LOG.info("Using KRITIS simulation: " + kritisSimulation.getName());
    }

    public void shutDown() {
        LOG.info("Coupled simulation terminated externally");
        reactiveSimControl.shutDown();
    }
}
