package smartgridsecurity.graphiti.patterns;

import org.eclipse.graphiti.features.IDirectEditingInfo;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.pattern.AbstractPattern;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;

import smartgridtopo.LogicalCommunication;
import smartgridtopo.PhysicalConnection;

/**
 * Pattern to create text comments.
 *
 * @author mario
 *
 */
public class TextCommentPattern extends AbstractPattern {

    private MultiText multiText;

    @Override
    public String getCreateName() {
        return "Text Comment";
    }

    @Override
    protected boolean isPatternControlled(final PictogramElement pictogramElement) {
        return true;
    }

    @Override
    protected boolean isPatternRoot(final PictogramElement pictogramElement) {
        return true;
    }

    @Override
    public boolean isMainBusinessObjectApplicable(final Object mainBusinessObject) {
        if (mainBusinessObject instanceof LogicalCommunication || mainBusinessObject instanceof PhysicalConnection) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canCreate(final ICreateContext context) {
        return context.getTargetContainer() instanceof Diagram;
    }

    @Override
    public boolean canAdd(final IAddContext context) {
        if (context.getTargetContainer() instanceof Diagram) {
            return true;
        }
        return false;
    }

    @Override
    public PictogramElement add(final IAddContext context) {

        final Diagram targetDiagram = (Diagram) context.getTargetContainer();

        // CONTAINER SHAPE
        final IPeCreateService peCreateService = Graphiti.getPeCreateService();
        final ContainerShape containerShape = peCreateService.createContainerShape(targetDiagram, true);

        final IDirectEditingInfo directEditingInfo = getFeatureProvider().getDirectEditingInfo();
        directEditingInfo.setMainPictogramElement(containerShape);
        directEditingInfo.setPictogramElement(containerShape);

        final IGaService gaService = Graphiti.getGaService();
        final Rectangle rect = gaService.createRectangle(containerShape);
        rect.setForeground(this.manageColor(IColorConstant.WHITE));
        rect.setBackground(this.manageColor(IColorConstant.WHITE));
        gaService.setLocationAndSize(rect, context.getX(), context.getY(), 200, 35);

        final Shape textShape = peCreateService.createShape(containerShape, false);
        multiText = gaService.createMultiText(textShape);
        multiText.setFont(gaService.manageFont(getDiagram(), "Arial", 14));
        multiText.setValue("");
        multiText.setForeground(this.manageColor(IColorConstant.BLACK));
        multiText.setHorizontalAlignment(Orientation.ALIGNMENT_LEFT);
        multiText.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
        gaService.setLocationAndSize(multiText, 5, 5, 190, 25);

        // Enable direct editing
        getFeatureProvider().getDirectEditingInfo().setActive(true);
        getFeatureProvider().getDirectEditingInfo().setMainPictogramElement(containerShape);
        getFeatureProvider().getDirectEditingInfo().setGraphicsAlgorithm(multiText);

        return containerShape;
    }

    @Override
    public int getEditingType() {
        return TYPE_MULTILINETEXT;
    }

    @Override
    public String getInitialValue(final IDirectEditingContext context) {
        return ((MultiText) context.getGraphicsAlgorithm()).getValue();
    }

    @Override
    public boolean canDirectEdit(final IDirectEditingContext context) {

        final GraphicsAlgorithm ga = context.getGraphicsAlgorithm();
        if (ga instanceof MultiText) {
            return true;
        }
        return false;
    }

    @Override
    public void setValue(final String value, final IDirectEditingContext context) {
        final MultiText text = (MultiText) context.getGraphicsAlgorithm();
        text.setValue(value);
        updatePictogramElement(context.getPictogramElement());
    }

    @Override
    public Object[] create(final ICreateContext context) {
        addGraphicalRepresentation(context, null);

        // return newly created business object(s)
        return new Object[] { null };
    }
}
