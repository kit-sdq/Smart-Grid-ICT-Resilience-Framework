/**
 * 
 */
package smartgrid.simcontrol.interfaces;

import smartgridinput.ScenarioState;
import smartgridtopo.Scenario;

/**
 * @author Christian
 *
 */
public class InputModelDTO {
	

	/*Private Fields */
	
	private Scenario myScenarioTopo;
	private ScenarioState myScenarioStates;
	
	
	/* Getter Setter */
	
	/**
	 * @return the myScenarioTopo
	 */
	public Scenario getMyScenarioTopo() {
		return myScenarioTopo;
	}
	/**
	 * @param myScenarioTopo the myScenarioTopo to set
	 */
	public void setMyScenarioTopo(Scenario myScenarioTopo) {
		this.myScenarioTopo = myScenarioTopo;
	}
	/**
	 * @return the myScenarioStates
	 */
	public ScenarioState getMyScenarioStates() {
		return myScenarioStates;
	}
	/**
	 * @param myScenarioStates the myScenarioStates to set
	 */
	public void setMyScenarioStates(ScenarioState myScenarioStates) {
		this.myScenarioStates = myScenarioStates;
	}
	
	
	/* Constructors */
	
	
	/**
	 * Constructs a Input Model Data Transfer Object
	 * 
	 * @param myScenarioTopo
	 * @param myScenarioStates
	 */
	public InputModelDTO(Scenario myScenarioTopo, ScenarioState myScenarioStates) {
		super();
		this.myScenarioTopo = myScenarioTopo;
		this.myScenarioStates = myScenarioStates;
	}

	

}
