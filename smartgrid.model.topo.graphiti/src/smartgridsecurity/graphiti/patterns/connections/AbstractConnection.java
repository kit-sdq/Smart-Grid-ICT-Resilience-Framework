package smartgridsecurity.graphiti.patterns.connections;

import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.AbstractConnectionPattern;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;

import smartgridsecurity.graphiti.ConstantProvider;

/**
 * Abstract pattern which implements common features of all connections.
 *
 * @author mario
 *
 */
public abstract class AbstractConnection extends AbstractConnectionPattern {

    @Override
    public PictogramElement add(final IAddContext context) {
        // Initializations
        final IAddConnectionContext addConContext = (IAddConnectionContext) context;
        final IPeCreateService peCreateService = Graphiti.getPeCreateService();
        final IGaService gaService = Graphiti.getGaService();

        // Create connection
        final Connection connection = peCreateService.createCompositeConnection(this.getDiagram());
        connection.setStart(addConContext.getSourceAnchor());
        connection.setEnd(addConContext.getTargetAnchor());

        // Create the line
        final Polyline polyline = gaService.createPlainPolygon(connection);
        polyline.setLineWidth(ConstantProvider.connectionLineWidth);
        polyline.setForeground(this.manageColor(this.getConnectionColor()));

        // Do linking
        this.link(connection, context.getNewObject());

        return connection;
    }

    /**
     * Gets a business object from anchor.
     *
     * @param sourceAnchor
     *            the anchor
     * @return the retrieved business object
     */
    protected Object getBoFromAnchor(final Anchor sourceAnchor) {
        return this.getBusinessObjectForPictogramElement(sourceAnchor.getParent());
    }

    /**
     * Gets the color of a current connection.
     *
     * @return the color
     */
    protected abstract IColorConstant getConnectionColor();

}
