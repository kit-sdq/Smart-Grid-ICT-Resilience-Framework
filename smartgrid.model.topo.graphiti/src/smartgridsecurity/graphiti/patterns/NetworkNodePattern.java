package smartgridsecurity.graphiti.patterns;

import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.util.ColorConstant;

import smartgridsecurity.graphiti.helper.FeatureRepresentationHelper;
import smartgridsecurity.graphiti.helper.UIDHelper;
import smartgridtopo.NetworkNode;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoFactory;

/**
 * Pattern to create network nodes.
 *
 * @author mario
 *
 */
public class NetworkNodePattern extends AbstractFormPattern {

    @Override
    public boolean isMainBusinessObjectApplicable(final Object mainBusinessObject) {
        return mainBusinessObject instanceof NetworkNode;
    }

    @Override
    public Object[] create(final ICreateContext context) {
        this.addScenario();
        final SmartGridTopology container = (SmartGridTopology) this.getBusinessObjectForPictogramElement(this.getDiagram());
        final NetworkNode node = SmartgridtopoFactory.eINSTANCE.createNetworkNode();
        // Add model element to resource.
        // We add the model element to the resource of the diagram for
        // simplicity's sake. Normally, a customer would use its own
        // model persistence layer for storing the business model separately.
        container.getContainsNE().add(node);

        node.setId(UIDHelper.generateUID());

        // do the add
        this.addGraphicalRepresentation(context, node);

        // return newly created business object(s)
        return new Object[] { node };
    }

    @Override
    protected String getName() {
        return "Network Node";
    }

    @Override
    protected GraphicsAlgorithm getGraphicalPatternRepresentation(final ContainerShape containerShape) {
        return FeatureRepresentationHelper.createRect(containerShape, this.manageColor(new ColorConstant(0, 51, 102)), this.manageColor(new ColorConstant(0, 102, 204)));
    }

    @Override
    protected void linkObjects(final ContainerShape containerShape, final Object businessObject) {
        final NetworkNode addedNode = (NetworkNode) businessObject;
        if (addedNode.eResource() == null) {
            // getDiagram().eResource().getContents().add(addedNode);
            throw new RuntimeException("BO not in Resource! (unecpectedly)");
        }

        // create link and wire it
        this.link(containerShape, addedNode);

    }

}
