package smartgrid.impactanalysis.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import smartgrid.impactanalysis.GraphAnalyzer;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

/**
 * Class to test specific cases of smart_grids, in case of ignoring the logical connections ( every
 * entity can connect with another one via physical connections )
 *
 * @author mazenebada
 *
 */
public class AnalyzerTestIgnoreLogical {

    private static GraphAnalyzer analyzer;
    private static ScenarioState state1;
    private static SmartGridTopology smartGridTopology1;
    private static ScenarioState state2;
    private static SmartGridTopology smartGridTopology2;
    private static ScenarioState state3;
    private static SmartGridTopology smartGridTopology3;
    private static ScenarioState state4;
    private static SmartGridTopology smartGridTopology4;

    /**
     * Initializing the topology and the Scenario-state for each example to be used by the run
     * method from the Graph-analysis class.
     *
     * @throws IOException
     */
    @BeforeClass
    public static void setup() {

        try {
            Helper.copyFilesForTesting("IgnoreLogical");
        } catch (final IOException e1) {
            e1.printStackTrace();
            fail("Problem in copying files to be tested from resources to the plugin");
        }

        smartGridTopology1 = Helper.loadTopology("1_NoLogical.smartgridtopo");
        state1 = Helper.loadInput("1_NoLogical.smartgridinput");

        smartGridTopology2 = Helper.loadTopology("2_NoLogical.smartgridtopo");
        state2 = Helper.loadInput("2_NoLogical.smartgridinput");

        smartGridTopology3 = Helper.loadTopology("3_NoLogical.smartgridtopo");
        state3 = Helper.loadInput("3_NoLogical.smartgridinput");

        smartGridTopology4 = Helper.loadTopology("4_NoLogical.smartgridtopo");
        state4 = Helper.loadInput("4_NoLogical.smartgridinput");

        analyzer = new GraphAnalyzer();
        try {
            Helper.initializeAnalyzer(analyzer, "true");
        	//analyzer.initForTesting(true);
        } catch (final Exception e) {
            e.printStackTrace();
            fail("Initialization of the graph analyser failed!");
        }

        // Only for test purposes
        // analyzer.initForTesting(true);
    }

    /**
     * Testing Example 1
     *
     * - Simple Example containing two smart meters and a control center, all are - connected via a
     * network node - No power_outage - No damage
     */
    @Test
    public void testExample1() {
        final ScenarioResult result1 = analyzer.run(smartGridTopology1, state1);

        // check the number of the generated clusters
        Helper.checkNumberOfClusters(result1, 1);

        // check the contained Entities in each Cluster
        final boolean clusterCorrect = Helper.checkCluster(result1, 0, 2, 1, "Online");
        assertTrue("Error in expected Cluster with 2 Sm and 1 CC " + Helper.printResult(result1), clusterCorrect);
    }

    /**
     * Testing Example 2
     *
     * - Clusters of only one Network Entity - Smart meters connected directly via physical
     * connection - NoUplink Smart meters ( not connected to CC ) - No power_outage - damage of
     * Network Node
     */
    @Test
    public void testExample2() {
        final ScenarioResult result2 = analyzer.run(smartGridTopology2, state2);

        // check the number of the generated clusters
        Helper.checkNumberOfClusters(result2, 3);

        // check the contained Entities in each Cluster
        boolean cluster1Correct = false;
        boolean cluster2Correct = false;
        boolean cluster3Correct = false;

        for (int i = 0; i < result2.getClusters().size(); i++) {
            if (!cluster1Correct && Helper.checkCluster(result2, i, 1, 0, "NoUplink")) {
                cluster1Correct = true;
                continue;
            }
            if (!cluster2Correct && Helper.checkCluster(result2, i, 2, 0, "NoUplink")) {
                cluster2Correct = true;
                continue;
            }
            if (!cluster3Correct && Helper.checkCluster(result2, i, 0, 1, "Online")) {
                cluster3Correct = true;
                continue;
            }
        }

        assertTrue("Error in expected Cluster with 1 SM and 0 CC" + Helper.printResult(result2), cluster1Correct);
        assertTrue("Error in expected Cluster with 2 SM and 0 CC" + Helper.printResult(result2), cluster2Correct);
        assertTrue("Error in expected Cluster with 0 SM and 1 CC" + Helper.printResult(result2), cluster3Correct);
    }

