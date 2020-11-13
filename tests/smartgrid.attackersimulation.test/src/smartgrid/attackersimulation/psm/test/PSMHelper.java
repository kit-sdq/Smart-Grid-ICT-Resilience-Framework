package smartgrid.attackersimulation.psm.test;

import couplingToICT.PowerSpecContainer;
import couplingToICT.PowerSpec;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class PSMHelper {

	public static PowerSpecContainer createPowerSpecContainer() {
		LinkedHashMap<String, Map<String, PowerSpec>> powerDemanMap = new LinkedHashMap<String, Map<String, PowerSpec>>();
		HashMap<String, PowerSpec> map = new HashMap<String, PowerSpec>();
		
		PowerSpec powerspec = new PowerSpec("Smart-Meter", "Test", "1234", "1234", 1, 1.0, 1.0, 1.0);
		map.put("1234", powerspec);
		
		powerspec = new PowerSpec("Smart-Meter", "Medicine", "234", "234", 1, 2.0, 1.0, 1.0);
		map.put("234", powerspec);
		
		powerspec = new PowerSpec("Smart-Meter", "Important", "34", "34", 1, 1.0, 1.0, 1.0);
		map.put("34", powerspec);
		
		powerDemanMap.put("1", map);
		return new PowerSpecContainer(powerDemanMap, null);
		
	
	}
	
	public static Set<String> getHackedSmartMeters(){
		 Set<String> hackedSmartMeters = new HashSet<String>();
		 hackedSmartMeters.add("1234");
		 hackedSmartMeters.add("234");
		 return hackedSmartMeters;
	}

}
