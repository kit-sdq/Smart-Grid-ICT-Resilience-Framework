package smartgrid.simcontrol.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import couplingToICT.ISimulationController;
import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SimcontrolException;
import couplingToICT.SmartGridTopoContainer;
import smartgrid.simcontrol.test.client.Client;

public class RmiServerTest {

	private static final Logger LOG = Logger.getLogger(Client.class);
	static boolean error;
	static boolean init;
	
	static ISimulationController connector;
	static SmartGridTopoContainer topoContainer;
	static PowerSpecContainer powerSpec;
	static PowerAssigned powerAssigned;
	
	/**
     * setup of the input and scenario states
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ClassNotFoundException 
     */
    @BeforeClass
    public static void setup() throws FileNotFoundException, IOException, ClassNotFoundException {
    	String path = "/Users/mazenebada/Hiwi/SmartgridWorkspace/smartgrid.model.examples/outputTopoContainer";
		ObjectInputStream objectInputStream =
			    new ObjectInputStream(new FileInputStream(path));
		topoContainer = (SmartGridTopoContainer) objectInputStream.readObject();
	    objectInputStream.close();
	    
	    String path2 = "/Users/mazenebada/Hiwi/SmartgridWorkspace/smartgrid.model.examples/outputPowerAssigned";
		ObjectInputStream objectInputStream2 =
			    new ObjectInputStream(new FileInputStream(path2));
		powerSpec = (PowerSpecContainer) objectInputStream2.readObject();
		objectInputStream2.close();
		
		
		BasicConfigurator.configure();
		LinkedHashMap<String, HashMap<String, Double>> _powerAssigned = new LinkedHashMap<String, HashMap<String,Double>>();;
		powerAssigned = new PowerAssigned(_powerAssigned);

		
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
    
    @Test
    public static void standardTest() throws RemoteException, SimcontrolException, InterruptedException {
    	
    	initRMI();
		LOG.info("Init topo will be called");
		connector.initTopo(topoContainer);
		init = false;

		connector.getModifiedPowerSpec(powerSpec, powerAssigned);
		connector.terminate();
    }
}
