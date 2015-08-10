package smartgrid.simcontrol.wrapper.powerloadsimulation.wrapper;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.coupling.PowerPerNode;
import smartgrid.simcontrol.interfaces.ErrorCodeEnum;
import smartgrid.simcontrol.interfaces.IPowerLoadSimulation;
import smartgrid.simcontrol.wrapper.powerloadsimulation.simulation.PowerLoadSimulation;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.Scenario;

public class PowerLoadSimulationWrapper implements IPowerLoadSimulation {

	@Override
	public ScenarioState run(Scenario smartGridTopo, ScenarioState impactAnalysisInput,
			ScenarioResult impactAnalysisOutput) {
		PowerLoadSimulation simulation = new PowerLoadSimulation();
		
		//TODO: Get smartMeterStates and cost functions for the "doSimulation"-method
		
		List<PowerPerNode> list = simulation.doSimuation(null, null);
		
		//TODO: Convert PowerPerNode list to ScenarioState
		
		return null;
	}

	@Override
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {
		return ErrorCodeEnum.SUCCESS;
	}

	@Override
	public String getName() {
		return "PowerLoadSimulationWrapper";
	}

}
