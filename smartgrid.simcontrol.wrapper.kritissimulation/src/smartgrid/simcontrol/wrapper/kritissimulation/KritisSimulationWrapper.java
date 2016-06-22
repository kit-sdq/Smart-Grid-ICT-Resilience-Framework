package smartgrid.simcontrol.wrapper.kritissimulation;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulation;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.PowerSpec;

public class KritisSimulationWrapper implements IKritisSimulationWrapper {

    @Override
    public Map<String, PowerSpec> run(Map<String, Double> power) {

        // TODO implement wrapper!
        final IKritisSimulation dummy = new IKritisSimulation() {
            @Override
            public Map<String, PowerSpec> run(Map<String, Double> power) {
                throw new RuntimeException("KRITIS wrapper not yet implemented!");
            }
        };

        return dummy.run(power);
    }

    @Override
    public Map<String, PowerSpec> getDefaultDemand() {
        // TODO implement wrapper!
        return null;
    }

    @Override
    public ErrorCodeEnum init(final ILaunchConfiguration config) throws CoreException {
        return ErrorCodeEnum.SUCCESS;
    }

    @Override
    public String getName() {
        return "KRITIS Simulation Wrapper";
    }
}
