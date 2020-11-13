package smartgrid.attackersimulation.psm.test;


import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import couplingToICT.PowerSpec;
import couplingToICT.PowerSpecContainer;
import smartgrid.attackersimulation.psm.PowerSpecsModifier;
import smartgrid.attackersimulation.psm.ZeroPSM;

/**
 * Test Class to test the DFS Hacking of the local hacker
 *
 * @author mazenebada
 *
 */
public class ZeroPSMTeter {
    
	private static PowerSpecsModifier powerSpecsModifier;
	private static PowerSpecContainer inputPowerSpecContainer;
	private static Set<String> hackedSmartMeters; 
	
    @BeforeClass
    public static void setup() {
    	powerSpecsModifier = new ZeroPSM();
    	inputPowerSpecContainer = PSMHelper.createPowerSpecContainer();
    	hackedSmartMeters = PSMHelper.getHackedSmartMeters();
    }

 

    @Test
    public void test() {
    	Map<String, PowerSpec> oldMap = inputPowerSpecContainer.getPowerDemand().get("1");
    	
    	PowerSpecContainer outputPowerSpecContainer = powerSpecsModifier.
    			modifyPowerSpecs(inputPowerSpecContainer, hackedSmartMeters);
    	Map<String, PowerSpec> map = outputPowerSpecContainer.getPowerDemand().get("1");
    	for (String id : map.keySet()) {
    		PowerSpec powerSpec = map.get(id);
    		double newOptDemand = powerSpec.getOptDemand();
    		double oldOptDemand = oldMap.get(id).getOptDemand();
    		if (hackedSmartMeters.contains(id)) {
    			Assert.assertTrue(newOptDemand == 0.0);
    		} else {
    			Assert.assertTrue(newOptDemand == oldOptDemand);
    		}
    	}
    	
    }

}
