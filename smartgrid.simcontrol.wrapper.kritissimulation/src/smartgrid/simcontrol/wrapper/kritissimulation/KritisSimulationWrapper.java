package smartgrid.simcontrol.wrapper.kritissimulation;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;
import smartgrid.simcontrol.coupling.PowerSpec;
import smartgrid.simcontrol.rmi.BlockingKritisDataExchanger;

public class KritisSimulationWrapper implements IKritisSimulationWrapper {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(KritisSimulationWrapper.class);

    @Override
    public Map<String, Map<String, PowerSpec>> run(Map<String, Map<String, Double>> power) {
        return BlockingKritisDataExchanger.passDataToKritisSim(power);
    }

    @Override
    public Map<String, Map<String, PowerSpec>> getDefaultDemand() {
        return new HashMap<String, Map<String, PowerSpec>>(); // TODO implement?
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
