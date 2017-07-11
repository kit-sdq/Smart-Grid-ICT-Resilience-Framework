package smartgrid.simcontrol.coupling;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import smartgrid.simcontrol.coupling.Exceptions.SimcontrolException;

/**
 * Initializing topological quantities within the connected ICT-module, retrieving supplied power
 * acoording to the conditions described in the power specific data sheet per CI entity
 */

public interface ISimulationController extends Remote {

    void initActive() throws RemoteException, SimcontrolException;

    void initReactive(String outputPath, String topoPath, String inputStatePath) throws RemoteException, SimcontrolException;

    //input data for initializing ICT/power grid -topology
    void initTopo(Map<String, Map<String, SmartMeterGeoData>> _smartMeterGeoData) throws RemoteException, SimcontrolException;

    //receiving calculated suppliable power
    Map<String, Map<String, Double>> runAndGetPowerSupplied(Map<String, Map<String, PowerSpec>> kritisPowerDemand) throws RemoteException, SimcontrolException;

    void shutDown() throws RemoteException, SimcontrolException;
}
