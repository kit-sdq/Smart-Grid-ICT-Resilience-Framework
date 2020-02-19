
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

import smartgrid.attackersimulation.ViralHacker;
import smartgrid.impactanalysis.GraphAnalyzer;
import smartgridinput.ScenarioState;
import smartgridoutput.On;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

/**
 * Class to test the fullymeshed hacking of the viral Hacker
 *
 * @author mazenebada
 *
 */
public class ViralHackerFullyMeshedTest {
    private static GraphAnalyzer analyzer;
    private static ScenarioState state1;
    private static SmartGridTopology smartGridTopology1;
    private static ScenarioState state2;
    private static SmartGridTopology smartGridTopology2;
    private static ScenarioState state3;
    private static SmartGridTopology smartGridTopology3;
    private ViralHacker viralHacker;

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

        smartGridTopology1 = AttackerHelper.loadTopology("1ViralFM.smartgridtopo");
        state1 = AttackerHelper.loadInput("1ViralFM.smartgridinput");

        smartGridTopology2 = AttackerHelper.loadTopology("2ViralFM.smartgridtopo");
        state2 = AttackerHelper.loadInput("2ViralFM.smartgridinput");

        smartGridTopology3 = AttackerHelper.loadTopology("3ViralFM.smartgridtopo");
        state3 = AttackerHelper.loadInput("3ViralFM.smartgridinput");

