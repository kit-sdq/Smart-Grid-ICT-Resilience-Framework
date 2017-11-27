package smartgrid.simcontrol.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;

import smartgrid.simcontrol.ReactiveSimulationController;
import smartgrid.simcontrol.SimcontroLaunchConfigurationDelegate;
import smartgrid.simcontrol.SimulationController;
import smartgrid.simcontrol.coupling.ISimulationController;
import smartgrid.simcontrol.coupling.PowerSpec;
import smartgrid.simcontrol.coupling.TopologyContainer;
import smartgrid.simcontrol.coupling.Exceptions.SimcontrolException;
import smartgrid.simcontrol.coupling.Exceptions.SimcontrolInitializationException;

/**
 * This class acts as RMI Server for the KRITIS simulation of the IKET. The server is always
 * registered when Eclipse starts by the early {@link Startup} hook of the plugin. It also
 * implements the RMI Interface of the simulation coupling. The server delegates to the simulation
 * coupling either in active or reactive mode.
 * 
 * In <b>active mode</b>, the {@link SimulationController} has to be started via the
 * {@link SimcontroLaunchConfigurationDelegate}. The simulation coupling and the KRITIS simulation
 * will synchronize and exchange data using the {@link BlockingKritisDataExchanger}.
 * 
 * In <b>reactive mode</b>, the {@link ReactiveSimulationController} will be used. All control flow
 * will originate from the KRITIS simulation, thus no synchronization is required. However, all
 * configuration must also be passed in via the KRITIS simulation.
 * 
 * @author Misha
 */
public class RmiServer implements ISimulationController {

    private static final Logger LOG = Logger.getLogger(RmiServer.class);

    private static final String ERROR_SERVER_NOT_INITIALIZED = "The SimControl RMI server is not initialized.";
    private static final String ERROR_SERVER_ALREADY_INITIALIZED = "The SimControl RMI server is already initialized.";

    private static RmiServer instance;

    public static synchronized void ensureRunning() {
        if (instance == null) {
            instance = new RmiServer();
            instance.startServer();
        }
    }

    public static synchronized void resetState() {
        if (instance != null) {
            instance.state = RmiServerState.NOT_INIT;
            LOG.info("The state of the RMI Server was reset to \"not initiated\".");
        }
    }

    public static synchronized void shutDown() {
        if (instance != null) {
            instance.shutDownServer();
            instance = null;
        }
    }

    /**
     * The {@link ReactiveSimulationController} is only initialized and used in reactive mode.
     */
    private ReactiveSimulationController reactiveSimControl;

    /**
     * The registry is used to bind and unbind the server (on start and shutdown).
     */
    private Registry registry;

    /**
     * This state of the server enforces a meaningful call sequence protocol.
     */
    private RmiServerState state = RmiServerState.NOT_INIT;

    private enum RmiServerState {
        NOT_INIT, ACTIVE, REACTIVE;
    }

    /**
     * Binds the server to port 1099 ({@link java.rmi.registry.Registry.REGISTRY_PORT}).
     */
    private void startServer() {
        try {
            ISimulationController stub = (ISimulationController) UnicastRemoteObject.exportObject(this, 0);
            registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registry.bind("ISimulationController", stub);
            LOG.info("RMI server running");
        } catch (Exception e) {
            LOG.error("Could not start RMI server", e);
        }
    }

    /**
     * Properly unbinds the server from the port.
     */
    private void shutDownServer() {
        if (registry != null) {
            try {
                registry.unbind("ISimulationController");
                LOG.info("SimControl RMI server shutdown successful");
            } catch (RemoteException e) {
                LOG.error("SimControl RMI server shutdown failed", e);
            } catch (NotBoundException e) {
                LOG.warn("SimControl RMI server shutdown failed: port was unbound. This is unexpected but harmless.", e);
            }
            registry = null;
        }
    }

    @Override
    public void initActive() {
        LOG.info("init active called remotely");
        if (state != RmiServerState.NOT_INIT) {
            LOG.warn(ERROR_SERVER_ALREADY_INITIALIZED);
        }
        state = RmiServerState.ACTIVE;
    }

    @Override
    public void initReactive(String outputPath, String topoPath, String inputStatePath) throws SimcontrolException {
        LOG.info("init reactive called remotely");
        if (state != RmiServerState.NOT_INIT) {
            LOG.warn(ERROR_SERVER_ALREADY_INITIALIZED);
        }
        state = RmiServerState.REACTIVE;

        reactiveSimControl = new ReactiveSimulationController();
        reactiveSimControl.init(outputPath);
        reactiveSimControl.initModelsFromFiles(topoPath, inputStatePath); // To-do conditional auto generation
        try {
            // To-do initiator (KRITIS Sim) should be able to choose analyses
            // To-do initiator should send init data
            reactiveSimControl.loadDefaultAnalyses();
        } catch (CoreException e) {
            throw new SimcontrolInitializationException("Simcontrol failed to initialize all simulation components.", e);
        }
    }

    @Override
    public void initTopo(TopologyContainer topo) {
        LOG.info("init topo called remotely");
        BlockingKritisDataExchanger.storeGeoData(topo);
    }

    @Override
    public Map<String, Map<String, Double>> runAndGetPowerSupplied(Map<String, Map<String, PowerSpec>> kritisPowerDemand) throws SimcontrolException, InterruptedException {
        LOG.info("run was called remotely");
        Map<String, Map<String, Double>> powerSupply;
        if (state == RmiServerState.ACTIVE) {
            try {
                powerSupply = BlockingKritisDataExchanger.getDataFromCoupling(kritisPowerDemand);
            } catch (InterruptedException e) {
                throw e;
            } catch (Throwable e) {
                resetState();
                BlockingKritisDataExchanger.freeAll();
                LOG.info("The stored exception that originally occured in SimControl was passed to the remote KRITIS simulation. The RMI server and data exchange are now reset.");
                throw new SimcontrolException("There was an exception in SimControl. The RMI server has now been reset to 'uninitialized'.", e);
            }
        } else if (state == RmiServerState.REACTIVE) {
            powerSupply = reactiveSimControl.run(kritisPowerDemand);
        } else {
            LOG.warn(ERROR_SERVER_NOT_INITIALIZED);
            throw new SimcontrolException(ERROR_SERVER_NOT_INITIALIZED);
        }
        return powerSupply;
    }

    @Override
    public void terminate() throws SimcontrolException {
        LOG.info("shutDown was called remotely");
        if (state == RmiServerState.NOT_INIT) {
            LOG.warn(ERROR_SERVER_NOT_INITIALIZED);
            throw new SimcontrolException(ERROR_SERVER_NOT_INITIALIZED);
        }
        // shut down if reactive
        else if (state == RmiServerState.REACTIVE) {
            reactiveSimControl.shutDown();
        }
        // To-do here, the active simcontrol could be shutDown if there was a pointer (rendezvous required)

        state = RmiServerState.NOT_INIT;
    }
}
