package smartgrid.model.output.actions;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
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
import smartgridoutput.Defect;
import smartgridoutput.NoUplink;
import smartgridoutput.Online;
import smartgridoutput.ScenarioResult;
import smartgridsecurity.graphiti.actions.ToolbarButtonAction;
import smartgridtopo.NetworkEntity;
import smartgridtopo.SmartGridTopology;

/**
 * This class defines an action which loads a specific output model.
 *
 * @author mario
 *
 */
public class LoadOutputToolbarButtonAction extends ToolbarButtonAction {

    private static final Logger LOG = Logger.getLogger(LoadOutputToolbarButtonAction.class);

    private boolean loadSuccessful = true;

    public LoadOutputToolbarButtonAction(final IPropertyChangeListener listener) {
        this.ACTION_ID = "LoadOutputToolbarButtonActionId";
        this.TOOL_TIP = "Load a State Result";
        this.setToolTipText(this.TOOL_TIP);
        this.setId(this.ACTION_ID);
        this.addPropertyChangeListener(listener);
        this.setImageDescriptor(
                this.createImage("icons" + File.separator + "load_output.png", "smartgrid.model.output"));
    }

    @Override
    public void setDiagramContainer(final IDiagramContainerUI container) {
        this.diagramContainer = container;
    }

    @Override
    public void run() {
        LOG.info("[LoadOutputToolbarButtonAction]: Start loading output model");
        final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

        for (final EObject obj : this.diagramContainer.getDiagramTypeProvider().getDiagram().getLink()
                .getBusinessObjects()) {
            // no input loaded
            if ((obj instanceof SmartGridTopology) && this.diagramContainer.getDiagramTypeProvider().getDiagram()
                    .getLink().getBusinessObjects().size() == 1) {
                final MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
                messageBox.setMessage("Cannot load output model without input model");
                messageBox.open();
                LOG.info(
                        "[LoadOutputToolbarButtonAction]: Loading output model failed due to non existent input model");
                return;
            }
        }

        final FileDialog dialog = new FileDialog(shell, SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*.smartgridoutput" });
        dialog.setFilterNames(new String[] { "Smart Grid Output" });
        final String fileSelected = dialog.open();

        if (fileSelected == null) {
            return;
        } else {
            // Perform Action, like save the file.
            LOG.info("Selected file: " + fileSelected);
            final TransactionalEditingDomain domain = this.diagramContainer.getDiagramBehavior().getEditingDomain();

            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap()
                    .put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

            // load the resource and resolve the proxies
            final File source = new File(fileSelected);
            final ResourceSet rs = new ResourceSetImpl();
            final Resource r = rs.createResource(URI.createFileURI(source.getAbsolutePath()));

            try {
                r.load(null);
                EcoreUtil.resolveAll(rs);
                final EObject obj = r.getContents().get(0);
                final RecordingCommand cmd = new RecordingCommand(domain) {
                    @Override
                    protected void doExecute() {
                        final ManageNodeAppearances manager = new ManageNodeAppearances(
                                LoadOutputToolbarButtonAction.this.diagramContainer);
                        // is input loaded?
                        boolean hasOutput = false;
                        final EList<EObject> boList = LoadOutputToolbarButtonAction.this.diagramContainer
                                .getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects();

                        // Conformity check
                        for (final EObject current : boList) {
                            if (current instanceof SmartGridTopology) {
                                if (!(LoadOutputModelConformityHelper.checkOutputModelConformitySimple(
                                        (ScenarioResult) obj, (SmartGridTopology) current))) {
                                    final MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
                                    messageBox.setMessage("Output model is not conform to the current topo model");
                                    messageBox.open();
                                    LoadOutputToolbarButtonAction.this.loadSuccessful = false;
                                    LOG.info(
                                            "[LoadOutputToolbarButtonAction]: Loading output model failed since it is not conform to the topo model");
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

                        final Shape[] shapes = (Shape[]) LoadOutputToolbarButtonAction.this.diagramContainer
                                .getDiagramTypeProvider().getDiagram().getChildren().toArray();
                        LOG.debug("[LoadOutputToolbarButtonAction]: Applying output elements to the diagram");
                        if (hasOutput) {
                            // Clear all output Elements
                            for (final Shape currentShape : shapes) {
                                // set visible true for all input pe's
                                if ((currentShape.getLink().getBusinessObjects().get(0) instanceof NetworkEntity)) {
                                    currentShape.setVisible(true);
                                    manager.manageGraphicalPatternRepresentation((ContainerShape) currentShape, true);
                                    final Object bo = manager.resolveBOfromNetworkEntity(
                                            (NetworkEntity) currentShape.getLink().getBusinessObjects().get(0),
                                            result.getStates());

                                    if (bo != null && bo instanceof NoUplink) {
                                        manager.removeChildren((ContainerShape) currentShape);
                                    }
                                }
                            }
                            LoadOutputToolbarButtonAction.this.diagramContainer.getDiagramTypeProvider().getDiagram()
                                    .getLink().getBusinessObjects().remove(result);
                        }
                        for (final Shape containerShape : shapes) {
                            if (containerShape.getLink().getBusinessObjects().get(0) instanceof NetworkEntity) {
                                final Object ob = manager.resolveBOfromNetworkEntity(
                                        (NetworkEntity) containerShape.getLink().getBusinessObjects().get(0),
                                        ((ScenarioResult) obj).getStates());
                                if (ob != null && !(ob instanceof Online) && !(ob instanceof Defect)) {
                                    manager.manageGraphicalPatternRepresentation((ContainerShape) containerShape,
                                            false);
                                }
                                if (ob != null && ob instanceof NoUplink) {
                                    if (((NoUplink) ob).isIsHacked()) {
                                        manager.drawExclamationMark((ContainerShape) containerShape);
                                    } else {
                                        manager.drawQuestionMark((ContainerShape) containerShape);
                                    }
                                }
                                if (ob != null && ob instanceof Online && ((Online) ob).isIsHacked()) {
                                    manager.drawExclamationMark((ContainerShape) containerShape);
                                }

                            }
                        }
                        if (LoadOutputToolbarButtonAction.this.loadSuccessful) {
                            boList.add(obj);
                        }
                    }
                };
                domain.getCommandStack().execute(cmd);
            } catch (final IOException e) {
                LOG.error("[smartgrid.model.output.graphiti.LoadOutputToolbarButtonAction]: IOException occured");
                e.printStackTrace();
            }
        }
        if (this.loadSuccessful) {
            this.firePropertyChange(StringProvider.ENABLE_CLEAR_BUTTON, null, null);
        }
        this.loadSuccessful = true;
        LOG.info("[LoadOutputToolbarButtonAction]: Successfully finished loading output model");
    }
}
