package smartgrid.newsimcontrol.mocks.test;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.simcontrol.test.mocks.NoAttackerSimulation;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

public class NoAttackerSimulationTest {


	static NoAttackerSimulation noAttackerSimulation;
	@BeforeClass
    public static void setup() {
		noAttackerSimulation = new NoAttackerSimulation();
    }

    @Test
    public void testInit() {
    	Map<InitializationMapKeys, String> initMap = null;
    	noAttackerSimulation.init(initMap);
    }
    @Test
    public void testRun() {
    	SmartGridTopology topo = null;
		ScenarioResult output = null;
		output = noAttackerSimulation.run(topo, output);
		Assert.assertNull(output);
    }
    
    @Test
    public void testGetName() {
		 Assert.assertTrue(noAttackerSimulation.getName().equals("No Attack Simulation"));
    }
    
    @Test
    public void testEnableHackingSpeed() {
		 Assert.assertFalse(noAttackerSimulation.enableHackingSpeed());
    }
    
    @Test
    public void testEnableRootNode() {
    	Assert.assertFalse(noAttackerSimulation.enableRootNode());
    }
    
    @Test
    public void testEnableLogicalConnections() {
    	Assert.assertFalse(noAttackerSimulation.enableLogicalConnections());
    }

}
