/**
 * 
 */
package smartgrid.simcontrol.coupling;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tim
 */
public class DummySimulationController implements ISimulationController {

    /*
     * (non-Javadoc)
     * 
     * @see smartgrid.simcontrol.coupling.ISimulationController#run(java.util.Map)
     */
    @Override
    public Map<String, Map<String, Double>> runAndGetPowerSupplied(Map<String, Map<String, PowerSpec>> kritisPowerDemand) {
        System.out.println("RMI: run called");
        return new HashMap<>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see smartgrid.simcontrol.coupling.ISimulationController#shutDown()
     */
    @Override
    public void terminate() {
        System.out.println("RMI: shutdown called");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            // likely to work without security manager
//      if (System.getSecurityManager() == null) {
//        System.setSecurityManager(new SecurityManager());
//      }
            DummySimulationController endpoint = new DummySimulationController();
            ISimulationController endpoint_stub = (ISimulationController) UnicastRemoteObject.exportObject(endpoint, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("ISimulationController", endpoint_stub);
            System.out.println("RMI server initialised");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initActive() {
        System.out.println("RMI: init active called");
    }

    @Override
    public void initReactive(String outputPath, String topoPath, String inputStatePath) {
        System.out.println("RMI: init reactive called");
    }

    @Override
    public void initTopo(TopologyContainer _topologyContainer) {
        // TODO Auto-generated method stub

    }

}
