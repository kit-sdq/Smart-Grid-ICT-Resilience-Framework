package smartgrid.simcontrol.baselib.coupling;

import java.util.List;

import smartgridinput.ScenarioState;

public class PowerLoadResult {

    private ScenarioState scenarioState;
    private List<PowerPerNode> powerPerNodes;

    public ScenarioState getScenarioState() {
        return scenarioState;
    }

    public List<PowerPerNode> getPowerPerNodes() {
        return powerPerNodes;
    }

    public PowerLoadResult(ScenarioState scenarioState, List<PowerPerNode> powerPerNodes) {
        super();
        this.scenarioState = scenarioState;
        this.powerPerNodes = powerPerNodes;
    }
}
