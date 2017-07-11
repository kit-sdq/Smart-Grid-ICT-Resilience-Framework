/**
 *
 */
package smartgrid.simcontrol.mocks;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.ITerminationCondition;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;

/**
 * A fixed iteration count termination condition
 *
 * @author Christian
 *
 */
public class IterationCountTerminationCondition implements ITerminationCondition {

    private static final Logger LOG = Logger.getLogger(IterationCountTerminationCondition.class);

    private int maxIterations;

    public IterationCountTerminationCondition() {
        maxIterations = 2;
    }

    /**
     * Constructs a new Instance of TerminationConditionMock
     *
     * @param breakafterIterationCount
     *            Set the number of Iterations
     */
    public IterationCountTerminationCondition(final int breakafterIterationCount) {

        maxIterations = breakafterIterationCount;
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * Simply breaks after specified IterationCount from the Contructor
     *
     */
    @Override
    public boolean evaluate(final int currentIteration, final ScenarioState impactInput, final ScenarioState impactInputOld, final ScenarioResult impactResult, final ScenarioResult impactResultOld) {

        final boolean continueRunning = currentIteration < maxIterations;
        return continueRunning;
    }

    @Override
    public ErrorCodeEnum init(final ILaunchConfiguration config) throws CoreException {
        maxIterations = Integer.parseInt(config.getAttribute(Constants.ITERATION_COUNT_KEY, Constants.DEFAULT_ITERATION_COUNT));
        LOG.info("Performing at most " + maxIterations + " iterations between power/impact per time step");

        return ErrorCodeEnum.SUCCESS;
    }

    @Override
    public String getName() {
        return "Iteration Count";
    }

}
