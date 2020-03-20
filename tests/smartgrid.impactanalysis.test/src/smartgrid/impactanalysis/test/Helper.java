
package smartgrid.impactanalysis.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;

import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.impactanalysis.GraphAnalyzer;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputPackage;
import smartgridoutput.Defect;
import smartgridoutput.NoPower;
import smartgridoutput.NoUplink;
import smartgridoutput.On;
import smartgridoutput.Online;
import smartgridoutput.ScenarioResult;
import smartgridtopo.ControlCenter;
import smartgridtopo.GenericController;
import smartgridtopo.InterCom;
import smartgridtopo.NetworkNode;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;
import smartgridtopo.SmartgridtopoPackage;

/**
 * Contains helping method for AnalyzerTest classes.
 *
 * @author mazenebada
 *
 */
public class Helper {

    /**
     * @param result
     *            SceneriaResult to be printed
     * @return description of the ScenarioResult to be printed in case of failure of test(s)
     */
    protected static String printResult(final ScenarioResult result) {
        String resultString = "\n";
        if (result.getStates().size() > 0) {
            resultString += result.getStates().size() + " Network Entity(ies) found : \n";
        }
        for (int i = 0; i < result.getStates().size(); i++) {
            if (result.getStates().get(i) instanceof On) {
                resultString += ((On) result.getStates().get(i)).isIsHacked() + " ";
            }
            if (result.getStates().get(i) instanceof Online) {
                resultString += "Online ";
            }
            if (result.getStates().get(i) instanceof NoUplink) {
                resultString += "NoUplink ";
            }
            if (result.getStates().get(i) instanceof Defect) {
                resultString += "Destroyed ";
            }
            if (result.getStates().get(i) instanceof NoPower) {
                resultString += "NoPower ";
            }
            if (result.getStates().get(i).getOwner() instanceof SmartMeter) {
                resultString += "SM with id " + result.getStates().get(i).getOwner().getId() + "\n";
            }
            if (result.getStates().get(i).getOwner() instanceof ControlCenter) {
                resultString += "CC with id " + result.getStates().get(i).getOwner().getId() + "\n";
            }
            if (result.getStates().get(i).getOwner() instanceof GenericController) {
                resultString += "GC with id " + result.getStates().get(i).getOwner().getId() + "\n";
            }
            if (result.getStates().get(i).getOwner() instanceof InterCom) {
                resultString += "IC with id " + result.getStates().get(i).getOwner().getId() + "\n";
            }
            if (result.getStates().get(i).getOwner() instanceof NetworkNode) {
                resultString += "NN with id " + result.getStates().get(i).getOwner().getId() + "\n";
            }
        }
        if (result.getClusters().size() > 0) {
            resultString += "\n";
            resultString += result.getClusters().size() + " Cluster(s) found : \n";
        }
        for (int i = 0; i < result.getClusters().size(); i++) {
            resultString += "cluster " + i + " with ";
            resultString += result.getClusters().get(i).getSmartMeterCount() + " Sm and ";
            resultString += result.getClusters().get(i).getControlCenterCount() + " CC" + "\n";
            for (int j = 0; j < result.getClusters().get(i).getHasEntities().size(); j++) {
                if (result.getClusters().get(i).getHasEntities().get(j).getOwner() instanceof SmartMeter) {
                    resultString += "SM with id "
                            + result.getClusters().get(i).getHasEntities().get(j).getOwner().getId() + "\n";
                }
                if (result.getClusters().get(i).getHasEntities().get(j).getOwner() instanceof ControlCenter) {
                    resultString += "CC with id "
                            + result.getClusters().get(i).getHasEntities().get(j).getOwner().getId() + "\n";
                }
                if (result.getClusters().get(i).getHasEntities().get(j).getOwner() instanceof InterCom) {
                    resultString += "IC with id "
                            + result.getClusters().get(i).getHasEntities().get(j).getOwner().getId() + "\n";
                }
                if (result.getClusters().get(i).getHasEntities().get(j).getOwner() instanceof GenericController) {
                    resultString += "GC with id "
                            + result.getClusters().get(i).getHasEntities().get(j).getOwner().getId() + "\n";
                }
                if (result.getClusters().get(i).getHasEntities().get(j).getOwner() instanceof NetworkNode) {
                    resultString += "NN with id "
                            + result.getClusters().get(i).getHasEntities().get(j).getOwner().getId() + "\n";
                }
            }
        }
        resultString += "\n";

        return resultString;
    }

