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
 * Class to test the standard hacking of the viral Hacker
 *
 * @author mazenebada
 *
 */
public class ViralHackerStandardTest {
    private static ScenarioState state1;
    private static SmartGridTopology smartGridTopology1;
    private static ScenarioState state2;
    private static SmartGridTopology smartGridTopology2;
    private static ScenarioState state3;
    private static SmartGridTopology smartGridTopology3;

    private ViralHacker viralHacker;
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

        smartGridTopology1 = AttackerHelper.loadTopology("1ViralSTANDARD.smartgridtopo");
        state1 = AttackerHelper.loadInput("1ViralSTANDARD.smartgridinput");

        smartGridTopology2 = AttackerHelper.loadTopology("2ViralSTANDARD.smartgridtopo");
        state2 = AttackerHelper.loadInput("2ViralSTANDARD.smartgridinput");

        smartGridTopology3 = AttackerHelper.loadTopology("3ViralSTANDARD.smartgridtopo");
        state3 = AttackerHelper.loadInput("3ViralSTANDARD.smartgridinput");

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
        //viralHacker.initForTest("STANDARD_HACKING", hackingSpeed);

        AttackerHelper.initializeViralHacker(this.viralHacker, "STANDARD_HACKING", hackingSpeed);
    }

    /**
     * simple model, where after two timesteps 4 nodes must be hacked and the last one shouldn't be
     * hacked, according to the definition of the Standard hacking in ViralHacker
     */
    @Test
    public void testExample1() {

        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology1, state1);
            this.initializeHacker("1");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        for (int i = 0; i < result.getStates().size(); i++) {
            if (result.getStates().get(i).getOwner().getId().equals("1571125568")) {
                ((On) result.getStates().get(i)).setIsHacked(true);
            }
        }

        result = this.viralHacker.run(smartGridTopology1, result);
        result = this.viralHacker.run(smartGridTopology1, result);

        assertTrue((AttackerHelper.isHacked("2031923734", result)));
        assertTrue((AttackerHelper.isHacked("908399952", result)));
        assertTrue((AttackerHelper.isHacked("1281830768", result)));

        // checks that no further hacking takes place
        assertFalse((AttackerHelper.isHacked("1270990099", result)));

        result = this.viralHacker.run(smartGridTopology1, result);
        assertTrue((AttackerHelper.isHacked("1270990099", result)));

    }

    /**
     * a little more complicated example where there is more than one cluster, however exists
     * logical connections between nodes in different clusters testing concepts of standard hacking
     * ( looking for the nearest node to hack )
     */
    @Test
    public void testExample2() {

        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology2, state2);
            this.initializeHacker("1");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        for (int i = 0; i < result.getStates().size(); i++) {
            if (result.getStates().get(i).getOwner().getId().equals("271357168")) {
                ((On) result.getStates().get(i)).setIsHacked(true);
            }
        }
        result = this.viralHacker.run(smartGridTopology2, result);

        if (AttackerHelper.isHacked("318283790", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1083901485", result));
            result = this.viralHacker.run(smartGridTopology2, result);
            assertTrue((AttackerHelper.isHacked("1083901485", result)));
            assertTrue((AttackerHelper.isHacked("235823947", result)));

        } else if (AttackerHelper.isHacked("1083901485", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("318283790", result));
            result = this.viralHacker.run(smartGridTopology2, result);
            assertTrue((AttackerHelper.isHacked("318283790", result)));
            // result = viralHacker.run(smartGridTopology2, result);
            assertTrue((AttackerHelper.isHacked("235823947", result)));

        } else {
            fail("no new enitity is hacked");
        }

        result = this.viralHacker.run(smartGridTopology2, result);
        assertTrue((AttackerHelper.isHacked("1182937025", result)));
        assertTrue((AttackerHelper.isHacked("1764862482", result)));
        assertTrue((AttackerHelper.isHacked("1779839586", result)));

        // checks that no further hacking takes place in other clusters
        result = this.viralHacker.run(smartGridTopology1, result);
        assertFalse(AttackerHelper.isHacked("1996837252", result));
        assertFalse(AttackerHelper.isHacked("1297359386", result));
        assertFalse(AttackerHelper.isHacked("1866636555", result));
        assertFalse(AttackerHelper.isHacked("1320992232", result));

    }

    /**
     * more complicated example testing loops of smartmeters
     */
    @Test
    public void testExample3() {

        ScenarioResult result = null;
        try {
            // for fast testing
            //analyzer.initForTesting(false);

            AttackerHelper.initializeAnalyzer(analyzer, "false");

            result = analyzer.run(smartGridTopology3, state3);
            this.initializeHacker("1");
        } catch (final Exception e) {
            fail("Failed to initialize the launch configuration");
        }

        for (int i = 0; i < result.getStates().size(); i++) {
            if (result.getStates().get(i).getOwner().getId().equals("489594540")) {
                ((On) result.getStates().get(i)).setIsHacked(true);
            }
        }
        result = this.viralHacker.run(smartGridTopology3, result);

        if (AttackerHelper.isHacked("435196693", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1844545366", result));
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("462256016", result));

            result = this.viralHacker.run(smartGridTopology3, result);
            if (AttackerHelper.isHacked("462256016", result) && AttackerHelper.isHacked("1844545366", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("2081541092", result));
            } else if (AttackerHelper.isHacked("2081541092", result) && AttackerHelper.isHacked("1844545366", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("462256016", result));
            } else if (AttackerHelper.isHacked("462256016", result) && AttackerHelper.isHacked("2081541092", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1844545366", result));
            } else {
                fail();
            }
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("210658029", result));
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1318625049", result));
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1811934783", result));

        } else if (AttackerHelper.isHacked("1844545366", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("435196693", result));
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("462256016", result));

            result = this.viralHacker.run(smartGridTopology3, result);
            if (AttackerHelper.isHacked("462256016", result) && AttackerHelper.isHacked("435196693", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("2081541092", result));
            } else if (AttackerHelper.isHacked("2081541092", result) && AttackerHelper.isHacked("435196693", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("462256016", result));
            } else if (AttackerHelper.isHacked("462256016", result) && AttackerHelper.isHacked("2081541092", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("435196693", result));
            } else {
                fail();
            }
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("210658029", result));
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1318625049", result));
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1811934783", result));

        } else if (AttackerHelper.isHacked("462256016", result)) {
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("435196693", result));
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1844545366", result));

            result = this.viralHacker.run(smartGridTopology3, result);
            if (AttackerHelper.isHacked("435196693", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1844545366", result));
                if (AttackerHelper.isHacked("1811934783", result)) {
                    assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1318625049", result));
                } else if (AttackerHelper.isHacked("1318625049", result)) {
                    assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1811934783", result));
                } else {
                    fail();
                }
            } else if (AttackerHelper.isHacked("1844545366", result)) {
                assertFalse("more than one entity is hacked", AttackerHelper.isHacked("435196693", result));
                if (AttackerHelper.isHacked("1811934783", result)) {
                    assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1318625049", result));
                } else if (AttackerHelper.isHacked("1318625049", result)) {
                    assertFalse("more than one entity is hacked", AttackerHelper.isHacked("1811934783", result));
                } else {
                    fail();
                }
            } else {
                fail();
            }
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("210658029", result));
            assertFalse("more than one entity is hacked", AttackerHelper.isHacked("2081541092", result));

        } else {
            fail("no new enitity is hacked");
        }

        result = this.viralHacker.run(smartGridTopology3, result);
        assertTrue((AttackerHelper.isHacked("462256016", result)));
        assertTrue((AttackerHelper.isHacked("1811934783", result)));
        assertTrue((AttackerHelper.isHacked("1318625049", result)));
        assertTrue((AttackerHelper.isHacked("210658029", result)));

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
        Files.copy(new File("resources/1ViralSTANDARD.smartgridtopo").toPath(),
                new File("1ViralSTANDARD.smartgridtopo").toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/1ViralSTANDARD.smartgridinput").toPath(),
                new File("1ViralSTANDARD.smartgridinput").toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/2ViralSTANDARD.smartgridtopo").toPath(),
                new File("2ViralSTANDARD.smartgridtopo").toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/2ViralSTANDARD.smartgridinput").toPath(),
                new File("2ViralSTANDARD.smartgridinput").toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/3ViralSTANDARD.smartgridtopo").toPath(),
                new File("3ViralSTANDARD.smartgridtopo").toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(new File("resources/3ViralSTANDARD.smartgridinput").toPath(),
                new File("3ViralSTANDARD.smartgridinput").toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * delete files which are used for testing
     *
     * @throws IOException
     *             IOException
     */
    @AfterClass
    public static void deleteFilesForTesting() throws IOException {
        Files.delete(new File("1ViralSTANDARD.smartgridtopo").toPath());
        Files.delete(new File("1ViralSTANDARD.smartgridinput").toPath());
        Files.delete(new File("2ViralSTANDARD.smartgridtopo").toPath());
        Files.delete(new File("2ViralSTANDARD.smartgridinput").toPath());
        Files.delete(new File("3ViralSTANDARD.smartgridtopo").toPath());
        Files.delete(new File("3ViralSTANDARD.smartgridinput").toPath());

    }

}
