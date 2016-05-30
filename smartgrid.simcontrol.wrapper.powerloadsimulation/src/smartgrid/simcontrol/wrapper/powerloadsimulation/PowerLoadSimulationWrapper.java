package smartgrid.simcontrol.wrapper.powerloadsimulation;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.AbstractCostFunction;
import smartgrid.simcontrol.baselib.coupling.IPowerLoadSimulation;
import smartgrid.simcontrol.baselib.coupling.IPowerLoadSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.PowerPerNode;
import smartgrid.simcontrol.baselib.coupling.SmartMeterState;

public class PowerLoadSimulationWrapper implements IPowerLoadSimulationWrapper {

    @Override
    public List<PowerPerNode> run(final List<AbstractCostFunction> costFunctions,
            final List<SmartMeterState> smartMeterStates) {

        // TODO implement wrapper!
        final IPowerLoadSimulation dummy = new IPowerLoadSimulation() {
            @Override
            public List<PowerPerNode> run(final List<AbstractCostFunction> costFunctions,
                    final List<SmartMeterState> smartMeterStates) {
                throw new RuntimeException("Power load simulation wrapper not implemented!");
            }
        };

        final List<PowerPerNode> powerPerNodes = dummy.run(costFunctions, smartMeterStates);

        return powerPerNodes;
    }

    @Override
    public ErrorCodeEnum init(final ILaunchConfiguration config) throws CoreException {
        return ErrorCodeEnum.SUCCESS;
    }

    @Override
    public String getName() {
        return "Power Load Simulation Wrapper";
    }
}
