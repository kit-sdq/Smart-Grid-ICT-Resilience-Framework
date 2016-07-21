package smartgrid.model.topo.sirius.service;

import java.util.Random;

import org.eclipse.emf.ecore.EObject;

public class IDGenerator {
	public IDGenerator() {
		
	}
	
	public int generateRandom(EObject obj) {
		Random rand = new Random();
		return rand.nextInt(10000000);
	}
}
