package smartgrid.simcontrol.mocks;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.PowerSpec;

public class KritisSimulationMock implements IKritisSimulationWrapper {

    @Override
    public Map<String, PowerSpec> run(Map<String, Double> power) {
        return new HashMap<String, PowerSpec>();
    }

    @Override
    public Map<String, PowerSpec> getDefaultDemand() {
        return new HashMap<String, PowerSpec>();
    }

    @Override
    public ErrorCodeEnum init(final ILaunchConfiguration config) throws CoreException {
        return ErrorCodeEnum.SUCCESS;
    }

    @Override
    public String getName() {
        return "Mock";
    }
}
