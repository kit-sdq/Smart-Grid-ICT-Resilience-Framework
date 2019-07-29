package smartgrid.simcontrol.test.util;

import java.util.HashSet;

import couplingToICT.PowerSpecContainer;

public interface PowerSpecsModifier {
    
    public PowerSpecContainer modifyPowerSpecs(PowerSpecContainer powerSpecContainer, HashSet<String> hackedSmartMeters);
    
}
