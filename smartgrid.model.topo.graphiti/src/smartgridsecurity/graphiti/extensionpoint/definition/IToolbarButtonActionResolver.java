package smartgridsecurity.graphiti.extensionpoint.definition;

import java.util.List;

import smartgridsecurity.graphiti.actions.ToolbarButtonAction;

/**
 * resolver for all toolbar actions.
 *
 * @author mario
 *
 */
public interface IToolbarButtonActionResolver {

    List<ToolbarButtonAction> getToolbarButtons();

}
