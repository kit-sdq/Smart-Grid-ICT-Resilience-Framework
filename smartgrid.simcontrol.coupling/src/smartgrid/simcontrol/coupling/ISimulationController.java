package smartgrid.simcontrol.coupling;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import smartgrid.simcontrol.coupling.Exceptions.SimcontrolException;

public interface ISimulationController extends Remote {

    void initActive() throws RemoteException, SimcontrolException;

    void initReactive(String outputPath, String topoPath, String inputStatePath) throws RemoteException, SimcontrolException;

    Map<String, Map<String, Double>> run(Map<String, Map<String, PowerSpec>> kritisPowerDemand) throws RemoteException, SimcontrolException;

    void shutDown() throws RemoteException, SimcontrolException;
}
