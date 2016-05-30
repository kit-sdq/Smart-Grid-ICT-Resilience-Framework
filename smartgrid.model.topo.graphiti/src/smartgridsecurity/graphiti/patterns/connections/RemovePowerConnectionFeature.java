package smartgridsecurity.graphiti.patterns.connections;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.impl.DefaultRemoveFeature;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;

/**
 * Feature to remove power connections.
 * 
 * @author mario
 *
 */
public class RemovePowerConnectionFeature extends DefaultRemoveFeature {

    public RemovePowerConnectionFeature(IFeatureProvider fp) {
        super(fp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.graphiti.features.impl.DefaultRemoveFeature#canRemove(org
     * .eclipse.graphiti.features.context.IRemoveContext)
     */
    @Override
    public boolean canRemove(IRemoveContext context) {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.graphiti.features.impl.DefaultRemoveFeature#remove(org.eclipse
     * .graphiti.features.context.IRemoveContext)
     */
    @Override
    public void remove(IRemoveContext context) {
        IPeService peService = Graphiti.getPeService();
        peService.deletePictogramElement(context.getPictogramElement());
        super.remove(context);
    }

}
