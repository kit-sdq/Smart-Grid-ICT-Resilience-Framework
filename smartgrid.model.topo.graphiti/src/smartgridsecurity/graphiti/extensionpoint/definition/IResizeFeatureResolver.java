package smartgridsecurity.graphiti.extensionpoint.definition;

import java.util.List;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IResizeShapeFeature;

/**
 * Resolver for all resize features.
 * 
 * @author mario
 *
 */
public interface IResizeFeatureResolver {

    public List<IResizeShapeFeature> getResizeShapeFeature(IFeatureProvider fp);

}
