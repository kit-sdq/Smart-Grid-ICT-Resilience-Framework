package smartgrid.graphiti.extensionpoint.contribution;

import java.util.Arrays;
import java.util.List;

import smartgrid.graphiti.actions.ClearButtonAction;
import smartgrid.graphiti.actions.LoadToolbarButtonAction;
import smartgrid.graphiti.actions.NewToolbarButtonAction;
import smartgridsecurity.graphiti.actions.ToolbarButtonAction;
import smartgridsecurity.graphiti.extensionpoint.definition.IToolbarButtonActionResolver;

/**
 * Contributor class to contribute all toolbar buttons to the main editor.
 *
 * @author mario
 *
 */
public class ToolbarActionContributor implements IToolbarButtonActionResolver {

    @Override
    public List<ToolbarButtonAction> getToolbarButtons() {
        final ClearButtonAction enableAction = new ClearButtonAction();
        // EnableButtonAction is a Listener who listens to changes in new and load action
        final ToolbarButtonAction[] actions = { new NewToolbarButtonAction(enableAction),
                new LoadToolbarButtonAction(enableAction), enableAction };
        return Arrays.asList(actions);
    }

}
