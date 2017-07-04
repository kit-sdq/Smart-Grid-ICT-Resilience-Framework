package smartgrid.simcontrol.wrapper.kritissimulation;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;
import smartgrid.simcontrol.coupling.PowerSpec;

public class KritisSimulationWrapper implements IKritisSimulationWrapper {

    private static final Logger LOG = Logger.getLogger(KritisSimulationWrapper.class);

    @Override
    public Map<String, PowerSpec> run(Map<String, Double> power) {
        return null;
    }

    @Override
    public Map<String, PowerSpec> getDefaultDemand() {
        return null;
    }

    @Override
    public ErrorCodeEnum init(final ILaunchConfiguration config) {
        return ErrorCodeEnum.SUCCESS;
    }

    @Override
    public String getName() {
        return "KRITIS Remote Simulation Wrapper";
    }
}
