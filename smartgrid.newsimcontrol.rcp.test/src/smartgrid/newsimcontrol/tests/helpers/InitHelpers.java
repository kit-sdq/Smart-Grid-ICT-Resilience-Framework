package smartgrid.newsimcontrol.tests.helpers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import couplingToICT.PowerSpec;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SmartComponentGeoData;
import couplingToICT.initializer.AttackerSimulationsTypes;
import couplingToICT.initializer.HackingStyle;
import couplingToICT.initializer.InitializationMapKeys;
import couplingToICT.initializer.PowerSpecsModificationTypes;

public class InitHelpers {
	
	public static int NO_OF_POWER_DISTRICTS_POWER_INFEED = 1;
	public static int NO_OF_POWER_DISTRICTS_POWER_DEMAND = 1;
	public static int NO_OF_POWER_SPECS_POWER_DEMAND = 100;
	public static int NO_OF_POWER_SPECS_POWER_INFEED = 100;
	private static PowerSpecContainer powerSpecContainer;
	
	public InitHelpers() {
	}

	public static HashMap<InitializationMapKeys, String> createDTOMAP(String path) {
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
		initMap.put(InitializationMapKeys.IMPACT_ANALYSIS_SIMULATION_KEY, "Graph Analyzer Impact Analysis");
		initMap.put(InitializationMapKeys.TIME_PROGRESSOR_SIMULATION_KEY, "No Operation");
		return initMap;
	}

	public static PowerSpecContainer createPowerSpecContainer() {
		var powerDemandMap = getPowerDemandMap();
		var powerInfeedMap = getPowerInfeedMap();
		
		powerSpecContainer = new PowerSpecContainer(powerDemandMap, powerInfeedMap);
		return powerSpecContainer;
	
	}
	
	public static LinkedHashMap<String, Map<String, PowerSpec>> getPowerDemandMap(){
		var powerDemandMap = new LinkedHashMap<String, Map<String, PowerSpec>>();
		
		String powerDistrictId;
		HashMap<String, PowerSpec> powerDistrictPowerDemand;
		
		for (int i=0; i<NO_OF_POWER_DISTRICTS_POWER_DEMAND; i++) {
			powerDistrictId = createRandomPowerDistrictId();
			powerDistrictPowerDemand = createRandomPowerDistrictPowerDemand(powerDistrictId);
			powerDemandMap.put(powerDistrictId, powerDistrictPowerDemand);
		}
		
		return powerDemandMap;
	}
	
	public static LinkedHashMap<String, Map<String, PowerSpec>> getPowerInfeedMap(){
		var powerInfeedMap = new LinkedHashMap<String, Map<String, PowerSpec>>();
		
		String powerDistrictId;
		HashMap<String, PowerSpec> powerDistrictPowerInfeed;
		
		for (int i=0; i<NO_OF_POWER_DISTRICTS_POWER_INFEED; i++) {
			powerDistrictId = createRandomPowerDistrictId();
			powerDistrictPowerInfeed = createRandomPowerDistrictPowerInfeed(powerDistrictId);
			powerInfeedMap.put(powerDistrictId, powerDistrictPowerInfeed);
		}
		
		return powerInfeedMap;
	}
	
	public static HashMap<String, PowerSpec> createRandomPowerDistrictPowerInfeed(String powerDistrictId){
		var map = new HashMap<String, PowerSpec>();
		
		PowerSpec powerspec;
		String smartID;
		for (int i=0; i<NO_OF_POWER_SPECS_POWER_INFEED; i++) {
			smartID = createRandomSmartId();
			powerspec = createRandomPowerSpec(smartID, powerDistrictId);
			map.put(smartID, powerspec);
		}
		
		return map;
	}
	
