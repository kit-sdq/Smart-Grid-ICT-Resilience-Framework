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
 * Class to test the fully meshed hacking of the local hacker
 *
 * @author mazenebada
 *
 */
public class LokalHakerFullyMeshedTest {

    private static GraphAnalyzer analyzer;
    private static ScenarioState state1;
    private static SmartGridTopology smartGridTopology1;
    private static ScenarioState state2;
    private static SmartGridTopology smartGridTopology2;
    private LocalHacker localHacker;

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

        smartGridTopology1 = AttackerHelper.loadTopology("1LokalFM.smartgridtopo");
        state1 = AttackerHelper.loadInput("1LokalFM.smartgridinput");

        smartGridTopology2 = AttackerHelper.loadTopology("2LokalFM.smartgridtopo");
        state2 = AttackerHelper.loadInput("2LokalFM.smartgridinput");

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
        //localHacker.initForTest("FULLY_MESHED_HACKING", hackingSpeed, rootNode);

        AttackerHelper.initializeLokalHacker(this.localHacker, "FULLY_MESHED_HACKING", rootNode, hackingSpeed);
    }

    /**
     * tests the hacking in one timeStep
     */
    @Test
    public void testExample1HighSpeed() {

        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology1, state1);
            this.initializeHacker("349823785", "2");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        result = this.localHacker.run(smartGridTopology1, result);

        assertTrue((AttackerHelper.isHacked("349823785", result)));
        assertTrue((AttackerHelper.isHacked("97621890", result)));
        assertTrue((AttackerHelper.isHacked("7932686", result)));
    }

    /**
     * Simple case taking in consideration the existence of the logical connections
     */
    @Test
    public void testExample1() {

        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology1, state1);
            this.initializeHacker("349823785", "1");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        result = this.localHacker.run(smartGridTopology1, result);
        assertTrue((AttackerHelper.isHacked("349823785", result)));

        if ((AttackerHelper.isHacked("97621890", result))) {
            assertFalse("more than one entit is hacked", (AttackerHelper.isHacked("7932686", result)));
            result = this.localHacker.run(smartGridTopology1, result);
            assertTrue((AttackerHelper.isHacked("7932686", result)));
        } else if ((AttackerHelper.isHacked("7932686", result))) {
            assertFalse("more than one entit is hacked", (AttackerHelper.isHacked("97621890", result)));
            result = this.localHacker.run(smartGridTopology1, result);
            assertTrue((AttackerHelper.isHacked("97621890", result)));
        } else {
            fail("no enitity is hacked");
        }

    }

    /**
     * special case taking in consideration the ignoring of the logical connections
     */
    @Test
    public void testExample2HighSpeed() {

        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(true);

            AttackerHelper.initializeAnalyzer(analyzer, "true");

            result = analyzer.run(smartGridTopology2, state2);
            this.initializeHacker("228448021", "2");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        result = this.localHacker.run(smartGridTopology2, result);

        assertTrue((AttackerHelper.isHacked("228448021", result)));
        assertTrue((AttackerHelper.isHacked("217849229", result)));
        assertTrue((AttackerHelper.isHacked("1559757403", result)));

    }

    /**
     * copy files from resources to be tested in the plugin
     *
     * @throws IOException
     *             IOException
     */
    public static void copyFilesForTesting() throws IOException {
        Files.copy(new File("resources/1LokalFM.smartgridtopo").toPath(), new File("1LokalFM.smartgridtopo").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/1LokalFM.smartgridinput").toPath(), new File("1LokalFM.smartgridinput").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/2LokalFM.smartgridtopo").toPath(), new File("2LokalFM.smartgridtopo").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/2LokalFM.smartgridinput").toPath(), new File("2LokalFM.smartgridinput").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * delete files which are used for testing
     *
     * @throws IOException
     *             IOException
     */
    @AfterClass
    public static void deleteFilesForTesting() throws IOException {
        Files.delete(new File("1LokalFM.smartgridtopo").toPath());
        Files.delete(new File("1LokalFM.smartgridinput").toPath());
        Files.delete(new File("2LokalFM.smartgridtopo").toPath());
        Files.delete(new File("2LokalFM.smartgridinput").toPath());

    }
}
