package smartgrid.simcontrol.baselib.coupling;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

/**
 * 
 * Interface for the PowerLoad Simulation
 *
 */
public interface IPowerLoadSimulationWrapper {

	/**
	 * Runs the Power Load Simulation
	 * 
	 * @param smartGridTopo
	 * @param impactAnalysisInput
	 * @param impactAnalysisOutput
	 *            contains up to date hacked states (don't use hacked state of
	 *            input)
	 * @return
	 */
	public ScenarioState run(SmartGridTopology smartGridTopo, ScenarioState impactAnalysisInput,
			ScenarioResult impactAnalysisOutput);

	/**
	 * If using ExtensionPoints and so 0-parameter Constructor pass the config
	 * from Simcontrol UI to this Method to build the desired AttackerSimulation
	 * ("Factory Method")
	 * 
	 * @param config
	 *            behavior for the Attacker
	 * @return true if Init was successful
	 * @throws CoreException
	 *             If ILaunchConfiguration.getAttribute fails
	 */
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException;

	public String getName();
}
