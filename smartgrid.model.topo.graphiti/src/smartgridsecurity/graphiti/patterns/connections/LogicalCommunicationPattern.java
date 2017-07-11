package smartgridsecurity.graphiti.patterns.connections;

import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.util.IColorConstant;

import smartgridtopo.CommunicatingEntity;
import smartgridtopo.LogicalCommunication;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoFactory;

/**
 * Connection pattern to create a logical connection.
 *
 * @author mario
 *
 */
public class LogicalCommunicationPattern extends AbstractConnection {

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.graphiti.pattern.AbstractBasePattern#canAdd(org.eclipse.
     * graphiti.features.context.IAddContext)
     */
    @Override
    public boolean canAdd(final IAddContext context) {
        if (false == context instanceof IAddConnectionContext) {
            return false;
        }
        final IAddConnectionContext acc = (IAddConnectionContext) context;
        return null != acc.getNewObject() && acc.getNewObject() instanceof LogicalCommunication;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.graphiti.pattern.AbstractConnectionPattern#create(org.eclipse
     * .graphiti.features.context.ICreateConnectionContext)
     */
    @Override
    public Connection create(final ICreateConnectionContext context) {
        // Get source and target nodes

        // Create new role and add to contents
        final LogicalCommunication newRole = SmartgridtopoFactory.eINSTANCE.createLogicalCommunication();

        // add to resources
        final SmartGridTopology scenario = (SmartGridTopology) getBusinessObjectForPictogramElement(getDiagram());
        scenario.getContainsLC().add(newRole);

        // Get anchors
        final Anchor sourceAnchor = context.getSourceAnchor();
        final Anchor targetAnchor = context.getTargetAnchor();

        newRole.getLinks().add((CommunicatingEntity) getBoFromAnchor(sourceAnchor));
        newRole.getLinks().add((CommunicatingEntity) getBoFromAnchor(targetAnchor));
        final AddConnectionContext addContext = new AddConnectionContext(sourceAnchor, targetAnchor);
        addContext.setNewObject(newRole);

        final Connection connection = (Connection) getFeatureProvider().addIfPossible(addContext);

        return connection;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.graphiti.pattern.AbstractConnectionPattern#canStartConnection
     * (org.eclipse.graphiti.features.context.ICreateConnectionContext)
     */
    @Override
    public boolean canStartConnection(final ICreateConnectionContext context) {
        final Object sourceElement = getBusinessObjectForPictogramElement(context.getSourcePictogramElement());
        return sourceElement instanceof CommunicatingEntity;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.graphiti.pattern.AbstractConnectionPattern#canCreate(org.
     * eclipse.graphiti.features.context.ICreateConnectionContext)
     */
    @Override
    public boolean canCreate(final ICreateConnectionContext context) {
        // Get source and target picto elements
        final PictogramElement sourcePictogramElement = context.getSourcePictogramElement();
        final PictogramElement targetPictogramElement = context.getTargetPictogramElement();

        // False if one is null
        if (sourcePictogramElement == null || targetPictogramElement == null) {
            return false;
        }

        // Get BOs
        final Object sourceDomainObject = getBusinessObjectForPictogramElement(sourcePictogramElement);
        final Object targetDomainObject = getBusinessObjectForPictogramElement(targetPictogramElement);

        // Check for type
        final boolean sourceIsCorrect = sourceDomainObject instanceof CommunicatingEntity;
        final boolean targetIsCorrect = targetDomainObject instanceof CommunicatingEntity;

        // True if both are of type BasicComp or CompositeComp
        return sourceIsCorrect && targetIsCorrect;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.graphiti.pattern.AbstractConnectionPattern#getCreateName()
     */
    @Override
    public String getCreateName() {
        return "Logical Communication";
    }

    @Override
    protected IColorConstant getConnectionColor() {
        return IColorConstant.DARK_GREEN;
    }

}
