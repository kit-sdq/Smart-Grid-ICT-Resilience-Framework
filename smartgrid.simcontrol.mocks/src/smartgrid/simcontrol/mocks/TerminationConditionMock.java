/**
 * 
 */
package smartgrid.simcontrol.mocks;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;
import smartgrid.simcontrol.interfaces.*;

/**
 * Mocks the TerminationCondition
 * 
 * @author Christian
 *
 */
public class TerminationConditionMock implements ITerminationCondition {

	private final int _breakafterIterationCount;
	
	
	
	public TerminationConditionMock() {
		
		//TODO  TerminationConditionMock Debug Parameter set here
		_breakafterIterationCount = 2;
	}
	
	
	
	/**
	 * 
	 * Constructs a new Instance of TerminationConditionMock
	 * 
	 * 
	 * @param breakafterIterationCount Set the number of Iterations
	 */
	public TerminationConditionMock(int breakafterIterationCount){
		
		_breakafterIterationCount = breakafterIterationCount;
		
	}
	
	
	/**
	 * {@inheritDoc} <p>
	 * 
	 * Simply breaks after specified IterationCount from the Contructor
	 * 
	 */
	@Override
	public boolean evaluate(int iterationCount, ScenarioState impactInput,
			ScenarioState impactInputOld, ScenarioResult impactResult,
			ScenarioResult impactResultOld) {
		
		
		boolean run;
		
		if (iterationCount > _breakafterIterationCount){
			
			run = false;
		} else {
			run = true;
		}
		
		
		
		return run;
	}


	@Override
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {
		// Nothing to do here ...
		return ErrorCodeEnum.SUCCESS;
	}



    @Override
    public String getName() {
        return "Termination Condition Mock";
    }

}
