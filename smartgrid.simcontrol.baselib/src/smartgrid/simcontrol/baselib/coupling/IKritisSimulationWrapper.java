package smartgrid.simcontrol.baselib.coupling;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;

public interface IKritisSimulationWrapper extends IKritisSimulation {

    public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException;

    public String getName();

    public Map<String, PowerSpec> getDefaultDemand();
}
