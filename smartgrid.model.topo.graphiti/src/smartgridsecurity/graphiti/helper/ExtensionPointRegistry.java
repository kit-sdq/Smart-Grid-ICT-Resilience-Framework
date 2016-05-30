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
    private List<ToolbarButtonAction> buttonIDs = new LinkedList<ToolbarButtonAction>();
    private List<AbstractCustomFeature> featureIDs = new LinkedList<AbstractCustomFeature>();
    private List<IResizeShapeFeature> resizefeatureIDs = new LinkedList<IResizeShapeFeature>();
    private List<ResourceSetListener> resourceListeners = new LinkedList<ResourceSetListener>();

    private ExtensionPointRegistry() {
    };

    public static ExtensionPointRegistry getInstance() {
        if (instance == null) {
            instance = new ExtensionPointRegistry();
        }
        return instance;
    }

    public void addActionToRegistry(ToolbarButtonAction action) {
        buttonIDs.add(action);
    }

    public void removeActionFromRegistry(ToolbarButtonAction action) {
        buttonIDs.remove(action);
    }

    public List<ToolbarButtonAction> getAllToolbarActions() {
        return buttonIDs;
    }

    public int getButtonActionsSize() {
        return buttonIDs.size();
    }

    public int getCustomFeaturesSize() {
        return featureIDs.size();
    }

    public void addContextButtonsToRegistry(List<AbstractCustomFeature> features) {
        featureIDs.addAll(features);
    }

    public List<AbstractCustomFeature> getAllContextButtons() {
        return featureIDs;
    }

    public int getResizeFeaturesSize() {
        return resizefeatureIDs.size();
    }

    public void addResizeFeaturesToRegistry(List<IResizeShapeFeature> features) {
        resizefeatureIDs.addAll(features);
    }

    public List<IResizeShapeFeature> getAllResizeShape() {
        return resizefeatureIDs;
    }

    public void clearResizeFeatures() {
        resizefeatureIDs.clear();
    }

    public int getResourceListenersSize() {
        return resourceListeners.size();
    }

    public void addResourceListenersToRegistry(List<ResourceSetListener> features) {
        resourceListeners.addAll(features);
    }

    public List<ResourceSetListener> getAllResourceListeners() {
        return resourceListeners;
    }

    public void clearResourceListeners() {
        resourceListeners.clear();
    }

    public boolean isPatternRegistredInRegistry(IAction action) {
        return buttonIDs.contains(action);
    }

    public void clearContextButtons() {
        featureIDs.clear();
    }
}
