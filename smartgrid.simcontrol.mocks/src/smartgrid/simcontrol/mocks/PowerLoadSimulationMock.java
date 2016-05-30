/**
 * 
 */
package smartgrid.simcontrol.mocks;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.AbstractCostFunction;
import smartgrid.simcontrol.baselib.coupling.IPowerLoadSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.PowerPerNode;
import smartgrid.simcontrol.baselib.coupling.SmartMeterState;

/**
 * @author Christian
 *
 */
public class PowerLoadSimulationMock implements IPowerLoadSimulationWrapper {

	/**
	 * {@inheritDoc}
	 * <P>
	 * Runs a mocked Power Load Simulation. This Mock does not change anything.
	 */
	@Override
	public List<PowerPerNode> run(List<AbstractCostFunction> costFunctions, List<SmartMeterState> smartMeterStates) {
		return null;
	}

	@Override
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {
		return ErrorCodeEnum.SUCCESS;
	}

	@Override
	public String getName() {
		return "Power Load Simulation Mock";
	}
}
