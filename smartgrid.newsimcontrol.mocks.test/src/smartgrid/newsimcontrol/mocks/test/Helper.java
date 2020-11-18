
package smartgrid.newsimcontrol.mocks.test;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;

import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputPackage;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoPackage;

/**
 * Contains helping method for AnalyzerTest classes.
 *
 * @author mazenebada
 *
 */
public class Helper {
	
	private Helper() {
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
     * copy files from resources to be tested in the plugin
     *
     * @param string
     *            whether ignoring logical conn. or not
     * @throws IOException
     *             IOException
     */
    protected static void copyFilesForTesting(final String string) throws IOException {
        if (string.equals("IgnoreLogical")) {
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
        } else if (string.contentEquals("WithLogical")) {
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
        if (string.equals("IgnoreLogical")) {
            Files.delete(new File("1_NoLogical.smartgridtopo").toPath());
            Files.delete(new File("2_NoLogical.smartgridtopo").toPath());
            Files.delete(new File("3_NoLogical.smartgridtopo").toPath());
            Files.delete(new File("4_NoLogical.smartgridtopo").toPath());
            Files.delete(new File("1_NoLogical.smartgridinput").toPath());
            Files.delete(new File("2_NoLogical.smartgridinput").toPath());
            Files.delete(new File("3_NoLogical.smartgridinput").toPath());
            Files.delete(new File("4_NoLogical.smartgridinput").toPath());

        } else if (string.equals("WithLogical")) {
            Files.delete(new File("1_WithLogical.smartgridtopo").toPath());
            Files.delete(new File("2_WithLogical.smartgridtopo").toPath());
            Files.delete(new File("3_WithLogical.smartgridtopo").toPath());
            Files.delete(new File("1_WithLogical.smartgridinput").toPath());
            Files.delete(new File("2_WithLogical.smartgridinput").toPath());
            Files.delete(new File("3_WithLogical.smartgridinput").toPath());
        }
    }

}
