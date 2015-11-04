package smartgrid.simcontrol.mocks;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.interfaces.ErrorCodeEnum;
import smartgrid.simcontrol.interfaces.IKritisSimulation;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

public class KritisSimulationMock implements IKritisSimulation {

	@Override
	public ScenarioResult run(SmartGridTopology smartGridTopo, ScenarioState kritisInput) {
		return null;
	}

	@Override
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {
		return ErrorCodeEnum.SUCCESS;
	}

	@Override
	public String getName() {
		return "Kritis Simulation Mock";
	}

}
