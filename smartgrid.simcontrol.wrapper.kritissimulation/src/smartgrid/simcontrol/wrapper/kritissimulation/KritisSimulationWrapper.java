package smartgrid.simcontrol.wrapper.kritissimulation;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;
import smartgrid.simcontrol.coupling.PowerSpec;
import smartgrid.simcontrol.rmi.BlockingKritisDataExchanger;

public class KritisSimulationWrapper implements IKritisSimulationWrapper {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(KritisSimulationWrapper.class);

    @Override
    public Map<String, Map<String, PowerSpec>> run(Map<String, Map<String, Double>> power) throws InterruptedException {
        return BlockingKritisDataExchanger.bufferSupplyGetDemand(power);
    }

    @Override
    public Map<String, Map<String, PowerSpec>> getDefaultDemand() throws InterruptedException {
        // TODO implement?
        return BlockingKritisDataExchanger.bufferSupplyGetDemand(new HashMap<String, Map<String, Double>>());
    }

    @Override
    public void init(final ILaunchConfiguration config) {
    }

    @Override
    public String getName() {
        return Constants.KRITIS_SIMULATION_NAME;
    }
}
