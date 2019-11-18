package smartgrid.newsimcontrol;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

import smartgrid.newsimcontrol.controller.ActiveSimulationController;
import smartgrid.newsimcontrol.rmi.BlockingDataExchanger;

/**
 * This class provides the Delegate for the SimControl Approach of the Smartgrid Analysis'
 *
 * @implements ILaunchConfigurationDelegate
 */
public class SimcontrolLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

    private static final Logger LOG = Logger.getLogger(SimcontrolLaunchConfigurationDelegate.class);

    private int attempt = 0;
    private int maxAttempts = 5;

    /**
     * {@inheritDoc}
     * <P>
     *
     * Launches an SimController Analysis with the given Launch Configuration
     */
    @Override
    public void launch(final ILaunchConfiguration configuration, final String mode, final ILaunch launch, final IProgressMonitor monitor) throws CoreException {

        ActiveSimulationController simControl = new ActiveSimulationController();
        try {
            simControl.init(configuration);
            simControl.run();
        } catch (InterruptedException e) {
            LOG.info("The simulation was interrupted.");
        } catch (Throwable e) {
            LOG.fatal("An unexpected exception occured.", e);
            BlockingDataExchanger.storeException(e);

            if (attempt < maxAttempts) {
                attempt++;
                LOG.info("Attempting another run (" + (attempt + 1) + '/' + (maxAttempts + 1) + ')');
                this.launch(configuration, mode, launch, monitor);
            } else {
                LOG.info("The maximal amount of attempts failed (" + (maxAttempts + 1) + "). Terminating.");
                throw e;
            }
        }
    }
}
