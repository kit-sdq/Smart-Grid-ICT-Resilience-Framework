package smartgrid.graphiti;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.editor.IDiagramContainerUI;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputFactory;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;

/**
 * This class creates a new inputmodel instance.
 *
 * @author mario
 *
 */

public class InputModelCreator {

    private final IDiagramContainerUI diagramContainer;

    public InputModelCreator(final IDiagramContainerUI dc) {
        diagramContainer = dc;
    }

    /**
     * Creates a new input model instance.
     *
     * @param isDefault
     *            if parameter is true: a default model will becreated
     * @return return code which button was selected
     */
    public int createNewInputModel(final boolean isDefault) {
        final TransactionalEditingDomain domain = diagramContainer.getDiagramBehavior().getEditingDomain();
        final EList<EObject> boList = diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects();

        final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        URI uri = null;
        if (!isDefault) {
            final InputDialog dialog = new InputDialog(shell, "Create new scenario state model", "Type your model name", "", null);
            final int returnCode = dialog.open();

            if (returnCode == Window.CANCEL) {
                return Window.CANCEL;
            }

            uri = getCurrentUri().appendSegment(dialog.getValue() + ".smartgridinput");
        } else {
            // TODO clear button muss enabled werden -> schwierig -> deshalb
            // custom actions disabled
            // solange kein input modell geladen ist
            uri = getCurrentUri().appendSegment("default.smartgridinput");
            if (doesFileExist(uri.path(), shell)) {
                return Window.CANCEL;
            }
        }

        // Remove current input model
        for (int i = 0; i < boList.size(); i++) {
            if (boList.get(i) instanceof ScenarioState) {
                final int toremove = i;
                final RecordingCommand c = new RecordingCommand(domain) {
                    @Override
                    protected void doExecute() {
                        boList.remove(toremove);
                    }
                };
                domain.getCommandStack().execute(c);
            }
        }

        // creates new input model depending on the current pictogram elements
        final ResourceSet set = domain.getResourceSet();

        final URI currentUri = uri;
        final RecordingCommand c = new RecordingCommand(domain) {

            @Override
            protected void doExecute() {
                final Resource rs = set.createResource(currentUri);
                rs.setTrackingModification(true);
                final ScenarioState domainModel = SmartgridinputFactory.eINSTANCE.createScenarioState();

                for (final EObject tmp : boList) {
                    if (tmp instanceof SmartGridTopology) {
                        domainModel.setScenario((SmartGridTopology) tmp);
                    }
                }

                final EList<Shape> shapes = diagramContainer.getDiagramTypeProvider().getDiagram().getChildren();
                for (final Shape shape : shapes) {
                    final EObject obj = shape.getLink().getBusinessObjects().get(0);
                    if (obj instanceof NetworkEntity) {
                        final EntityState state = SmartgridinputFactory.eINSTANCE.createEntityState();
                        state.setOwner((NetworkEntity) obj);
                        domainModel.getEntityStates().add(state);
                    }
                    if (obj instanceof PowerGridNode) {
                        final PowerState state = SmartgridinputFactory.eINSTANCE.createPowerState();
                        state.setOwner((PowerGridNode) obj);
                        domainModel.getPowerStates().add(state);
                    }
                }

                rs.getContents().add(domainModel);
                diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects().add(domainModel);
                try {
                    rs.save(InputModelCreator.this.createSaveOptions());
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        };
        domain.getCommandStack().execute(c);
        return Window.OK;
    }

    /**
     * Retrieves the current uri of the project.
     *
     * @return the current uri
     */
    private URI getCurrentUri() {
        return diagramContainer.getDiagramEditorInput().getUri().trimSegments(1);
    }

    /**
     * Create save options.
     *
     * @return the save options
     */
    private Map<?, ?> createSaveOptions() {
        final HashMap<String, Object> saveOptions = new HashMap<>();
        saveOptions.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
        saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
        return saveOptions;
    }

    /**
     * Checks if a a default input model already exist.
     *
     * @param filePath
     *            the current file path
     * @param shell
     *            the current swt shell
     * @return true if a default model exists
     */
    private boolean doesFileExist(final String filePath, final Shell shell) {
        final File f = new File(filePath);
        if (f.exists()) {
            final MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
            messageBox.setMessage("Default input model already exists. Create a custom input model.");
            messageBox.open();
        }
        return f.exists();
    }

}
