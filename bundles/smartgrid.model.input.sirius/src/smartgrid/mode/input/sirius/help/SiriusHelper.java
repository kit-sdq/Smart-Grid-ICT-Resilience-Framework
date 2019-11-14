package smartgrid.mode.input.sirius.help;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.ui.PlatformUI;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;

import smartgrid.simcontrol.test.baselib.Constants;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputPackage;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoPackage;

public class SiriusHelper {

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
    public static void generateOutput(final String inputPath, final String topoPath, final String outputPath) throws CoreException {
        final ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        final ILaunchConfigurationType type = manager
                .getLaunchConfigurationType("smartgrid.simcontrol.test.SimcontrolLaunchConfigurationType");
        final ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, "testInstance (Change name in order to save it)");
        workingCopy.setAttribute(Constants.IGNORE_LOC_CON_KEY, "true");
        workingCopy.setAttribute(Constants.INPUT_PATH_KEY, inputPath);
        workingCopy.setAttribute(Constants.TOPO_PATH_KEY, topoPath);
        workingCopy.setAttribute(Constants.OUTPUT_PATH_KEY, outputPath);
        workingCopy.setAttribute(Constants.TIME_STEPS_KEY, "1");
        workingCopy.setAttribute(Constants.ATTACKER_SIMULATION_KEY, "No Attack Simulation");
        final ILaunchConfiguration config = workingCopy.doSave();
        DebugUITools.openLaunchConfigurationDialog(
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                config,
                DebugUITools.getLaunchGroup(config, "run").getIdentifier(),
                null);
    }
    
}
