package smartgrid.graphiti.m2d.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutBendPoint;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutRelationship;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.DirectedGraphLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalShift;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.VerticalLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.BendPoint;
import org.eclipse.zest.layouts.exampleStructures.SimpleNode;

/**
 * A class to test various Zest {@link LayoutAlgorithm} in Graphiti
 *
 * @author Patrick Talbot - Initial code
 * @author Karsten Thoms - Adoption for Spray
 * @author Michael Junker - Log4J
 * @see http://www.eclipse.org/forums/index.php/m/758573/#msg_758573
 * @see http://code.google.com/a/eclipselabs.org/p/spray/issues/detail?id=91
 */
public class ZestLayoutDiagramFeature extends AbstractCustomFeature {

    private static final Logger LOG = Logger.getLogger(ZestLayoutDiagramFeature.class);

    /**
     * Constructor
     */
    public ZestLayoutDiagramFeature(final IFeatureProvider fp) {
        super(fp);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.graphiti.features.custom.AbstractCustomFeature#getDescription ()
     */
    @Override
    public String getDescription() {
        return "Layout diagram with Zest Layouter"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.graphiti.features.impl.AbstractFeature#getName()
     */
    @Override
    public String getName() {
        return "Layout with Zest"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.graphiti.features.custom.AbstractCustomFeature#canExecute(org
     * .eclipse.graphiti .features.context.ICustomContext)
     */
    @Override
    public boolean canExecute(final ICustomContext context) {
        final PictogramElement[] pes = context.getPictogramElements();
        if (pes != null && pes.length == 1 && pes[0] instanceof Diagram) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.graphiti.features.custom.ICustomFeature#execute(org.eclipse.
     * graphiti.features .context.ICustomContext)
     */
    @Override
    public void execute(final ICustomContext context) {

        // get a map of the self connection anchor locations
        final Map<Connection, Point> selves = this.getSelfConnections();

        // get the chosen LayoutAlgorithmn instance
        final LayoutAlgorithm layoutAlgorithm = this.getLayoutAlgorithmn(5);

        if (layoutAlgorithm != null) {
            try {

                // Get the map of SimpleNode per Shapes
                final Map<Shape, SimpleNode> map = this.getLayoutEntities();

                // Get the array of Connection LayoutRelationships
                final LayoutRelationship[] connections = this.getConnectionEntities(map);

                // Setup the array of Shape LayoutEntity
                final LayoutEntity[] entities = map.values().toArray(new LayoutEntity[0]);

                // Get the diagram GraphicsAlgorithmn (we need the graph
                // dimensions)
                final GraphicsAlgorithm ga = this.getDiagram().getGraphicsAlgorithm();

                // Apply the LayoutAlgorithmn
                layoutAlgorithm.applyLayout(entities, connections, 0, 0, ga.getWidth(), ga.getHeight(), false, false);

                // Update the Graphiti Shapes and Connections locations
                this.updateGraphCoordinates(entities, connections);

                // Reposition the self connections bendpoints:
                this.adaptSelfBendPoints(selves);

            } catch (final InvalidLayoutConfiguration e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Used to keep track of the initial Connection locations for self connections<br/>
     * The self connections cannot be computed by the LayoutAlgorithmn but the Nodes will probably
     * be moved<br/>
     * So we need to recompute the bend points locations based on the offset of the Anchor from the
     * initial location
     *
     * @return a {@link Map} of initial {@link Anchor} location {@link Point} per {@link Connection}
     *         s
     */
    private Map<Connection, Point> getSelfConnections() {
        final IGaService gaService = Graphiti.getGaService();
        final Map<Connection, Point> selves = new HashMap<Connection, Point>();
        final EList<Connection> connections = this.getDiagram().getConnections();
        for (final Connection connection : connections) {
            final AnchorContainer source = connection.getStart().getParent();
            final AnchorContainer target = connection.getEnd().getParent();
            if (source == target) {
                final GraphicsAlgorithm p = source.getGraphicsAlgorithm();
                final Point start = gaService.createPoint(p.getX(), p.getY());
                selves.put(connection, start);
            }
        }
        return selves;
    }

    /**
     * Reposition the bendpoints based on the offset from the initial {@link Anchor} location to the
     * new location
     *
     * @param selves
     *            The {@link Map} of initial {@link Anchor} location {@link Point} per
     *            {@link Connection}s
     */
    private void adaptSelfBendPoints(final Map<Connection, Point> selves) {
        for (final Connection connection : selves.keySet()) {
            final Point p = selves.get(connection);
            final FreeFormConnection ffcon = (FreeFormConnection) connection;
            final EList<Point> pointList = ffcon.getBendpoints();
            final AnchorContainer source = connection.getStart().getParent();
            final GraphicsAlgorithm start = source.getGraphicsAlgorithm();
            final int deltaX = start.getX() - p.getX();
            final int deltaY = start.getY() - p.getY();
            for (int i = 0; i < pointList.size(); i++) {
                final Point bendPoint = pointList.get(i);
                final int x = bendPoint.getX();
                bendPoint.setX(x + deltaX);
                final int y = bendPoint.getY();
                bendPoint.setY(y + deltaY);
            }
        }
    }

    /**
     * Reposition the Graphiti {@link PictogramElement}s and {@link Connection}s based on the Zest
     * {@link LayoutAlgorithm} computed locations
     *
     * @param entities
     * @param connections
     */
    private void updateGraphCoordinates(final LayoutEntity[] entities, final LayoutRelationship[] connections) {
        for (final LayoutEntity entity : entities) {
            final SimpleNode node = (SimpleNode) entity;
            final Shape shape = (Shape) node.getRealObject();
            final Double x = node.getX();
            final Double y = node.getY();
            shape.getGraphicsAlgorithm().setX(x.intValue());
            shape.getGraphicsAlgorithm().setY(y.intValue());
            final Double width = node.getWidth();
            final Double height = node.getHeight();
            shape.getGraphicsAlgorithm().setWidth(width.intValue());
            shape.getGraphicsAlgorithm().setHeight(height.intValue());
        }

        final IGaService gaService = Graphiti.getGaService();
        for (final LayoutRelationship relationship : connections) {
            final SimpleRelationship rel = (SimpleRelationship) relationship;
            // Using FreeFormConnections with BendPoints, we reset them to the
            // Zest computed
            // locations
            if (rel.getGraphData() instanceof FreeFormConnection) {
                final FreeFormConnection connection = (FreeFormConnection) rel.getGraphData();
                connection.getBendpoints().clear();
                final LayoutBendPoint[] bendPoints = rel.getBendPoints();
                for (final LayoutBendPoint bendPoint : bendPoints) {
                    final Double x = bendPoint.getX();
                    final Double y = bendPoint.getY();
                    final Point p = gaService.createPoint(x.intValue(), y.intValue());
                    connection.getBendpoints().add(p);
                }
            }
        }
    }

    /**
     * @return a {@link Map} of {@link SimpleNode} per {@link Shape}
     */
    private Map<Shape, SimpleNode> getLayoutEntities() {
        final Map<Shape, SimpleNode> map = new HashMap<Shape, SimpleNode>();
        final EList<Shape> children = this.getDiagram().getChildren();
        for (final Shape shape : children) {
            final GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
            final SimpleNode entity = new SimpleNode(shape, ga.getX(), ga.getY(), ga.getWidth(), ga.getHeight());
            map.put(shape, entity);
        }
        return map;
    }

    /**
     * @param map
     *            a {@link Map} of {@link SimpleNode} per {@link Shape} - used to link
     *            {@link SimpleRelationship} to source and target entities
     * @return the array of {@link LayoutRelationship}s to compute
     */
    private LayoutRelationship[] getConnectionEntities(final Map<Shape, SimpleNode> map) {
        final List<LayoutRelationship> liste = new ArrayList<LayoutRelationship>();
        final EList<Connection> connections = this.getDiagram().getConnections();
        for (final Connection connection : connections) {

            String label = null;
            final EList<ConnectionDecorator> decorators = connection.getConnectionDecorators();
            for (final ConnectionDecorator decorator : decorators) {
                if (decorator.getGraphicsAlgorithm() instanceof Text) {
                    label = ((Text) decorator.getGraphicsAlgorithm()).getValue();
                }
            }

            // get the SimpleNode already created from the map:
            final Shape source = (Shape) connection.getStart().getParent();
            final SimpleNode sourceEntity = map.get(source);
            final Shape target = (Shape) connection.getEnd().getParent();
            final SimpleNode targetEntity = map.get(target);

            if (source != target) { // we don't add self relations to avoid
                                        // Cycle errors
                final SimpleRelationship relationship = new SimpleRelationship(sourceEntity, targetEntity, (source != target));
                relationship.setGraphData(connection);
                relationship.clearBendPoints();
                relationship.setLabel(label);
                if (connection instanceof FreeFormConnection) {
                    final FreeFormConnection ffcon = (FreeFormConnection) connection;

                    final EList<Point> pointList = ffcon.getBendpoints();
                    final List<LayoutBendPoint> bendPoints = new ArrayList<LayoutBendPoint>();
                    for (int i = 0; i < pointList.size(); i++) {
                        final Point point = pointList.get(i);
                        final boolean isControlPoint = (i != 0) && (i != pointList.size() - 1);
                        final LayoutBendPoint bendPoint = new BendPoint(point.getX(), point.getY(), isControlPoint);
                        bendPoints.add(bendPoint);
                    }

                    relationship.setBendPoints(bendPoints.toArray(new LayoutBendPoint[0]));
                }
                liste.add(relationship);
                sourceEntity.addRelationship(relationship);
                targetEntity.addRelationship(relationship);
            }
        }
        return liste.toArray(new LayoutRelationship[0]);
    }

    /**
     * Leave this for testing purposes
     *
     * @param current
     * @return
     */
    private LayoutAlgorithm getLayoutAlgorithmn(final int current) {
        LayoutAlgorithm layout;
        final int style = LayoutStyles.NO_LAYOUT_NODE_RESIZING;
        switch (current) {
        case 1:
            layout = new SpringLayoutAlgorithm(style);
            LOG.info("Using SpringLayoutAlgorithmn");
            break;
        case 2:
            layout = new TreeLayoutAlgorithm(style);
            LOG.info("Using TreeLayoutAlgorithm");
            break;
        case 3:
            layout = new GridLayoutAlgorithm(style);
            LOG.info("Using GridLayoutAlgorithm");
            break;
        case 4:
            layout = new HorizontalLayoutAlgorithm(style);
            LOG.info("Using HorizontalLayoutAlgorithm");
            break;
        case 5:
            layout = new HorizontalTreeLayoutAlgorithm(style);
            LOG.info("Using HorizontalTreeLayoutAlgorithm");
            break;
        case 6:
            layout = new VerticalLayoutAlgorithm(style);
            LOG.info("Using VerticalLayoutAlgorithm");
            break;
        case 7:
            layout = new RadialLayoutAlgorithm(style);
            LOG.info("Using RadialLayoutAlgorithm");
            break;
        case 8:
            layout = new DirectedGraphLayoutAlgorithm(style);
            LOG.info("Using DirectedGraphLayoutAlgorithm");
            break;
        case 9:
            layout = new CompositeLayoutAlgorithm(new LayoutAlgorithm[] { new DirectedGraphLayoutAlgorithm(style), new HorizontalShift(style) });
            LOG.info("Using CompositeLayoutAlgorithm [DirectedGraphLayoutAlgorithm+HorizontalShift]");
            break;
        case 10:
            layout = new CompositeLayoutAlgorithm(new LayoutAlgorithm[] { new SpringLayoutAlgorithm(style), new HorizontalShift(style) });
            LOG.info("Using CompositeLayoutAlgorithm [SpringLayoutAlgorithm+HorizontalShift]");
            break;
        case 11:
            layout = new CompositeLayoutAlgorithm(new LayoutAlgorithm[] { new RadialLayoutAlgorithm(style), new HorizontalShift(style) });
            LOG.info("Using CompositeLayoutAlgorithm [RadialLayoutAlgorithm+HorizontalShift]");
            break;
        case 12:
            layout = new HorizontalShift(style);
            LOG.info("Using HorizontalShift");
            break;
        default:
            layout = new CompositeLayoutAlgorithm(new LayoutAlgorithm[] { new TreeLayoutAlgorithm(style), new HorizontalShift(style) });
            LOG.info("Using CompositeLayoutAlgorithm [TreeLayoutAlgorithm+HorizontalShift]");
        }
        return layout;
    }

    /**
     * A {@link org.eclipse.zest.layouts.exampleStructures.SimpleRelationship} subclass used to hold
     * the Graphiti connection reference
     */
    private class SimpleRelationship extends org.eclipse.zest.layouts.exampleStructures.SimpleRelationship {

        private Object graphData;

        public SimpleRelationship(final LayoutEntity sourceEntity, final LayoutEntity destinationEntity, final boolean bidirectional) {
            super(sourceEntity, destinationEntity, bidirectional);
        }

        @Override
        public Object getGraphData() {
            return this.graphData;
        }

        @Override
        public void setGraphData(final Object o) {
            this.graphData = o;
        }
    }

}