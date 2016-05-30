package smartgrid.graphiti.extensionpoint.contribution;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;

import smartgrid.graphiti.listeners.ScenarioModelChangedListener;
import smartgridsecurity.graphiti.extensionpoint.definition.IDomainModelChangeListenerResolver;

/**
 * Contributor class to contribute all resource listeners to the main editor.
 * 
 * @author mario
 *
 */
public class ResourceListenerContributor implements IDomainModelChangeListenerResolver {

    @Override
    public List<ResourceSetListener> getDomainModelChangeListener(DiagramBehavior behavior) {
        ResourceSetListener[] listeners = { new ScenarioModelChangedListener(behavior) };
        return Arrays.asList(listeners);
    }

}
