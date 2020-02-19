package smartgrid.simcontrol.test.mocks;

import java.util.Map;


import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.simcontrol.test.baselib.coupling.IAttackerSimulation;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

/**
 * @author Christian
 */
public class NoAttackerSimulation implements IAttackerSimulation {

    /**
     * {@inheritDoc}
     * <p>
     *
     * An attacker who doesn't attack
     */
    @Override
    public ScenarioResult run(final SmartGridTopology smartGridTopo, final ScenarioResult impactAnalysisOutput) {

        return impactAnalysisOutput;
    }

    @Override
    public void init(final Map<InitializationMapKeys, String> initMap) {
        // Nothing to do here
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
