package smartgrid.simcontrol.baselib.coupling;

import java.util.List;

public interface IPowerLoadSimulation {
    List<PowerPerNode> doSimuation(List<AbstractCostFunction> costFunctions, List<SmartMeterState> smartMeterStates);
}
