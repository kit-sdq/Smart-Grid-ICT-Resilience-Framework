package smartgrid.model.helper.output;

import java.util.ArrayList;
import java.util.List;

import smartgridoutput.EntityState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.NetworkEntity;
import smartgridtopo.SmartGridTopology;

public final class LoadOutputModelConformityHelper {

    private LoadOutputModelConformityHelper() {
    }

    public static boolean checkOutputModelConformitySimple(final ScenarioResult result, final SmartGridTopology current) {
        if (result.getScenario() == null) {
            return true;
        }

        return result.getScenario().getId() == current.getId();
    }

    public static boolean checkOutputModelConformity(final ScenarioResult output, final SmartGridTopology current) {
        // Check differences that would be obvious and ignore if
        // output.getScenario() is null
        final boolean result = checkOutputModelConformitySimple(output, current);

        if (result) {
            return compareAndCountEntityStates(output.getStates(), current.getContainsNE());
        }

        return result;
    }

    private static boolean compareAndCountEntityStates(final List<EntityState> states, final List<NetworkEntity> entities) {
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
        final List<EntityState> noZombies = new ArrayList<>();

        for (final EntityState state : states) {
            if (state.getOwner() != null) {
                noZombies.add(state);
            }
        }

        return noZombies;
    }
}
