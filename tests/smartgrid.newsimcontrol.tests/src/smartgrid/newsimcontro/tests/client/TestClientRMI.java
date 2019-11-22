package smartgrid.newsimcontro.tests.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import couplingToICT.ISimulationController;
import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpec;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SimcontrolInitializationException;
import couplingToICT.SmartComponentGeoData;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.initializer.AttackerSimulationsTypes;
import couplingToICT.initializer.HackingStyle;
import couplingToICT.initializer.InitializationMapKeys;
import couplingToICT.initializer.PowerSpecsModificationTypes;

public class TestClientRMI {
	private static final String hostName = "localhost";
	private static final String lookupName = "ISimulationController";

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Wrong parameter count. Only one parameter with the output path is allowed");
			System.exit(1);
		}
		var powerSpec = createPowerSpecContainer();
		var map = createTopoMap();
		var topoContainer = new SmartGridTopoContainer(map, null);
		LinkedHashMap<String, HashMap<String, Double>> _powerAssigned = new LinkedHashMap<String, HashMap<String, Double>>();
		;
		PowerAssigned powerAssigned = new PowerAssigned(_powerAssigned);
		try {
			var connector = initRMIConnection(createDTOMAP(args[0]));
			connector.initTopo(topoContainer);
			var test = connector.getModifiedPowerSpec(powerSpec, powerAssigned);
			connector.getModifiedPowerSpec(test, powerAssigned);
			connector.terminate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private static LinkedHashMap<String, Map<String, SmartComponentGeoData>> createTopoMap() {
		var smartMeter = new LinkedHashMap<String, Map<String, SmartComponentGeoData>>();

		// insert powerDistrict 1
		var district = new HashMap<String, SmartComponentGeoData>();

		var geodata = new SmartComponentGeoData();
		geodata.setPowerDistrictId(1);
		geodata.setCompName("Test");
		geodata.setCompType("1234");
		district.put("1234", geodata);

		geodata = new SmartComponentGeoData();
		geodata.setPowerDistrictId(1);
		geodata.setCompName("Medicine");
		geodata.setCompType("234");
		district.put("234", geodata);

		geodata = new SmartComponentGeoData();
		geodata.setPowerDistrictId(1);
		geodata.setCompName("Important");
		geodata.setCompType("34");
		district.put("34", geodata);

		smartMeter.put("1", district);

		return smartMeter;
	}

	private static PowerSpecContainer createPowerSpecContainer() {
		var powerDemanMap = new LinkedHashMap<String, Map<String, PowerSpec>>();
		var map = new HashMap<String, PowerSpec>();
		
		var powerspec = new PowerSpec("Smart-Meter", "Test", "1234", "1234", 1, 1.0, 1.0, 1.0);
		map.put("1234", powerspec);
		
		powerspec = new PowerSpec("Smart-Meter", "Medicine", "234", "234", 1, 1.0, 1.0, 1.0);
		map.put("234", powerspec);
		
		powerspec = new PowerSpec("Smart-Meter", "Important", "34", "34", 1, 1.0, 1.0, 1.0);
		map.put("34", powerspec);
		
		powerDemanMap.put("1", map);
		return new PowerSpecContainer(powerDemanMap, null);

	}

	private static ISimulationController initRMIConnection(HashMap<InitializationMapKeys, String> exchangeMap)
			throws RemoteException, NotBoundException, SimcontrolInitializationException {
		System.setProperty("java.security.policy", "file:java.policy");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		System.setProperty("java.rmi.server.hostname", "localhost");		
		var registry = LocateRegistry.getRegistry(hostName);
		var connector = (ISimulationController) registry.lookup(lookupName);
		connector.initReactive(exchangeMap);
		return connector;
	}

	private static HashMap<InitializationMapKeys, String> createDTOMAP(String path) {
		var initMap = new HashMap<InitializationMapKeys, String>();
		initMap.put(InitializationMapKeys.INPUT_PATH_KEY, "");
		initMap.put(InitializationMapKeys.OUTPUT_PATH_KEY, path);
		initMap.put(InitializationMapKeys.TOPO_PATH_KEY, "");
		initMap.put(InitializationMapKeys.TOPO_GENERATION_KEY, Boolean.toString(true));
		initMap.put(InitializationMapKeys.IGNORE_LOC_CON_KEY, Boolean.toString(true));
		initMap.put(InitializationMapKeys.HACKING_SPEED_KEY, Integer.toString(1));
		initMap.put(InitializationMapKeys.TIME_STEPS_KEY, Integer.toString(1));
		initMap.put(InitializationMapKeys.ROOT_NODE_ID_KEY, "");
		initMap.put(InitializationMapKeys.HACKING_STYLE_KEY, HackingStyle.BFS_HACKING.toString());
		initMap.put(InitializationMapKeys.ATTACKER_SIMULATION_KEY, AttackerSimulationsTypes.LOCAL_HACKER.toString());
		initMap.put(InitializationMapKeys.POWER_MODIFY_KEY, PowerSpecsModificationTypes.NO_CHANGE_MODIFIER.toString());
		return initMap;
	}
}