        analyzer = new GraphAnalyzer();

    }

    /**
     * private method to initialize the Hacker using the launch configuration
     *
     * @param hackingSpeed
     *            hackingSpeed
     * @throws Exception
     *             Exception
     */
    private void initializeHacker(final String hackingSpeed) throws Exception {
        this.viralHacker = new ViralHacker();

        // for fast testing
        //viralHacker.initForTest("FULLY_MESHED_HACKING", hackingSpeed);

        AttackerHelper.initializeViralHacker(this.viralHacker, "FULLY_MESHED_HACKING", hackingSpeed);
    }

    /**
     * Simple case taking in consideration the existence of the logical connections
     */
    @Test
    public void testExample1() {
        //
        ScenarioResult result = null;
        try {
            //analyzer.initForTesting(false);
            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology1, state1);
            this.initializeHacker("1");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        for (int i = 0; i < result.getStates().size(); i++) {
            if (result.getStates().get(i).getOwner().getId().equals("1018235233")) {
                ((On) result.getStates().get(i)).setIsHacked(true);
            }
        }

        result = this.viralHacker.run(smartGridTopology1, result);
        if ((AttackerHelper.isHacked("726160233", result))) {
            assertFalse("more than one entit is hacked", (AttackerHelper.isHacked("110686151", result)));
        } else if ((AttackerHelper.isHacked("110686151", result))) {
            assertFalse("more than one entit is hacked", (AttackerHelper.isHacked("726160233", result)));
        } else {
            fail("no enitity is hacked");
        }

        result = this.viralHacker.run(smartGridTopology1, result);

        assertTrue((AttackerHelper.isHacked("726160233", result)));
        assertTrue((AttackerHelper.isHacked("110686151", result)));
        assertFalse((AttackerHelper.isHacked("1208066222", result)));

    }

    /**
     * Simple case taking in consideration the ignoring of the logical connections
     */
    @Test
    public void testExample2() {

        ScenarioResult result = null;
        try {
            //analyzer.initForTesting(true);
            AttackerHelper.initializeAnalyzer(analyzer, "true");

            result = analyzer.run(smartGridTopology2, state2);
            this.initializeHacker("1");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        for (int i = 0; i < result.getStates().size(); i++) {
            if (result.getStates().get(i).getOwner().getId().equals("365939879")) {
                ((On) result.getStates().get(i)).setIsHacked(true);
            }
        }

        // first time step
        result = this.viralHacker.run(smartGridTopology2, result);

        if ((AttackerHelper.isHacked("1539940675", result))) {
            assertFalse("more than one entit is hacked", (AttackerHelper.isHacked("1139460151", result)));
        } else if ((AttackerHelper.isHacked("1139460151", result))) {
            assertFalse("more than one entit is hacked", (AttackerHelper.isHacked("1539940675", result)));
        } else {
            fail("no enitity is hacked");
        }

        // second time step
        result = this.viralHacker.run(smartGridTopology1, result);

        assertTrue((AttackerHelper.isHacked("1539940675", result)));
        assertTrue((AttackerHelper.isHacked("1139460151", result)));
        assertTrue((AttackerHelper.isHacked("518549169", result)));
    }

    /**
     * little more complicated, with more than one cluster, starting with a hacked node in two
     * clusters
     */
    @Test
    public void testExample3() {

        ScenarioResult result = null;
        try {
            //analyzer.initForTesting(false);
            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology3, state3);
            this.initializeHacker("1");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        for (int i = 0; i < result.getStates().size(); i++) {
            if (result.getStates().get(i).getOwner().getId().equals("1203320135")
                    || result.getStates().get(i).getOwner().getId().equals("29489090")) {
                ((On) result.getStates().get(i)).setIsHacked(true);
            }
        }

        // first time step
        result = this.viralHacker.run(smartGridTopology3, result);

        // first cluster :
        assertTrue(AttackerHelper.isHacked("1063163866", result));

        // second cluster :
        if (AttackerHelper.isHacked("1982613961", result)) {
            assertFalse("more than one entit is hacked", AttackerHelper.isHacked("112474108", result));
            assertFalse("more than one entit is hacked", AttackerHelper.isHacked("336939794", result));
        } else if (AttackerHelper.isHacked("112474108", result)) {
            assertFalse("more than one entit is hacked", AttackerHelper.isHacked("1982613961", result));
            assertFalse("more than one entit is hacked", AttackerHelper.isHacked("336939794", result));
        } else if (AttackerHelper.isHacked("336939794", result)) {
            assertFalse("more than one entit is hacked", AttackerHelper.isHacked("1982613961", result));
            assertFalse("more than one entit is hacked", AttackerHelper.isHacked("112474108", result));
        } else {
            fail("no enitity is hacked");
        }

        // third cluster :
        assertFalse(AttackerHelper.isHacked("882098380", result));
        assertFalse(AttackerHelper.isHacked("1895773290", result));
        assertFalse(AttackerHelper.isHacked("3930050", result));

        // second time step
        result = this.viralHacker.run(smartGridTopology3, result);

        // first cluster is already hacked
        // second cluster :
        assertTrue(AttackerHelper.isHacked("1982613961", result));
        assertTrue(AttackerHelper.isHacked("112474108", result));
        assertTrue(AttackerHelper.isHacked("336939794", result));

        // third cluster :
        assertFalse(AttackerHelper.isHacked("882098380", result));
        assertFalse(AttackerHelper.isHacked("1895773290", result));
        assertFalse(AttackerHelper.isHacked("3930050", result));
    }

    /**
     * tests the hacking in one timeStep
     */
    @Test
    public void testExample3HighSpeed() {
        ScenarioResult result = null;
        try {
            //analyzer.initForTesting(false);
            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology3, state3);
            this.initializeHacker("3");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        for (int i = 0; i < result.getStates().size(); i++) {
            if (result.getStates().get(i).getOwner().getId().equals("1203320135")
                    || result.getStates().get(i).getOwner().getId().equals("29489090")) {
                ((On) result.getStates().get(i)).setIsHacked(true);
            }
        }

        result = this.viralHacker.run(smartGridTopology3, result);

        // first cluster :
        assertTrue(AttackerHelper.isHacked("1063163866", result));

        // second cluster :
        assertTrue(AttackerHelper.isHacked("1982613961", result));
        assertTrue(AttackerHelper.isHacked("112474108", result));
        assertTrue(AttackerHelper.isHacked("336939794", result));

        // third cluster :
        assertFalse(AttackerHelper.isHacked("882098380", result));
        assertFalse(AttackerHelper.isHacked("1895773290", result));
        assertFalse(AttackerHelper.isHacked("3930050", result));

    }

    /**
     * test the case where there is no hacked nodes in the result scenario
     * It will be ignored as this case where no hacked nodes is now handled with another way
     */
    @Ignore
    @Test
    public void testNoHackedCase() {
        ScenarioResult result = null;
        try {
            //analyzer.initForTesting(false);
            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology3, state3);
            this.initializeHacker("1");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        result = this.viralHacker.run(smartGridTopology3, result);

        boolean noHacked = true;
        for (final smartgridinput.EntityState state : state3.getEntityStates()) {
            if (state.isIsHacked()) {
                noHacked = false;
            }
        }

        if (noHacked) {
            for (final smartgridoutput.EntityState state : result.getStates()) {
                if (((On) state).isIsHacked()) {
                    fail("Entity with id " + state.getOwner().getId() + " is hacked without a known reason");
                }
            }
        }

    }

    /**
     * copy files from resources to be tested in the plugin
     *
     * @throws IOException
     *             IOException
     */
    public static void copyFilesForTesting() throws IOException {
        Files.copy(new File("resources/1ViralFM.smartgridtopo").toPath(), new File("1ViralFM.smartgridtopo").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/1ViralFM.smartgridinput").toPath(), new File("1ViralFM.smartgridinput").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/2ViralFM.smartgridtopo").toPath(), new File("2ViralFM.smartgridtopo").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/2ViralFM.smartgridinput").toPath(), new File("2ViralFM.smartgridinput").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/3ViralFM.smartgridtopo").toPath(), new File("3ViralFM.smartgridtopo").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/3ViralFM.smartgridinput").toPath(), new File("3ViralFM.smartgridinput").toPath(),
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
        Files.delete(new File("1ViralFM.smartgridtopo").toPath());
        Files.delete(new File("1ViralFM.smartgridinput").toPath());
        Files.delete(new File("2ViralFM.smartgridtopo").toPath());
        Files.delete(new File("2ViralFM.smartgridinput").toPath());
        Files.delete(new File("3ViralFM.smartgridtopo").toPath());
        Files.delete(new File("3ViralFM.smartgridinput").toPath());

    }

}
