package smartgrid.simcontrol.baselib.coupling;

import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;

public interface IKritisSimulationWrapper extends IKritisSimulation {

	public ErrorCodeEnum init(ILaunchConfiguration config) throws Exception;

	public String getName();
}
