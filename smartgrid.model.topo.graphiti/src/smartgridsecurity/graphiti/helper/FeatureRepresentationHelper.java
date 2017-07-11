package smartgridsecurity.graphiti.helper;

import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

import smartgridsecurity.graphiti.ConstantProvider;

/**
 * Helper class to draw the SGS pe#s.
 *
 * @author mario
 *
 */
public class FeatureRepresentationHelper {

    /**
     * Create an ellipse.
     *
     * @param containerShape
     *            the current container shape
     * @param colorForeground
     *            the foreground color
     * @param colorBackground
     *            the background color
     * @return new ellipse representation
     */
    public static GraphicsAlgorithm createEllipse(final ContainerShape containerShape, final Color colorForeground, final Color colorBackground) {
        final IGaService gaService = Graphiti.getGaService();
        final Ellipse ellipse = gaService.createEllipse(containerShape);
        // ellipse.setForeground(this.manageColor(new ColorConstant(0, 60, 30)));
        ellipse.setForeground(colorForeground);
        ellipse.setBackground(colorBackground);
        ellipse.setLineWidth(ConstantProvider.shapeLineWidth);
        return ellipse;
    }

    /**
     * Create a rectangle.
     *
     * @param containerShape
     *            the current container shape
     * @param colorForeground
     *            the foreground color
     * @param colorBackground
     *            the background color
     * @return new rectangle representation
     */
    public static GraphicsAlgorithm createRect(final ContainerShape containerShape, final Color colorForeground, final Color colorBackground) {
        final IGaService gaService = Graphiti.getGaService();
        final Rectangle rect = gaService.createRectangle(containerShape);
        rect.setWidth(25);
        rect.setHeight(25);
        rect.setForeground(colorForeground);
        rect.setBackground(colorBackground);
        rect.setLineWidth(ConstantProvider.shapeLineWidth);
        return rect;
    }

    /**
     * Create a polygon.
     *
     * @param containerShape
     *            the current container shape
     * @param colorForeground
     *            the foreground color
     * @param colorBackground
     *            the background color
     * @return new polygon representation
     */
    public static GraphicsAlgorithm createPolygon(final ContainerShape containerShape, final Color colorForeground, final Color colorBackground) {
        final IGaService gaService = Graphiti.getGaService();
        final int halfWidth = 13;
        final int width = halfWidth * 2;
        final int xy[] = new int[] { halfWidth, 0, width, width, 0, width };
        final Polygon p = gaService.createPolygon(containerShape, xy);
        p.setForeground(colorForeground);
        p.setBackground(colorBackground);
        p.setLineWidth(ConstantProvider.shapeLineWidth);
        return p;
    }

}
