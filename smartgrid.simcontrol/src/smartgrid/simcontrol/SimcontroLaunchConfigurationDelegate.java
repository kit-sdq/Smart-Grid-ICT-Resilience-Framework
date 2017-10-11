package smartgrid.simcontrol;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

/**
 * This class provides the Delegate for the SimControl Approach of the Smartgrid Analysis'
 *
 * @author Christian
 * @author Misha
 * @implements ILaunchConfigurationDelegate
 */
public class SimcontroLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

    private static final Logger LOG = Logger.getLogger(SimcontroLaunchConfigurationDelegate.class);

    /**
     * {@inheritDoc}
     * <P>
     *
     * Launches an SimController Analysis with the given Launch Configuration
     */
    @Override
    public void launch(final ILaunchConfiguration configuration, final String mode, final ILaunch launch, final IProgressMonitor monitor) throws CoreException {

        SimulationController simControl = new SimulationController();
        try {
            simControl.init(configuration);
            simControl.run();
        } catch (InterruptedException e) {
            LOG.info("The simulation was interrupted.");
        }
    }
}
