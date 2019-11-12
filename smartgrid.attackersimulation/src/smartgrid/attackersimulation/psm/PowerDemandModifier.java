package smartgrid.attackersimulation.psm;

import java.util.Map;

import smartgrid.coupling.ICT.PowerSpec;
import smartgrid.coupling.ICT.PowerSpecContainer;

public interface PowerDemandModifier {
    
    public PowerSpecContainer modifyPowerDemand(Map<String, Map<String, PowerSpec>> powerDemand);
}
