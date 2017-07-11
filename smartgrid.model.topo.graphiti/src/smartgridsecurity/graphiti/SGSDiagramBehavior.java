package smartgridsecurity.graphiti;

import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.ui.editor.IDiagramContainerUI;

import smartgridsecurity.graphiti.actions.ToolbarButtonAction;
import smartgridsecurity.graphiti.extensionpoint.evaluation.EvaluateDomainModelChangeListeners;
import smartgridsecurity.graphiti.helper.ExtensionPointRegistry;

/**
 * Specific Graphiti diagram behavior implementation.
 *
 * @author mario
 *
 */
public class SGSDiagramBehavior extends DiagramBehavior {
    private final IDiagramContainerUI diagramContainer;
    private ResourceSetListener listener;

    public SGSDiagramBehavior(final IDiagramContainerUI diagramContainer) {
        super(diagramContainer);
        this.diagramContainer = diagramContainer;
        this.initListeners();
    }

    /**
     * Initializes all resource listeners of other plugins.
     */
    private void initListeners() {
        ExtensionPointRegistry.getInstance().clearResourceListeners();
        final List<ResourceSetListener> resListeners = (new EvaluateDomainModelChangeListeners(this)).evaluateFeatureExtension(Platform.getExtensionRegistry());
        ExtensionPointRegistry.getInstance().addResourceListenersToRegistry(resListeners);
        if (ExtensionPointRegistry.getInstance().getResourceListenersSize() > 0) {
            this.listener = ExtensionPointRegistry.getInstance().getAllResourceListeners().get(0);
        }
    }

    @Override
    protected void initActionRegistry(final ZoomManager zoomManager) {
        super.initActionRegistry(zoomManager);
        final ActionRegistry actionRegistry = this.diagramContainer.getActionRegistry();
        @SuppressWarnings("unchecked")
        final List<String> selectionActions = this.diagramContainer.getSelectionActions();

        for (final ToolbarButtonAction action : ExtensionPointRegistry.getInstance().getAllToolbarActions()) {
            action.setDiagramContainer(this.diagramContainer);
            actionRegistry.registerAction(action);
            selectionActions.add(action.getId());
        }
    }

    @Override
    protected void unregisterBusinessObjectsListener() {
        super.unregisterBusinessObjectsListener();
        if (this.listener != null) {
            final TransactionalEditingDomain eDomain = this.getEditingDomain();
            eDomain.removeResourceSetListener(this.listener);
        }
    }

    @Override
    protected void registerBusinessObjectsListener() {
        super.registerBusinessObjectsListener();
        if (this.listener != null) {
            final TransactionalEditingDomain eDomain = this.getEditingDomain();
            eDomain.addResourceSetListener(this.listener);
        }
    }

}
