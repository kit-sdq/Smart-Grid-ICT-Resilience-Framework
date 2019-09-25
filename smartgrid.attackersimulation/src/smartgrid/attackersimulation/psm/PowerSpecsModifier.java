package smartgrid.attackersimulation.psm;

import java.util.HashSet;

import couplingToICT.PowerSpecContainer;

public interface PowerSpecsModifier {
    
    public PowerSpecContainer modifyPowerSpecs(PowerSpecContainer powerSpecContainer, HashSet<String> hackedSmartMeters);
    
}
