package smartgridsecurity.graphiti.patterns;

import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.util.ColorConstant;

import smartgridsecurity.graphiti.helper.FeatureRepresentationHelper;
import smartgridsecurity.graphiti.helper.UIDHelper;
import smartgridtopo.PowerGridNode;
import smartgridtopo.Scenario;
import smartgridtopo.SmartgridtopoFactory;

/**
 * Pattern to create power grid nodes.
 * @author mario
 *
 */
public class PowerGridNodePattern extends AbstractFormPattern {

    @Override
    public boolean isMainBusinessObjectApplicable(final Object mainBusinessObject) {
        return mainBusinessObject instanceof PowerGridNode;
    }

    @Override
    public Object[] create(final ICreateContext context) {
        addScenario();
        Scenario container = (Scenario) getBusinessObjectForPictogramElement(getDiagram());
        final PowerGridNode node = SmartgridtopoFactory.eINSTANCE.createPowerGridNode();
        // Add model element to resource.
        // We add the model element to the resource of the diagram for
        // simplicity's sake. Normally, a customer would use its own
        // model persistence layer for storing the business model separately.
        container.getContainsPGN().add(node);

        // ask user for EClass name
        // String newClassName = ExampleUtil.askString("Node ID", "Enter new class name", "");
        // if (newClassName == null || newClassName.trim().length() == 0) {
        // return EMPTY;
        // }

        node.setId(UIDHelper.generateUID());

        // do the add
        this.addGraphicalRepresentation(context, node);

        // return newly created business object(s)
        return new Object[] { node };
    }

    @Override
    protected String getName() {
        return "Power Grid Node";
    }

    @Override
    protected GraphicsAlgorithm getGraphicalPatternRepresentation(ContainerShape containerShape) {
    	return FeatureRepresentationHelper.createPolygon(containerShape, this.manageColor(new ColorConstant(0, 0, 0)), this.manageColor(new ColorConstant(255, 255, 0)));
    }

    @Override
    protected void linkObjects(ContainerShape containerShape, Object businessObject) {
        PowerGridNode addedNode = (PowerGridNode) businessObject;
        if (addedNode.eResource() == null) {
            // getDiagram().eResource().getContents().add(addedNode);
            throw new RuntimeException("BO not in Resource! (unecpectedly)");
        }

        // create link and wire it
        this.link(containerShape, addedNode);

    }
}
