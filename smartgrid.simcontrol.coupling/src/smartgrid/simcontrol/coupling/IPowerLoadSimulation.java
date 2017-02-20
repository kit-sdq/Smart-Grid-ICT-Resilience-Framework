package smartgrid.simcontrol.coupling;

import java.util.Map;

public interface IPowerLoadSimulation {
    Map<String, Double> run(Map<String, PowerSpec> kritisDemands, Map<String, SmartMeterState> smartMeterStates);
}
