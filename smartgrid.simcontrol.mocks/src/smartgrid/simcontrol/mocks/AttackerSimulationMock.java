/**
 * 
 */
package smartgrid.simcontrol.mocks;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.interfaces.ErrorCodeEnum;
import smartgrid.simcontrol.interfaces.IAttackerSimulation;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

/**
 * @author Christian
 *
 */
public class AttackerSimulationMock implements IAttackerSimulation {

	/**
	 * {@inheritDoc}
	 * <p>
	 * 
	 * Mocks a Atacker who doesn't atack
	 * 
	 */
	@Override
	public ScenarioResult run(SmartGridTopology smartGridTopo, ScenarioResult impactAnalysisOutput) {

		return impactAnalysisOutput;
	}

	@Override
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {
		// Nothing to do here
		return ErrorCodeEnum.SUCCESS;
	}

	@Override
	public String getName() {
		return "No Attack Simulation";
	}

	@Override
	public boolean enableFurtherAttributes() {
		return false;
	}

}
