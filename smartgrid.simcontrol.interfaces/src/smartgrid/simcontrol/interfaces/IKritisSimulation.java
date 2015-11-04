package smartgrid.simcontrol.interfaces;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

public interface IKritisSimulation {

	/**
	 * Runs the Kritis simulation
	 * 
	 * @param smartGridTopo
	 * @param kritisInput
	 * @return
	 */
	public ScenarioResult run(SmartGridTopology smartGridTopo, ScenarioState kritisInput);

	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException;

	public String getName();
}