	public static HashMap<String, PowerSpec> createRandomPowerDistrictPowerDemand(String powerDistrictId){
		var map = new HashMap<String, PowerSpec>();
		
		PowerSpec powerspec;
		String smartID;
		for (int i=0; i<NO_OF_POWER_SPECS_POWER_DEMAND; i++) {
			smartID = createRandomSmartId();
			powerspec = createRandomPowerSpec(smartID, powerDistrictId);
			map.put(smartID, powerspec);
		}
		
		return map;
	}
	
	
	public static PowerSpec createRandomPowerSpec(String smartID, String powerDistrictId2) {
		Random rand = new Random();
		String ciType = "SmartMeter" + Integer.toString(rand.nextInt(10000000)); // CI, owner type
		String ciOwner = "Owner" + Integer.toString(rand.nextInt(10000000)); // CI, owner name
		String ciID = Integer.toString(rand.nextInt(10000000)); // CI, owner id
		String ciSmartID = smartID; //Integer.toString(rand.nextInt(10000000)); // Smart meter agent-ID
		int powerDistrictId = Integer.valueOf(powerDistrictId2); //rand.nextInt(10000000);// determined by the Smart Grid scenario
		double optDemand = rand.nextInt(20); // optimal demand
		double minReqDemand = rand.nextInt(20); // Minimum required power
		double criticality = rand.nextInt(20); // 1, if e.g. human casualties (has to be // supplied)
		double maxPowerInfeed = rand.nextInt(20);// maximum power infeed by generators or storages
		
		var powerspec = new PowerSpec(ciType, ciOwner, ciID, ciSmartID, powerDistrictId, optDemand, minReqDemand, criticality);
		powerspec.setMaxPowerInfeed(maxPowerInfeed);
		return powerspec;
	}
	
	public static String createRandomSmartId() {
		Random rand = new Random();
		return Integer.toString(rand.nextInt(10000000));
	}
	
	public static String createRandomPowerDistrictId() {
		Random rand = new Random();
		return Integer.toString(rand.nextInt(10000000));
	}

	public static LinkedHashMap<String, Map<String, SmartComponentGeoData>> createTopoMap() {
		var smartMeter = new LinkedHashMap<String, Map<String, SmartComponentGeoData>>();
	
		var powerDemandMap = powerSpecContainer.getPowerDemand();
		var powerInfeedMap = powerSpecContainer.getPowerInfeed();
		
		
		for (String powerDistrictId: powerDemandMap.keySet()) {
			var district = new HashMap<String, SmartComponentGeoData>();
			int count = 0;
			for(String smartId : powerDemandMap.get(powerDistrictId).keySet()) {
				var geodata = new SmartComponentGeoData();
				geodata.setCompName("testComp" + powerDistrictId + count);
				geodata.setCompType("testCompType" + powerDistrictId + count);
				geodata.setPowerDistrictId(Integer.valueOf(powerDistrictId));
				district.put(smartId, geodata);
				count ++;
			}
			smartMeter.put(powerDistrictId, district);
		}
		
		for (String powerDistrictId: powerInfeedMap.keySet()) {
			var district = new HashMap<String, SmartComponentGeoData>();
			int count = 0;
			for(String smartId : powerInfeedMap.get(powerDistrictId).keySet()) {
				var geodata = new SmartComponentGeoData();
				geodata.setCompName("testComp" + powerDistrictId + count);
				geodata.setCompType("testCompType" + powerDistrictId + count);
				geodata.setPowerDistrictId(Integer.valueOf(powerDistrictId));
				district.put(smartId, geodata);
				count ++;
			}
			smartMeter.put(powerDistrictId, district);
		}
		
	
		return smartMeter;
	}

	public static LinkedHashMap<String, HashMap<String, Double>> assignPower() {
		var assignment = new LinkedHashMap<String, HashMap<String, Double>>();
		
		var powerDemandMap = powerSpecContainer.getPowerDemand();
		var powerInfeedMap = powerSpecContainer.getPowerInfeed();
		
		
		for (String powerDistrictId: powerDemandMap.keySet()) {
			var singleAssignment = new HashMap<String, Double>();
			for(String smartId : powerDemandMap.get(powerDistrictId).keySet()) {
				singleAssignment.put(smartId, generateRandomPower());
			}
			assignment.put(powerDistrictId, singleAssignment);
		}
		
		for (String powerDistrictId: powerInfeedMap.keySet()) {
			var singleAssignment = new HashMap<String, Double>();
			for(String smartId : powerInfeedMap.get(powerDistrictId).keySet()) {
				singleAssignment.put(smartId, generateRandomPower());
			}
			assignment.put(powerDistrictId, singleAssignment);
		}
		
		return assignment;
		
	}

	private static Double generateRandomPower() {
		Random rand = new Random();
		return rand.nextDouble() * 10;
	}

}
