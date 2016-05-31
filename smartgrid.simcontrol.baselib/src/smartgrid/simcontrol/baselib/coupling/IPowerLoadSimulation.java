package smartgrid.simcontrol.baselib.coupling;

import java.util.Map;

import smartgridoutput.EntityState;

public interface IPowerLoadSimulation {
    Map<String,Double> run(Map<String,Double> kritisDemand, Map<String,EntityState> smartMeterStates);
}
