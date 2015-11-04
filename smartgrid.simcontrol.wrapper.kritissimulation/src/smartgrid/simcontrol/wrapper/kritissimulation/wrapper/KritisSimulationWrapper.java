package smartgrid.simcontrol.wrapper.kritissimulation.wrapper;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.coupling.AbstractCostFunction;
import smartgrid.simcontrol.interfaces.ErrorCodeEnum;
import smartgrid.simcontrol.interfaces.IKritisSimulation;
import smartgrid.simcontrol.wrapper.kritissimulation.simulation.KritisSimulation;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

public class KritisSimulationWrapper implements IKritisSimulation {

	@Override
	public ScenarioResult run(SmartGridTopology smartGridTopo, ScenarioState kritisInput) {
		KritisSimulation simulation = new KritisSimulation();

		// TODO: Get power per state from given parameters as parameter for
		// simulation

		List<AbstractCostFunction> list = simulation.doSimuation(null);

		// TODO: Convert AbstractCostFunction list to ScenarioResult

		return null;
	}

	@Override
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {
		return ErrorCodeEnum.SUCCESS;
	}

	@Override
	public String getName() {
		return "KritisSimulationWrapper";
	}

}
