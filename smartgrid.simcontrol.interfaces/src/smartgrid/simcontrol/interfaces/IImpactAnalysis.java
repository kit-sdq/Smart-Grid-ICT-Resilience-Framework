package smartgrid.simcontrol.interfaces;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.Scenario;

//TODO JavaDoc
/**
 * 
 * Interface for the Impact Analysis
 *
 */
public interface IImpactAnalysis {
	
	//TODO JavaDoc
	/**
	 * Runs the Impact Analysis
	 * 
	 * @param smartGridTopo
	 * @param impactAnalysisInput
	 * @return
	 */
    ScenarioResult run(Scenario smartGridTopo, ScenarioState impactAnalysisInput);
    
    
	/**
	 * If using ExtensionPoints and so 0-parameter Constructor pass the config from Simcontrol 
	 * UI to this Method to build the desired ImpactAnalysis ("Factory Method")
	 * 
	 * 
	 * 
	 * 
	 * @param config behavior for the Attacker
	 * @return true if Init was successful
	 * @throws CoreException If ILaunchConfiguration.getAttribute fails
	 */
	ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException;
}
