package smartgrid.simcontrol.coupling;

import java.util.Map;

public interface IKritisSimulation {
    Map<String,PowerSpec> run(Map<String,Double> power);
    Map<String, PowerSpec> getDefaultDemand();
}
