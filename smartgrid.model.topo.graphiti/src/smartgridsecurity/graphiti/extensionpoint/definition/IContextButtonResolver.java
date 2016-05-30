package smartgridsecurity.graphiti.extensionpoint.definition;

import java.util.List;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;

/**
 * Resolver for all context buttons.
 * 
 * @author mario
 *
 */
public interface IContextButtonResolver {

    List<AbstractCustomFeature> getContextButtons(IFeatureProvider fp);

}
