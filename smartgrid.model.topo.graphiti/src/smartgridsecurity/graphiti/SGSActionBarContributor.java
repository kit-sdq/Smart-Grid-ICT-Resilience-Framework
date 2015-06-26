package smartgridsecurity.graphiti;

import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.ui.editor.DiagramEditorActionBarContributor;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.actions.RetargetAction;

import smartgridsecurity.graphiti.actions.ToolbarButtonAction;
import smartgridsecurity.graphiti.extensionpoint.evaluation.EvaluateToolbarActions;
import smartgridsecurity.graphiti.helper.ExtensionPointRegistry;

/**
 * Specific action bar contributor implementation.
 * @author mario
 *
 */
public class SGSActionBarContributor extends DiagramEditorActionBarContributor {
	
	public SGSActionBarContributor() {
		super();
		addToolbarButtonActions();
	}
	
    /**
     * Retrieves all AbstractPattern, which were found by EvaluateToolbarActions extension point and adds them to ExtensionPointRegistry.
     */
    private void addToolbarButtonActions() {
    	if (ExtensionPointRegistry.getInstance().getButtonActionsSize() == 0) { 
    		List<ToolbarButtonAction> extensionToolbarActions = (new EvaluateToolbarActions()).evaluateFeatureExtension(Platform.getExtensionRegistry());
    		for (ToolbarButtonAction t : extensionToolbarActions) {
    			ExtensionPointRegistry.getInstance().addActionToRegistry(t);
    		}
    	}
    }
	
	@Override
	protected void buildActions() {
		super.buildActions();
		for (ToolbarButtonAction toolbarAction : ExtensionPointRegistry.getInstance().getAllToolbarActions()) {
			RetargetAction action = new RetargetAction(toolbarAction.ACTION_ID, toolbarAction.TOOL_TIP);
			action.setImageDescriptor(toolbarAction.getImageDescriptor());
			addRetargetAction(action);
		}
	}

	@Override
	public void contributeToToolBar(IToolBarManager tbm) {
		super.contributeToToolBar(tbm);

		for (ToolbarButtonAction toolbarAction : ExtensionPointRegistry.getInstance().getAllToolbarActions()) {
			tbm.add(getAction(toolbarAction.ACTION_ID));
		}

	}

}
