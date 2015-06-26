package smartgrid.graphiti.customfeatures;

import java.util.Collection;

import org.eclipse.graphiti.mm.algorithms.Polygon;
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

    private IDiagramContainer diagramContainer;

    public ManageNodeAppearances(IDiagramContainer diagramContainer) {
        this.diagramContainer = diagramContainer;
    }

    /**
     * Removes all children from a given container shape. Children are lines or other elements that
     * lie inside the shape. NodeDestroyed is an example for a shape with children
     * 
     * @param containerShape
     *            the shape to remove children from
     */
    public void removeChildren(ContainerShape containerShape) {
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
    public void drawDestroyed(ContainerShape containerShape) {
        final IPeCreateService peCreateService = Graphiti.getPeCreateService();
        final IGaService gaService = Graphiti.getGaService();
        // create line
        Shape lineShape = peCreateService.createShape(containerShape, false);
        int xy[] = new int[] { 0, 20, 20, 0 };
        Polygon p = gaService.createPolygon(lineShape, xy);
        p.setForeground(manageColor(ConstantProvider.FOREGROUND_BLACK));
        p.setLineWidth(ConstantProvider.shapeLineWidth);
        xy = new int[] { 0, 0, 20, 20 };
        Shape secondLine = peCreateService.createShape(containerShape, false);
        Polygon poly = gaService.createPolygon(secondLine, xy);
        poly.setForeground(manageColor(ConstantProvider.FOREGROUND_BLACK));
        poly.setLineWidth(ConstantProvider.shapeLineWidth);
    }

    public Color manageBackground(boolean powerOutage) {
        return powerOutage ? manageColor(new ColorConstant(220, 220, 220)) : manageColor(ConstantProvider.POWER_GRID_NODE_BACKGROUND);
    }

    public Color manageForeground() {
        return manageColor(ConstantProvider.FOREGROUND_BLACK);
    }

    /**
     * This method is primary used for showing that a PowerGridNode is enabled or disabled
     * 
     * @param constant
     *            the new color for a container shape
     * @return the created color
     */
    private Color manageColor(IColorConstant constant) {
        Collection<Color> colors = diagramContainer.getDiagramTypeProvider().getDiagram().getColors();
        for (Color existingColor : colors) {
            if (existingColor.getRed() == constant.getRed() && existingColor.getGreen() == constant.getGreen()
                    && existingColor.getBlue() == constant.getBlue()) {
                return existingColor;
            }
        }

        Color newColor = StylesFactory.eINSTANCE.createColor();
        newColor.eSet(StylesPackage.eINSTANCE.getColor_Red(), constant.getRed());
        newColor.eSet(StylesPackage.eINSTANCE.getColor_Green(), constant.getGreen());
        newColor.eSet(StylesPackage.eINSTANCE.getColor_Blue(), constant.getBlue());
        colors.add(newColor);
        return newColor;
    }
}
