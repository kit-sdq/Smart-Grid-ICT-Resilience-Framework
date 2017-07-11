package smartgridsecurity.graphiti.patterns.connections;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;

import smartgridsecurity.graphiti.ConstantProvider;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;

/**
 * Feature to add a power connection.
 *
 * @author mario
 *
 */
public class AddPowerConnectionFeature extends AbstractAddFeature {

    private static final IColorConstant COLOR_FG = IColorConstant.BLACK;

    public AddPowerConnectionFeature(final IFeatureProvider fp) {
        super(fp);
    }

    @Override
    public boolean canAdd(final IAddContext context) {

        if (context instanceof IAddConnectionContext && context.getNewObject() instanceof Object[]) {
            final Object[] newObjects = (Object[]) context.getNewObject();
            if (newObjects.length == 2) {
                if (newObjects[0] instanceof NetworkEntity && newObjects[1] instanceof PowerGridNode) {
                    if (context.getProperty("RelType").toString().equals("Plain")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public PictogramElement add(final IAddContext context) {

        // CONNECTION WITH POLYLINE
        final IPeCreateService peCreateService = Graphiti.getPeCreateService();
        final Connection connection = peCreateService.createFreeFormConnection(getDiagram());
        final IAddConnectionContext addConContext = (IAddConnectionContext) context;
        connection.setStart(addConContext.getSourceAnchor());
        connection.setEnd(addConContext.getTargetAnchor());
        Graphiti.getPeService().setPropertyValue(connection, "RelType", "Plain");

        // Create line
        final IGaService gaService = Graphiti.getGaService();
        final Polyline polyline = gaService.createPolyline(connection);
        polyline.setLineWidth(ConstantProvider.connectionLineWidth);
        polyline.setForeground(this.manageColor(COLOR_FG));

        // Create arrow
        final ConnectionDecorator cd = peCreateService.createConnectionDecorator(connection, false, 1.0, true);
        createArrow(cd);

        // create link and wire it
        // this.link(connection, (Object[]) context.getNewObject());

        return connection;
    }

    private Polyline createArrow(final GraphicsAlgorithmContainer gaContainer) {
        final IGaService gaService = Graphiti.getGaService();
        final Polyline polyline = gaService.createPolyline(gaContainer, new int[] { -15, 10, 0, 0, -15, -10 });
        polyline.setForeground(this.manageColor(COLOR_FG));
        polyline.setLineWidth(ConstantProvider.connectionLineWidth);
        return polyline;
    }
}
