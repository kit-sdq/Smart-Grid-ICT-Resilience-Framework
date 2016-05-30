package smartgrid.simcontrol.baselib.coupling;

import java.util.List;

public interface IPowerLoadSimulation {
    List<PowerPerNode> run(List<AbstractCostFunction> costFunctions, List<SmartMeterState> smartMeterStates);
}
