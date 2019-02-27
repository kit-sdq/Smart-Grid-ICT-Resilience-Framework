package smartgrid.model.output.sirius.services;

import smartgrid.model.output.sirius.ScenarioResultHelper;
import smartgridoutput.EntityState;
import smartgridoutput.NoUplink;
import smartgridtopo.NetworkEntity;

public class ShowOutputElements {
	public ShowOutputElements() {

	}
	
	public boolean isNoUplinkAndHacked(NetworkEntity entity) {
        EntityState state = ScenarioResultHelper.getCorrectEntityState(entity);
        if (state instanceof NoUplink && ((NoUplink) state).isIsHacked()) {
            return true;
        }
        return false;
    }
	
	public boolean isNoUplinkAndNotHacked(NetworkEntity entity) {
		EntityState state = ScenarioResultHelper.getCorrectEntityState(entity);
		if (state instanceof NoUplink && !((NoUplink) state).isIsHacked()) {
			return true;
		}
		return false;
	}
}
