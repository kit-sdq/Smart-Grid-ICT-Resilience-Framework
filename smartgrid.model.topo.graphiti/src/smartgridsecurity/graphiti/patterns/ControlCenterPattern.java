package smartgridsecurity.graphiti.patterns;

import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.util.ColorConstant;

import smartgridsecurity.graphiti.helper.FeatureRepresentationHelper;
import smartgridsecurity.graphiti.helper.UIDHelper;
import smartgridtopo.ControlCenter;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoFactory;

/**
 * Pattern to implement control centers.
 *
 * @author mario
 *
 */
public class ControlCenterPattern extends AbstractFormPattern {

    @Override
    public boolean isMainBusinessObjectApplicable(final Object mainBusinessObject) {
        return mainBusinessObject instanceof ControlCenter;
    }

    @Override
    public Object[] create(final ICreateContext context) {
        this.addScenario();
        final SmartGridTopology container = (SmartGridTopology) this.getBusinessObjectForPictogramElement(this.getDiagram());
        final ControlCenter node = SmartgridtopoFactory.eINSTANCE.createControlCenter();
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
        return "Control Center";
    }

    @Override
    protected GraphicsAlgorithm getGraphicalPatternRepresentation(final ContainerShape containerShape) {
        return FeatureRepresentationHelper.createEllipse(containerShape, this.manageColor(new ColorConstant(0, 0, 0)), this.manageColor(new ColorConstant(51, 102, 0)));
    }

    @Override
    protected void linkObjects(final ContainerShape containerShape, final Object businessObject) {
        final ControlCenter addedNode = (ControlCenter) businessObject;
        if (addedNode.eResource() == null) {
            // getDiagram().eResource().getContents().add(addedNode);
            throw new RuntimeException("BO not in Resource! (unecpectedly)");
        }

        // create link and wire it
        this.link(containerShape, addedNode);

    }

}
