/**
 * 
 */
package smartgrid.inputgenerator;

import smartgridinput.ScenarioState;
import smartgridtopo.SmartGridTopology;

/**
 * @author Christian
 *
 */
public class InputModelDTO {

    private SmartGridTopology myScenarioTopo;
    private ScenarioState myScenarioStates;

    /**
     * @return the myScenarioTopo
     */
    public SmartGridTopology getMyScenarioTopo() {
        return myScenarioTopo;
    }

    /**
     * @param myScenarioTopo
     *            the myScenarioTopo to set
     */
    public void setMyScenarioTopo(SmartGridTopology myScenarioTopo) {
        this.myScenarioTopo = myScenarioTopo;
    }

    /**
     * @return the myScenarioStates
     */
    public ScenarioState getMyScenarioStates() {
        return myScenarioStates;
    }

    /**
     * @param myScenarioStates
     *            the myScenarioStates to set
     */
    public void setMyScenarioStates(ScenarioState myScenarioStates) {
        this.myScenarioStates = myScenarioStates;
    }

    /**
     * Constructs a Input Model Data Transfer Object
     * 
     * @param myScenarioTopo
     * @param myScenarioStates
     */
    public InputModelDTO(SmartGridTopology myScenarioTopo, ScenarioState myScenarioStates) {
        super();
        this.myScenarioTopo = myScenarioTopo;
        this.myScenarioStates = myScenarioStates;
    }
}