    /**
     * Testing Example 3
     *
     * - Chains of Smart Meters (also inter_coms) connecting each other - chains broken via
     * destroyed smart meter - power_outage of network nodes ( not working ) - power_outage of smart
     * meters - damage of smart meters
     */
    @Test
    public void testExample3() {
        final ScenarioResult result3 = analyzer.run(smartGridTopology3, state3);

        // check the number of the generated clusters
        Helper.checkNumberOfClusters(result3, 4);

        // check the contained Entities in each Cluster
        boolean cluster1Correct = false;
        boolean cluster2Correct = false;
        boolean cluster3Correct = false;
        boolean cluster4Correct = false;

        for (int i = 0; i < result3.getClusters().size(); i++) {
            if (Helper.checkCluster(result3, i, 4, 0, "NoUplink")) {
                cluster1Correct = true;
                continue;
            }
            if (!cluster2Correct && Helper.checkCluster(result3, i, 1, 0, "NoUplink")) {
                cluster2Correct = true;
                continue;
            }
            if (!cluster3Correct && Helper.checkCluster(result3, i, 1, 0, "NoUplink")) {
                cluster3Correct = true;
                continue;
            }
            if (!cluster4Correct && Helper.checkCluster(result3, i, 1, 0, "NoUplink")) {
                cluster4Correct = true;
                continue;
            }
        }

        assertTrue("Error in Expected Cluster with 4 SM and 0 CC" + Helper.printResult(result3), cluster1Correct);
        assertTrue("Error in Expected Cluster with 1 SM and 0 CC" + Helper.printResult(result3), cluster2Correct);
        assertTrue("Error in Expected Cluster with 1 SM and 0 CC" + Helper.printResult(result3), cluster3Correct);
        assertTrue("Error in Expected Cluster with 1 SM and 0 CC" + Helper.printResult(result3), cluster4Correct);
    }

    /**
     * Testing Example 4
     *
     * - Connecting via chains of network nodes - CC Connectivity via chains of Nodes ( Online ) -
     * NoUplink Smart meters ( not connected to CC ) - damage of Control centers
     */
    @Test
    public void testExample4() {
        final ScenarioResult result4 = analyzer.run(smartGridTopology4, state4);

        // check the number of the generated clusters
        Helper.checkNumberOfClusters(result4, 3);

        // check the contained Entities in each Cluster
        boolean cluster1Correct = false;
        boolean cluster2Correct = false;
        boolean cluster3Correct = false;

        for (int i = 0; i < result4.getClusters().size(); i++) {
            if (Helper.checkCluster(result4, i, 2, 0, "NoUplink")) {
                cluster1Correct = true;
                continue;
            }
            if (!cluster2Correct && Helper.checkCluster(result4, i, 3, 1, "Online")) {
                cluster2Correct = true;
                continue;
            }
            if (!cluster3Correct && Helper.checkCluster(result4, i, 5, 0, "NoUplink")) {
                cluster3Correct = true;
                continue;
            }
        }

        assertTrue("Error in Expected Cluster with 2 SM and 0 CC" + Helper.printResult(result4), cluster1Correct);
        assertTrue("Error in Expected Cluster with 3 SM and 1 CC" + Helper.printResult(result4), cluster2Correct);
        assertTrue("Error in Expected Cluster with 5 SM and 0 CC" + Helper.printResult(result4), cluster3Correct);
    }

    /**
     * delete copied files used for testing
     *
     * @throws IOException
     *             IOException
     */
    @AfterClass
    public static void deleteFiles() throws IOException {
        Helper.deleteFilesForTesting("IgnoreLogical");
    }

}
