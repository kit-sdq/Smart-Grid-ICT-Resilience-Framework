package smartgrid.graphiti.customfeatures;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import smartgrid.graphiti.InputModelCreator;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridsecurity.graphiti.SGSImageProvider;
import smartgridsecurity.graphiti.helper.GraphitiHelper;
import smartgridtopo.PowerGridNode;
import smartgridtopo.Scenario;

/**
 * This class implements a custom feature to set the power of PowerGridNodes.
 * 
 * @author mario
 *
 */
public class PowerEnabledFeature extends AbstractCustomFeature {

    public PowerEnabledFeature(IFeatureProvider fp) {
        super(fp);
    }

    @Override
    public void execute(ICustomContext context) {
        if (!isInputModelAlreadyCreated()) {
            InputModelCreator creator = new InputModelCreator(GraphitiHelper.getInstance().getDiagramContainer());
            creator.createNewInputModel(true);
        }

        final PictogramElement pe = context.getPictogramElements()[0];
        TransactionalEditingDomain domain = getDiagramBehavior().getEditingDomain();
        RecordingCommand rc = new RecordingCommand(domain) {

            @Override
            protected void doExecute() {
                ManageNodeAppearances manager = new ManageNodeAppearances(getDiagramBehavior().getDiagramContainer());
                EObject obj = pe.getLink().getBusinessObjects().get(0);

                EObject states = null;
                for (EObject tmp : getDiagramBehavior().getDiagramContainer().getDiagramTypeProvider().getDiagram()
                        .getLink().getBusinessObjects()) {
                    if (tmp instanceof ScenarioState) {
                        states = tmp;
                        break;
                    }
                }

                // create new input model pe
                if (states != null) {
                    for (PowerState power : ((ScenarioState) states).getPowerStates()) {
                        if ((obj instanceof PowerGridNode && ((PowerGridNode) obj).getId() == power.getOwner().getId())
                                || (obj instanceof PowerState && ((PowerState) obj).getOwner().getId() == power
                                        .getOwner().getId())) {

                            if (!power.isPowerOutage()) {
                                power.setPowerOutage(!power.isPowerOutage());
                                pe.getGraphicsAlgorithm().setBackground(manager.manageBackground(true));
                                pe.getGraphicsAlgorithm().setForeground(manager.manageForeground());
                            } else {
                                power.setPowerOutage(!power.isPowerOutage());
                                pe.getGraphicsAlgorithm().setBackground(manager.manageBackground(false));
                                pe.getGraphicsAlgorithm().setForeground(manager.manageForeground());
                            }

                        }
                    }
                }
            }
        };
        domain.getCommandStack().execute(rc);
    }

    /**
     * This method checks if an input model is already loaded.
     * 
     * @return true if loaded, false otherwise
     */
    private boolean isInputModelAlreadyCreated() {
        for (EObject obj : getDiagramBehavior().getDiagramContainer().getDiagramTypeProvider().getDiagram().getLink()
                .getBusinessObjects()) {
            if (obj instanceof ScenarioState) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canExecute(ICustomContext context) {
        boolean ret = false;
        for (EObject obj : getDiagram().getLink().getBusinessObjects()) {
            if (!(obj instanceof ScenarioState) && !(obj instanceof Scenario)) {
                return false;
            }
            if ((obj instanceof ScenarioState)) {
                ret = true;
            }
        }
        PictogramElement[] pes = context.getPictogramElements();
        if (pes != null && pes.length == 1) {
            Object bo = getBusinessObjectForPictogramElement(pes[0]);
            if (bo instanceof PowerGridNode || bo instanceof PowerState) {
                ret = ret & true;
            } else {
                ret = ret & false;
            }
        }
        return ret;
    }

    @Override
    public String getName() {
        return "is power enabled?";
    }

    @Override
    public String getImageId() {
        return SGSImageProvider.IMG_ENABLE;
    }

}
