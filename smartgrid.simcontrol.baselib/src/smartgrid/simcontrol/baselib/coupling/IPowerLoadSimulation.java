package smartgrid.simcontrol.baselib.coupling;

import java.util.Map;

import smartgridoutput.EntityState;

public interface IPowerLoadSimulation {
    Map<String, Double> run(Map<String, PowerSpec> kritisDemands, Map<String, EntityState> smartMeterStates);
}
