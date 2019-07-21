package smartgrid.simcontrol.test.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import couplingToICT.PowerDemand;
import couplingToICT.PowerSpec;

public class ZeroPDM implements PowerDemandModifier {

    @Override
    public PowerDemand modifyPowerDemand(PowerDemand powerDemand) {

        
        LinkedHashMap<String, HashMap<String, PowerSpec>> newPowerDemandsHashMap = new LinkedHashMap<String, HashMap<String,PowerSpec>>();
        LinkedHashMap<String, HashMap<String, PowerSpec>> oldPowerDemandsHashMap = powerDemand.getPowerDemands();
        
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
                
                // change power
                double _optDemand = 0;
                
                PowerSpec newPowerSpec =  new PowerSpec(_ciType, _ciName, _ciID,
                        _ciSmartID, _powerDistrictId, _aggregation, _optDemand, _minReqDemand, _criticality);
                
                newSmartMeterPowerSpecsHashMap.put(smartMeterID, newPowerSpec);
            }
            
            newPowerDemandsHashMap.put(powerDistrictId, newSmartMeterPowerSpecsHashMap);
        }
        
        PowerDemand newPowerDemand = new PowerDemand(newPowerDemandsHashMap);
        
        return newPowerDemand;
    }


}
