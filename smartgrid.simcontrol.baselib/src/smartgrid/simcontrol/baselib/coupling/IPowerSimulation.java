package smartgrid.simcontrol.baselib.coupling;

import java.util.List;

public interface IPowerSimulation {
    List<PowerPerNode> doSimuation(List<AbstractCostFunction> costFunctions, List<SmartMeterState> smartMeterStates);
}
