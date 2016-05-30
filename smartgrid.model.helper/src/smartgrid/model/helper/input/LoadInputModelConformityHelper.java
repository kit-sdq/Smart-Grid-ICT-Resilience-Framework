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

    public static boolean checkInputModelConformitySimple(final ScenarioState state, final SmartGridTopology current) {
        if (state.getScenario() == null) {
            return true;
        }
        return state.getScenario().getId() == current.getId();
    }

    public static boolean checkInputModelConformity(final ScenarioState state, final SmartGridTopology current) {
        // Check differences that would be obvious
        final boolean result = checkInputModelConformitySimple(state, current);

        if (result) {
            return compareAndCountPowerStates(state.getPowerStates(), current.getContainsPGN())
                    && compareAndCountEntityStates(state.getEntityStates(), current.getContainsNE());
        }

        return result;
    }

    private static boolean compareAndCountEntityStates(final List<EntityState> states,
            final List<NetworkEntity> entities) {
        boolean result = true;

        final List<EntityState> noZombies = getListWithoutZombies(states);

        if (noZombies.size() == entities.size()) {
            for (final NetworkEntity entity : entities) {
                boolean found = false;
                for (final EntityState state : noZombies) {
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

    private static List<EntityState> getListWithoutZombies(final List<EntityState> states) {
        final List<EntityState> noZombies = new ArrayList<EntityState>();

        for (final EntityState state : states) {
            if (state.getOwner() != null) {
                noZombies.add(state);
            }
        }

        return noZombies;
    }

    private static boolean compareAndCountPowerStates(final List<PowerState> states,
            final List<PowerGridNode> current) {
        boolean result = true;

        final List<PowerState> noZombies = getListWithoutZombiesPower(states);

        if (noZombies.size() == current.size()) {
            for (final PowerGridNode node : current) {
                boolean found = false;
                for (final PowerState state : noZombies) {
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

    private static List<PowerState> getListWithoutZombiesPower(final List<PowerState> states) {
        final List<PowerState> noZombies = new ArrayList<PowerState>();

        for (final PowerState state : states) {
            if (state.getOwner() != null) {
                noZombies.add(state);
            }
        }

        return noZombies;
    }
}
