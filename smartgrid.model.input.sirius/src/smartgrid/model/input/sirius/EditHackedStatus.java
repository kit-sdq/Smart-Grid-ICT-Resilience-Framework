package smartgrid.model.input.sirius;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;

import smartgridinput.EntityState;
import smartgridtopo.ControlCenter;

public class EditHackedStatus implements IExternalJavaAction {

	public EditHackedStatus() {
	}

	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		return true;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		ControlCenter cc = (ControlCenter) ((List<? extends EObject>) arg0).get(0);
		EntityState required = ScenarioStateHelper.getCorrectEntityState(cc);

		String argument = (String) arg1.get("name");

		// Check whether the String has true or false in it and act accordingly
		if ((required.isIsHacked() && argument.toLowerCase().contains("false"))
				|| (!required.isIsHacked() && argument.toLowerCase().contains("true"))) {
			required.setIsHacked(!required.isIsHacked());
		}
	}
}
