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
 * Class to test specific cases of smart_grids, in case of not ignoring the logical connections (
 * every entity can connect with another one via physical connections only if they are logically
 * connected)
 *
 * @author mazenebada
 *
 */
public class AnalyzerTestWithLogical {

    private static GraphAnalyzer analyzer;
    private static ScenarioState state1;
    private static SmartGridTopology smartGridTopology1;
    private static ScenarioState state2;
    private static SmartGridTopology smartGridTopology2;
    private static ScenarioState state3;
    private static SmartGridTopology smartGridTopology3;

    /**
     * Initializing the topology and the Scenario-state for each example to be used by the run
     * method from the Graph-analysis class.
     *
     * @throws IOException
     *             IOException
     */
    @BeforeClass
    public static void setup() throws IOException {

        Helper.copyFilesForTesting("WithLogical");

        smartGridTopology1 = Helper.loadTopology("1_WithLogical.smartgridtopo");
        state1 = Helper.loadInput("1_WithLogical.smartgridinput");

        smartGridTopology2 = Helper.loadTopology("2_WithLogical.smartgridtopo");
        state2 = Helper.loadInput("2_WithLogical.smartgridinput");

        smartGridTopology3 = Helper.loadTopology("3_WithLogical.smartgridtopo");
        state3 = Helper.loadInput("3_WithLogical.smartgridinput");

        analyzer = new GraphAnalyzer();
        try {
            Helper.initializeAnalyzer(analyzer, "false");
        	//analyzer.initForTesting(false);
        } catch (final Exception e) {
            e.printStackTrace();
            fail("Initialization of the graph analyser failed!");
        }

        // Only for test purposes
        // analyzer.initForTesting(false);
    }

    /**
     * Testing Example 1
     *
     * - Simple Example containing two smart meters and a control center, all are connected via a
     * network node, where there is a small restriction via Log. connections - no Uplink smart
     * meters - No power_outage - No damage
     */
    @Test
    public void testExample1() {
        final ScenarioResult result1 = analyzer.run(smartGridTopology1, state1);

        // check the number of the generated clusters
        Helper.checkNumberOfClusters(result1, 2);

        // check the contained Entities in each Cluster
        boolean cluster1Correct = false;
        boolean cluster2Correct = false;

        for (int i = 0; i < result1.getClusters().size(); i++) {
            if (!cluster1Correct && Helper.checkCluster(result1, i, 2, 0, "NoUplink")) {
                cluster1Correct = true;
                continue;
            }
            if (!cluster2Correct && Helper.checkCluster(result1, i, 0, 1, "Online")) {
                cluster2Correct = true;
                continue;
            }
            if (!cluster1Correct && Helper.checkCluster(result1, i, 2, 0, "NoUplink")) {
                cluster1Correct = true;
                continue;
            }
        }

        assertTrue("Error in expected Cluster with 2 SM and 0 CC" + Helper.printResult(result1), cluster1Correct);
        assertTrue("Error in expected Cluster with 0 SM and 1 CC" + Helper.printResult(result1), cluster2Correct);
    }

    /**
     * Testing Example 2
     *
     * - Three smart meters and two control center, all are connected via network nodes, where there
     * is no restrictions via Log. connections - not all nodes are logically direct connected, but
     * via chains - all smart meters are online - No power_outage - No damage
     */
    @Test
    public void testExample2() {
        final ScenarioResult result2 = analyzer.run(smartGridTopology2, state2);

        // check the number of the generated clusters
        Helper.checkNumberOfClusters(result2, 1);

        // check the contained Entities in each Cluster
        final boolean cluster1Correct = Helper.checkCluster(result2, 0, 3, 2, "Online");
        assertTrue("Error in expected Cluster with 3 SM and 2 CC" + Helper.printResult(result2), cluster1Correct);
    }

    /**
     * Testing Example 3 - more advanced example - 7 Clusters considering different cases of
     * connections - Destroyed NN, SM and CC - all smart meters are online - broken clusters because
     * of lake of logical connections - power_outage - NoUplink and Online SMs
     */
    @Test
    public void testExample3() {
        final ScenarioResult result3 = analyzer.run(smartGridTopology3, state3);

        // check the number of the generated clusters
        Helper.checkNumberOfClusters(result3, 7);

        // check the contained Entities in each Cluster
        boolean cluster1Correct = false;
        boolean cluster2Correct = false;
        boolean cluster3Correct = false;
        boolean cluster4Correct = false;
        boolean cluster5Correct = false;
        boolean cluster6Correct = false;
        boolean cluster7Correct = false;

        for (int i = 0; i < result3.getClusters().size(); i++) {
            if (!cluster1Correct && Helper.checkCluster(result3, i, 4, 1, "Online")) {
                cluster1Correct = true;
                continue;
            }
            if (!cluster2Correct && Helper.checkCluster(result3, i, 1, 1, "Online")) {
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
            if (!cluster5Correct && Helper.checkCluster(result3, i, 1, 0, "NoUplink")) {
                cluster5Correct = true;
                continue;
            }
            if (!cluster6Correct && Helper.checkCluster(result3, i, 1, 0, "NoUplink")) {
                cluster6Correct = true;
                continue;
            }
            if (!cluster7Correct && Helper.checkCluster(result3, i, 1, 0, "NoUplink")) {
                cluster7Correct = true;
                continue;
            }
        }

        assertTrue("Error in expected Cluster with 4 SM and 1 CC" + Helper.printResult(result3), cluster1Correct);
        assertTrue("Error in expected Cluster with 1 SM and 1 CC" + Helper.printResult(result3), cluster2Correct);
        assertTrue("Error in expected Cluster with 1 SM and 0 CC a" + Helper.printResult(result3), cluster3Correct);
        assertTrue("Error in expected Cluster with 1 SM and 0 CC b" + Helper.printResult(result3), cluster4Correct);
        assertTrue("Error in expected Cluster with 1 SM and 0 CC c" + Helper.printResult(result3), cluster5Correct);
        assertTrue("Error in expected Cluster with 1 SM and 0 CC d" + Helper.printResult(result3), cluster6Correct);
        assertTrue("Error in expected Cluster with 1 SM and 0 CC e" + Helper.printResult(result3), cluster7Correct);

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
