package smartgrid.simcontrol.baselib.coupling;

import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

public interface IKritisSimulationWrapper {

	/**
	 * Runs the Kritis simulation
	 * 
	 * @param smartGridTopo
	 * @param kritisInput
	 * @return
	 */
	public ScenarioResult run(SmartGridTopology smartGridTopo, ScenarioState kritisInput);

	public ErrorCodeEnum init(ILaunchConfiguration config) throws Exception;

	public String getName();
}
