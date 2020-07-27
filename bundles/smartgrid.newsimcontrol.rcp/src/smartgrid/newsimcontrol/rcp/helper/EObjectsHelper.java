package smartgrid.newsimcontrol.rcp.helper;

import java.io.IOException;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;

import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputPackage;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoPackage;


public class EObjectsHelper {

	private static final Logger LOG = Logger.getLogger(EObjectsHelper.class);

    /**
     * This Method saves a Scenario Result on the File System at the given Path
     *
     *
     * @param result
     *            Scenario Result to be written on File System
     * @param path
     *            Path there the File will be written to
     */
    public static void saveToFileSystem(final EObject result, final String path) {

        if (result == null) {
            LOG.error("Cannot persist a model with the content of \"null\". Something went wrong.");
            return;
        }

        final ResourceSet resSet = new ResourceSetImpl();

        final Resource resource = resSet.createResource(URI.createFileURI(path));
        resource.getContents().add(result);

        try {
            resource.save(Collections.EMPTY_MAP);
        } catch (final IOException e) {
            LOG.error("Could not save to file.", e);
        }
    }
    
    /**
     * @param path
     *            path of the ScenarioState to be used
     * @return The read ScenarioState file
     */
    public static ScenarioState loadInput(final String path) {

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
    public static SmartGridTopology loadTopology(final String path) {

        final ResourceSet resourceSet = new ResourceSetImpl();

        resourceSet.getPackageRegistry().put("http://www.modelversioning.org/emfprofile/application/1.1",
                EMFProfileApplicationPackage.eINSTANCE);
        resourceSet.getPackageRegistry().put("http://sdq.ipd.uka.de/smartgridtopo/1.1", SmartgridtopoPackage.eINSTANCE);

        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("smartgridtopo",
                new XMIResourceFactoryImpl());
        final Resource resource = resourceSet.getResource(URI.createFileURI(path), true);
        return (SmartGridTopology) resource.getContents().get(0);
    }

}
