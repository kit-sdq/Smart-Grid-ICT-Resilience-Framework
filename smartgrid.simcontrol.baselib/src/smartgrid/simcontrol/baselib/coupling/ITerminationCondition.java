package smartgrid.simcontrol.baselib.coupling;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgridinput.ScenarioState;
import smartgridoutput.ScenarioResult;

//TODO JavaDoc
/**
 * ITerminationCondition Interface
 *
 *
 */
public interface ITerminationCondition {

    // TODO JavaDoc
    /**
     * Evaluates the TerminationCondition.
     *
     * @param iterationCount
     * @param impactInput
     * @param impactInputOld
     * @param impactResult
     * @param impactResultOld
     * @return
     */
    boolean evaluate(int iterationCount, ScenarioState impactInput, ScenarioState impactInputOld,
            ScenarioResult impactResult, ScenarioResult impactResultOld);

    /**
     * If using ExtensionPoints and so 0-parameter Constructor pass the config from Simcontrol UI to
     * this Method to build the desired AttackerSimulation ("Factory Method")
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
    ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException;

    public String getName();
}
