package smartgrid.newsimcontrol.tests.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.log4j.BasicConfigurator;

import couplingToICT.ISimulationControllerRemote;
import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SimcontrolException;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.initializer.AttackerSimulationsTypes;
import couplingToICT.initializer.InitializationMapKeys;
import couplingToICT.initializer.PowerSpecsModificationTypes;

public class Client {

	static boolean error;
	static boolean init;
	private static final String path = "/users/mazenebada/tmp/";
	
	static ISimulationControllerRemote connector;
	
	
	public static void main(String[] args) throws SimcontrolException, IOException, ClassNotFoundException {
		System.out.println("Start");
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
			System.out.println("Init Topo");
			connector.initTopo(topoContainer);
			init = false;
		} catch (RemoteException | SimcontrolException e) {
			e.printStackTrace();
		} 
		try {
			var test = connector.getModifiedPowerSpec(powerSpec, powerAssigned);
			connector.getModifiedPowerSpec(test,powerAssigned);
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
	      
	      connector = (ISimulationControllerRemote) registry.lookup(lookupName);
	      var initMap = new HashMap<InitializationMapKeys, String>();
			initMap.put(InitializationMapKeys.INPUT_PATH_KEY, "");
			initMap.put(InitializationMapKeys.OUTPUT_PATH_KEY, "/home/majuwa/git/Smart-Grid-ICT-Resilience-Framework/examples/smartgrid.model.examples");
			initMap.put(InitializationMapKeys.TOPO_PATH_KEY, "");
			initMap.put(InitializationMapKeys.TOPO_GENERATION_KEY, Boolean.toString(true));
			initMap.put(InitializationMapKeys.IGNORE_LOC_CON_KEY, Boolean.toString(true));
			initMap.put(InitializationMapKeys.HACKING_SPEED_KEY, Integer.toString(1));
			initMap.put(InitializationMapKeys.TIME_STEPS_KEY, Integer.toString(1));
			initMap.put(InitializationMapKeys.ROOT_NODE_ID_KEY, "");
			initMap.put(InitializationMapKeys.HACKING_STYLE_KEY, null);
			initMap.put(InitializationMapKeys.ATTACKER_SIMULATION_KEY,
					AttackerSimulationsTypes.NO_ATTACK_SIMULATION.toString());
			initMap.put(InitializationMapKeys.POWER_MODIFY_KEY, PowerSpecsModificationTypes.NO_CHANGE_MODIFIER.toString());

	      connector.initConfiguration(initMap);
	      //LOG.debug("RMI connector activated (" + lookupName + ")");
	    } catch (Exception e) {
	      //LOG.error("Failed to connect to RMI server", e);
	      e.printStackTrace();
	      error = true;
	      connector = null; // explicit setting
	    }
	  }
}
