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
 *
 * @author mario
 *
 */
public class SGSActionBarContributor extends DiagramEditorActionBarContributor {

    public SGSActionBarContributor() {
        super();
        this.addToolbarButtonActions();
    }

    /**
     * Retrieves all AbstractPattern, which were found by EvaluateToolbarActions extension point and
     * adds them to ExtensionPointRegistry.
     */
    private void addToolbarButtonActions() {
        if (ExtensionPointRegistry.getInstance().getButtonActionsSize() == 0) {
            final List<ToolbarButtonAction> extensionToolbarActions = (new EvaluateToolbarActions()).evaluateFeatureExtension(Platform.getExtensionRegistry());
            for (final ToolbarButtonAction t : extensionToolbarActions) {
                ExtensionPointRegistry.getInstance().addActionToRegistry(t);
            }
        }
    }

    @Override
    protected void buildActions() {
        super.buildActions();
        for (final ToolbarButtonAction toolbarAction : ExtensionPointRegistry.getInstance().getAllToolbarActions()) {
            final RetargetAction action = new RetargetAction(toolbarAction.ACTION_ID, toolbarAction.TOOL_TIP);
            action.setImageDescriptor(toolbarAction.getImageDescriptor());
            this.addRetargetAction(action);
        }
    }

    @Override
    public void contributeToToolBar(final IToolBarManager tbm) {
        super.contributeToToolBar(tbm);

        for (final ToolbarButtonAction toolbarAction : ExtensionPointRegistry.getInstance().getAllToolbarActions()) {
            tbm.add(this.getAction(toolbarAction.ACTION_ID));
        }

    }

}
