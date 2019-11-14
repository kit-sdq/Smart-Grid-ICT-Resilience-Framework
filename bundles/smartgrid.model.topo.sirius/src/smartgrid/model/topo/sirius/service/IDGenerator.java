package smartgrid.model.topo.sirius.service;

import java.util.Random;

import org.eclipse.emf.ecore.EObject;

public class IDGenerator {
    
    public IDGenerator() {
        
    }
    
    public String generateRandom(EObject obj) {
        Random rand = new Random();
        return Integer.toString(rand.nextInt(10000000));
    }
}
