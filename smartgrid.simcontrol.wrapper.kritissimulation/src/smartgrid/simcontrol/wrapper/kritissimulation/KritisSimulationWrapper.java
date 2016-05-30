package smartgrid.simcontrol.wrapper.kritissimulation;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.AbstractCostFunction;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulation;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.PowerPerNode;

public class KritisSimulationWrapper implements IKritisSimulationWrapper {

	@Override
	public List<AbstractCostFunction> run(List<PowerPerNode> power) {

	    // TODO implement wrapper!
	    IKritisSimulation dummy = new IKritisSimulation() {
            @Override
            public List<AbstractCostFunction> run(List<PowerPerNode> power) {
                throw new RuntimeException("KRITIS wrapper not yet implemented!");
            }
        };
	    
        List<AbstractCostFunction> costFunctions = dummy.run(power);
		return costFunctions;
	}

	@Override
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {
		return ErrorCodeEnum.SUCCESS;
	}

	@Override
	public String getName() {
		return "KRITIS Simulation Wrapper";
	}

}
