package smartgridsecurity.graphiti.patterns.connections;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;

import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;

/**
 * Feature to create a power connection. That means that a business object will be created.
 *
 * @author mario
 *
 */
public class CreatePowerConnectionFeature extends AbstractCreateConnectionFeature {

    public CreatePowerConnectionFeature(final IFeatureProvider fp) {
        super(fp, "Power Connection", "description of create power connection feature. where does this show?");
    }

    @Override
    public boolean canCreate(final ICreateConnectionContext context) {
        final Anchor sourceAnchor = context.getSourceAnchor();
        final Anchor targetAnchor = context.getTargetAnchor();
        if (sourceAnchor != null && targetAnchor != null) {
            final Object target = getBoFromAnchor(targetAnchor);
            final Object source = getBoFromAnchor(sourceAnchor);

            if (source != null && target != null && source instanceof NetworkEntity && target instanceof PowerGridNode) {
                final NetworkEntity sourceNetworkEntity = (NetworkEntity) source;
                if (!sourceNetworkEntity.getConnectedTo().contains(target)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Object getBoFromAnchor(final Anchor sourceAnchor) {
        return getBusinessObjectForPictogramElement(sourceAnchor.getParent());
    }

    @Override
    public boolean canStartConnection(final ICreateConnectionContext context) {
        final Anchor sourceAnchor = context.getSourceAnchor();
        if (sourceAnchor != null) {
            final Object source = getBoFromAnchor(sourceAnchor);
            if (source instanceof NetworkEntity) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Connection create(final ICreateConnectionContext context) {
        // get EClasses which should be connected
        final NetworkEntity source = (NetworkEntity) getBoFromAnchor(context.getSourceAnchor());
        final PowerGridNode target = (PowerGridNode) getBoFromAnchor(context.getTargetAnchor());

        // set reference in domain model
        source.getConnectedTo().add(target);

        // add connection for business object
        final AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(), context.getTargetAnchor());
        final Object[] newObject = new Object[] { source, target };
        addContext.setNewObject(newObject);

        addContext.putProperty("RelType", "Plain");

        Connection newConnection;
        newConnection = (Connection) getFeatureProvider().addIfPossible(addContext);
        // newConnection = this.addGraphicalRepresentation(addContext, newObject);
        // newConnection = (Connection) add(addContext);

        return newConnection;
    }
}
