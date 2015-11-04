package smartgrid.simcontrol.interfaces;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

// TODO JavaDoc
/**
 * Interface for the Attacker Simulation
 * 
 */
public interface IAttackerSimulation {

	// TODO JavaDoc
	/**
	 * Runs the Atacker Simulation
	 * 
	 * @param smartGridTopo
	 * @param impactAnalysisOutput
	 * @return
	 */
	public ScenarioResult run(SmartGridTopology smartGridTopo, ScenarioResult impactAnalysisOutput);

	/**
	 * If using ExtensionPoints and so 0-parameter Constructor pass the config
	 * from Simcontrol UI to this Method to build the desired AttackerSimulation
	 * ("Factory Method")
	 * 
	 * 
	 * 
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
