package smartgrid.newsimcontro.tests.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import couplingToICT.ISimulationController;
import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SimcontrolException;
import couplingToICT.SmartGridTopoContainer;

public class Client {

	private static final Logger LOG = Logger.getLogger(Client.class);
	static boolean error;
	static boolean init;
	private static final String path = "/home/majuwa/tmp/";
	
	static ISimulationController connector;
	
	
	public static void main(String[] args) throws SimcontrolException, IOException, ClassNotFoundException {
		
		String path = Client.path + "outputTopoContainer";
		ObjectInputStream objectInputStream =
			    new ObjectInputStream(new FileInputStream(path));
		SmartGridTopoContainer topoContainer = (SmartGridTopoContainer) objectInputStream.readObject();
	    objectInputStream.close();
	    
	    String path2 = Client.path + "outputPowerAssigned";
		ObjectInputStream objectInputStream2 =
			    new ObjectInputStream(new FileInputStream(path2));
		PowerSpecContainer powerSpec = (PowerSpecContainer) objectInputStream2.readObject();
		objectInputStream2.close();
		
		
		BasicConfigurator.configure();
		
//		LinkedHashMap<String, Map<String, SmartComponentGeoData>> _iedContainer = null;
//		LinkedHashMap<String, Map<String, SmartComponentGeoData>> _smartMeterContainer = null;
//		SmartGridTopoContainer topoContainer = new SmartGridTopoContainer(_smartMeterContainer, _iedContainer);
//		
//		LinkedHashMap<String, Map<String, PowerSpec>> _powerDemands = new LinkedHashMap<String, Map<String,PowerSpec>>();
//		LinkedHashMap<String, Map<String, PowerSpec>> _powerInfeeds = new LinkedHashMap<String, Map<String,PowerSpec>>();
//		PowerSpecContainer powerSpecs = new PowerSpecContainer(_powerDemands ,_powerInfeeds );
//		
		LinkedHashMap<String, HashMap<String, Double>> _powerAssigned = new LinkedHashMap<String, HashMap<String,Double>>();;
		PowerAssigned powerAssigned = new PowerAssigned(_powerAssigned);

		initRMI();
		
		try {

			connector.initTopo(topoContainer);
			init = false;
		} catch (RemoteException | SimcontrolException e) {
			e.printStackTrace();
		} 
		try {
			connector.getModifiedPowerSpec(powerSpec, powerAssigned);
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
	      System.setProperty("java.rmi.server.hostname","localhost");

	      hostName = "localhost";
	      lookupName = "ISimulationController";
	      
	      registry = LocateRegistry.getRegistry(hostName);
	      //LOG.info("RMI remote connector registry initialised (" + hostName + ")");
	      
	      connector = (ISimulationController) registry.lookup(lookupName);
	      connector.initActive();
	      //LOG.debug("RMI connector activated (" + lookupName + ")");
	    } catch (Exception e) {
	      //LOG.error("Failed to connect to RMI server", e);
	      e.printStackTrace();
	      error = true;
	      connector = null; // explicit setting
	    }
	  }
}
