package smartgridsecurity.graphiti.patterns.connections;

import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.util.IColorConstant;

import smartgridtopo.NetworkEntity;
import smartgridtopo.PhysicalConnection;
import smartgridtopo.Scenario;
import smartgridtopo.SmartgridtopoFactory;

/**
 * Connection feature to create physical connection.
 * @author mario
 *
 */
public class PhysicalConnectionPattern extends AbstractConnection {
	

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractBasePattern#canAdd(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public boolean canAdd(IAddContext context) {
		if (false == context instanceof IAddConnectionContext) {
			return false;
		}
		IAddConnectionContext acc = (IAddConnectionContext) context;
		return null != acc.getNewObject() && acc.getNewObject() instanceof PhysicalConnection;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractConnectionPattern#create(org.eclipse.graphiti.features.context.ICreateConnectionContext)
	 */
	@Override
	public Connection create(ICreateConnectionContext context) {
		// Get source and target nodes
		
		// Create new role and add to contents
        PhysicalConnection newRole = SmartgridtopoFactory.eINSTANCE.createPhysicalConnection();


		// add to resources
		Scenario scenario = (Scenario) getBusinessObjectForPictogramElement(getDiagram());
		scenario.getContainsC().add(newRole);

		// Get anchors
		Anchor sourceAnchor = context.getSourceAnchor();
		Anchor targetAnchor = context.getTargetAnchor();
        newRole.getLinks().add((NetworkEntity) getBoFromAnchor(sourceAnchor));
        newRole.getLinks().add((NetworkEntity) getBoFromAnchor(targetAnchor));
		
		AddConnectionContext addContext = new AddConnectionContext(
				sourceAnchor, targetAnchor);
		addContext.setNewObject(newRole);

		Connection connection = (Connection) getFeatureProvider()
				.addIfPossible(addContext);
		
		return connection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractConnectionPattern#canStartConnection(org.eclipse.graphiti.features.context.ICreateConnectionContext)
	 */
	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		Object sourceElement = getBusinessObjectForPictogramElement(context
				.getSourcePictogramElement());
		return sourceElement instanceof NetworkEntity;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractConnectionPattern#canCreate(org.eclipse.graphiti.features.context.ICreateConnectionContext)
	 */
	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		// Get source and target picto elements
		PictogramElement sourcePictogramElement = context
				.getSourcePictogramElement();
		PictogramElement targetPictogramElement = context
				.getTargetPictogramElement();

		// False if one is null
		if (sourcePictogramElement == null || targetPictogramElement == null) {
			return false;
		}

		// Get BOs
		Object sourceDomainObject = getBusinessObjectForPictogramElement(sourcePictogramElement);
		Object targetDomainObject = getBusinessObjectForPictogramElement(targetPictogramElement);

		// Check for type
		boolean sourceIsCorrect = sourceDomainObject instanceof NetworkEntity;
		boolean targetIsCorrect = targetDomainObject instanceof NetworkEntity;

		// True if both are of type BasicComp or CompositeComp
		return sourceIsCorrect && targetIsCorrect;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractConnectionPattern#getCreateName()
	 */
	@Override
	public String getCreateName() {
		return "Physical Connection";
	}

	@Override
	protected IColorConstant getConnectionColor() {
		return IColorConstant.BLUE;
	}

}
