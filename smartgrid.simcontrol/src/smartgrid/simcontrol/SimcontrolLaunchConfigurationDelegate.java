package smartgrid.simcontrol;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

/*TODO Delete after Branching */

//import smartgrid.simcontrol.interfaces.IAttackerSimulation;
//import smartgrid.simcontrol.interfaces.IImpactAnalysis;
//import smartgrid.simcontrol.interfaces.IPowerLoadSimulation;
//import smartgrid.simcontrol.interfaces.ITerminationCondition;
//import smartgrid.simcontrol.interfaces.ITimeProgressor;

/**
 * This class provides the Delegate for the SimControl Approach of the Smartgrid Analysis'
 *
 *
 * @author Christian
 * @implements ILaunchConfigurationDelegate
 *
 */
public class SimcontrolLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(SimcontrolLaunchConfigurationDelegate.class);

    /**
     * {@inheritDoc}
     * <P>
     *
     * Launches an SimController Analysis with the given Launch Configuration
     */
    @Override
    public void launch(final ILaunchConfiguration configuration, final String mode, final ILaunch launch,
            final IProgressMonitor monitor) throws CoreException {

        SimulationController.init(configuration);

        SimulationController.run();
    }

//	@Deprecated
//	private SmartGridTopology loadScenario(String path) {
//		SmartGridTopology s = null;
//		SmartgridtopoPackageImpl.init();
//
//		// Code Removed - Christian
//
//		ResourceSet resSet = new ResourceSetImpl();
//		Resource resource = resSet.getResource(URI.createFileURI(path), true);
//
//		EObject r = resource.getContents().get(0);
//		LOG.debug("[SimcontrolLaunchConfigurationDelegate]: Class: " + r.getClass());
//		s = (SmartGridTopology) resource.getContents().get(0);
//
//		return s;
//	}
//
//	@Deprecated
//	private ScenarioState loadInput(String path) {
//		ScenarioState input = null;
//		SmartgridinputPackageImpl.init();
//
//		// Code Removed - Christian
//
//		ResourceSet resSet = new ResourceSetImpl();
//		Resource resource = resSet.getResource(URI.createFileURI(path), true);
//
//		EObject r = resource.getContents().get(0);
//		LOG.debug("[SimcontrolLaunchConfigurationDelegate]: Class: " + r.getClass());
//		input = (ScenarioState) resource.getContents().get(0);
//
//		return input;
//	}
}
