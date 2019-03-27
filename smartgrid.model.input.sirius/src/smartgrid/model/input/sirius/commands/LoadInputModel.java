package smartgrid.model.input.sirius.commands;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.sirius.viewpoint.description.DAnnotation;
import org.eclipse.sirius.viewpoint.description.DescriptionFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;

import smartgrid.model.helper.sirius.LoadExtensionModel;
import smartgrid.model.topo.sirius.CheckInput;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputPackage;
import smartgridtopo.SmartGridTopology;

public class LoadInputModel extends LoadExtensionModel {

    protected String inputID;
	@Override
	protected void setFileDialogExtension() {
		dialog.setFilterExtensions(new String[] { "*.smartgridinput" });
		dialog.setFilterNames(new String[] { "Input Model" });
	}
	
	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        dialog = new FileDialog(shell, SWT.OPEN);
        final ScenarioState state;
        setFileDialogExtension();

        final String fileSelected = dialog.open();
        String ScenarioTopologieId = "";
        boolean inWorkSpace = false;
        if (fileSelected != null) {
            try {
                ScenarioState uploadedScenarioState = loadInput(fileSelected);
                state=uploadedScenarioState;
                ScenarioTopologieId = uploadedScenarioState.getScenario().getId();
                if (!ScenarioTopologieId.equals(getCurrentId())) {
                    MessageDialog.openError(shell, "Error", "The chosen Input doesn't conform to this topologie");
                    return null;
                }
                IWorkspace wokspace = ResourcesPlugin.getWorkspace();
                File workspaceDirectory = wokspace.getRoot().getLocation().toFile();
                String pString = workspaceDirectory.getPath();

                if (fileSelected.contains(pString)) { //means that it is in the workspace
                    inWorkSpace = true;
                }
            } catch (Exception e) {
                MessageDialog.openError(shell, "Error", "Couldn't load the input file");
                return null;
            }
            if (!inWorkSpace) {
                File src = new File(fileSelected);
    
                IWorkspace wokspace = ResourcesPlugin.getWorkspace();
                File workspaceDirectory = wokspace.getRoot().getLocation().toFile();
                String dest = workspaceDirectory.getPath();
                dest = dest + "/" + getCurrentProject() + "/" + src.getName();
    
    
                final String dess  = dest;
                IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                        .getActiveEditor();
                editor = (DDiagramEditor) part;
                TransactionalEditingDomain domain = (TransactionalEditingDomain) editor.getEditingDomain();
                final RecordingCommand cmd = new RecordingCommand(domain) {
                final ResourceSet set = domain.getResourceSet();
                    @Override
                    protected void doExecute() {
                        
                        LinkedList<Resource> toDelete = new LinkedList<Resource>();
                        for ( Resource resource : set.getResources()) {
                            if (resource.getURI().equals(URI.createFileURI(dess))) {
                                toDelete.add(resource);
                            }
                        }
                        for (Resource resource : toDelete) {
                            set.getResources().remove(resource);
                        }
                        final Resource rs = set.createResource(URI.createFileURI(dess));
                        final ScenarioState domainModel = state;
                        CheckInput checkInput = new CheckInput();
                        checkInput.updateInput((SmartGridTopology)((DSemanticDiagram) editor.getRepresentation()).getTarget(), domainModel);
                        domainModel.setScenario((SmartGridTopology)((DSemanticDiagram) editor.getRepresentation()).getTarget());
                        rs.getContents().add(domainModel);
                        inputID = domainModel.getId();
                        try {
                            rs.save(LoadInputModel.this.createSaveOptions());
                        } catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                domain.getCommandStack().execute(cmd);
                    MessageDialog.openInformation(shell, null, "The input model is copied to the workspace");
            }
            
            else {
                CheckInput checkInput = new CheckInput();
                checkInput.updateInput(state.getScenario(), state);
                inputID = state.getId();
            }
            
            URI sessionResourceURI = getCurrentUri();
            Session createdSession = SessionManager.INSTANCE.getExistingSession(sessionResourceURI);
            final TransactionalEditingDomain domain = createdSession.getTransactionalEditingDomain();
            
            IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            DDiagramEditor editor = (DDiagramEditor) part;
            DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
                final RecordingCommand c = new RecordingCommand(domain) {
                    @Override
                    protected void doExecute() {
                        
                        DAnnotation inputModel = rep.getDAnnotation("attached");
                        if (inputModel == null ) {
                            inputModel = DescriptionFactory.eINSTANCE.createDAnnotation();
                            inputModel.setSource("attached");
                            rep.getEAnnotations().add(inputModel);
                            inputModel.getDetails().put("input", inputID);
                        } else {
                            
                            if (inputModel.getDetails().containsKey("input")) {
                                inputModel.getDetails().removeKey("input");
                            }
                            inputModel.getDetails().put("input", inputID);
                        }
                    }
                };
                domain.getCommandStack().execute(c);
            
        }
        return null;
    }
	
    private String getCurrentProject() {
        String projectString = getCurrentUri().segment(1);
        return projectString;
    }

    /**
     * @param path
     *            path of the ScenarioState to be used
     * @return The read ScenarioState file
     */
    protected static ScenarioState loadInput(final String path) {

        final ResourceSet resourceSet = new ResourceSetImpl();

        resourceSet.getPackageRegistry().put("http://www.modelversioning.org/emfprofile/application/1.1",
                EMFProfileApplicationPackage.eINSTANCE);
        resourceSet.getPackageRegistry().put("http://sdq.ipd.uka.de/smartgridinput/1.0",
                SmartgridinputPackage.eINSTANCE);

        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("smartgridinput",
                new XMIResourceFactoryImpl());
        final Resource resource = resourceSet.getResource(URI.createFileURI(path), true);
        return (ScenarioState) resource.getContents().get(0);
    }
    
    private String getCurrentId() {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
        return ((SmartGridTopology)rep.getTarget()).getId();
    }
    
    
    
    private URI getCurrentUri() {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        String session = editor.getSession().getSessionResource().getURI().toString();
        String[] partsStrings = session.split("/");
        String returnString = "/" + partsStrings[2] + "/" + partsStrings[3]; 
              
     return URI.createPlatformResourceURI(returnString, true);
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
}
