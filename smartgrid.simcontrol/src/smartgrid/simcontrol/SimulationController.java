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

    private ReactiveSimulationController reactiveSimControl;
    private IKritisSimulationWrapper kritisSimulation;
    private ITimeProgressor timeProgressor;

    private int maxTimeSteps;

    public void run() {
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

            // modify the scenario between time steps
            timeProgressor.progress();
        }

        LOG.info("Coupled simulation terminated internally");
        reactiveSimControl.shutDown();
    }

    public void init(final ILaunchConfiguration launchConfig) throws CoreException, SimcontrolException {

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

        // Retrieve simulations from extension points
        initTimeProgressor(launchConfig);

        // Init time progressor
        timeProgressor.init(launchConfig);
        LOG.info("Using time progressor: " + timeProgressor.getName());

        reactiveSimControl = new ReactiveSimulationController();
        reactiveSimControl.init(outputPath, topoPath, inputPath);
    }

    public void shutDown() {
        LOG.info("Coupled simulation terminated externally");
        reactiveSimControl.shutDown();
    }

    /**
     * @throws CoreException
     */
    private void initTimeProgressor(final ILaunchConfiguration launchConfig) throws CoreException {
        final SimulationExtensionPointHelper helper = new SimulationExtensionPointHelper();

        final List<ITimeProgressor> time = helper.getProgressorExtensions();
        for (final ITimeProgressor e : time) {

            if (launchConfig.getAttribute(Constants.TIME_PROGRESSOR_SIMULATION_CONFIG, "").equals(e.getName())) {
                timeProgressor = e;
            }
        }
    }
}
