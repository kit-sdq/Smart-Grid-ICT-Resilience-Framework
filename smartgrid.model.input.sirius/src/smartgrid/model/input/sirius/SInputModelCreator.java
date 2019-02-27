package smartgrid.model.input.sirius;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.SelectExistingElementForSourceOperation;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputFactory;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;

public class SInputModelCreator {

    private final DSemanticDiagram diagramContainer;
    
    public SInputModelCreator(DSemanticDiagram rep) {
        this.diagramContainer = rep;
    }
    
    /**
     * Retrieves the current uri of the project.
     *
     * @return the current uri
     */
    private URI getCurrentUri() {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        String session = editor.getSession().getSessionResource().getURI().toString();
        String[] partsStrings = session.split("/");
//        
//        SmartGridTopology topology = (SmartGridTopology)diagramContainer.getTarget();
//        String nameeditor=topology.getName();
//        String string = diagramContainer.getUid();
//        String string2 = diagramContainer.getDocumentation();
//        String string3 = diagramContainer.getDescription().getRootExpression();
//        String string4 = diagramContainer.getDescription().toString();
//        IWorkbench string5 = PlatformUI.getWorkbench();
//        IWorkbenchWindow window = string5.getActiveWorkbenchWindow();
//        IWorkbenchPage page = window.getActivePage();
        
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
    
    /**
     * Creates a new input model instance.
     *
     * @param isDefault
     *            if parameter is true: a default model will becreated
     * @return return code which button was selected
     */
    public URI createNewInputModel(final boolean isDefault) {
        URI sessionResourceURI = getCurrentUri();
        Session createdSession = SessionManager.INSTANCE.getExistingSession(sessionResourceURI);
        final TransactionalEditingDomain domain = createdSession.getTransactionalEditingDomain();
        //final TransactionalEditingDomain domain = diagramContainer.getDiagramBehavior().getEditingDomain();
        final EList<DDiagramElement> boList = diagramContainer.getDiagramElements();
        getCurrentUri();
        final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        URI uri = null;
        if (!isDefault) {
            final InputDialog dialog = new InputDialog(shell, "Create new scenario state model", "Type your model name", "", null);
            final int returnCode = dialog.open();

            if (returnCode == Window.CANCEL) {
                return null;
            }
            
            String uriSegments[]= sessionResourceURI.toString().split("/");
            String path = "/";
            for (int i=2 ; i < uriSegments.length - 1; i++) {
                path += uriSegments[i] + "/";
            }
            uri = URI.createURI(path).appendSegment(dialog.getValue() + ".smartgridinput");
        } else {
            // TODO clear button muss enabled werden -> schwierig -> deshalb custom actions disabled
            // solange kein input modell geladen ist
            String uriSegments[]= sessionResourceURI.toString().split("/");
            uri = URI.createURI("/"+uriSegments[2]+"/").appendSegment("default.smartgridinput");
            if (doesFileExist(uri.path(), shell)) {
                return null;
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
                LinkedList<Resource> toDelete = new LinkedList<Resource>();
                for ( Resource resource : set.getResources()) {
                    if (resource.getURI().equals(currentUri)) {
                        toDelete.add(resource);
                    }
                }
                for (Resource resource : toDelete) {
                    set.getResources().remove(resource);
                }
                final Resource rs = set.createResource(currentUri);
                
                rs.setTrackingModification(true);
                final ScenarioState domainModel = SmartgridinputFactory.eINSTANCE.createScenarioState();

                for (final EObject tmp : boList) {
                    if (tmp instanceof SmartGridTopology) {
                        domainModel.setScenario((SmartGridTopology) tmp);
                    }
                }
                SmartGridTopology topology = (SmartGridTopology)diagramContainer.getTarget();
                domainModel.setScenario(topology);
                
                final EList<DDiagramElement> dDiagramElements = diagramContainer.getOwnedDiagramElements();
                for (final DDiagramElement dDiagramElement : dDiagramElements) {
                    if ( dDiagramElement.getSemanticElements().size()>0) {
                        final EObject obj = dDiagramElement.getSemanticElements().get(0);
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
                }

                rs.getContents().add(domainModel);
                IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                DDiagramEditor editor = (DDiagramEditor) part;
//                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().get
//                editor.
//                diagramContainer.
//                diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects().add(domainModel);
                try {
                    rs.save(SInputModelCreator.this.createSaveOptions());
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        };
        domain.getCommandStack().execute(c);
        return uri;
    }
    
//    /**
//     * Creates a new input model instance.
//     *
//     * @param isDefault
//     *            if parameter is true: a default model will becreated
//     * @return return code which button was selected
//     */
//    public int createNewInputModel1(final boolean isDefault) {
//        //Session session = SessionManager.INSTANCE.getExistingSession(URI.createURI("/Users/mazenebada/runtime-Sirius10102018/Sirius"));
//        //final TransactionalEditingDomain domain = session.getTransactionalEditingDomain();
//        //final TransactionalEditingDomain domain = diagramContainer.getDiagramBehavior().getEditingDomain();
//        final EList<DDiagramElement> boList = diagramContainer.getDiagramElements();
//
//        final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//        URI uri = null;
//        if (!isDefault) {
//            final InputDialog dialog = new InputDialog(shell, "Create new scenario state model", "Type your model name", "", null);
//            final int returnCode = dialog.open();
//
//            if (returnCode == Window.CANCEL) {
//                return Window.CANCEL;
//            }
//
//            uri = URI.createURI("/Sirius/").appendSegment(dialog.getValue() + ".smartgridinput");
//        } else {
//            // TODO clear button muss enabled werden -> schwierig -> deshalb custom actions disabled
//            // solange kein input modell geladen ist
//            uri = URI.createURI("/Sirius/").appendSegment("default.smartgridinput");
//            if (doesFileExist(uri.path(), shell)) {
//                return Window.CANCEL;
//            }
//        }
//
//        // creates new input model depending on the current pictogram elements
//
//        final URI currentUri = uri;
//        final ScenarioState domainModel = SmartgridinputFactory.eINSTANCE.createScenarioState();
//        for (final EObject tmp : boList) {
//            if (tmp instanceof SmartGridTopology) {
//                domainModel.setScenario((SmartGridTopology) tmp);
//            }
//        }
//
//        final EList<DDiagramElement> dDiagramElements = diagramContainer.getOwnedDiagramElements();
//        for (final DDiagramElement dDiagramElement : dDiagramElements) {
//            final EObject obj = dDiagramElement.getSemanticElements().get(0);
//            if (obj instanceof NetworkEntity) {
//                final EntityState state = SmartgridinputFactory.eINSTANCE.createEntityState();
//                state.setOwner((NetworkEntity) obj);
//                domainModel.getEntityStates().add(state);
//            }
//            if (obj instanceof PowerGridNode) {
//                final PowerState state = SmartgridinputFactory.eINSTANCE.createPowerState();
//                state.setOwner((PowerGridNode) obj);
//                domainModel.getPowerStates().add(state);
//            }
//        }
//
////            
//
//        
//        return Window.OK;
//    }
    
   

}
