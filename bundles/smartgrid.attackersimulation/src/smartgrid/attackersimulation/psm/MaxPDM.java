package smartgrid.attackersimulation.psm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import couplingToICT.PowerSpec;
import couplingToICT.PowerSpecContainer;

public class MaxPDM implements PowerDemandModifier {
	@Deprecated
    @Override
    public PowerSpecContainer modifyPowerDemand(Map<String, Map<String, PowerSpec>> oldPowerDemandsHashMap) {

        
        LinkedHashMap<String, HashMap<String, PowerSpec>> newPowerDemandsHashMap = new LinkedHashMap<String, HashMap<String,PowerSpec>>();
        
        double maxOptDemand = getMaxOptDemand(oldPowerDemandsHashMap);
        
        for (String powerDistrictId : oldPowerDemandsHashMap.keySet()) {
            
            Map<String, PowerSpec> oldSmartMeterPowerSpecs = oldPowerDemandsHashMap.get(powerDistrictId);
            HashMap<String, PowerSpec> newSmartMeterPowerSpecsHashMap = new HashMap<String, PowerSpec>();
            
            for (String smartMeterID : oldSmartMeterPowerSpecs.keySet()) {
                PowerSpec powerSpec = oldSmartMeterPowerSpecs.get(smartMeterID);
                
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
            }
            
            newPowerDemandsHashMap.put(powerDistrictId, newSmartMeterPowerSpecsHashMap);
        }
        
//        PowerDemand newPowerDemand = new PowerDemand(newPowerDemandsHashMap);
//        
//        return newPowerDemand;
        throw new UnsupportedOperationException();
       // return null;
    }

    private double getMaxOptDemand(Map<String, Map<String, PowerSpec>> oldPowerDemandsHashMap) {
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

}
