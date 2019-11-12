package smartgrid.attackersimulation.psm;

import java.util.HashSet;

import smartgrid.coupling.ICT.PowerSpecContainer;

public interface PowerSpecsModifier {
    
    public PowerSpecContainer modifyPowerSpecs(PowerSpecContainer powerSpecContainer, HashSet<String> hackedSmartMeters);
    
}
