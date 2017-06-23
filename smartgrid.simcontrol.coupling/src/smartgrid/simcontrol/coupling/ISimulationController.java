package smartgrid.simcontrol.coupling;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ISimulationController extends Remote {

    void init(String outputPath, String topoPath, String inputStatePath) throws RemoteException, SimcontrolException;

    Map<String, Map<String, Double>> run(Map<String, Map<String, PowerSpec>> kritisPowerDemand) throws RemoteException;

    void shutDown() throws RemoteException;
}
