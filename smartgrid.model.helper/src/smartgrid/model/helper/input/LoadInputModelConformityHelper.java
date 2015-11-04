package smartgrid.model.helper.input;

import java.util.ArrayList;
import java.util.List;

import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;

public class LoadInputModelConformityHelper {

	public static boolean checkInputModelConformitySimple(ScenarioState state, SmartGridTopology current) {
		if (state.getScenario() == null) {
			return true;
		}
		return state.getScenario().getId() == current.getId();
	}

	public static boolean checkInputModelConformity(ScenarioState state, SmartGridTopology current) {
		// Check differences that would be obvious
		boolean result = checkInputModelConformitySimple(state, current);

		if (result) {
			return compareAndCountPowerStates(state.getPowerStates(), current.getContainsPGN())
					&& compareAndCountEntityStates(state.getEntityStates(), current.getContainsNE());
		}

		return result;
	}

	private static boolean compareAndCountEntityStates(List<EntityState> states, List<NetworkEntity> entities) {
		boolean result = true;

		List<EntityState> noZombies = getListWithoutZombies(states);

		if (noZombies.size() == entities.size()) {
			for (NetworkEntity entity : entities) {
				boolean found = false;
				for (EntityState state : noZombies) {
					if (state.getOwner().getId() == entity.getId()) {
						found = true;
						break;
					}
				}
				if (!found) {
					result = false;
				}
			}
		} else {
			result = false;
		}

		return result;
	}

	private static List<EntityState> getListWithoutZombies(List<EntityState> states) {
		List<EntityState> noZombies = new ArrayList<EntityState>();

		for (EntityState state : states) {
			if (state.getOwner() != null) {
				noZombies.add(state);
			}
		}

		return noZombies;
	}

	private static boolean compareAndCountPowerStates(List<PowerState> states, List<PowerGridNode> current) {
		boolean result = true;

		List<PowerState> noZombies = getListWithoutZombiesPower(states);

		if (noZombies.size() == current.size()) {
			for (PowerGridNode node : current) {
				boolean found = false;
				for (PowerState state : noZombies) {
					if (state.getOwner().getId() == node.getId()) {
						found = true;
						break;
					}
				}
				if (!found) {
					result = false;
				}
			}
		} else {
			result = false;
		}

		return result;
	}

	private static List<PowerState> getListWithoutZombiesPower(List<PowerState> states) {
		List<PowerState> noZombies = new ArrayList<PowerState>();

		for (PowerState state : states) {
			if (state.getOwner() != null) {
				noZombies.add(state);
			}
		}

		return noZombies;
	}
}
