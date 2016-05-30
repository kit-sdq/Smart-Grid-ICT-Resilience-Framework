package smartgrid.model.output.contributor;

import java.util.Arrays;
import java.util.List;

import smartgrid.model.output.actions.ClearOutputButtonAction;
import smartgrid.model.output.actions.LoadOutputToolbarButtonAction;
import smartgridsecurity.graphiti.actions.ToolbarButtonAction;
import smartgridsecurity.graphiti.extensionpoint.definition.IToolbarButtonActionResolver;

/**
 * Contributor class to contribute all toolbar buttons to the main editor.
 * 
 * @author mario
 *
 */
public class OutputActionContributor implements IToolbarButtonActionResolver {

    @Override
    public List<ToolbarButtonAction> getToolbarButtons() {
        ClearOutputButtonAction enableAction = new ClearOutputButtonAction();
        // EnableButtonAction is a Listener who listens to changes in new and load action
        ToolbarButtonAction[] actions = { new LoadOutputToolbarButtonAction(enableAction), enableAction };
        return Arrays.asList(actions);
    }

}
