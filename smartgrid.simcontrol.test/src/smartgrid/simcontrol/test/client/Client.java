package smartgrid.simcontrol.test.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import couplingToICT.ISimulationController;
import couplingToICT.PowerAssigned;
import couplingToICT.PowerDemand;
import couplingToICT.PowerSpec;
import couplingToICT.SimcontrolException;
import couplingToICT.SmartComponentGeoData;
import couplingToICT.SmartGridTopoContainer;

public class Client {

	private static final Logger LOG = Logger.getLogger(Client.class);
	static boolean error;
	static boolean init;
	
	static ISimulationController connector;
	
	
	public static void main(String[] args) throws RemoteException, SimcontrolException {
		BasicConfigurator.configure();
		
		LinkedHashMap<String, Map<String, SmartComponentGeoData>> _iedContainer = null;
		LinkedHashMap<String, Map<String, SmartComponentGeoData>> _smartMeterContainer = null;
		SmartGridTopoContainer topoContainer = new SmartGridTopoContainer(_smartMeterContainer, _iedContainer);
		
		LinkedHashMap<String, HashMap<String, PowerSpec>> _powerDemands = new LinkedHashMap<String, HashMap<String,PowerSpec>>();
		PowerDemand powerDemand = new PowerDemand(_powerDemands );
		
		LinkedHashMap<String, HashMap<String, Double>> _powerAssigned = new LinkedHashMap<String, HashMap<String,Double>>();;
		PowerAssigned powerAssigned = new PowerAssigned(_powerAssigned);

		initRMI();
		
		try {

			LOG.info("Init topo will be called");
			connector.initTopo(topoContainer);
			init = false;
		} catch (RemoteException | SimcontrolException e) {
			e.printStackTrace();
		} 
		try {
			connector.getModifiedPowerSpec(powerDemand, powerAssigned);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		connector.terminate();
		
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
