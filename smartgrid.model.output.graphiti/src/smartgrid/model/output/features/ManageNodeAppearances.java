package smartgrid.model.output.features;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
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

import smartgridoutput.EntityState;
import smartgridsecurity.graphiti.ConstantProvider;
import smartgridsecurity.graphiti.helper.FeatureRepresentationHelper;
import smartgridtopo.ControlCenter;
import smartgridtopo.GenericController;
import smartgridtopo.InterCom;
import smartgridtopo.NetworkEntity;
import smartgridtopo.NetworkNode;
import smartgridtopo.SmartMeter;

public class ManageNodeAppearances {

    private IDiagramContainer diagramContainer;

    public ManageNodeAppearances(IDiagramContainer diagramContainer) {
        this.diagramContainer = diagramContainer;
    }

    public void removeChildren(ContainerShape shape) {
        while (shape.getChildren().size() > 0) {
            shape.getChildren().remove(0);
        }
    }

    /**
     * Method to draw a question mark into a pe.
     * 
     * @param containerShape
     *            the current shape
     */
    public void drawQuestionMark(ContainerShape containerShape) {
        int xy[] = new int[] { 4, 2, 14, 2, 14, 8, 6, 8, 6, 15, 16, 15 };
        drawMark(containerShape, xy);
    }

    private void drawMark(ContainerShape containerShape, int[] xy) {
        final IPeCreateService peCreateService = Graphiti.getPeCreateService();
        final IGaService gaService = Graphiti.getGaService();
        // create line
        Shape lineShape = peCreateService.createShape(containerShape, false);

        Polyline p = gaService.createPolyline(lineShape, xy);
        p.setForeground(manageColor(new ColorConstant(0, 0, 0)));
        p.setLineWidth(ConstantProvider.questionMarkLineWidth);

        Shape circleShape = peCreateService.createShape(containerShape, false);
        Ellipse circle = gaService.createEllipse(circleShape);
        circle.setLineWidth(ConstantProvider.questionMarkLineWidth);
        circle.setX(9);
        circle.setY(17);
        circle.setHeight(3);
        circle.setWidth(3);
        circle.setForeground(manageColor(new ColorConstant(0, 0, 0)));
    }

    public void drawExclamationMark(ContainerShape shape) {
        int xy[] = new int[] { 9, 2, 9, 15 };
        drawMark(shape, xy);
    }

    /**
     * Draw a specific network entity.
     * 
     * @param containerShape
     *            the current container shape
     * @return the specific network entity ga
     */
    public void manageGraphicalPatternRepresentation(ContainerShape containerShape, boolean original) {
        GraphicsAlgorithm ga = null;
        int locationX = containerShape.getGraphicsAlgorithm().getX();
        int locationY = containerShape.getGraphicsAlgorithm().getY();
        int width = containerShape.getGraphicsAlgorithm().getWidth();
        int height = containerShape.getGraphicsAlgorithm().getHeight();
        EObject bo = containerShape.getLink().getBusinessObjects().get(0);

        if (bo instanceof SmartMeter) {
            ga = original ? FeatureRepresentationHelper.createEllipse(containerShape,
                    this.manageColor(ConstantProvider.SMART_METER_FOREGROUND),
                    this.manageColor(ConstantProvider.SMART_METER_BACKGROUND)) : FeatureRepresentationHelper
                    .createEllipse(containerShape, this.manageColor(new ColorConstant(140, 0, 0)),
                            this.manageColor(new ColorConstant(225, 200, 200)));
        }
        if (bo instanceof ControlCenter) {
            ga = original ? FeatureRepresentationHelper.createEllipse(containerShape,
                    this.manageColor(ConstantProvider.FOREGROUND_BLACK),
                    this.manageColor(ConstantProvider.CONTROL_CENTER_BACKGROUND)) : FeatureRepresentationHelper
                    .createEllipse(containerShape, this.manageColor(new ColorConstant(0, 0, 0)),
                            this.manageColor(new ColorConstant(124, 154, 139)));
        }
        if (bo instanceof GenericController) {
            ga = original ? FeatureRepresentationHelper.createEllipse(containerShape,
                    this.manageColor(ConstantProvider.GENERIC_CONTROLLER_FOREGROUND),
                    this.manageColor(ConstantProvider.GENERIC_CONTROLLER_BACKGROUND)) : FeatureRepresentationHelper
                    .createEllipse(containerShape, this.manageColor(new ColorConstant(210, 60, 0)),
                            this.manageColor(new ColorConstant(200, 188, 168)));
        }
        if (bo instanceof InterCom) {
            ga = original ? FeatureRepresentationHelper.createEllipse(containerShape,
                    this.manageColor(ConstantProvider.INTER_COM_FOREGROUND),
                    this.manageColor(ConstantProvider.INTER_COM_BACKGROUND)) : FeatureRepresentationHelper
                    .createEllipse(containerShape, this.manageColor(new ColorConstant(0, 139, 0)),
                            this.manageColor(new ColorConstant(199, 212, 139)));
        }
        if (bo instanceof NetworkNode) {
            ga = original ? FeatureRepresentationHelper.createRect(containerShape,
                    this.manageColor(ConstantProvider.NETWORK_NODE_FOREGROUND),
                    this.manageColor(ConstantProvider.NETWORK_NODE_BACKGROUND)) : FeatureRepresentationHelper
                    .createRect(containerShape, this.manageColor(new ColorConstant(0, 51, 102)),
                            this.manageColor(new ColorConstant(182, 191, 204)));
        }
        IGaService gaService = Graphiti.getGaService();
        gaService.setLocationAndSize(ga, locationX, locationY, width, height);
        // }

    }

    /**
     * Resolve output bo from NetworkEnitity bo.
     * 
     * @param toResolve
     *            bo to resolve
     * @param loadedBO
     *            lis of all output bo's
     * @return the resolve bo
     */
    public Object resolveBOfromNetworkEntity(NetworkEntity toResolve, EList<?> loadedBO) {
        for (Object obj : loadedBO) {
            if (obj instanceof EntityState && ((EntityState) obj).getOwner().getId() == toResolve.getId()) {
                return obj;
            }
        }
        return null;
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
