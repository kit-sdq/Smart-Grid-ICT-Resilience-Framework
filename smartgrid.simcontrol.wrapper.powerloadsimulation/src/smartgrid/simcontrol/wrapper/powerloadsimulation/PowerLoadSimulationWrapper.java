package smartgrid.simcontrol.wrapper.powerloadsimulation;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.IPowerLoadSimulationWrapper;
import smartgrid.simcontrol.coupling.IPowerLoadSimulation;
import smartgrid.simcontrol.coupling.ISmartMeterState;
import smartgrid.simcontrol.coupling.PowerSpec;
import smartgrid.simcontrol.coupling.TopologyContainer;
import smartgrid.simcontrol.iip.PowerLoadSimulation;

public class PowerLoadSimulationWrapper implements IPowerLoadSimulationWrapper {

    private IPowerLoadSimulation powerSim;

    @Override
    public ErrorCodeEnum init(final ILaunchConfiguration config) {
        return ErrorCodeEnum.SUCCESS;
    }

    @Override
    public String getName() {
        return Constants.IIP_OPF_NAME;
    }

    @Override
    public Map<String, Map<String, Double>> run(Map<String, Map<String, PowerSpec>> kritisDemands, Map<String, Map<String, ISmartMeterState>> smartMeterStates) {
        if (powerSim == null) {
            throw new RuntimeException("Protocol violation: initData() has to be called prior to run().");
        }
        return powerSim.run(kritisDemands, smartMeterStates);
    }

    @Override
    public void initData(TopologyContainer topoData) {
        powerSim = new PowerLoadSimulation(topoData.getOpfConfig());
        powerSim.initializeNodeIDs(new ArrayList<String>(topoData.getMapCItoHVN().keySet()));
    }
}
