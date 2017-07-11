package smartgrid.graphiti.actions;

import java.io.File;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.editor.IDiagramContainerUI;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import smartgrid.graphiti.InputModelCreator;
import smartgrid.graphiti.StringProvider;
import smartgrid.graphiti.customfeatures.ManageNodeAppearances;
import smartgridinput.ScenarioState;
import smartgridsecurity.graphiti.actions.ToolbarButtonAction;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;

/**
 * This class defines a toolbar action to create new input models.
 *
 * @author mario
 *
 */
public class NewToolbarButtonAction extends ToolbarButtonAction {

    public NewToolbarButtonAction(final IPropertyChangeListener listener) {
        super();
        this.ACTION_ID = "NewToolbarButtonActionId";
        this.TOOL_TIP = "Create new State Scenario";
        this.setToolTipText(this.TOOL_TIP);
        this.setId(this.ACTION_ID);
        this.addPropertyChangeListener(listener);
        this.setImageDescriptor(this.createImage("icons" + File.separator + "new_input.png", "smartgrid.model.input"));
    }

    @Override
    public void run() {
        for (final EObject obj : this.diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects()) {
            if (!(obj instanceof ScenarioState) && !(obj instanceof SmartGridTopology)) {
                final MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR);
                messageBox.setMessage("Cannot create new input model while output model is loaded");
                messageBox.open();
                return;
            }
        }

        for (final EObject obj : this.diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects()) {
            if (obj instanceof ScenarioState) {

                final TransactionalEditingDomain domain = this.diagramContainer.getDiagramBehavior().getEditingDomain();
                final RecordingCommand c = new RecordingCommand(domain) {
                    @Override
                    protected void doExecute() {
                        // copy list to array -> otherwise you'll get a
                        // concurrency exception
                        final Shape[] shapes = (Shape[]) NewToolbarButtonAction.this.diagramContainer.getDiagramTypeProvider().getDiagram().getChildren().toArray();

                        // First, clear all states if there are any
                        final ManageNodeAppearances manager = new ManageNodeAppearances(NewToolbarButtonAction.this.diagramContainer);
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
                    }
                };
                domain.getCommandStack().execute(c);
            }
        }

        final InputModelCreator creator = new InputModelCreator(this.diagramContainer);
        final int returnCode = creator.createNewInputModel(false);
        if (returnCode == Window.OK) {
            this.firePropertyChange(StringProvider.ENABLE_CLEAR_BUTTON, null, null);
        }
    }

    @Override
    public void setDiagramContainer(final IDiagramContainerUI container) {
        this.diagramContainer = container;
    }
}
