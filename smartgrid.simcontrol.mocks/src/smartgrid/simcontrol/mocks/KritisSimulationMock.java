package smartgrid.simcontrol.mocks;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;
import smartgrid.simcontrol.coupling.PowerSpec;

public class KritisSimulationMock implements IKritisSimulationWrapper {

    @Override
    public Map<String, Map<String, PowerSpec>> run(Map<String, Map<String, Double>> power) {
        return new HashMap<String, Map<String, PowerSpec>>(); // TODO at least transfer structure from power to outpout
    }

    @Override
    public Map<String, Map<String, PowerSpec>> getDefaultDemand() {
        return new HashMap<String, Map<String, PowerSpec>>(); // TODO return more meaningful values?
    }

    @Override
    public ErrorCodeEnum init(final ILaunchConfiguration config) {
        return ErrorCodeEnum.SUCCESS;
    }

    @Override
    public String getName() {
        return "Mock";
    }
}
