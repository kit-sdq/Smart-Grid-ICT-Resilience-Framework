/**
 * 
 */
package smartgrid.simcontrol.mocks;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.interfaces.ErrorCodeEnum;
import smartgrid.simcontrol.interfaces.IAttackerSimulation;
import smartgridoutput.ScenarioResult;
import smartgridtopo.Scenario;

/**
 * @author Christian
 *
 */
public class AttackerSimulationMock implements IAttackerSimulation {

	/**
	 *  {@inheritDoc}<p>
	 *  
	 *  Mocks a Atacker who doesn't atack
	 * 
	 */
	@Override
	public ScenarioResult run(Scenario smartGridTopo,
			ScenarioResult impactAnalysisOutput) {
		
		
		
		
		
		return impactAnalysisOutput;
	}

	@Override
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {
		// Nothing to do here
		return ErrorCodeEnum.SUCESS;
	}

    @Override
    public String getName() {
        // TODO Implement
        return "";
    }

}
