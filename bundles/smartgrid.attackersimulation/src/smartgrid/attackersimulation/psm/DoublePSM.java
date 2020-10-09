package smartgrid.attackersimulation.psm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import couplingToICT.PowerSpec;
import couplingToICT.PowerSpecContainer;

public class DoublePSM implements PowerSpecsModifier {

	private Set<String> hackedSmartMeters;
	
	@Override
    public PowerSpecContainer modifyPowerSpecs(PowerSpecContainer powerSpecContainer, Set<String> hackedSmartMeters) {

		this.hackedSmartMeters = hackedSmartMeters;
		
        LinkedHashMap<String, Map<String, PowerSpec>> oldPowerDemandsHashMap = powerSpecContainer.getPowerDemand();
        LinkedHashMap<String, Map<String, PowerSpec>> newPowerDemandsHashMap = modifyPowerDemand(oldPowerDemandsHashMap);
        
        LinkedHashMap<String, Map<String, PowerSpec>> oldPowerInfeedHashMap = powerSpecContainer.getPowerDemand();
        LinkedHashMap<String, Map<String, PowerSpec>> newPowerInfeedHashMap = modifyPowerInfeed(oldPowerInfeedHashMap);
        
        return new PowerSpecContainer(newPowerDemandsHashMap, newPowerInfeedHashMap);
        
    }
	
	 private  LinkedHashMap<String, Map<String, PowerSpec>> modifyPowerDemand(LinkedHashMap<String, Map<String, PowerSpec>> oldPowerDemandsHashMap) {
	    	LinkedHashMap<String, Map<String, PowerSpec>> newPowerDemandsHashMap = new LinkedHashMap<>();
	    	
	        
	        for (String powerDistrictIdString : oldPowerDemandsHashMap.keySet()) {
	            
	            Map<String, PowerSpec> oldSmartMeterPowerSpecs = oldPowerDemandsHashMap.get(powerDistrictIdString);
	            HashMap<String, PowerSpec> newSmartMeterPowerSpecsHashMap = new HashMap<>();
	            
	            for (String smartMeterID : oldSmartMeterPowerSpecs.keySet()) {
	                PowerSpec powerSpec = oldSmartMeterPowerSpecs.get(smartMeterID);
	                
	                if (hackedSmartMeters.contains(smartMeterID)) {
	                	String ciType = powerSpec.getCiType();
		                String ciName = powerSpec.getCiName();
		                String ciSmartID = powerSpec.getCiSmartID();
		                String ciID = powerSpec.getCiID();
		                int powerDistrictId = powerSpec.getPowerDistrictId();
		                int aggregation = powerSpec.getAggregation();
		                double minReqDemand = powerSpec.getMinReqDemand();
		                double criticality = powerSpec.getCriticality();
		                double optDemand = 2 * powerSpec.getOptDemand();
		                
		                PowerSpec newPowerSpec =  new PowerSpec(ciType, ciName, ciID, 
		                        ciSmartID, powerDistrictId, aggregation, optDemand, minReqDemand, criticality);
		                
		                newSmartMeterPowerSpecsHashMap.put(smartMeterID, newPowerSpec);
	                } else {
	            		newSmartMeterPowerSpecsHashMap.put(smartMeterID, powerSpec);
	            	}
	                
	            }
	            
	            newPowerDemandsHashMap.put(powerDistrictIdString, newSmartMeterPowerSpecsHashMap);
	        }

	    	return newPowerDemandsHashMap;
	    }
	 
	 private  LinkedHashMap<String, Map<String, PowerSpec>> modifyPowerInfeed(LinkedHashMap<String, Map<String, PowerSpec>> oldPowerInfeedHashMap) {
	    	LinkedHashMap<String, Map<String, PowerSpec>> newPowerInfeedHashMap = new LinkedHashMap<>();
	    	
	        
	        for (String powerDistrictIdString : oldPowerInfeedHashMap.keySet()) {
	            
	            Map<String, PowerSpec> oldSmartMeterPowerSpecs = oldPowerInfeedHashMap.get(powerDistrictIdString);
	            HashMap<String, PowerSpec> newSmartMeterPowerSpecsHashMap = new HashMap<>();
	            
	            for (Entry<String, PowerSpec> smartMeterIDEntry : oldSmartMeterPowerSpecs.entrySet()) {
	                PowerSpec powerSpec = smartMeterIDEntry.getValue();
	                
	                if (hackedSmartMeters.contains(smartMeterIDEntry.getKey())) {
	                	String ciType = powerSpec.getCiType();
		                String ciName = powerSpec.getCiName();
		                String ciSmartID = powerSpec.getCiSmartID();
		                String ciID = powerSpec.getCiID();
		                int powerDistrictId = powerSpec.getPowerDistrictId();
		                int aggregation = powerSpec.getAggregation();
		                double minReqDemand = powerSpec.getMinReqDemand();
		                double criticality = powerSpec.getCriticality();
		                double optDemand = powerSpec.getOptDemand();
		                
		                PowerSpec newPowerSpec =  new PowerSpec(ciType, ciName, ciID, 
		                        ciSmartID, powerDistrictId, aggregation, optDemand, minReqDemand, criticality);
		                
		                newPowerSpec.setMaxPowerInfeed(2 * powerSpec.getMaxPowerInfeed());
		                
		                newSmartMeterPowerSpecsHashMap.put(smartMeterIDEntry.getKey(), newPowerSpec);
	                } else {
	            		newSmartMeterPowerSpecsHashMap.put(smartMeterIDEntry.getKey(), powerSpec);
	            	}
	                
	            }
	            
	            newPowerInfeedHashMap.put(powerDistrictIdString, newSmartMeterPowerSpecsHashMap);
	        }

	    	return newPowerInfeedHashMap;
	    }
    

}
