package smartgrid.simcontrol.baselib.coupling;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.coupling.IKritisSimulation;

public interface IKritisSimulationWrapper extends IKritisSimulation, ISimulationComponent {

    void init(ILaunchConfiguration config) throws CoreException;
}
