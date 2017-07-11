package smartgrid.graphiti.customfeatures;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import smartgrid.graphiti.InputModelCreator;
import smartgridinput.EntityState;
import smartgridinput.ScenarioState;
import smartgridsecurity.graphiti.SGSImageProvider;
import smartgridsecurity.graphiti.helper.GraphitiHelper;
import smartgridtopo.NetworkEntity;
import smartgridtopo.SmartGridTopology;

/**
 * This class implements a custom action to destroy and repair NetworkNodes.
 *
 * @author mario
 *
 */
public class NodeDestoryedFeature extends AbstractCustomFeature {

    public NodeDestoryedFeature(final IFeatureProvider fp) {
        super(fp);
    }

    /**
     * This method checks if an input model is already loaded.
     *
     * @return true if loaded, false otherwise
     */
    private boolean isInputModelAlreadyCreated() {
        for (final EObject obj : getDiagramBehavior().getDiagramContainer().getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects()) {
            if (obj instanceof ScenarioState) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(final ICustomContext context) {
        if (!isInputModelAlreadyCreated()) {
            final InputModelCreator creator = new InputModelCreator(GraphitiHelper.getInstance().getDiagramContainer());
            creator.createNewInputModel(true);
        }

        final PictogramElement pe = context.getPictogramElements()[0];
        final TransactionalEditingDomain domain = getDiagramBehavior().getEditingDomain();
        final RecordingCommand rc = new RecordingCommand(domain) {

            @Override
            protected void doExecute() {
                final ManageNodeAppearances manager = new ManageNodeAppearances(NodeDestoryedFeature.this.getDiagramBehavior().getDiagramContainer());
                // get location of the current topo pe
                final EObject obj = pe.getLink().getBusinessObjects().get(0);

                EObject states = null;
                for (final EObject tmp : NodeDestoryedFeature.this.getDiagramBehavior().getDiagramContainer().getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects()) {
                    if (tmp instanceof ScenarioState) {
                        states = tmp;
                        break;
                    }
                }

                // create new input pe
                if (states != null) {
                    for (final EntityState entity : ((ScenarioState) states).getEntityStates()) {
                        if (obj instanceof NetworkEntity && ((NetworkEntity) obj).getId() == entity.getOwner().getId()
                                || obj instanceof EntityState && ((EntityState) obj).getOwner().getId() == entity.getOwner().getId()) {
                            // if (obj.equals(entity.getOwner()) || obj
                            // instanceof EntityState) {

                            if (entity.isIsDestroyed()) {
                                entity.setIsDestroyed(false);
                                // Remove cross lines
                                final ContainerShape shape = (ContainerShape) pe;
                                manager.removeChildren(shape);
                            } else {
                                entity.setIsDestroyed(true);
                                manager.drawDestroyed((ContainerShape) pe);
                            }
                        }
                    }
                }
            }
        };
        domain.getCommandStack().execute(rc);
    }

    @Override
    public boolean canExecute(final ICustomContext context) {
        boolean ret = false;
        for (final EObject obj : getDiagram().getLink().getBusinessObjects()) {
            if (!(obj instanceof ScenarioState) && !(obj instanceof SmartGridTopology)) {
                return false;
            }
            if (obj instanceof ScenarioState) {
                ret = true;
            }
        }
        final PictogramElement[] pes = context.getPictogramElements();
        if (pes != null && pes.length == 1) {
            final Object bo = getBusinessObjectForPictogramElement(pes[0]);
            if (bo instanceof NetworkEntity || bo instanceof EntityState) {
                ret = ret & true;
            } else {
                ret = ret & false;
            }
        }
        return ret;
    }

    @Override
    public String getName() {
        return "is node broken?";
    }

    @Override
    public String getImageId() {
        return SGSImageProvider.IMG_BROKEN_NEW;
    }

}
