package smartgrid.model.input.sirius.commands;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;

import smartgrid.model.input.sirius.InputModelRefresher;

@SuppressWarnings("restriction")
public class RefreshInputModel extends AbstractHandler {
    /**
     * Creates an ImageDescriptor for a toolbar action.
     *
     * @param filename
     *            filename of the image
     * @param bundleid
     *            the current osgi plugin id
     * @return new ImageDescriptor
     */
    protected ImageDescriptor createImage(final String filename, final String bundleid) {
        final Bundle bundle = Platform.getBundle(bundleid);
        final URL fullPathString = BundleUtility.find(bundle, filename);
        final ImageDescriptor imageDcr = ImageDescriptor.createFromURL(fullPathString);
        return imageDcr;
    }
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        System.out.println("sda");
//        for (final EObject obj : diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects()) {
//            if (!(obj instanceof ScenarioState) && !(obj instanceof SmartGridTopology)) {
//                final MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR);
//                messageBox.setMessage("Cannot create new input model while output model is loaded");
//                messageBox.open();
//                return;
//            }
//        }
//
//        for (final EObject obj : diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects()) {
//            if (obj instanceof ScenarioState) {
//
//                final TransactionalEditingDomain domain = diagramContainer.getDiagramBehavior().getEditingDomain();
//                final RecordingCommand c = new RecordingCommand(domain) {
//                    @Override
//                    protected void doExecute() {
//                        // copy list to array -> otherwise you'll get a
//                        // concurrency exception
//                        final Shape[] shapes = (Shape[]) NewToolbarButtonAction.this.diagramContainer.getDiagramTypeProvider().getDiagram().getChildren().toArray();
//
//                        // First, clear all states if there are any
//                        final ManageNodeAppearances manager = new ManageNodeAppearances(NewToolbarButtonAction.this.diagramContainer);
//                        for (final Shape currentShape : shapes) {
//                            if (currentShape.getLink().getBusinessObjects().get(0) instanceof PowerGridNode) {
//                                currentShape.setVisible(true);
//
//                                currentShape.getGraphicsAlgorithm().setBackground(manager.manageBackground(false));
//                                currentShape.getGraphicsAlgorithm().setForeground(manager.manageForeground());
//
//                            } else if (currentShape.getLink().getBusinessObjects().get(0) instanceof NetworkEntity) {
//                                if (currentShape instanceof ContainerShape) {
//                                    manager.removeChildren((ContainerShape) currentShape);
//                                }
//                            }
//                        }
//                    }
//                };
//                domain.getCommandStack().execute(c);
//            }
//        }
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
        
        final InputModelRefresher creator = new InputModelRefresher(rep);
        creator.refreshInputModel(false);
        return null;
    }

    
    /**
     * Create save options.
     *
     * @return the save options
     */
    public static Map<?, ?> createSaveOptions() {
        final HashMap<String, Object> saveOptions = new HashMap<>();
        saveOptions.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
        saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
        return saveOptions;
    }
}
