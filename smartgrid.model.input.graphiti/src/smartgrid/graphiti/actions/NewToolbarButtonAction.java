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

    public NewToolbarButtonAction(IPropertyChangeListener listener) {
        super();
        ACTION_ID = "NewToolbarButtonActionId";
        TOOL_TIP = "Create new State Scenario";
        setToolTipText(TOOL_TIP);
        setId(ACTION_ID);
        addPropertyChangeListener(listener);
        setImageDescriptor(createImage("icons" + File.separator + "new_input.png", "smartgrid.model.input"));
    }

    @Override
    public void run() {
        for (EObject obj : diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects()) {
            if (!(obj instanceof ScenarioState) && !(obj instanceof SmartGridTopology)) {
                MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        SWT.ICON_ERROR);
                messageBox.setMessage("Cannot create new input model while output model is loaded");
                messageBox.open();
                return;
            }
        }

        for (EObject obj : diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects()) {
            if (obj instanceof ScenarioState) {

                final TransactionalEditingDomain domain = diagramContainer.getDiagramBehavior().getEditingDomain();
                RecordingCommand c = new RecordingCommand(domain) {
                    @Override
                    protected void doExecute() {
                        // copy list to array -> otherwise you'll get a
                        // concurrency exception
                        Shape[] shapes = (Shape[]) diagramContainer.getDiagramTypeProvider().getDiagram().getChildren()
                                .toArray();

                        // First, clear all states if there are any
                        ManageNodeAppearances manager = new ManageNodeAppearances(diagramContainer);
                        for (int i = 0; i < shapes.length; i++) {
                            Shape currentShape = shapes[i];
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

        InputModelCreator creator = new InputModelCreator(diagramContainer);
        int returnCode = creator.createNewInputModel(false);
        if (returnCode == Window.OK) {
            firePropertyChange(StringProvider.ENABLE_CLEAR_BUTTON, null, null);
        }
    }

    public void setDiagramContainer(IDiagramContainerUI container) {
        diagramContainer = container;
    }
}
