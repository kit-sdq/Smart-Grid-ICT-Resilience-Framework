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
import org.junit.Ignore;
import org.junit.Test;

import smartgrid.attackersimulation.LocalHacker;
import smartgrid.impactanalysis.GraphAnalyzer;
import smartgridinput.ScenarioState;
import smartgridoutput.On;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

/**
 * Test Class to test the DFS Hacking of the local hacker
 *
 * @author mazenebada
 *
 */
public class LokalHakerDFSTest {
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

        smartGridTopology1 = AttackerHelper.loadTopology("1LokalDFS.smartgridtopo");
        state1 = AttackerHelper.loadInput("1LokalDFS.smartgridinput");

        smartGridTopology2 = AttackerHelper.loadTopology("2LokalDFS.smartgridtopo");
        state2 = AttackerHelper.loadInput("2LokalDFS.smartgridinput");

        smartGridTopology3 = AttackerHelper.loadTopology("3LokalDFS.smartgridtopo");
        state3 = AttackerHelper.loadInput("3LokalDFS.smartgridinput");

        smartGridTopology4 = AttackerHelper.loadTopology("4LokalDFS.smartgridtopo");
        state4 = AttackerHelper.loadInput("4LokalDFS.smartgridinput");

        analyzer = new GraphAnalyzer();

    }

    /**
     * private method to initialize the Hacker using the launch configuration
     *
     * @param rootNode
     *            the root node
     * @param hackingSpeed
     *            the hacking speed
     * @throws Exception
     *             Exception
     */
    private void initializeHacker(final String rootNode, final String hackingSpeed) throws Exception {
        this.localHacker = new LocalHacker();

        // for fast testing
        //localHacker.initForTest("DFS_HACKING", hackingSpeed, rootNode);

        AttackerHelper.initializeLokalHacker(this.localHacker, "DFS_HACKING", rootNode, hackingSpeed);
    }

    /**
     * repeat example 1 10 times
     */
    @Test
    public void repeatTestExample1() {
        for (int i = 0; i < 10; i++) {
            try {
                this.testExample1();
            } catch (final Exception e) {
                fail("Couldn't initialize the SimControl launch configuration");
            }
        }
    }

    /**
     * simple model with one cluster testing simply ( not deeply ) DFS.
     *
     * @throws Exception
     *             Exception
     */
    public void testExample1() throws Exception {

        // for fast testing
        //analyzer.initForTesting(false);

        AttackerHelper.initializeAnalyzer(analyzer, "false");

        ScenarioResult result = analyzer.run(smartGridTopology1, state1);
        this.initializeHacker("544272241", "1");

        result = this.localHacker.run(smartGridTopology1, result);
        assertTrue(AttackerHelper.isHacked("1362127159", result));

        result = this.localHacker.run(smartGridTopology1, result);
        if (AttackerHelper.isHacked("962856184", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1945820820", result));
        } else if (AttackerHelper.isHacked("1945820820", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("962856184", result));
        } else {
            fail("no new enitity is hacked");
        }

        result = this.localHacker.run(smartGridTopology1, result);
        assertTrue(AttackerHelper.isHacked("962856184", result));
        assertTrue(AttackerHelper.isHacked("1945820820", result));

    }

    /**
     * repeat example 2 10 times
     */
    @Test
    public void repeatTestExample2() {
        for (int i = 0; i < 10; i++) {
            try {
                this.testExample2();
            } catch (final Exception e) {
                fail("Couldn't initialize the SimControl launch configuration");
            }
        }
    }

    /**
     * here the DFS is deeply tested with all posible cases
     *
     * @throws Exception
     *             Exception
     */
    public void testExample2() throws Exception {
        // for fast testing
        //analyzer.initForTesting(false);
        AttackerHelper.initializeAnalyzer(analyzer, "false");
        ScenarioResult result = analyzer.run(smartGridTopology2, state2);
        this.initializeHacker("60290462", "1");

        result = this.localHacker.run(smartGridTopology2, result);
        if (AttackerHelper.isHacked("1760136754", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("287949256", result));
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("789750590", result));

            result = this.localHacker.run(smartGridTopology2, result);
            assertTrue(AttackerHelper.isHacked("835768660", result));
            result = this.localHacker.run(smartGridTopology2, result);
            assertTrue(AttackerHelper.isHacked("405101474", result));

            result = this.localHacker.run(smartGridTopology2, result);
            if (AttackerHelper.isHacked("1589120546", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("115519003", result));
                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue("more than one entity is hacked", AttackerHelper.isHacked("115519003", result));
            } else if (AttackerHelper.isHacked("115519003", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1589120546", result));
                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue("more than one entity is hacked", AttackerHelper.isHacked("1589120546", result));
            } else {
                fail("no new enitity is hacked");
            }

            result = this.localHacker.run(smartGridTopology2, result);
            if (AttackerHelper.isHacked("287949256", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("789750590", result));
                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue("more than one entity is hacked", AttackerHelper.isHacked("789750590", result));
            } else if (AttackerHelper.isHacked("789750590", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("287949256", result));
                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue("more than one entity is hacked", AttackerHelper.isHacked("287949256", result));
            } else {
                fail("no new enitity is hacked");
            }

        } else if (AttackerHelper.isHacked("287949256", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1760136754", result));
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("789750590", result));
            result = this.localHacker.run(smartGridTopology2, result);
            if (AttackerHelper.isHacked("1760136754", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("789750590", result));
                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue(AttackerHelper.isHacked("835768660", result));

                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue(AttackerHelper.isHacked("405101474", result));

                result = this.localHacker.run(smartGridTopology2, result);
                if (AttackerHelper.isHacked("1589120546", result)) {
                    assertFalse("more than one entity is hacked", AttackerHelper.isHacked("115519003", result));
                    result = this.localHacker.run(smartGridTopology2, result);
                    assertTrue("more than one entity is hacked", AttackerHelper.isHacked("115519003", result));
                } else if (AttackerHelper.isHacked("115519003", result)) {
                    assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1589120546", result));
                    result = this.localHacker.run(smartGridTopology2, result);
                    assertTrue("more than one entity is hacked", AttackerHelper.isHacked("1589120546", result));
                } else {
                    fail("no new enitity is hacked");
                }
                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue("more than one entity is hacked", AttackerHelper.isHacked("789750590", result));

            } else if (AttackerHelper.isHacked("789750590", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1760136754", result));
                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue("more than one entity is hacked", AttackerHelper.isHacked("1760136754", result));
                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue(AttackerHelper.isHacked("835768660", result));
                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue(AttackerHelper.isHacked("405101474", result));

                result = this.localHacker.run(smartGridTopology2, result);
                if (AttackerHelper.isHacked("1589120546", result)) {
                    assertFalse("more than one entity is hacked", AttackerHelper.isHacked("115519003", result));
                    result = this.localHacker.run(smartGridTopology2, result);
                    assertTrue("more than one entity is hacked", AttackerHelper.isHacked("115519003", result));
                } else if (AttackerHelper.isHacked("115519003", result)) {
                    assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1589120546", result));
                    result = this.localHacker.run(smartGridTopology2, result);
                    assertTrue("more than one entity is hacked", AttackerHelper.isHacked("1589120546", result));
                } else {
                    fail("no new enitity is hacked");
                }
            } else {
                fail("no new enitity is hacked");
            }
        } else if (AttackerHelper.isHacked("789750590", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("287949256", result));
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1760136754", result));
            result = this.localHacker.run(smartGridTopology2, result);
            if (AttackerHelper.isHacked("1760136754", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("287949256", result));
                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue(AttackerHelper.isHacked("835768660", result));

                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue(AttackerHelper.isHacked("405101474", result));

                result = this.localHacker.run(smartGridTopology2, result);
                if (AttackerHelper.isHacked("1589120546", result)) {
                    assertFalse("more than one entity is hacked", AttackerHelper.isHacked("115519003", result));
                    result = this.localHacker.run(smartGridTopology2, result);
                    assertTrue("more than one entity is hacked", AttackerHelper.isHacked("115519003", result));
                } else if (AttackerHelper.isHacked("115519003", result)) {
                    assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1589120546", result));
                    result = this.localHacker.run(smartGridTopology2, result);
                    assertTrue("more than one entity is hacked", AttackerHelper.isHacked("1589120546", result));
                } else {
                    fail("no new enitity is hacked");
                }
                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue("more than one entity is hacked", AttackerHelper.isHacked("287949256", result));

            } else if (AttackerHelper.isHacked("287949256", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1760136754", result));
                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue("more than one entity is hacked", AttackerHelper.isHacked("1760136754", result));
                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue(AttackerHelper.isHacked("835768660", result));

                result = this.localHacker.run(smartGridTopology2, result);
                assertTrue(AttackerHelper.isHacked("405101474", result));

                result = this.localHacker.run(smartGridTopology2, result);
                if (AttackerHelper.isHacked("1589120546", result)) {
                    assertFalse("more than one entity is hacked", AttackerHelper.isHacked("115519003", result));
                    result = this.localHacker.run(smartGridTopology2, result);
                    assertTrue("more than one entity is hacked", AttackerHelper.isHacked("115519003", result));
                } else if (AttackerHelper.isHacked("115519003", result)) {
                    assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1589120546", result));
                    result = this.localHacker.run(smartGridTopology2, result);
                    assertTrue("more than one entity is hacked", AttackerHelper.isHacked("1589120546", result));
                } else {
                    fail("no new enitity is hacked");
                }
            } else {
                fail("no new enitity is hacked");
            }
        } else {
            fail("no new enitity is hacked");
        }
    }

    /**
     * repeat example 3 10 times
     */
    @Test
    public void repeatTestExample3() {
        for (int i = 0; i < 10; i++) {
            try {
                this.testExample3();
            } catch (final Exception e) {
                fail("Couldn't initialize the SimControl launch configuration");
            }
        }
    }

    /**
     * testing simply DFS with a special case where there is already one hacked node in addition to
     * the root node
     *
     * @throws Exception
     *             Exception
     */
    public void testExample3() throws Exception {

        // for fast testing
        //analyzer.initForTesting(false);

        AttackerHelper.initializeAnalyzer(analyzer, "false");

        ScenarioResult result = analyzer.run(smartGridTopology3, state3);
        this.initializeHacker("1300266070", "1");

        for (int i = 0; i < result.getStates().size(); i++) {
            if (result.getStates().get(i).getOwner().getId().equals("935307198")) {
                ((On) result.getStates().get(i)).setIsHacked(true);
            }
        }

        result = this.localHacker.run(smartGridTopology3, result);
        if (AttackerHelper.isHacked("1114290134", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1347393527", result));
            result = this.localHacker.run(smartGridTopology3, result);
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1347393527", result));
            assertTrue(AttackerHelper.isHacked("276215558", result));
            result = this.localHacker.run(smartGridTopology3, result);
            assertTrue(AttackerHelper.isHacked("1347393527", result));
        } else if (AttackerHelper.isHacked("1347393527", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1114290134", result));
            result = this.localHacker.run(smartGridTopology3, result);
            assertTrue(AttackerHelper.isHacked("1114290134", result));
            result = this.localHacker.run(smartGridTopology3, result);
            assertTrue(AttackerHelper.isHacked("276215558", result));

        } else {
            fail("no new enitity is hacked");
        }
    }

    /**
     * repeat example 4 10 times
     */
  //TODO: korrigieren, funktioniert nicht. Was war das Problem mit der alte Version?
    @Test
    @Ignore
    public void repeatTestExample4() {
        for (int i = 0; i < 10; i++) {
            try {
                this.testExample4();
            } catch (final Exception e) {
                fail("Couldn't initialize the SimControl launch configuration");
            }
        }
    }

    /**
     * complex model contatining loops of nodes to be tested, also containing destroyed nodes and
     * power outage
     *
     * @throws Exception
     *             Exception
     */
    public void testExample4() throws Exception {

        // for fast testing
        //analyzer.initForTesting(false);

        AttackerHelper.initializeAnalyzer(analyzer, "false");

        ScenarioResult result = analyzer.run(smartGridTopology4, state4);
        this.initializeHacker("245702078", "1");

        result = this.localHacker.run(smartGridTopology4, result);
        if (AttackerHelper.isHacked("1393836843", result) || AttackerHelper.isHacked("333265609", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1221865866", result));
            for (int i = 0; i < 4; i++) {
                result = this.localHacker.run(smartGridTopology4, result);
            }
            assertTrue(AttackerHelper.isHacked("1393836843", result));
            assertTrue(AttackerHelper.isHacked("333265609", result));
            assertTrue(AttackerHelper.isHacked("119482048", result));
            assertTrue(AttackerHelper.isHacked("435082100", result));
            assertTrue(AttackerHelper.isHacked("1565650058", result));

            result = this.localHacker.run(smartGridTopology4, result);
            assertTrue(AttackerHelper.isHacked("1221865866", result));

            result = this.localHacker.run(smartGridTopology4, result);
            assertTrue(AttackerHelper.isHacked("449699814", result));

        } else if (AttackerHelper.isHacked("1221865866", result)) {

            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1393836843", result));
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("333265609", result));

            result = this.localHacker.run(smartGridTopology4, result);
            assertTrue(AttackerHelper.isHacked("449699814", result));

            result = this.localHacker.run(smartGridTopology4, result);
            assertTrue(AttackerHelper.isHacked("449699814", result));

            for (int i = 0; i < 5; i++) {
                result = this.localHacker.run(smartGridTopology4, result);
            }
            assertTrue(AttackerHelper.isHacked("1393836843", result));
            assertTrue(AttackerHelper.isHacked("333265609", result));
            assertTrue(AttackerHelper.isHacked("119482048", result));
            assertTrue(AttackerHelper.isHacked("435082100", result));
            assertTrue(AttackerHelper.isHacked("1565650058", result));

        } else {
            fail("no new enitity is hacked");
        }

        for (int i = 0; i < 2; i++) {
            result = this.localHacker.run(smartGridTopology4, result);
        }
        assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1229758823", result));
        assertFalse("more than one entity is hacked", AttackerHelper.isHacked("2096886186", result));
        assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1711321592", result));

    }

    /**
     * tests the hacking in one timeStep
     */
    //TODO: korrigieren, funktioniert nicht. Was war das Problem mit der alte Version?
    @Test
    @Ignore
    public void testExample4HighSpeed() {

        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology4, state4);
            this.initializeHacker("245702078", "7");
        } catch (final Exception e) {
            fail("Couldn't initialize the SimControl launch configuration");
        }

        result = this.localHacker.run(smartGridTopology4, result);

        assertTrue(AttackerHelper.isHacked("1393836843", result));
        assertTrue(AttackerHelper.isHacked("333265609", result));
        assertTrue(AttackerHelper.isHacked("119482048", result));
        assertTrue(AttackerHelper.isHacked("435082100", result));
        assertTrue(AttackerHelper.isHacked("1565650058", result));
        assertTrue(AttackerHelper.isHacked("1221865866", result));
        assertTrue(AttackerHelper.isHacked("449699814", result));

        for (int i = 0; i < 2; i++) {
            result = this.localHacker.run(smartGridTopology4, result);
        }
        assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1229758823", result));
        assertFalse("more than one entity is hacked", AttackerHelper.isHacked("2096886186", result));
        assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1711321592", result));

    }

    /**
     * copy files from resources to be tested in the plugin
     *
     * @throws IOException
     *             IOException
     */
    public static void copyFilesForTesting() throws IOException {
        Files.copy(new File("resources/1LokalDFS.smartgridtopo").toPath(), new File("1LokalDFS.smartgridtopo").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/1LokalDFS.smartgridinput").toPath(),
                new File("1LokalDFS.smartgridinput").toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/2LokalDFS.smartgridtopo").toPath(), new File("2LokalDFS.smartgridtopo").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/2LokalDFS.smartgridinput").toPath(),
                new File("2LokalDFS.smartgridinput").toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/3LokalDFS.smartgridtopo").toPath(), new File("3LokalDFS.smartgridtopo").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/3LokalDFS.smartgridinput").toPath(),
                new File("3LokalDFS.smartgridinput").toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/4LokalDFS.smartgridtopo").toPath(), new File("4LokalDFS.smartgridtopo").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/4LokalDFS.smartgridinput").toPath(),
                new File("4LokalDFS.smartgridinput").toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * delete files which are used for testing
     *
     * @throws IOException
     *             IOException
     */
    @AfterClass
    public static void deleteFilesForTesting() throws IOException {
        Files.delete(new File("1LokalDFS.smartgridtopo").toPath());
        Files.delete(new File("1LokalDFS.smartgridinput").toPath());
        Files.delete(new File("2LokalDFS.smartgridtopo").toPath());
        Files.delete(new File("2LokalDFS.smartgridinput").toPath());
        Files.delete(new File("3LokalDFS.smartgridtopo").toPath());
        Files.delete(new File("3LokalDFS.smartgridinput").toPath());
        Files.delete(new File("4LokalDFS.smartgridtopo").toPath());
        Files.delete(new File("4LokalDFS.smartgridinput").toPath());

    }
}
