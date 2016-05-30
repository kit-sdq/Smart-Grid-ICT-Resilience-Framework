package smartgridsecurity.graphiti.patterns;

import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.AbstractPattern;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;

/**
 * Abstract pattern which implements common features of all patterns.
 *
 * @author mario
 *
 */
public abstract class AbstractFormPattern extends AbstractPattern {

    @Override
    public String getCreateName() {
        return this.getName();
    }

    @Override
    protected boolean isPatternControlled(final PictogramElement pictogramElement) {
        final Object domainObject = this.getBusinessObjectForPictogramElement(pictogramElement);
        return this.isMainBusinessObjectApplicable(domainObject);
    }

    @Override
    protected boolean isPatternRoot(final PictogramElement pictogramElement) {
        // TODO: better implementation
        final Object domainObject = this.getBusinessObjectForPictogramElement(pictogramElement);
        return this.isMainBusinessObjectApplicable(domainObject);
    }

    @Override
    public boolean canAdd(final IAddContext context) {
        // check if user wants to add a EClass
        if (this.isMainBusinessObjectApplicable(context.getNewObject())) {
            // check if user wants to add to a diagram
            if (context.getTargetContainer() instanceof Diagram) {
                return true;
            }
        }
        return false;
    }

    @Override
    public PictogramElement add(final IAddContext context) {

        final Diagram targetDiagram = (Diagram) context.getTargetContainer();

        // CONTAINER SHAPE
        final IPeCreateService peCreateService = Graphiti.getPeCreateService();
        final ContainerShape containerShape = peCreateService.createContainerShape(targetDiagram, true);

        // add a chopbox anchor to the shape
        peCreateService.createChopboxAnchor(containerShape);

        final IGaService gaService = Graphiti.getGaService();
        final GraphicsAlgorithm shape = this.getGraphicalPatternRepresentation(containerShape);
        gaService.setLocationAndSize(shape, context.getX(), context.getY(), 20, 20);

        this.linkObjects(containerShape, context.getNewObject());

        return containerShape;
    }

    @Override
    public boolean canCreate(final ICreateContext context) {
        return context.getTargetContainer() instanceof Diagram;
    }

    /**
     * Add new scenario.
     */
    protected void addScenario() {
    }

    @Override
    public boolean canResizeShape(final IResizeShapeContext context) {
        return false;
    }

    /**
     * Gets the pattern name.
     *
     * @return the name
     */
    protected abstract String getName();

    /**
     * Gets the graphical representation of the pattern.
     *
     * @param containerShape
     *            the current shape
     * @return the current graphical representation
     */
    protected abstract GraphicsAlgorithm getGraphicalPatternRepresentation(ContainerShape containerShape);

    /**
     * Links a business object to a specific pe.
     *
     * @param containerShape
     *            the current shape
     * @param businessObject
     *            the current bo
     */
    protected abstract void linkObjects(ContainerShape containerShape, Object businessObject);
}
