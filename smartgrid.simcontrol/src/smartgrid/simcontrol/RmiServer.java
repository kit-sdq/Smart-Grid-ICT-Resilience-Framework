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

public class RmiServer implements ISimulationController {

    private static final Logger LOG = Logger.getLogger(RmiServer.class);

    //TODO connect with simControl
    private final ISimulationController simControl;

    public RmiServer() {
        simControl = new StaticSimulationController();
    }

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
    public void init(String outputPath, String topoPath, String inputStatePath) throws RemoteException {
        LOG.info("init was called remotely");
    }

    @Override
    public Map<String, Map<String, Double>> run(Map<String, Map<String, PowerSpec>> kritisPowerDemand) throws RemoteException {
        LOG.info("run was called remotely");
        return new HashMap<String, Map<String,Double>>();
    }

    @Override
    public void shutDown() throws RemoteException {
        // TODO properly shutdown this server
        LOG.info("shutDown was called remotely");
    }
}
