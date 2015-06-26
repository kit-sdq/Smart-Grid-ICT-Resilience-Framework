/**
 * 
 */
package smartgrid.simcontrol.mocks;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.interfaces.ErrorCodeEnum;
import smartgrid.simcontrol.interfaces.IImpactAnalysis;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.Scenario;

/**
 * This Class mocks the Imapct Analysis
 * 
 * @author Christian
 *
 */
public class ImpactAnalysisMock implements IImpactAnalysis {

	/**
	 * {@inheritDoc}<p>
	 * 
	 * 
	 * NOT yet implemented!
	 * 
	 */
	@Override
	public ScenarioResult run(Scenario smartGridTopo,
			ScenarioState impactAnalysisInput) {
		

		//TODO Implement
		
			
		
		
		
		return null;
	}

	/**
	 * {@inheritDoc}<p>
	 * 
	 * 
	 * NOT yet implemented!
	 * 
	 */
	@Override
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {


		//TODO Implement
		
		return ErrorCodeEnum.SUCESS;
	}

}
