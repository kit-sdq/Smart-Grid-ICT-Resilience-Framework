package smartgrid.model.helper.output;

import java.util.List;

import smartgridoutput.EntityState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.NetworkEntity;
import smartgridtopo.Scenario;

public final class LoadOutputModelConformityHelper {

    private LoadOutputModelConformityHelper() {
    }

    public static boolean checkOutputModelConformity(ScenarioResult output, Scenario current) {
        // Check differences that would be obvious
        boolean result = (output.getEntityStates().size() == current.getContainsNE().size());

        if (result) {
            return compareEntityStates(output.getEntityStates(), current.getContainsNE());
        }

        return result;
    }

    private static boolean compareEntityStates(List<EntityState> states, List<NetworkEntity> entities) {
        boolean result = true;

        for (EntityState state : states) {
            boolean found = false;
            for (NetworkEntity entity : entities) {
                if (state.getOwner().getId() == entity.getId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result = false;
            }
        }

        return result;
    }
}
