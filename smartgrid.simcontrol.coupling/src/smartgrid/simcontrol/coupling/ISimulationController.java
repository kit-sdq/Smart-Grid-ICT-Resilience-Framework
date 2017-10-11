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
    void initTopo(Map<String, Map<String, SmartMeterGeoData>> _smartMeterGeoData) throws RemoteException, SimcontrolException, InterruptedException;

    /**
     * Get a list of power supplied to ... Remotely aborting leaves an undefined state and requires
     * the connection to be reinitialised before continuing.
     * 
     * @param kritisPowerDemand
     * @return
     * @throws RemoteException
     *             if
     * @throws SimcontrolException
     * @throws InterruptedException
     *             if aborted remotely
     */
    Map<String, Map<String, Double>> runAndGetPowerSupplied(Map<String, Map<String, PowerSpec>> kritisPowerDemand) throws RemoteException, SimcontrolException, InterruptedException;

    /**
     * Signals the remote application to perform a controlled shutdown.
     * 
     * @throws RemoteException
     * @throws SimcontrolException
     */
    void shutDown() throws RemoteException, SimcontrolException;

}
