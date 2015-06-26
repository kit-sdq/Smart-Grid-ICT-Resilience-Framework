package smartgridsecurity.graphiti.patterns.connections;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.ui.features.DefaultDeleteFeature;

import smartgridtopo.NetworkEntity;

/**
 * Feature to delete a power connection.
 * @author mario
 *
 */
public class DeletePowerConnectionFeature extends DefaultDeleteFeature {

    public DeletePowerConnectionFeature(final IFeatureProvider fp) {
        super(fp);
    }

    @Override
    public boolean canDelete(final IDeleteContext context) {
        return true;
    }

    @Override
    public void delete(final IDeleteContext context) {
        // Get interfaces
        NetworkEntity start = getNetworkEntity(((Connection) context.getPictogramElement()).getStart());

        Object end = getBusinessObjectForPictogramElement(((Connection) context.getPictogramElement()).getEnd()
                .getParent());

        // Remove end from start's parent interfaces list
        start.getConnectedTo().remove(end);

        super.delete(context);
    }

    /**
     * Get a {@link Anchor}'s parent interface.
     * 
     * @param anchor
     *            The {@link Anchor}.
     * @return The {@link NetworkEntity}.
     */
    private NetworkEntity getNetworkEntity(Anchor anchor) {
        if (anchor != null) {
            Object object = getBusinessObjectForPictogramElement(anchor.getParent());
            if (object instanceof NetworkEntity) {
                return (NetworkEntity) object;
            }
        }
        return null;
    }
}
