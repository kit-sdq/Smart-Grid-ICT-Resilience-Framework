package smartgrid.model.output.actions;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.editor.IDiagramContainerUI;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import smartgrid.model.helper.output.LoadOutputModelConformityHelper;
import smartgrid.model.output.StringProvider;
import smartgrid.model.output.features.ManageNodeAppearances;
import smartgridoutput.Destroyed;
import smartgridoutput.NoUplink;
import smartgridoutput.Online;
import smartgridoutput.ScenarioResult;
import smartgridsecurity.graphiti.actions.ToolbarButtonAction;
import smartgridtopo.NetworkEntity;
import smartgridtopo.Scenario;

/**
 * This class defines an action which loads a specific output model.
 * 
 * @author mario
 *
 */
public class LoadOutputToolbarButtonAction extends ToolbarButtonAction {

    public LoadOutputToolbarButtonAction(IPropertyChangeListener listener) {
        ACTION_ID = "LoadOutputToolbarButtonActionId";
        TOOL_TIP = "Load a State Result";
        setToolTipText(TOOL_TIP);
        setId(ACTION_ID);
        addPropertyChangeListener(listener);
        setImageDescriptor(createImage("icons" + File.separator + "load_output.png", "smartgrid.model.output"));
    }

    @Override
    public void setDiagramContainer(IDiagramContainerUI container) {
        diagramContainer = container;
    }

    @Override
    public void run() {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

        for (EObject obj : diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects()) {
            // no input loaded
            if ((obj instanceof Scenario)
                    && diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects().size() == 1) {
                MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
                messageBox.setMessage("Cannot load output model without input model");
                messageBox.open();
                return;
            }
        }

        final FileDialog dialog = new FileDialog(shell, SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*.smartgridoutput" });
        dialog.setFilterNames(new String[] { "Smart Grid Output" });
        String fileSelected = dialog.open();

        if (fileSelected == null) {
            return;
        } else {
            // Perform Action, like save the file.
            System.out.println("Selected file: " + fileSelected);
            final TransactionalEditingDomain domain = diagramContainer.getDiagramBehavior().getEditingDomain();

            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
                    Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

            // load the resource and resolve the proxies
            File source = new File(fileSelected);
            ResourceSet rs = new ResourceSetImpl();
            Resource r = rs.createResource(URI.createFileURI(source.getAbsolutePath()));

            try {
                r.load(null);
                EcoreUtil.resolveAll(rs);
                final EObject obj = r.getContents().get(0);
                RecordingCommand cmd = new RecordingCommand(domain) {
                    @Override
                    protected void doExecute() {
                        ManageNodeAppearances manager = new ManageNodeAppearances(diagramContainer);
                        // is input loaded?
                        boolean hasOutput = false;
                        boolean loadSuccessful = true;
                        EList<EObject> boList = diagramContainer.getDiagramTypeProvider().getDiagram().getLink()
                                .getBusinessObjects();

                        // Conformity check
                        for (final EObject current : boList) {
                            if (current instanceof Scenario) {
                                if (!(LoadOutputModelConformityHelper.checkOutputModelConformity((ScenarioResult) obj,
                                        (Scenario) current))) {
                                    MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
                                    messageBox.setMessage("Output model is not conform to the current topo model");
                                    messageBox.open();
                                    loadSuccessful = false;
                                    return;
                                }
                            }
                        }

                        ScenarioResult result = null;
                        for (final EObject tmp : boList) {
                            if (tmp instanceof ScenarioResult) {
                                result = (ScenarioResult) tmp;
                                hasOutput = true;
                                break;
                            }
                        }

                        Shape[] shapes = (Shape[]) diagramContainer.getDiagramTypeProvider().getDiagram().getChildren()
                                .toArray();
                        if (hasOutput) {
                            // Clear all output Elements
                            for (int i = 0; i < shapes.length; i++) {
                                Shape currentShape = shapes[i];
                                // set visible true for all input pe's
                                if ((currentShape.getLink().getBusinessObjects().get(0) instanceof NetworkEntity)) {
                                    currentShape.setVisible(true);
                                    manager.manageGraphicalPatternRepresentation((ContainerShape) currentShape, true);
                                    Object bo = manager.resolveBOfromNetworkEntity((NetworkEntity) currentShape
                                            .getLink().getBusinessObjects().get(0), result.getEntityStates());

                                    if (bo != null && bo instanceof NoUplink) {
                                        manager.removeChildren((ContainerShape) currentShape);
                                    }
                                }
                            }
                            diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects()
                                    .remove(result);
                        }
                        for (int i = 0; i < shapes.length; i++) {
                            Shape containerShape = shapes[i];

                            if (containerShape.getLink().getBusinessObjects().get(0) instanceof NetworkEntity) {
                                Object ob = manager.resolveBOfromNetworkEntity((NetworkEntity) containerShape.getLink()
                                        .getBusinessObjects().get(0), ((ScenarioResult) obj).getEntityStates());
                                if (ob != null && !(ob instanceof Online) && !(ob instanceof Destroyed)) {
                                    manager.manageGraphicalPatternRepresentation((ContainerShape) containerShape, false);
                                }
                                if (ob != null && ob instanceof NoUplink) {
                                    manager.drawQuestionMark((ContainerShape) containerShape);
                                }
                            }
                        }
                        if (loadSuccessful) {
                            boList.add(obj);
                        }
                    }
                };
                domain.getCommandStack().execute(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        firePropertyChange(StringProvider.ENABLE_CLEAR_BUTTON, null, null);
    }
}
