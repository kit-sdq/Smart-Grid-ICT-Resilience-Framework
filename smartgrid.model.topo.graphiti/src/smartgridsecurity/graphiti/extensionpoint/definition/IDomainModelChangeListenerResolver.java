package smartgridsecurity.graphiti.extensionpoint.definition;

import java.util.List;

import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;

/**
 * Resolver for all resource listeners.
 * @author mario
 *
 */
public interface IDomainModelChangeListenerResolver {
	
	public List<ResourceSetListener> getDomainModelChangeListener(DiagramBehavior behavior);

}
