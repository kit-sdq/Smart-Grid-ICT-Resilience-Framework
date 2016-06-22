package smartgrid.simcontrol.wrapper.powerloadsimulation;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.IPowerLoadSimulation;
import smartgrid.simcontrol.baselib.coupling.IPowerLoadSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.PowerSpec;
import smartgridoutput.EntityState;

public class PowerLoadSimulationWrapper implements IPowerLoadSimulationWrapper {



    @Override
    public Map<String, Double> run(Map<String, PowerSpec> kritisDemands, Map<String, EntityState> smartMeterStates) {

        // TODO implement wrapper!
        final IPowerLoadSimulation dummy = new IPowerLoadSimulation() {
            @Override
            public Map<String, Double> run(Map<String, PowerSpec> kritisDemands,
                    Map<String, EntityState> smartMeterStates) {
                throw new RuntimeException("Power load simulation wrapper not implemented!");
            }
        };

        return dummy.run(kritisDemands, smartMeterStates);
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
