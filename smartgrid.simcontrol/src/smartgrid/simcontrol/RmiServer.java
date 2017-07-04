package smartgrid.simcontrol;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import smartgrid.simcontrol.coupling.ISimulationController;
import smartgrid.simcontrol.coupling.PowerSpec;
import smartgrid.simcontrol.coupling.SimcontrolException;

/**
 * @author Misha
 */
public class RmiServer implements ISimulationController {

    private static final Logger LOG = Logger.getLogger(RmiServer.class);

    private RmiServerState state = RmiServerState.NOT_INIT;

    public void initServer() {
        try {
            ISimulationController stub = (ISimulationController) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("ISimulationController", stub);
            LOG.info("RMI server running");
        } catch (Exception e) {
            LOG.error("Could not start RMI server");
            e.printStackTrace();
        }
    }

    @Override
    public void initActive() throws RemoteException {
        LOG.info("init active called remotely");
        state = RmiServerState.ACTIVE;
    }

    @Override
    public void initReactive(String outputPath, String topoPath, String inputStatePath) throws RemoteException, SimcontrolException {
        LOG.info("init reactive called remotely");
        state = RmiServerState.REACTIVE;
    }

    @Override
    public Map<String, Map<String, Double>> run(Map<String, Map<String, PowerSpec>> kritisPowerDemand) throws RemoteException {
        LOG.info("run was called remotely");
        if (state == RmiServerState.ACTIVE) {

        } else {

        }
        return new HashMap<String, Map<String, Double>>();
    }

    @Override
    public void shutDown() throws RemoteException {
        LOG.info("shutDown was called remotely");
        state = RmiServerState.NOT_INIT;
    }

    public void shutDownServer() {
        // TODO properly shut down this server
    }

    private enum RmiServerState {
        NOT_INIT, ACTIVE, REACTIVE;
    }
}
