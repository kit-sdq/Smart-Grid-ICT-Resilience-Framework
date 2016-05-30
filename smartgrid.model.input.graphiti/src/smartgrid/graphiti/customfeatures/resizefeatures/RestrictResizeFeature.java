package smartgrid.graphiti.customfeatures.resizefeatures;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;

/**
 * Resize feature to restrict the resize mechanism of input model pe's. The class is not used at the
 * moment.
 * 
 * @author mario
 *
 */
public class RestrictResizeFeature extends DefaultResizeShapeFeature {

    public RestrictResizeFeature(IFeatureProvider fp) {
        super(fp);
    }

    @Override
    public boolean canResizeShape(IResizeShapeContext context) {
        return false;
    }

}
