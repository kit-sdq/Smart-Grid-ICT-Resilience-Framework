package smartgrid.model.output.sirius.extensions;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;

import smartgrid.model.helper.sirius.LoadExtensionModel;
import smartgridoutput.ScenarioResult;
import smartgridoutput.SmartgridoutputPackage;
import smartgridtopo.SmartGridTopology;

public class LoadOutputModel extends LoadExtensionModel {

	private Session session;

	public LoadOutputModel() {
	}

	@Override
	protected void setFileDialogExtension() {
		dialog.setFilterExtensions(new String[] { "*.smartgridoutput" });
		dialog.setFilterNames(new String[] { "Output Model" });
	}

	/**
	 * Should only be enabled if and when a scenario state is loaded
	 */
	@Override
	public boolean isEnabled() {
	    if (true)
	        return true;
		boolean result = false;
		if (Display.getDefault() != null) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
				    IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			        DDiagramEditor editor = (DDiagramEditor) part;
					session = editor.getSession();
				}
			});

			Collection<Resource> coll = session.getSemanticResources();
			Iterator<Resource> it = coll.iterator();
			while (it.hasNext()) {
				Resource sr = it.next();
				if (sr.getContents().get(0) instanceof ScenarioResult) {
					result = true;
					break;
				}
			}
			if (!result) {
				return false;
			}
		}

//		return super.isEnabled();
		return false;
	}
	
	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        dialog = new FileDialog(shell, SWT.OPEN);
        final ScenarioResult result;
        setFileDialogExtension();

        final String fileSelected = dialog.open();
        String ScenarioTopologieId = "";
        boolean inWorkSpace = false;
        String path="";
        String uriString="";
        if (fileSelected != null) {
            try {
                ScenarioResult uploadedScenarioResult = loadOutput(fileSelected);
                result=uploadedScenarioResult;
                ScenarioTopologieId = uploadedScenarioResult.getScenario().getId();
                if (!ScenarioTopologieId.equals(getCurrentId())) {
                    MessageDialog.openError(shell, "Error", "The chosen Output doesn't conform to this topologie");
                    return null;
                }
                IWorkspace wokspace = ResourcesPlugin.getWorkspace();
                File workspaceDirectory = wokspace.getRoot().getLocation().toFile();
                String pString = workspaceDirectory.getPath();

                if (fileSelected.contains(pString)) { //means that it is in the workspace
                    uriString = getOutputPath(uploadedScenarioResult, uploadedScenarioResult.getScenario(),fileSelected);
                    inWorkSpace = true;
                }
            } catch (Exception e) {
                MessageDialog.openError(shell, "Error", "Couldn't load the output file");
                return null;
            }
            if (!inWorkSpace) {
            File src = new File(fileSelected);

            IWorkspace wokspace = ResourcesPlugin.getWorkspace();
            File workspaceDirectory = wokspace.getRoot().getLocation().toFile();
            String dest = workspaceDirectory.getPath();
            dest = dest + "/" + getCurrentProject() + "/" + src.getName();

            ResourceSet set = new ResourceSetImpl();
            final Resource r = set.createResource(URI.createFileURI(dest));

            final String dess  = dest;
            IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
            editor = (DDiagramEditor) part;
            final Session session = editor.getSession();
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
                    final ScenarioResult domainModel = result;
                    domainModel.setScenario((SmartGridTopology)((DSemanticDiagram) editor.getRepresentation()).getTarget());
                    rs.getContents().add(domainModel);
                    try {
                        rs.save(LoadOutputModel.this.createSaveOptions());
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            domain.getCommandStack().execute(cmd);
                uriString = dest;
            }
            
            
            path = "/Users/mazenebada/runtime-New_configuration(2)/Sirius/";
            path += "topology" + ScenarioTopologieId + ".txt";

            URI sessionResourceURI = getCurrentUri();
            Session createdSession = SessionManager.INSTANCE.getExistingSession(sessionResourceURI);
            final TransactionalEditingDomain domain = createdSession.getTransactionalEditingDomain();
            
            IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            DDiagramEditor editor = (DDiagramEditor) part;
            DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
            final String uri = uriString;
                final RecordingCommand c = new RecordingCommand(domain) {
                    @Override
                    protected void doExecute() {
                        
                        DAnnotation outputModel = rep.getDAnnotation("attached");
                        if (outputModel == null ) {
                            outputModel = DescriptionFactory.eINSTANCE.createDAnnotation();
                            outputModel.setSource("attached");
                            rep.getEAnnotations().add(outputModel);
                            outputModel.getDetails().put("output", uri);
                        } else {
                            
                            if (outputModel.getDetails().containsKey("output")) {
                                outputModel.getDetails().removeKey("output");
                            }
                            outputModel.getDetails().put("output", uri);
                        }
                    }
                };
                domain.getCommandStack().execute(c);
            
        }
        return null;
    }
    
    /**
     * @param path
     *            path of the ScenarioState to be used
     * @return The read ScenarioState file
     */
    protected static ScenarioResult loadOutput(final String path) {

        final ResourceSet resourceSet = new ResourceSetImpl();

        resourceSet.getPackageRegistry().put("http://www.modelversioning.org/emfprofile/application/1.1",
                EMFProfileApplicationPackage.eINSTANCE);
        resourceSet.getPackageRegistry().put("http://sdq.ipd.uka.de/smartgridoutput/1.0",
                SmartgridoutputPackage.eINSTANCE);

        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("smartgridoutput",
                new XMIResourceFactoryImpl());
        final Resource resource = resourceSet.getResource(URI.createFileURI(path), true);
        return (ScenarioResult) resource.getContents().get(0);
    }
    

    private String getCurrentId() {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
        return ((SmartGridTopology)rep.getTarget()).getId();
    }
 
    private String getOutputPath(ScenarioResult result, SmartGridTopology topology, String fileSelectedPath) {

        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        EList<Resource> objects2 = editor.getSession().getSessionResource().getResourceSet().getResources();
        for (Resource resource : objects2) {
            URI uri = resource.getURI();
            if (uri.toString().endsWith(".smartgridoutput")) {
                EList<EObject> objects = resource.getContents();
                ScenarioResult result2 = (ScenarioResult)objects.get(0);
                String path = uri.toString().replace("//", "/");
                String pathSegments[]= path.toString().split("/");
                String newPath = "/";
                for (int i=2; i<pathSegments.length; i++) {
                    if (i==2)
                        newPath += pathSegments[i];
                    else
                        newPath += "/"+pathSegments[i];
                }
                if(path.contains("platform")) {
                    path=newPath;
                }
                if (fileSelectedPath.contains(path) && result2.getScenario().getId().equals(topology.getId())) {
                    return path;
                }
            }
           
        }
        
        return null;
    }
    
    private String getCurrentProject() {
        String urlString = getCurrentUri().toString();
        String projectString = getCurrentUri().segment(1);
        return projectString;
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
