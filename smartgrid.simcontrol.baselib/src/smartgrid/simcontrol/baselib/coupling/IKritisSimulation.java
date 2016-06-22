package smartgrid.simcontrol.baselib.coupling;

import java.util.Map;

public interface IKritisSimulation {
    Map<String,PowerSpec> run(Map<String,Double> power);
}
