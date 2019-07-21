package smartgrid.simcontrol.test.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import couplingToICT.ISimulationController;
import couplingToICT.SimcontrolException;
import couplingToICT.SmartComponentGeoData;
import couplingToICT.SmartGridTopoContainer;

public class Client {

	private static final Logger LOG = Logger.getLogger(Client.class);
	static boolean error;
	static ISimulationController connector;
	static boolean init;
	
	public static void main(String[] args) throws RemoteException, SimcontrolException {
		BasicConfigurator.configure();
		
		initRMI();
		try {
			LinkedHashMap<String, Map<String, SmartComponentGeoData>> _iedContainer = null;
			LinkedHashMap<String, Map<String, SmartComponentGeoData>> _smartMeterContainer = null;
			
			SmartGridTopoContainer topoContainer = new SmartGridTopoContainer(_smartMeterContainer, _iedContainer);
			LOG.info("Init topo will be called");
			connector.initTopo(topoContainer);
			connector.getModifiedPowerSpec(null, null);
			connector.terminate();
			init = false;
		} catch (RemoteException | SimcontrolException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void initRMI() {
	    String hostName;
	    String lookupName;
	    Registry registry;

	    if (init)
	    	return;
	    
	    error = false;
	    try {
		  System.setProperty("java.security.policy","file:java.policy");
	      if (System.getSecurityManager() == null) {
	        System.setSecurityManager(new SecurityManager());
	      }


	      hostName = "localhost";
	      lookupName = "ISimulationController";
	      
	      registry = LocateRegistry.getRegistry(hostName);
	      LOG.info("RMI remote connector registry initialised (" + hostName + ")");
	      
	      connector = (ISimulationController) registry.lookup(lookupName);
	      connector.initActive();
	      
	      LOG.debug("RMI connector activated (" + lookupName + ")");
	    } catch (Exception e) {
	      LOG.error("Failed to connect to RMI server", e);
	      e.printStackTrace();
	      error = true;
	      connector = null; // explicit setting
	    }
	  }
}
