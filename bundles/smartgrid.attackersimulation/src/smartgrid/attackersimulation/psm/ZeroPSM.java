package smartgrid.attackersimulation.psm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import couplingToICT.PowerSpec;
import couplingToICT.PowerSpecContainer;

public class ZeroPSM implements PowerSpecsModifier {
	
	private Set<String> hackedSmartMeters;
	
	@Override
    public PowerSpecContainer modifyPowerSpecs(PowerSpecContainer powerSpecContainer, Set<String> hackedSmartMeters) {

		this.hackedSmartMeters = hackedSmartMeters;
        
        LinkedHashMap<String, Map<String, PowerSpec>> oldPowerDemandsHashMap = powerSpecContainer.getPowerDemand();
        LinkedHashMap<String, Map<String, PowerSpec>> newPowerDemandsHashMap = modifyPowerDemand(oldPowerDemandsHashMap);
        
        LinkedHashMap<String, Map<String, PowerSpec>> oldPowerInfeedHashMap = powerSpecContainer.getPowerDemand();
        LinkedHashMap<String, Map<String, PowerSpec>> newPowerInfeedHashMap = modifyPowerInfeed(oldPowerInfeedHashMap);
        
        PowerSpecContainer newPowerSpecContainer = new PowerSpecContainer(newPowerDemandsHashMap, newPowerInfeedHashMap);
        
        return newPowerSpecContainer;
    }
	
	 private  LinkedHashMap<String, Map<String, PowerSpec>> modifyPowerDemand(LinkedHashMap<String, Map<String, PowerSpec>> oldPowerDemandsHashMap) {
	    	LinkedHashMap<String, Map<String, PowerSpec>> newPowerDemandsHashMap = new LinkedHashMap<String, Map<String,PowerSpec>>();
	    	
	        
	        for (String powerDistrictId : oldPowerDemandsHashMap.keySet()) {
	            
	            Map<String, PowerSpec> oldSmartMeterPowerSpecs = oldPowerDemandsHashMap.get(powerDistrictId);
	            HashMap<String, PowerSpec> newSmartMeterPowerSpecsHashMap = new HashMap<String, PowerSpec>();
	            
	            for (String smartMeterID : oldSmartMeterPowerSpecs.keySet()) {
	                PowerSpec powerSpec = oldSmartMeterPowerSpecs.get(smartMeterID);
	                
	                if (this.hackedSmartMeters.contains(smartMeterID)) {
	                	String _ciType = powerSpec.getCiType();
		                String _ciName = powerSpec.getCiName();
		                String _ciSmartID = powerSpec.getCiSmartID();
		                String _ciID = powerSpec.getCiID();
		                int _powerDistrictId = powerSpec.getPowerDistrictId();
		                int _aggregation = powerSpec.getAggregation();
		                double _minReqDemand = powerSpec.getMinReqDemand();
		                double _criticality = powerSpec.getCriticality();
		                double _optDemand = 0;
		                
		                PowerSpec newPowerSpec =  new PowerSpec(_ciType, _ciName, _ciID, 
		                        _ciSmartID, _powerDistrictId, _aggregation, _optDemand, _minReqDemand, _criticality);
		                
		                newSmartMeterPowerSpecsHashMap.put(smartMeterID, newPowerSpec);
	                } else {
	            		newSmartMeterPowerSpecsHashMap.put(smartMeterID, powerSpec);
	            	}
	                
	            }
	            
	            newPowerDemandsHashMap.put(powerDistrictId, newSmartMeterPowerSpecsHashMap);
	        }

	    	return newPowerDemandsHashMap;
	    }
	 
	 private  LinkedHashMap<String, Map<String, PowerSpec>> modifyPowerInfeed(LinkedHashMap<String, Map<String, PowerSpec>> oldPowerInfeedHashMap) {
	    	LinkedHashMap<String, Map<String, PowerSpec>> newPowerInfeedHashMap = new LinkedHashMap<String, Map<String,PowerSpec>>();
	    	
	        
	        for (String powerDistrictId : oldPowerInfeedHashMap.keySet()) {
	            
	            Map<String, PowerSpec> oldSmartMeterPowerSpecs = oldPowerInfeedHashMap.get(powerDistrictId);
	            HashMap<String, PowerSpec> newSmartMeterPowerSpecsHashMap = new HashMap<String, PowerSpec>();
	            
	            for (String smartMeterID : oldSmartMeterPowerSpecs.keySet()) {
	                PowerSpec powerSpec = oldSmartMeterPowerSpecs.get(smartMeterID);
	                
	                if (this.hackedSmartMeters.contains(smartMeterID)) {
	                	String _ciType = powerSpec.getCiType();
		                String _ciName = powerSpec.getCiName();
		                String _ciSmartID = powerSpec.getCiSmartID();
		                String _ciID = powerSpec.getCiID();
		                int _powerDistrictId = powerSpec.getPowerDistrictId();
		                int _aggregation = powerSpec.getAggregation();
		                double _minReqDemand = powerSpec.getMinReqDemand();
		                double _criticality = powerSpec.getCriticality();
		                double _optDemand = powerSpec.getOptDemand();
		                
		                PowerSpec newPowerSpec =  new PowerSpec(_ciType, _ciName, _ciID, 
		                        _ciSmartID, _powerDistrictId, _aggregation, _optDemand, _minReqDemand, _criticality);
		                
		                newPowerSpec.setMaxPowerInfeed(0);
		                
		                newSmartMeterPowerSpecsHashMap.put(smartMeterID, newPowerSpec);
	                } else {
	            		newSmartMeterPowerSpecsHashMap.put(smartMeterID, powerSpec);
	            	}
	                
	            }
	            
	            newPowerInfeedHashMap.put(powerDistrictId, newSmartMeterPowerSpecsHashMap);
	        }

	    	return newPowerInfeedHashMap;
	    }
    

}
