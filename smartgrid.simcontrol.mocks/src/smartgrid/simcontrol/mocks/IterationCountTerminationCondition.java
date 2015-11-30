/**
 * 
 */
package smartgrid.simcontrol.mocks;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.interfaces.*;

/**
 * Mocks the TerminationCondition
 * 
 * @author Christian
 *
 */
public class IterationCountTerminationCondition implements ITerminationCondition {

	private int _breakafterIterationCount;

	public IterationCountTerminationCondition() {
		_breakafterIterationCount = 2;
	}

	/**
	 * Constructs a new Instance of TerminationConditionMock
	 * 
	 * @param breakafterIterationCount
	 *            Set the number of Iterations
	 */
	public IterationCountTerminationCondition(int breakafterIterationCount) {

		_breakafterIterationCount = breakafterIterationCount;

	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 
	 * Simply breaks after specified IterationCount from the Contructor
	 * 
	 */
	@Override
	public boolean evaluate(int iterationCount, ScenarioState impactInput, ScenarioState impactInputOld,
			ScenarioResult impactResult, ScenarioResult impactResultOld) {

		boolean run;

		if (iterationCount < _breakafterIterationCount) {

			run = true;
		} else {
			run = false;
		}

		return run;
	}

	@Override
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {
		_breakafterIterationCount = Integer
				.parseInt(config.getAttribute(Constants.ITERATION_COUNT_KEY, Constants.DEFAULT_ITERATION_COUNT));

		return ErrorCodeEnum.SUCCESS;
	}

	@Override
	public String getName() {
		return "Iteration Count";
	}

}
