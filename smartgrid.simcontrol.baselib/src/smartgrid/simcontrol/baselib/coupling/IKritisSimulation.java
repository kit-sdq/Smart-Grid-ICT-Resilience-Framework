package smartgrid.simcontrol.baselib.coupling;

import java.util.List;

public interface IKritisSimulation {
    List<AbstractCostFunction> doSimuation(List<PowerPerNode> power);
}
