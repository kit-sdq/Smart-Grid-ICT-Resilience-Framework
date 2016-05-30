package smartgridsecurity.graphiti.helper;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.jface.action.IAction;

import smartgridsecurity.graphiti.actions.ToolbarButtonAction;

/**
 * Registry which holds the input of all SGS extension points.
 *
 * @author mario
 *
 */
public class ExtensionPointRegistry {
    private static ExtensionPointRegistry instance = null;
    private final List<ToolbarButtonAction> buttonIDs = new LinkedList<ToolbarButtonAction>();
    private final List<AbstractCustomFeature> featureIDs = new LinkedList<AbstractCustomFeature>();
    private final List<IResizeShapeFeature> resizefeatureIDs = new LinkedList<IResizeShapeFeature>();
    private final List<ResourceSetListener> resourceListeners = new LinkedList<ResourceSetListener>();

    private ExtensionPointRegistry() {
    };

    public static ExtensionPointRegistry getInstance() {
        if (instance == null) {
            instance = new ExtensionPointRegistry();
        }
        return instance;
    }

    public void addActionToRegistry(final ToolbarButtonAction action) {
        this.buttonIDs.add(action);
    }

    public void removeActionFromRegistry(final ToolbarButtonAction action) {
        this.buttonIDs.remove(action);
    }

    public List<ToolbarButtonAction> getAllToolbarActions() {
        return this.buttonIDs;
    }

    public int getButtonActionsSize() {
        return this.buttonIDs.size();
    }

    public int getCustomFeaturesSize() {
        return this.featureIDs.size();
    }

    public void addContextButtonsToRegistry(final List<AbstractCustomFeature> features) {
        this.featureIDs.addAll(features);
    }

    public List<AbstractCustomFeature> getAllContextButtons() {
        return this.featureIDs;
    }

    public int getResizeFeaturesSize() {
        return this.resizefeatureIDs.size();
    }

    public void addResizeFeaturesToRegistry(final List<IResizeShapeFeature> features) {
        this.resizefeatureIDs.addAll(features);
    }

    public List<IResizeShapeFeature> getAllResizeShape() {
        return this.resizefeatureIDs;
    }

    public void clearResizeFeatures() {
        this.resizefeatureIDs.clear();
    }

    public int getResourceListenersSize() {
        return this.resourceListeners.size();
    }

    public void addResourceListenersToRegistry(final List<ResourceSetListener> features) {
        this.resourceListeners.addAll(features);
    }

    public List<ResourceSetListener> getAllResourceListeners() {
        return this.resourceListeners;
    }

    public void clearResourceListeners() {
        this.resourceListeners.clear();
    }

    public boolean isPatternRegistredInRegistry(final IAction action) {
        return this.buttonIDs.contains(action);
    }

    public void clearContextButtons() {
        this.featureIDs.clear();
    }
}
