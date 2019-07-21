package smartgrid.simcontrol.test.util;

import couplingToICT.PowerDemand;

public interface PowerDemandModifier {
    
    public PowerDemand modifyPowerDemand(PowerDemand powerDemand);
}
