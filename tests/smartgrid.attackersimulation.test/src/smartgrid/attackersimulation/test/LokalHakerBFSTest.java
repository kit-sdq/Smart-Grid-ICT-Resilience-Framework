package smartgrid.attackersimulation.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import smartgrid.attackersimulation.LocalHacker;
import smartgrid.impactanalysis.GraphAnalyzer;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

/**
 * Test Class to test the BFS Hacking of the local hacker
 *
 * @author mazenebada
 *
 */
public class LokalHakerBFSTest {
    private static ScenarioState state1;
    private static SmartGridTopology smartGridTopology1;
    private static ScenarioState state2;
    private static SmartGridTopology smartGridTopology2;
    private static ScenarioState state3;
    private static SmartGridTopology smartGridTopology3;
    private static ScenarioState state4;
    private static SmartGridTopology smartGridTopology4;

    private LocalHacker localHacker;
    private static GraphAnalyzer analyzer;

    /**
     * setup of the input and scenario states
     */
    @BeforeClass
    public static void setup() {

        try {
            copyFilesForTesting();
        } catch (final IOException e) {
            fail("cannot find files to use in simulation in resources file");
        }

        smartGridTopology1 = AttackerHelper.loadTopology("1LokalBFS.smartgridtopo");
        state1 = AttackerHelper.loadInput("1LokalBFS.smartgridinput");

        smartGridTopology2 = AttackerHelper.loadTopology("2LokalBFS.smartgridtopo");
        state2 = AttackerHelper.loadInput("2LokalBFS.smartgridinput");

        smartGridTopology3 = AttackerHelper.loadTopology("3LokalBFS.smartgridtopo");
        state3 = AttackerHelper.loadInput("3LokalBFS.smartgridinput");

        smartGridTopology4 = AttackerHelper.loadTopology("4LokalBFS.smartgridtopo");
        state4 = AttackerHelper.loadInput("4LokalBFS.smartgridinput");

        analyzer = new GraphAnalyzer();

    }

    /**
     * private method to initialize the Hacker using the launch configuration
     *
     * @param rootNode
     *            the root node
     * @param hackingSpeed
     *            the hacking speed
     * @
     *             cException
     */
    private void initializeHacker(final String rootNode, final String hackingSpeed)  {
        this.localHacker = new LocalHacker();

        // only for fast testing
        //localHacker.initForTest("BFS_HACKING", hackingSpeed, rootNode);

        AttackerHelper.initializeLokalHacker(this.localHacker, "BFS_HACKING", rootNode, hackingSpeed);
    }

    /**
     * simple case where there no hacking will tackplace except from the root node because of
     * non-existence of the logical connections
     */
    @Test
    public void testExample1() {

        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology1, state1);
            this.initializeHacker("1975215759", "1");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        result = this.localHacker.run(smartGridTopology1, result);
        assertTrue((AttackerHelper.isHacked("1975215759", result)));

