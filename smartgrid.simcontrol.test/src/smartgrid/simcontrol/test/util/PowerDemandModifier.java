package smartgrid.simcontrol.test.util;

import java.util.Map;

import couplingToICT.PowerDemand;
import couplingToICT.PowerSpec;

public interface PowerDemandModifier {
    
    public PowerDemand modifyPowerDemand(Map<String, Map<String, PowerSpec>> powerDemand);
}
