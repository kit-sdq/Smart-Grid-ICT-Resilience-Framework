/**
 * 
 */
package smartgrid.simcontrol.mocks;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.interfaces.ErrorCodeEnum;
import smartgrid.simcontrol.interfaces.IPowerLoadSimulation;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.Scenario;

/**
 * @author Christian
 *
 */
public class PowerLoadSimulationMock implements IPowerLoadSimulation {

	/**
	 * {@inheritDoc}<P>
	 * 
	 * 
	 * Runs a mocked Power Load Simulation. This Mock does not change anything.
	 * 
	 */
	@Override
	public ScenarioState run(Scenario smartGridTopo,
			ScenarioState impactAnalysisInput,
			ScenarioResult impactAnalysisOutput) {
		
		
		
		
		
		
		return impactAnalysisInput;
	}

	/**
	 * 
	 */
	@Override
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {
		// Nothing to do here..
		return ErrorCodeEnum.SUCESS;
	}

}
