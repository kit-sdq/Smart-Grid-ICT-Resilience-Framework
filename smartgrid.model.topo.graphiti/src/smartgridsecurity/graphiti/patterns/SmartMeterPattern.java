package smartgridsecurity.graphiti.patterns;

import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.util.ColorConstant;

import smartgridsecurity.graphiti.helper.FeatureRepresentationHelper;
import smartgridsecurity.graphiti.helper.UIDHelper;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;
import smartgridtopo.SmartgridtopoFactory;

/**
 * Pattern to create smart meters.
 *
 * @author mario
 *
 */
public class SmartMeterPattern extends AbstractFormPattern {

    @Override
    public boolean isMainBusinessObjectApplicable(final Object mainBusinessObject) {
        return mainBusinessObject instanceof SmartMeter;
    }

    @Override
    public Object[] create(final ICreateContext context) {
        addScenario();
        final SmartGridTopology container = (SmartGridTopology) getBusinessObjectForPictogramElement(getDiagram());
        final SmartMeter node = SmartgridtopoFactory.eINSTANCE.createSmartMeter();
        node.setId(UIDHelper.generateUID());

        container.getContainsNE().add(node);

        addGraphicalRepresentation(context, node);

        // return newly created business object(s)
        return new Object[] { node };
    }

    @Override
    protected String getName() {
        return "Smart Meter";
    }

    @Override
    protected GraphicsAlgorithm getGraphicalPatternRepresentation(final ContainerShape containerShape) {
        return FeatureRepresentationHelper.createEllipse(containerShape, this.manageColor(new ColorConstant(140, 0, 0)), this.manageColor(new ColorConstant(255, 0, 0)));
    }

    @Override
    protected void linkObjects(final ContainerShape containerShape, final Object businessObject) {
        final SmartMeter addedNode = (SmartMeter) businessObject;
        if (addedNode.eResource() == null) {
            // getDiagram().eResource().getContents().add(addedNode);
            throw new RuntimeException("BO not in Resource! (unecpectedly)");
        }

        // create link and wire it
        this.link(containerShape, addedNode);

    }
}
