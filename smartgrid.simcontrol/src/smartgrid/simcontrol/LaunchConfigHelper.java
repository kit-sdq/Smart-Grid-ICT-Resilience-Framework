package smartgrid.simcontrol;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;

public class LaunchConfigHelper {

    public static boolean isExtensionSelected(final ILaunchConfiguration launchConfig, final IKritisSimulationWrapper kritisSimExtension, String key) throws CoreException {
        return launchConfig.getAttribute(key, "").equals(kritisSimExtension.getName());
    }

}
