package smartgrid.graphiti.customfeatures;

import java.util.Collection;

import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.algorithms.styles.StylesFactory;
import org.eclipse.graphiti.mm.algorithms.styles.StylesPackage;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.platform.IDiagramContainer;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

import smartgridsecurity.graphiti.ConstantProvider;

public class ManageNodeAppearances {

    private final IDiagramContainer diagramContainer;

    public ManageNodeAppearances(final IDiagramContainer diagramContainer) {
        this.diagramContainer = diagramContainer;
    }

    /**
     * Removes all children from a given container shape. Children are lines or other elements that
     * lie inside the shape. NodeDestroyed is an example for a shape with children
     *
     * @param containerShape
     *            the shape to remove children from
     */
    public void removeChildren(final ContainerShape containerShape) {
        while (containerShape.getChildren().size() > 0) {
            containerShape.getChildren().remove(0);
        }
    }

    /**
     * Method to draw a cross. This is used to show that a network entity is destroyed.
     *
     * @param containerShape
     *            the current container shape
     */
    public void drawDestroyed(final ContainerShape containerShape) {
        final IPeCreateService peCreateService = Graphiti.getPeCreateService();
        final IGaService gaService = Graphiti.getGaService();
        // create line
        final Shape lineShape = peCreateService.createShape(containerShape, false);
        int xy[] = new int[] { 0, 20, 20, 0 };
        final Polygon p = gaService.createPolygon(lineShape, xy);
        p.setForeground(this.manageColor(ConstantProvider.FOREGROUND_BLACK));
        p.setLineWidth(ConstantProvider.shapeLineWidth);
        xy = new int[] { 0, 0, 20, 20 };
        final Shape secondLine = peCreateService.createShape(containerShape, false);
        final Polygon poly = gaService.createPolygon(secondLine, xy);
        poly.setForeground(this.manageColor(ConstantProvider.FOREGROUND_BLACK));
        poly.setLineWidth(ConstantProvider.shapeLineWidth);
    }

    /**
     * Method to draw exclamation mark. This is used to show that a network entity is hacked
     * 
     * @param containerShape
     *            the current container shape
     */
    public void drawHacked(final ContainerShape containerShape) {
        drawExclamationMark(containerShape);
    }

    public void drawExclamationMark(final ContainerShape shape) {
        //Code copied from smartgrid.model.output.features.ManageNodeAppearances maybe refactoring could resolve code duplications
        final int xy[] = new int[] { 9, 2, 9, 15 };
        this.drawMark(shape, xy);
    }

    private void drawMark(final ContainerShape containerShape, final int[] xy) {
        //Code copied from smartgrid.model.output.features.ManageNodeAppearances maybe refactoring could resolve code duplications
        final IPeCreateService peCreateService = Graphiti.getPeCreateService();
        final IGaService gaService = Graphiti.getGaService();
        // create line
        final Shape lineShape = peCreateService.createShape(containerShape, false);

        final Polyline p = gaService.createPolyline(lineShape, xy);
        p.setForeground(this.manageColor(new ColorConstant(0, 0, 0)));
        p.setLineWidth(ConstantProvider.questionMarkLineWidth);

        final Shape circleShape = peCreateService.createShape(containerShape, false);
        final Ellipse circle = gaService.createEllipse(circleShape);
        circle.setLineWidth(ConstantProvider.questionMarkLineWidth);
        circle.setX(9);
        circle.setY(17);
        circle.setHeight(3);
        circle.setWidth(3);
        circle.setForeground(this.manageColor(new ColorConstant(0, 0, 0)));
    }

    public Color manageBackground(final boolean powerOutage) {
        return powerOutage ? this.manageColor(new ColorConstant(220, 220, 220)) : this.manageColor(ConstantProvider.POWER_GRID_NODE_BACKGROUND);
    }

    public Color manageForeground() {
        return this.manageColor(ConstantProvider.FOREGROUND_BLACK);
    }

    /**
     * This method is primary used for showing that a PowerGridNode is enabled or disabled
     *
     * @param constant
     *            the new color for a container shape
     * @return the created color
     */
    private Color manageColor(final IColorConstant constant) {
        final Collection<Color> colors = this.diagramContainer.getDiagramTypeProvider().getDiagram().getColors();
        for (final Color existingColor : colors) {
            if (existingColor.getRed() == constant.getRed() && existingColor.getGreen() == constant.getGreen() && existingColor.getBlue() == constant.getBlue()) {
                return existingColor;
            }
        }

        final Color newColor = StylesFactory.eINSTANCE.createColor();
        newColor.eSet(StylesPackage.eINSTANCE.getColor_Red(), constant.getRed());
        newColor.eSet(StylesPackage.eINSTANCE.getColor_Green(), constant.getGreen());
        newColor.eSet(StylesPackage.eINSTANCE.getColor_Blue(), constant.getBlue());
        colors.add(newColor);
        return newColor;
    }
}