    /**
     * @param path
     *            path of the ScenarioState to be used
     * @return The read ScenarioState file
     */
    protected static ScenarioState loadInput(final String path) {

        final ResourceSet resourceSet = new ResourceSetImpl();

        resourceSet.getPackageRegistry().put("http://www.modelversioning.org/emfprofile/application/1.1",
                EMFProfileApplicationPackage.eINSTANCE);
        resourceSet.getPackageRegistry().put("http://sdq.ipd.uka.de/smartgridinput/1.0",
                SmartgridinputPackage.eINSTANCE);

        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("smartgridinput",
                new XMIResourceFactoryImpl());
        final Resource resource = resourceSet.getResource(URI.createFileURI(path), true);
        return (ScenarioState) resource.getContents().get(0);
    }

    /**
     * @param path
     *            path of the ScenarioTopology to be used
     * @return The read ScenarioTopology file
     */
    protected static SmartGridTopology loadTopology(final String path) {

        final ResourceSet resourceSet = new ResourceSetImpl();

        resourceSet.getPackageRegistry().put("http://www.modelversioning.org/emfprofile/application/1.1",
                EMFProfileApplicationPackage.eINSTANCE);
        resourceSet.getPackageRegistry().put("http://sdq.ipd.uka.de/smartgridtopo/1.1", SmartgridtopoPackage.eINSTANCE);

        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("smartgridtopo",
                new XMIResourceFactoryImpl());
        final Resource resource = resourceSet.getResource(URI.createFileURI(path), true);
        return (SmartGridTopology) resource.getContents().get(0);
    }

    /**
     * creating an instance of the SimControl launch-configuration to be used in the init-method of
     * the Graphanalyzer Class.
     *
     * @param analyzer
     *            analyzer
     * @param ignore
     *            whether to ignore the logical connections or not
     * @throws CoreException
     *             coreException
     */
    public static void initializeAnalyzer(final GraphAnalyzer analyzer, final String ignore) {
//        final ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
//        final ILaunchConfigurationType type = manager
//                .getLaunchConfigurationType("smartgrid.newsimcontrol.SimcontrolLaunchConfigurationType");
//        final ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, "testInstance");
//        workingCopy.setAttribute(Constants.IGNORE_LOC_CON_KEY, ignore);
//        final ILaunchConfiguration config = workingCopy.doSave();
        Map<InitializationMapKeys, String> initMap = new HashMap<InitializationMapKeys, String>();
        initMap.put(InitializationMapKeys.IGNORE_LOC_CON_KEY, ignore);
        analyzer.init(initMap);

    }

    /**
     * checks the number of clusters of a given ScenarioResult
     *
     * @param result1
     *            ScenarioResult to be checked
     * @param i
     *            expected number of clusters
     */
    protected static void checkNumberOfClusters(final ScenarioResult result1, final int i) {
        assertEquals("Number of cLusters not correct" + Helper.printResult(result1), i, result1.getClusters().size());
    }

    /**
     * checks the existance of an expected cluster
     *
     * @param result
     *            ScenarioResult to be checked
     * @param cluster
     *            the specific cluster to be checked
     * @param sm
     *            expected number of SM
     * @param cc
     *            expected number of CC
     * @param connection
     *            sort of connection ( NOUPLINK / ONLINE )
     * @return true if this cluster exists.
     */
    protected static boolean checkCluster(final ScenarioResult result, final int cluster, final int sm, final int cc,
            final String connection) {
        boolean numberCorrect = false;
        boolean connectionCorrect = true;

        if (result.getClusters().get(cluster).getSmartMeterCount() == sm
                && result.getClusters().get(cluster).getControlCenterCount() == cc) {
            numberCorrect = true;
        }

        if (connection == "NoUplink") {
            for (int j = 0; j < result.getClusters().get(cluster).getHasEntities().size(); j++) {
                if (!(result.getClusters().get(cluster).getHasEntities().get(j) instanceof NoUplink)) {
                    connectionCorrect = false;
                }
            }
        } else if (connection == "Online") {
            for (int j = 0; j < result.getClusters().get(cluster).getHasEntities().size(); j++) {
                if (!(result.getClusters().get(cluster).getHasEntities().get(j) instanceof Online)) {
                    connectionCorrect = false;
                }
            }
        }

        if (numberCorrect && connectionCorrect) {
            return true;
        }
        return false;
    }

