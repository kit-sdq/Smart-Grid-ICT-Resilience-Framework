package smartgrid.model.input.sirius.services;

import org.eclipse.emf.common.util.EList;

import smartgrid.model.input.sirius.ScenarioStateHelper;
import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;

public class ShowInputNotationElements {


	public ShowInputNotationElements() {

	}

	public EList<PowerState> getPowerStates(SmartGridTopology topo) {
		ScenarioState state = ScenarioStateHelper.getAndCheckScnearioState(topo);
		return state.getPowerStates();
	}

	public EList<EntityState> getEntityStates(SmartGridTopology topo) {
		ScenarioState state = ScenarioStateHelper.getAndCheckScnearioState(topo);
		return state.getEntityStates();
	}

	/**
	 * Retrieves the corresponding PowerState to the given node and tests if the
	 * power outage attribute is true
	 * 
	 * @param node
	 *            the power grid node to find its PowerState to
	 * @return the power outage attribute of the found PowerState or false if
	 *         there was no such state found
	 */
	public boolean isPowerOutage(PowerGridNode node) {
		boolean powerOutage = false;
		PowerState required = ScenarioStateHelper.getCorrectPowerState(node);
		if (required != null) {
			powerOutage = required.isPowerOutage();
		}

		return powerOutage;
	}

	/**
	 * Retrieves the corresponding EntityState to the given node and tests if
	 * the node destroyed attribute is true
	 * 
	 * @param node
	 *            the network entity to find its EntityState to
	 * @return the node destroyed attribute of the found EntityState or false if
	 *         there was no such state found
	 */
	public boolean isNodeDestroyed(NetworkEntity node) {
		boolean nodeDestroyed = false;
		EntityState required = ScenarioStateHelper.getCorrectEntityState(node);

		if (required != null) {
			nodeDestroyed = required.isIsDestroyed();
		}

		return nodeDestroyed;
	}
   
	/**
	 * Retrieves the corresponding EntityState to the given node and tests if
	 * the node hacked attribute is true
	 * 
	 * @param node
	 *            the network entity to find its EntityState to
	 * @return the node hacked attribute of the found EntityState or false if
	 *         there was no such state found
	 */
	public boolean isNodeHacked(NetworkEntity node) {
		boolean nodeHacked = false;
		EntityState required = ScenarioStateHelper.getCorrectEntityState(node);

		if (required != null) {
			nodeHacked = required.isIsHacked();
		}
		return nodeHacked;
	}


    public boolean isNodeDestroyedAndNotHacked(NetworkEntity node) {
        boolean result = false;
        EntityState required = ScenarioStateHelper.getCorrectEntityState(node);

        if (required != null) {
            result = required.isIsDestroyed() && !required.isIsHacked();
        }
        return result;
    }

    
	public boolean isNodeHackedAndDestroyed(NetworkEntity node) {
		boolean result = false;
		EntityState required = ScenarioStateHelper.getCorrectEntityState(node);

		if (required != null) {
			result = required.isIsDestroyed() && required.isIsHacked();
		}
		return result;
	}
	
	public String nodeBroken(NetworkEntity node) {
		StringBuffer result = new StringBuffer();
		result.append("destroyed = ");
		EntityState required = ScenarioStateHelper.getCorrectEntityState(node);

		if (required != null) {
			result.append(required.isIsDestroyed());
		}
		return result.toString();
	}

	public String nodeHacked(NetworkEntity node) {
		StringBuffer result = new StringBuffer();
		result.append("hacked = ");
		EntityState required = ScenarioStateHelper.getCorrectEntityState(node);

		if (required != null) {
			result.append(required.isIsHacked());
		}
		return result.toString();
	}

	public boolean setNodeBroken(NetworkEntity node) {
		EntityState state = ScenarioStateHelper.getCorrectEntityState(node);

		if (state != null) {
			state.setIsDestroyed(!state.isIsDestroyed());
			return state.isIsDestroyed();
		}
		return false;
	}

	public boolean setNodeHacked(NetworkEntity node) {
		EntityState state = ScenarioStateHelper.getCorrectEntityState(node);

		if (state != null) {
			state.setIsHacked(!state.isIsHacked());
			return state.isIsHacked();
		}
		return false;
	}

	public void setPowerOutage(PowerGridNode node) {
		PowerState state = ScenarioStateHelper.getCorrectPowerState(node);
		state.setPowerOutage(!state.isPowerOutage());
	}

}
