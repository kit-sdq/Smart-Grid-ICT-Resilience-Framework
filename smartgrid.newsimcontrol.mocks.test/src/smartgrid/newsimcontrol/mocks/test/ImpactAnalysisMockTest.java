package smartgrid.newsimcontrol.mocks.test;

import java.io.IOException;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.simcontrol.test.mocks.ImpactAnalysisMock;
import smartgridinput.ScenarioState;
import smartgridtopo.SmartGridTopology;

public class ImpactAnalysisMockTest {

	static ImpactAnalysisMock impactAnalysisMock;
	private static SmartGridTopology smartGridTopology1;
	private static ScenarioState state1;
	@BeforeClass
    public static void setup() throws IOException {
		impactAnalysisMock = new ImpactAnalysisMock();
		
        Helper.copyFilesForTesting("WithLogical");

        smartGridTopology1 = Helper.loadTopology("1_WithLogical.smartgridtopo");
        state1 = Helper.loadInput("1_WithLogical.smartgridinput");

    }

    @Test
    public void testInit() {
    	Map<InitializationMapKeys, String> initMap = null;
    	impactAnalysisMock.init(initMap);
    }

    @Test
    public void testRun() {
		impactAnalysisMock.run(smartGridTopology1, state1);
    }
    
    @Test
    public void testGetName() {
		 Assert.assertTrue(impactAnalysisMock.getName().equals("Mock"));
    }

    /**
     * delete copied files used for testing
     *
     * @throws IOException
     *             IOException
     */
    @AfterClass
    public static void deleteFiles() throws IOException {
        Helper.deleteFilesForTesting("WithLogical");
    }
}