    /**
     * copy files from resources to be tested in the plugin
     *
     * @param string
     *            whether ignoring logical conn. or not
     * @throws IOException
     *             IOException
     */
    protected static void copyFilesForTesting(final String string) throws IOException {
        if (string == "IgnoreLogical") {
            Files.copy(new File("resources/1_NoLogical.smartgridtopo").toPath(),
                    new File("1_NoLogical.smartgridtopo").toPath());
            Files.copy(new File("resources/1_NoLogical.smartgridinput").toPath(),
                    new File("1_NoLogical.smartgridinput").toPath());
            Files.copy(new File("resources/2_NoLogical.smartgridtopo").toPath(),
                    new File("2_NoLogical.smartgridtopo").toPath());
            Files.copy(new File("resources/2_NoLogical.smartgridinput").toPath(),
                    new File("2_NoLogical.smartgridinput").toPath());
            Files.copy(new File("resources/3_NoLogical.smartgridtopo").toPath(),
                    new File("3_NoLogical.smartgridtopo").toPath());
            Files.copy(new File("resources/3_NoLogical.smartgridinput").toPath(),
                    new File("3_NoLogical.smartgridinput").toPath());
            Files.copy(new File("resources/4_NoLogical.smartgridtopo").toPath(),
                    new File("4_NoLogical.smartgridtopo").toPath());
            Files.copy(new File("resources/4_NoLogical.smartgridinput").toPath(),
                    new File("4_NoLogical.smartgridinput").toPath());
        } else if (string == "WithLogical") {
            Files.copy(new File("resources/1_WithLogical.smartgridtopo").toPath(),
                    new File("1_WithLogical.smartgridtopo").toPath());
            Files.copy(new File("resources/1_WithLogical.smartgridinput").toPath(),
                    new File("1_WithLogical.smartgridinput").toPath());
            Files.copy(new File("resources/2_WithLogical.smartgridtopo").toPath(),
                    new File("2_WithLogical.smartgridtopo").toPath());
            Files.copy(new File("resources/2_WithLogical.smartgridinput").toPath(),
                    new File("2_WithLogical.smartgridinput").toPath());
            Files.copy(new File("resources/3_WithLogical.smartgridtopo").toPath(),
                    new File("3_WithLogical.smartgridtopo").toPath());
            Files.copy(new File("resources/3_WithLogical.smartgridinput").toPath(),
                    new File("3_WithLogical.smartgridinput").toPath());

        }
    }

    /**
     * delete files which are used for testing
     *
     * @param string
     *            whether ignoring logical conn. or not
     * @throws IOException
     *             IOException
     */
    protected static void deleteFilesForTesting(final String string) throws IOException {
        if (string == "IgnoreLogical") {
            Files.delete(new File("1_NoLogical.smartgridtopo").toPath());
            Files.delete(new File("2_NoLogical.smartgridtopo").toPath());
            Files.delete(new File("3_NoLogical.smartgridtopo").toPath());
            Files.delete(new File("4_NoLogical.smartgridtopo").toPath());
            Files.delete(new File("1_NoLogical.smartgridinput").toPath());
            Files.delete(new File("2_NoLogical.smartgridinput").toPath());
            Files.delete(new File("3_NoLogical.smartgridinput").toPath());
            Files.delete(new File("4_NoLogical.smartgridinput").toPath());

        } else if (string == "WithLogical") {
            Files.delete(new File("1_WithLogical.smartgridtopo").toPath());
            Files.delete(new File("2_WithLogical.smartgridtopo").toPath());
            Files.delete(new File("3_WithLogical.smartgridtopo").toPath());
            Files.delete(new File("1_WithLogical.smartgridinput").toPath());
            Files.delete(new File("2_WithLogical.smartgridinput").toPath());
            Files.delete(new File("3_WithLogical.smartgridinput").toPath());
        }
    }

}
