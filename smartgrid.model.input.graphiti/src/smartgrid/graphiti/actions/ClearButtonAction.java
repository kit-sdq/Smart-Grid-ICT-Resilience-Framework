package smartgrid.graphiti.actions;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.editor.IDiagramContainerUI;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import smartgrid.graphiti.StringProvider;
import smartgrid.graphiti.customfeatures.ManageNodeAppearances;
import smartgridinput.ScenarioState;
import smartgridsecurity.graphiti.actions.ToolbarButtonAction;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;

/**
 * This class defines a toolbar action. If this action is executed, the input model will be
 * reverted.
 *
 * @author mario
 *
 */
public class ClearButtonAction extends ToolbarButtonAction implements IPropertyChangeListener {

    private static final Logger LOG = Logger.getLogger(ClearButtonAction.class);

    public ClearButtonAction() {
        super(AS_CHECK_BOX);
        ACTION_ID = "EnableInputButtonActionId";
        TOOL_TIP = "Clear the State Scenario representation";
        setToolTipText(TOOL_TIP);
        setId(ACTION_ID);
        addPropertyChangeListener(this);
        setImageDescriptor(createImage("icons" + File.separator + "clear_input.png", "smartgrid.model.input"));
    }

    @Override
    public void setDiagramContainer(final IDiagramContainerUI container) {
        diagramContainer = container;
        checkInputModel();
    }

    /**
     * This method checks if there is an input model. If true, the action will be enabled.
     */
    private void checkInputModel() {
        final EList<EObject> list = diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects();
        for (final EObject model : list) {
            if (model instanceof ScenarioState) {
                setEnabled(true);
                return;
            }
        }
        setEnabled(false);
    }

    /**
     * This method clears the current input model.
     */
    @Override
    public void run() {
        LOG.info("[ClearButtonAction]: Start clearing input model from diagram");
        for (final EObject obj : diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects()) {
            if (!(obj instanceof ScenarioState) && !(obj instanceof SmartGridTopology)) {
                final MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR);
                messageBox.setMessage("Cannot clear input model while output model is loaded");
                messageBox.open();
                LOG.info("[ClearButtonAction]: End clearing due to failure");
                return;
            }
        }
        final TransactionalEditingDomain domain = diagramContainer.getDiagramBehavior().getEditingDomain();
        final RecordingCommand c = new RecordingCommand(domain) {
            @Override
            protected void doExecute() {
                final ManageNodeAppearances manager = new ManageNodeAppearances(ClearButtonAction.this.diagramContainer);
                // copy list to array -> otherwise you'll get a concurrency
                // exception
                final Shape[] shapes = (Shape[]) ClearButtonAction.this.diagramContainer.getDiagramTypeProvider().getDiagram().getChildren().toArray();
                LOG.debug("[ClearButtonAction]: Clearing all nodes");
                for (final Shape currentShape : shapes) {
                    if (currentShape.getLink().getBusinessObjects().get(0) instanceof PowerGridNode) {
                        currentShape.setVisible(true);

                        currentShape.getGraphicsAlgorithm().setBackground(manager.manageBackground(false));
                        currentShape.getGraphicsAlgorithm().setForeground(manager.manageForeground());

                    } else if (currentShape.getLink().getBusinessObjects().get(0) instanceof NetworkEntity) {
                        if (currentShape instanceof ContainerShape) {
                            manager.removeChildren((ContainerShape) currentShape);
                        }
                    }
                }
                // remove input model from boList so that node destroyed and
                // power enabled buttons
                // are disabled again
                final EList<EObject> boList = ClearButtonAction.this.diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects();
                LOG.debug("[ClearButtonAction]: Remove link between SmartGridTopology and ScenarioState in the diagram");
                for (final EObject tmp : boList) {
                    if (tmp instanceof ScenarioState) {
                        final TransactionalEditingDomain domain = ClearButtonAction.this.diagramContainer.getDiagramBehavior().getEditingDomain();
                        final RecordingCommand c = new RecordingCommand(domain) {
                            @Override
                            protected void doExecute() {
                                ClearButtonAction.this.diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects().remove(tmp);
                            }
                        };
                        domain.getCommandStack().execute(c);
                        break;
                    }
                }
            }
        };
        domain.getCommandStack().execute(c);
        setEnabled(false);
        LOG.info("[ClearButtonAction]: Clearing successfully finished");
    }

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        if (event.getProperty().equals(StringProvider.ENABLE_CLEAR_BUTTON)) {
            setEnabled(true);
        }
    }
}
