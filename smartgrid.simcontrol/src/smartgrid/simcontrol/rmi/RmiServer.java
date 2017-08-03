package smartgrid.simcontrol.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import org.apache.log4j.Logger;

import smartgrid.simcontrol.ReactiveSimulationController;
import smartgrid.simcontrol.coupling.ISimulationController;
import smartgrid.simcontrol.coupling.PowerSpec;
import smartgrid.simcontrol.coupling.SmartMeterGeoData;
import smartgrid.simcontrol.coupling.Exceptions.SimcontrolException;

/**
 * @author Misha
 */
public class RmiServer implements ISimulationController {

    private static final Logger LOG = Logger.getLogger(RmiServer.class);

    private static final String ERROR_SERVER_NOT_INITIALIZED = "The SimControl RMI server is not initialized.";
    private static final String ERROR_SERVER_ALREADY_INITIALIZED = "The SimControl RMI server is already initialized.";
    @SuppressWarnings("unused")
    private static final String ERROR_NOT_IMPLEMENTED = "Not yet implemented.";

    private ReactiveSimulationController reactiveSimControl;
    private Registry registry;
    private RmiServerState state = RmiServerState.NOT_INIT;

    private enum RmiServerState {
        NOT_INIT, ACTIVE, REACTIVE;
    }

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
    }

    @Override
    public void initTopo(Map<String, Map<String, SmartMeterGeoData>> smartMeterGeoData) {
        LOG.info("init topo called remotely");
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
    public Map<String, Map<String, Double>> runAndGetPowerSupplied(Map<String, Map<String, PowerSpec>> kritisPowerDemand) throws SimcontrolException {
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
