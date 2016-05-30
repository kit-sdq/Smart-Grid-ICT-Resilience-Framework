package smartgrid.simcontrol.baselib.coupling;

import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;

public interface ITimeProgressor {
    
//TODO Write JavaDoc 
	/**
	 * Makes some Progress 
	 * 
	 */
	public void progress();

	/**
	 * If using ExtensionPoints and so 0-parameter Constructor pass the config from Simcontrol 
	 * UI to this Method to build the desired AttackerSimulation ("Factory Method")
	 * 
	 * 
	 * 
	 * 
	 * @param config behavior for the Attacker
	 * @return true if Init was successful
	 * @throws CoreException If ILaunchConfiguration.getAttribute fails
	 */
	public ErrorCodeEnum init(ILaunchConfiguration config) throws Exception;
	
	public String getName();
}
