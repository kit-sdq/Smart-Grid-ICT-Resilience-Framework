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
import smartgrid.simcontrol.coupling.SmartMeterGeoData;
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
    public void startServer() {
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
    public void shutDownServer() {
        if (registry != null) {
            try {
                registry.unbind("ISimulationController");
                LOG.info("SimControl RMI server shutdown successfull");
            } catch (RemoteException e) {
                LOG.error("SimControl RMI server shutdown failed", e);
            } catch (NotBoundException e) {
                LOG.warn("SimControl RMI server shutdown failed: port was unbound. This is unexpected but harmless.", e);
            }
            registry = null;
        }
    }

    @Override
    public void initActive() throws SimcontrolException {
        LOG.info("init active called remotely");
        if (state != RmiServerState.NOT_INIT) {
            LOG.warn(ERROR_SERVER_ALREADY_INITIALIZED);
            throw new SimcontrolException(ERROR_SERVER_ALREADY_INITIALIZED);
        }
        state = RmiServerState.ACTIVE;
    }

    @Override
    public void initReactive(String outputPath, String topoPath, String inputStatePath) throws SimcontrolException {
        LOG.info("init reactive called remotely");
        if (state != RmiServerState.NOT_INIT) {
            LOG.warn(ERROR_SERVER_ALREADY_INITIALIZED);
            throw new SimcontrolException(ERROR_SERVER_ALREADY_INITIALIZED);
        }
        state = RmiServerState.REACTIVE;

        reactiveSimControl = new ReactiveSimulationController();
        reactiveSimControl.init(outputPath, topoPath, inputStatePath);
        try {
            // To-do initiator (KRITIS Sim) should be able to choose analyses
            // To-do initiator should send init data
            reactiveSimControl.loadDefaultAnalyses();
        } catch (CoreException e) {
            throw new SimcontrolInitializationException("Simcontrol failed to initialize all simulation components.", e);
        }
    }

    @Override
    public void initTopo(Map<String, Map<String, SmartMeterGeoData>> smartMeterGeoData) {
        LOG.info("init topo called remotely");
        BlockingKritisDataExchanger.storeGeoData(smartMeterGeoData);
//        if (state == RmiServerState.NOT_INIT) {
//            LOG.warn(ERROR_SERVER_NOT_INITIALIZED);
//            throw new SimcontrolException(ERROR_SERVER_NOT_INITIALIZED);
//        } else if (state == RmiServerState.REACTIVE) {
//            // To-do implement
//        } else {
//            // To-do implement
//        }
    }

    @Override
    public Map<String, Map<String, Double>> runAndGetPowerSupplied(Map<String, Map<String, PowerSpec>> kritisPowerDemand) throws SimcontrolException, InterruptedException {
        LOG.info("run was called remotely");
        Map<String, Map<String, Double>> powerSupply;
        if (state == RmiServerState.ACTIVE) {
            powerSupply = BlockingKritisDataExchanger.getDataFromCoupling(kritisPowerDemand);
        } else if (state == RmiServerState.REACTIVE) {
            powerSupply = reactiveSimControl.run(kritisPowerDemand);
        } else {
            LOG.warn(ERROR_SERVER_NOT_INITIALIZED);
            throw new SimcontrolException(ERROR_SERVER_NOT_INITIALIZED);
        }
        return powerSupply;
    }

    @Override
    public void shutDown() throws SimcontrolException {
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
