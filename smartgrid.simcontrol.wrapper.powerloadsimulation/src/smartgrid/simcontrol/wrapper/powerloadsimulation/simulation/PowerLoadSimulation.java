package smartgrid.simcontrol.wrapper.powerloadsimulation.simulation;

import java.util.List;

import smartgrid.simcontrol.baselib.coupling.AbstractCostFunction;
import smartgrid.simcontrol.baselib.coupling.IPowerLoadSimulation;
import smartgrid.simcontrol.baselib.coupling.PowerPerNode;
import smartgrid.simcontrol.baselib.coupling.SmartMeterState;

public class PowerLoadSimulation implements IPowerLoadSimulation {

	@Override
	public List<PowerPerNode> doSimuation(List<AbstractCostFunction> costFunctions,
			List<SmartMeterState> smartMeterStates) {
		return null;
	}

}
