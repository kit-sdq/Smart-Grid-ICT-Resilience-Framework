package smartgrid.attackersimulation.psm;

import java.util.Set;

import couplingToICT.PowerSpecContainer;

public interface PowerSpecsModifier {
    
    public PowerSpecContainer modifyPowerSpecs(PowerSpecContainer powerSpecContainer, Set<String> hackedSmartMeters);
    
}
