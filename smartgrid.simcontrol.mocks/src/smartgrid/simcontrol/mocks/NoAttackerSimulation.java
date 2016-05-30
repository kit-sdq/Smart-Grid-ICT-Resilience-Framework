/**
 * 
 */
package smartgrid.simcontrol.mocks;

import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.IAttackerSimulation;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

/**
 * @author Christian
 *
 */
public class NoAttackerSimulation implements IAttackerSimulation {

    /**
     * {@inheritDoc}
     * <p>
     * 
     * An attacker who doesn't attack
     * 
     */
    @Override
    public ScenarioResult run(SmartGridTopology smartGridTopo, ScenarioResult impactAnalysisOutput) {

        return impactAnalysisOutput;
    }

    @Override
    public ErrorCodeEnum init(ILaunchConfiguration config) {
        // Nothing to do here
        return ErrorCodeEnum.SUCCESS;
    }

    @Override
    public String getName() {
        return "No Attack Simulation";
    }

    @Override
    public boolean enableHackingSpeed() {
        return false;
    }

    @Override
    public boolean enableRootNode() {
        return false;
    }

    @Override
    public boolean enableLogicalConnections() {
        return false;
    }

}
