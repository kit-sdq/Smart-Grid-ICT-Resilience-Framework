package smartgrid.simcontrol.coupling;

import java.util.Map;

public interface IKritisSimulationNew {
    Map<String, Map<String, PowerSpec>> run(Map<String, Map<String, Double>> power);

    Map<String, Map<String, PowerSpec>> getDefaultDemand();
}