        result = this.localHacker.run(smartGridTopology1, result);
        assertFalse((AttackerHelper.isHacked("120683905", result)));
        assertFalse((AttackerHelper.isHacked("1115033456", result)));

    }

    /**
     * a little more complicated example where there is more than one cluster, and another entities
     * : Control center, which must be hacked here the principles of the BFS are not deeply tested
     **/
    @Test
    public void testExample2() {

        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology2, state2);
            this.initializeHacker("1393481011", "1");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        result = this.localHacker.run(smartGridTopology2, result);
        assertTrue((AttackerHelper.isHacked("246790859", result)));

        result = this.localHacker.run(smartGridTopology2, result);
        assertTrue((AttackerHelper.isHacked("1655292657", result)));

        result = this.localHacker.run(smartGridTopology2, result);
        assertTrue((AttackerHelper.isHacked("14521132", result)));

        // assert that no further hacking will take place in the other cluster
        result = this.localHacker.run(smartGridTopology2, result);
        assertFalse((AttackerHelper.isHacked("256891282", result)));
        assertFalse((AttackerHelper.isHacked("872033984", result)));

    }

    /**
     * tests the hacking in one timeStep
     */
    @Test
    public void testExample2HighSpeed() {

        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology2, state2);
            this.initializeHacker("1393481011", "3");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        result = this.localHacker.run(smartGridTopology2, result);

        assertTrue((AttackerHelper.isHacked("246790859", result)));
        assertTrue((AttackerHelper.isHacked("1655292657", result)));
        assertTrue((AttackerHelper.isHacked("14521132", result)));
        assertFalse((AttackerHelper.isHacked("256891282", result)));
        assertFalse((AttackerHelper.isHacked("872033984", result)));

    }

    /**
     * the same as example 2 but with another cluster not connected physically but connected
     * logically, to test that hacking takes place only between logicaly connected nodes in THE SAME
     * CLUSTER
     */
    @Test
    public void testExample3() {

        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology3, state3);
            this.initializeHacker("1747919629", "1");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        result = this.localHacker.run(smartGridTopology3, result);
        assertTrue((AttackerHelper.isHacked("467974583", result)));

        result = this.localHacker.run(smartGridTopology3, result);
        assertTrue((AttackerHelper.isHacked("863653531", result)));

        result = this.localHacker.run(smartGridTopology3, result);
        assertTrue((AttackerHelper.isHacked("498534568", result)));

        result = this.localHacker.run(smartGridTopology3, result);

        assertFalse((AttackerHelper.isHacked("1274744613", result)));
        assertFalse((AttackerHelper.isHacked("923648604", result)));
        assertFalse((AttackerHelper.isHacked("1582400279", result)));
        assertFalse((AttackerHelper.isHacked("1436710101", result)));

    }

    /**
     * tests the hacking in one timeStep
     */
    @Test
    public void testExample3HighSpeed() {

        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology3, state3);
            this.initializeHacker("1747919629", "3");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        result = this.localHacker.run(smartGridTopology3, result);

        assertTrue((AttackerHelper.isHacked("467974583", result)));
        assertTrue((AttackerHelper.isHacked("863653531", result)));
        assertTrue((AttackerHelper.isHacked("498534568", result)));

        assertFalse((AttackerHelper.isHacked("1274744613", result)));
        assertFalse((AttackerHelper.isHacked("923648604", result)));
        assertFalse((AttackerHelper.isHacked("1582400279", result)));
        assertFalse((AttackerHelper.isHacked("1436710101", result)));

    }

    /**
     * more complicated model, here the concepts of the BFS are deeply tested with testing the case
     * that there are damaged nodes and power outage.
     */
    @Test
    public void testExample4() {

        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology4, state4);
            this.initializeHacker("482167018", "1");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        result = this.localHacker.run(smartGridTopology4, result);
        assertTrue((AttackerHelper.isHacked("1205665067", result)));
        result = this.localHacker.run(smartGridTopology4, result);

        if (AttackerHelper.isHacked("2104270627", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("937455096", result));
            result = this.localHacker.run(smartGridTopology4, result);
            assertTrue(AttackerHelper.isHacked("937455096", result));
        } else if (AttackerHelper.isHacked("937455096", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("2104270627", result));
            result = this.localHacker.run(smartGridTopology4, result);
            assertTrue(AttackerHelper.isHacked("2104270627", result));
        } else {
            fail("no new enitity is hacked");
        }

        result = this.localHacker.run(smartGridTopology4, result);

        if (AttackerHelper.isHacked("1248608785", result)) {
            assertFalse((AttackerHelper.isHacked("1859529940", result)));
            assertFalse((AttackerHelper.isHacked("1165228897", result)));
            assertFalse((AttackerHelper.isHacked("751353869", result)));
            assertFalse((AttackerHelper.isHacked("1863246515", result)));

            result = this.localHacker.run(smartGridTopology4, result);
            result = this.localHacker.run(smartGridTopology4, result);
            result = this.localHacker.run(smartGridTopology4, result);
            result = this.localHacker.run(smartGridTopology4, result);

            assertTrue((AttackerHelper.isHacked("1859529940", result)));
            assertTrue((AttackerHelper.isHacked("1165228897", result)));
            assertTrue((AttackerHelper.isHacked("751353869", result)));
            assertTrue((AttackerHelper.isHacked("1863246515", result)));

        } else if (AttackerHelper.isHacked("1859529940", result) || AttackerHelper.isHacked("1165228897", result)
                || AttackerHelper.isHacked("751353869", result) || AttackerHelper.isHacked("1863246515", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1248608785", result));

            result = this.localHacker.run(smartGridTopology4, result);
            result = this.localHacker.run(smartGridTopology4, result);
            result = this.localHacker.run(smartGridTopology4, result);
            assertTrue((AttackerHelper.isHacked("1859529940", result)));
            assertTrue((AttackerHelper.isHacked("1165228897", result)));
            assertTrue((AttackerHelper.isHacked("751353869", result)));
            assertTrue((AttackerHelper.isHacked("1863246515", result)));

            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1248608785", result));

            result = this.localHacker.run(smartGridTopology4, result);
            assertTrue("more than one entity is hacked", AttackerHelper.isHacked("1248608785", result));
        } else {
            fail("no new enitity is hacked");
        }

        result = this.localHacker.run(smartGridTopology4, result);
        assertFalse((AttackerHelper.isHacked("472850471", result)));
        assertFalse((AttackerHelper.isHacked("1758982613", result)));
        assertFalse((AttackerHelper.isHacked("1308613170", result)));

    }

    /**
     * tests the hacking in one timeStep
     */
    @Test
    public void testExample4HighSpeed() {

        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology4, state4);
            this.initializeHacker("482167018", "8");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        result = this.localHacker.run(smartGridTopology4, result);

        assertTrue((AttackerHelper.isHacked("1205665067", result)));
        assertTrue((AttackerHelper.isHacked("2104270627", result)));
        assertTrue((AttackerHelper.isHacked("937455096", result)));
        assertTrue((AttackerHelper.isHacked("1248608785", result)));
        assertTrue((AttackerHelper.isHacked("1859529940", result)));
        assertTrue((AttackerHelper.isHacked("1165228897", result)));
        assertTrue((AttackerHelper.isHacked("751353869", result)));
        assertTrue((AttackerHelper.isHacked("1863246515", result)));

        assertFalse((AttackerHelper.isHacked("472850471", result)));
        assertFalse((AttackerHelper.isHacked("1758982613", result)));
        assertFalse((AttackerHelper.isHacked("1308613170", result)));

    }

    /**
     * testing the case that the root node is wrongly entered
     */
    @Test(expected = java.lang.RuntimeException.class)
    public void testFalschRootNodeID() {
        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology4, state4);
            this.initializeHacker("123", "8");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        result = this.localHacker.run(smartGridTopology4, result);
    }

    /**
     * repeat the random choose of the root node when no root node is entered
     */
    @Test
    public void repeatTestNoRootNodeID() {
        for (int i = 0; i < 10; i++) {
            this.testNoRootNodeID();
        }
    }

    /**
     * testing when no root node is entered
     */
    @Test
    public void testNoRootNodeID() {
        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology4, state4);
            this.initializeHacker("", "8");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        result = this.localHacker.run(smartGridTopology4, result);

        if (AttackerHelper.isHacked("472850471", result)) {
            assertFalse(AttackerHelper.isHacked("482167018", result));
            assertFalse(AttackerHelper.isHacked("1205665067", result));
            assertFalse((AttackerHelper.isHacked("2104270627", result)));
            assertFalse((AttackerHelper.isHacked("937455096", result)));
            assertFalse((AttackerHelper.isHacked("1248608785", result)));
            assertFalse((AttackerHelper.isHacked("1859529940", result)));
            assertFalse((AttackerHelper.isHacked("1165228897", result)));
            assertFalse((AttackerHelper.isHacked("751353869", result)));
            assertFalse((AttackerHelper.isHacked("1863246515", result)));
            assertFalse((AttackerHelper.isHacked("1758982613", result)));
            assertFalse((AttackerHelper.isHacked("1308613170", result)));

        } else if (AttackerHelper.isHacked("1758982613", result)) {
            assertFalse((AttackerHelper.isHacked("482167018", result)));
            assertFalse((AttackerHelper.isHacked("1205665067", result)));
            assertFalse((AttackerHelper.isHacked("2104270627", result)));
            assertFalse((AttackerHelper.isHacked("937455096", result)));
            assertFalse((AttackerHelper.isHacked("1248608785", result)));
            assertFalse((AttackerHelper.isHacked("1859529940", result)));
            assertFalse((AttackerHelper.isHacked("1165228897", result)));
            assertFalse((AttackerHelper.isHacked("751353869", result)));
            assertFalse((AttackerHelper.isHacked("1863246515", result)));
            assertFalse((AttackerHelper.isHacked("472850471", result)));
            assertFalse((AttackerHelper.isHacked("1308613170", result)));

        } else if (AttackerHelper.isHacked("1308613170", result)) {
            assertFalse((AttackerHelper.isHacked("482167018", result)));
            assertFalse((AttackerHelper.isHacked("1205665067", result)));
            assertFalse((AttackerHelper.isHacked("2104270627", result)));
            assertFalse((AttackerHelper.isHacked("937455096", result)));
            assertFalse((AttackerHelper.isHacked("1248608785", result)));
            assertFalse((AttackerHelper.isHacked("1859529940", result)));
            assertFalse((AttackerHelper.isHacked("1165228897", result)));
            assertFalse((AttackerHelper.isHacked("751353869", result)));
            assertFalse((AttackerHelper.isHacked("1863246515", result)));
            assertFalse((AttackerHelper.isHacked("472850471", result)));
            assertFalse((AttackerHelper.isHacked("1758982613", result)));

        } else {
            assertTrue((AttackerHelper.isHacked("482167018", result)));
            assertTrue((AttackerHelper.isHacked("1205665067", result)));
            assertTrue((AttackerHelper.isHacked("2104270627", result)));
            assertTrue((AttackerHelper.isHacked("937455096", result)));
            assertTrue((AttackerHelper.isHacked("1248608785", result)));
            assertTrue((AttackerHelper.isHacked("1859529940", result)));
            assertTrue((AttackerHelper.isHacked("1165228897", result)));
            assertTrue((AttackerHelper.isHacked("751353869", result)));
            assertTrue((AttackerHelper.isHacked("1863246515", result)));

            assertFalse((AttackerHelper.isHacked("472850471", result)));
            assertFalse((AttackerHelper.isHacked("1758982613", result)));
            assertFalse((AttackerHelper.isHacked("1308613170", result)));
        }

    }

    /**
     * copy files from resources to be tested in the plugin
     *
     * @throws IOException
     *             IOException
     */
    public static void copyFilesForTesting() throws IOException {
        Files.copy(new File("resources/1LokalBFS.smartgridtopo").toPath(), new File("1LokalBFS.smartgridtopo").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/1LokalBFS.smartgridinput").toPath(),
                new File("1LokalBFS.smartgridinput").toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/2LokalBFS.smartgridtopo").toPath(), new File("2LokalBFS.smartgridtopo").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/2LokalBFS.smartgridinput").toPath(),
                new File("2LokalBFS.smartgridinput").toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/3LokalBFS.smartgridtopo").toPath(), new File("3LokalBFS.smartgridtopo").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/3LokalBFS.smartgridinput").toPath(),
                new File("3LokalBFS.smartgridinput").toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/4LokalBFS.smartgridtopo").toPath(), new File("4LokalBFS.smartgridtopo").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/4LokalBFS.smartgridinput").toPath(),
                new File("4LokalBFS.smartgridinput").toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * delete files which are used for testing
     *
     * @throws IOException
     *             IOException
     */
    @AfterClass
    public static void deleteFilesForTesting() throws IOException {
        Files.delete(new File("1LokalBFS.smartgridtopo").toPath());
        Files.delete(new File("1LokalBFS.smartgridinput").toPath());
        Files.delete(new File("2LokalBFS.smartgridtopo").toPath());
        Files.delete(new File("2LokalBFS.smartgridinput").toPath());
        Files.delete(new File("3LokalBFS.smartgridtopo").toPath());
        Files.delete(new File("3LokalBFS.smartgridinput").toPath());
        Files.delete(new File("4LokalBFS.smartgridtopo").toPath());
        Files.delete(new File("4LokalBFS.smartgridinput").toPath());

    }
}
