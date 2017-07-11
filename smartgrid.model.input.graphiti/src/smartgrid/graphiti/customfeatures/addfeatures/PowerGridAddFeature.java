package smartgrid.graphiti.customfeatures.addfeatures;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;

import smartgridinput.PowerState;
import smartgridsecurity.graphiti.helper.FeatureRepresentationHelper;

/**
 * Custom AddFeature to draw power grid node pe's for input models.
 *
 * @author mario
 *
 */
public class PowerGridAddFeature extends AbstractAddFeature {
    private final EObject bo;

    public PowerGridAddFeature(final IFeatureProvider fp, final EObject o) {
        super(fp);
        bo = o;
    }

    @Override
    public boolean canAdd(final IAddContext context) {
        return true;
    }

    @Override
    public PictogramElement add(final IAddContext context) {
        final Diagram targetDiagram = (Diagram) context.getTargetContainer();

        // CONTAINER SHAPE
        final IPeCreateService peCreateService = Graphiti.getPeCreateService();
        final ContainerShape containerShape = peCreateService.createContainerShape(targetDiagram, true);

        // add a chopbox anchor to the shape
        peCreateService.createChopboxAnchor(containerShape);

        // define a default size for the shape
        // int width = 100;
        // int height = 50;
        final IGaService gaService = Graphiti.getGaService();
        final GraphicsAlgorithm shape = getGraphicalPatternRepresentation(containerShape);
        gaService.setLocationAndSize(shape, context.getX(), context.getY(), 20, 20);

        // gaService.setLocationAndSize(p, context.getX(), context.getY(), 20, 20);
        this.link(containerShape, context.getNewObject());

        return containerShape;
    }

    /**
     * Draw a power grid node.
     *
     * @param containerShape
     *            the current container shape
     * @return the graphical power grid representation
     */
    private GraphicsAlgorithm getGraphicalPatternRepresentation(final ContainerShape containerShape) {
        if (bo instanceof PowerState && ((PowerState) bo).isPowerOutage() == false) {
            return FeatureRepresentationHelper.createPolygon(containerShape, this.manageColor(new ColorConstant(0, 0, 0)), this.manageColor(new ColorConstant(255, 255, 0)));
        }
        return FeatureRepresentationHelper.createPolygon(containerShape, this.manageColor(new ColorConstant(0, 0, 0)), this.manageColor(new ColorConstant(220, 220, 220)));
    }
}
