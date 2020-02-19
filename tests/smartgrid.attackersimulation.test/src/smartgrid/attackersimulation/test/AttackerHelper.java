package smartgrid.attackersimulation.test;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;

import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.attackersimulation.LocalHacker;
import smartgrid.attackersimulation.ViralHacker;
import smartgrid.impactanalysis.GraphAnalyzer;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputPackage;
import smartgridoutput.On;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoPackage;

/**
 * Class contains helping methods for the Hacking-testing classes
 *
 * @author mazenebada
 *
 */
public class AttackerHelper {

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
    protected static void initializeAnalyzer(final GraphAnalyzer analyzer, final String ignore) {
//      final ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
//      final ILaunchConfigurationType type = manager
//              .getLaunchConfigurationType("smartgrid.newsimcontrol.SimcontrolLaunchConfigurationType");
//      final ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, "testInstance");
//      workingCopy.setAttribute(Constants.IGNORE_LOC_CON_KEY, ignore);
//      final ILaunchConfiguration config = workingCopy.doSave();
      Map<InitializationMapKeys, String> initMap = new HashMap<InitializationMapKeys, String>();
      initMap.put(InitializationMapKeys.IGNORE_LOC_CON_KEY, ignore);
      analyzer.init(initMap);

    }

    /**
     * creating an instance of the SimControl launch-configuration to be used in the init-method
     *
     * @param haker
     *            hacker
     * @param hackingStyle
     *            hackingStyle
     * @param rootNode
     *            rootNode
     * @param hackingSpeed
     *            hackingSpeed
     * @throws CoreException
     *             coreException
     */
    protected static void initializeLokalHacker(final LocalHacker haker, final String hackingStyle,
            final String rootNode, final String hackingSpeed) {
        Map<InitializationMapKeys, String> initMap = new HashMap<InitializationMapKeys, String>();
        initMap.put(InitializationMapKeys.HACKING_STYLE_KEY, hackingStyle);
        initMap.put(InitializationMapKeys.HACKING_SPEED_KEY, hackingSpeed);
        initMap.put(InitializationMapKeys.ROOT_NODE_ID_KEY, rootNode);
        haker.init(initMap);
    }

    /**
     * creating an instance of the SimControl launch-configuration to be used in the init-method
     *
     * @param haker
     *            hacker
     * @param hackingStyle
     *            hackingStyle
     * @param hackingSpeed
     *            hackingSpeed
     * @throws CoreException
     *             coreException
     */
    protected static void initializeViralHacker(final ViralHacker haker, final String hackingStyle,
            final String hackingSpeed) {

        Map<InitializationMapKeys, String> initMap = new HashMap<InitializationMapKeys, String>();
        initMap.put(InitializationMapKeys.HACKING_STYLE_KEY, hackingStyle);
        initMap.put(InitializationMapKeys.HACKING_SPEED_KEY, hackingSpeed);
        haker.init(initMap);
        
    }

    /**
     * get the entity state from the given id
     *
     * @param id
     *            id
     * @param result
     *            result
     * @return the entity state with the id given
     */
    public static smartgridoutput.EntityState getEntityFromResult(final String id, final ScenarioResult result) {
        for (int i = 0; i < result.getStates().size(); i++) {
            if (result.getStates().get(i).getOwner().getId().equals(id)) {
                return result.getStates().get(i);
            }
        }
        throw new NullPointerException();
    }

    /**
     * determine whether the entitystate is hacked
     *
     * @param id
     *            id
     * @param result
     *            result
     * @return true if it is hacked
     */
    public static boolean isHacked(final String id, final ScenarioResult result) {
        final smartgridoutput.EntityState entityState = getEntityFromResult(id, result);
        return ((On) entityState).isIsHacked();
    }
}
