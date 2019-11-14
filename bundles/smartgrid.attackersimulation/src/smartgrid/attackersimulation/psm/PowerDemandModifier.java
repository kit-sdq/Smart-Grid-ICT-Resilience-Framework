package smartgrid.attackersimulation.psm;

import java.util.Map;

import couplingToICT.PowerSpec;
import couplingToICT.PowerSpecContainer;

public interface PowerDemandModifier {
    
    public PowerSpecContainer modifyPowerDemand(Map<String, Map<String, PowerSpec>> powerDemand);
}
