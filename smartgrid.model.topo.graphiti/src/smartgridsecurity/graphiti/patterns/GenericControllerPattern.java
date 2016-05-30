package smartgridsecurity.graphiti.patterns;

import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.util.ColorConstant;

import smartgridsecurity.graphiti.helper.FeatureRepresentationHelper;
import smartgridsecurity.graphiti.helper.UIDHelper;
import smartgridtopo.GenericController;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoFactory;

/**
 * Pattern to create generic controller.
 * 
 * @author mario
 *
 */
public class GenericControllerPattern extends AbstractFormPattern {

    @Override
    public boolean isMainBusinessObjectApplicable(final Object mainBusinessObject) {
        return mainBusinessObject instanceof GenericController;
    }

    @Override
    public Object[] create(final ICreateContext context) {
        addScenario();
        SmartGridTopology container = (SmartGridTopology) getBusinessObjectForPictogramElement(getDiagram());
        GenericController node = SmartgridtopoFactory.eINSTANCE.createGenericController();
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
        return "Generic Controller";
    }

    @Override
    protected GraphicsAlgorithm getGraphicalPatternRepresentation(ContainerShape containerShape) {
        return FeatureRepresentationHelper.createEllipse(containerShape,
                this.manageColor(new ColorConstant(210, 60, 0)), this.manageColor(new ColorConstant(255, 140, 0)));
    }

    @Override
    protected void linkObjects(ContainerShape containerShape, Object businessObject) {
        GenericController addedNode = (GenericController) businessObject;
        if (addedNode.eResource() == null) {
            // getDiagram().eResource().getContents().add(addedNode);
            throw new RuntimeException("BO not in Resource! (unecpectedly)");
        }

        // create link and wire it
        this.link(containerShape, addedNode);

    }

}
