package smartgridsecurity.graphiti.resizefeatures;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;

/**
 * Dummy resize feature to restrict resizing of all pe's.
 *
 * @author mario
 *
 */
public class RestrictResizePictogramFeature extends DefaultResizeShapeFeature {

    public RestrictResizePictogramFeature(final IFeatureProvider fp) {
        super(fp);
    }

    @Override
    public boolean canResizeShape(final IResizeShapeContext context) {
        return false;
    }

}
