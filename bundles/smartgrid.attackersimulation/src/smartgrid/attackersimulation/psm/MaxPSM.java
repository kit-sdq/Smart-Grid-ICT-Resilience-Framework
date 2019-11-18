package smartgrid.attackersimulation.psm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import couplingToICT.PowerSpec;
import couplingToICT.PowerSpecContainer;

public class MaxPSM implements PowerSpecsModifier {

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
    	
    	double maxOptDemand = getMaxOptDemand(oldPowerDemandsHashMap);
        
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
	                
	
	                double _optDemand = maxOptDemand;
	                
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
    	
    	double maxInfeed = getMaxInfeed(oldPowerInfeedHashMap);
    	
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
	                double _maxInfeed = maxInfeed;
	                
	                PowerSpec newPowerSpec =  new PowerSpec(_ciType, _ciName, _ciID, 
	                        _ciSmartID, _powerDistrictId, _aggregation, _optDemand, _minReqDemand, _criticality);
	                
	                newPowerSpec.setMaxPowerInfeed(_maxInfeed);
	                
	                newSmartMeterPowerSpecsHashMap.put(smartMeterID, newPowerSpec);
            	} else {
            		newSmartMeterPowerSpecsHashMap.put(smartMeterID, powerSpec);
            	}
            }
            
            newPowerInfeedHashMap.put(powerDistrictId, newSmartMeterPowerSpecsHashMap);
        }

    	return newPowerInfeedHashMap;
    }
    

    private double getMaxOptDemand(LinkedHashMap<String, Map<String, PowerSpec>> oldPowerDemandsHashMap) {
        double maxOptDemand = 0;
        
        for (String powerDistrictId : oldPowerDemandsHashMap.keySet()) {
            Map<String, PowerSpec> oldSmartMeterPowerSpecs = oldPowerDemandsHashMap.get(powerDistrictId);
            for (String smartMeterID : oldSmartMeterPowerSpecs.keySet()) {
                PowerSpec powerSpec = oldSmartMeterPowerSpecs.get(smartMeterID);
                double _optDemand = powerSpec.getOptDemand();
                if (_optDemand > maxOptDemand)
                    maxOptDemand = _optDemand;
            }
        }
        return maxOptDemand;
    }
    
    private double getMaxInfeed(LinkedHashMap<String, Map<String, PowerSpec>> oldPowerInfeedHashMap) {
        double maxInfeed = 0;
        
        for (String powerDistrictId : oldPowerInfeedHashMap.keySet()) {
            Map<String, PowerSpec> oldSmartMeterPowerSpecs = oldPowerInfeedHashMap.get(powerDistrictId);
            for (String smartMeterID : oldSmartMeterPowerSpecs.keySet()) {
                PowerSpec powerSpec = oldSmartMeterPowerSpecs.get(smartMeterID);
                double _infeed = powerSpec.getMaxPowerInfeed();
                if (_infeed > maxInfeed)
                    maxInfeed = _infeed;
            }
        }
        return maxInfeed;
    }

}
