package smartgrid.model.helper.input;

import java.util.List;

import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;
import smartgridtopo.Scenario;

public class LoadInputModelConformityHelper {

    public static boolean checkInputModelConformity(ScenarioState state, Scenario current) {
        // Check differences that would be obvious
        boolean result = (state.getPowerStates().size() == current.getContainsPGN().size())
                && (state.getEntityStates().size() == current.getContainsNE().size());

        if (result) {
            return comparePowerStates(state.getPowerStates(), current.getContainsPGN())
                    && compareEntityStates(state.getEntityStates(), current.getContainsNE());
        }

        return result;
    }

    private static boolean compareEntityStates(List<EntityState> states, List<NetworkEntity> current) {
        boolean result = true;

        for (EntityState stateId : states) {
            boolean found = false;
            for (NetworkEntity currentId : current) {
                if (stateId.getOwner().getId() == currentId.getId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result = false;
                break;
            }
        }

        return result;
    }

    private static boolean comparePowerStates(List<PowerState> states, List<PowerGridNode> current) {
        boolean result = true;

        for (PowerState stateId : states) {
            boolean found = false;
            for (PowerGridNode currentId : current) {
                if (stateId.getOwner().getId() == currentId.getId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result = false;
                break;
            }
        }

        return result;
    }
}
