package smartgrid.simcontrol.test.baselib.coupling;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

public interface ITimeProgressor extends ISimulationComponent {

    public void progress();

    /**
     * If using ExtensionPoints and so 0-parameter Constructor pass the config from Simcontrol UI to
     * this Method to build the desired AttackerSimulation ("Factory Method")
     *
     * @param config
     *            behavior for the Attacker
     * @return true if Init was successful
     * @throws CoreException
     *             If ILaunchConfiguration.getAttribute fails
     */
    public void init(ILaunchConfiguration config) throws CoreException;
}
