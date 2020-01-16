package smartgrid.model.input.sirius;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
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
import org.eclipse.ui.PlatformUI;

import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputFactory;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;

public class InputModelRefresher {

    private final DSemanticDiagram diagramContainer;
    
    public InputModelRefresher(DSemanticDiagram rep) {
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
    
    public void refreshInputModel(final boolean isDefault) {
            ScenarioState scenarioState = ScenarioStateHelper.getAndCheckScnearioState((SmartGridTopology)diagramContainer.getTarget());
            if (scenarioState != null) {
              if (!updated(scenarioState))
              updateInput(scenarioState);
            } else {
               createNewInputModel(isDefault);
            }
    }
    
    private boolean updated(ScenarioState scenarioState) {
        SmartGridTopology topology = (SmartGridTopology) diagramContainer.getTarget();
        if (topology.getContainsNE().size() == scenarioState.getEntityStates().size()){ 
            for (NetworkEntity networkEntity : topology.getContainsNE()) {
                String id = networkEntity.getId();
                boolean found = false;
                for (EntityState state : scenarioState.getEntityStates()) {
                    String ownerId = state.getOwner().getId();
                    if (ownerId.equals(id)) {
                        found = true;
                        break;
                    }
                }
                if (!found)
                    return false;
            }
            
        } else {
            return false;
        }
        
        if (topology.getContainsPGN().size() == scenarioState.getPowerStates().size()){
            for (PowerGridNode powerGridNode : topology.getContainsPGN()) {
                String id = powerGridNode.getId();
                boolean found = false;
                for (PowerState state : scenarioState.getPowerStates()) {
                    String ownerId = state.getOwner().getId();
                    if (ownerId.equals(id)) {
                        found = true;
                        break;
                    }
                }
                if (!found)
                    return false;
            }
            
        } else {
            return false;
        }
        return true;
    }

    private void updateInput(ScenarioState scenarioState) {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
        
        URI sessionResourceURI = getCurrentUri();
        Session createdSession = SessionManager.INSTANCE.getExistingSession(sessionResourceURI);
        final TransactionalEditingDomain domain = createdSession.getTransactionalEditingDomain();

        //String uriSegments[]= sessionResourceURI.toString().split("/");
        //URI uri = URI.createURI("/"+uriSegments[2]+"/").appendSegment("My" + ".smartgridinput");

        URI uri = getInputPath(scenarioState, (SmartGridTopology)rep.getTarget());
        
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

                SmartGridTopology topology = (SmartGridTopology)rep.getTarget();
                domainModel.setScenario(topology);

                final EList<DDiagramElement> dDiagramElements = rep.getOwnedDiagramElements();
                for (final DDiagramElement dDiagramElement : dDiagramElements) {
                   
                    if ( dDiagramElement.getSemanticElements().size()>0) {
                        final EObject obj = dDiagramElement.getSemanticElements().get(0);
                        if (obj instanceof NetworkEntity) {
                            final EntityState state = SmartgridinputFactory.eINSTANCE.createEntityState();
                            state.setOwner((NetworkEntity) obj);
                            String id = ((NetworkEntity) obj).getId();
                            boolean exists = false;
                            EntityState oldStateTemp = null;
                            for (EntityState oldState : scenarioState.getEntityStates()) {
                                if (id.equals(oldState.getOwner().getId())) {
                                    exists = true;
                                    oldStateTemp = oldState;
                                    break;
                                }
                            }
                            
                            if (exists)
                                domainModel.getEntityStates().add(oldStateTemp);
                            else 
                                domainModel.getEntityStates().add(state);
                        }
                        if (obj instanceof PowerGridNode) {
                            final PowerState state = SmartgridinputFactory.eINSTANCE.createPowerState();
                            state.setOwner((PowerGridNode) obj);
                            
                            String id = ((PowerGridNode) obj).getId();
                            boolean exists = false;
                            PowerState oldStateTemp = null;
                            for (PowerState oldState : scenarioState.getPowerStates()) {
                                if (id.equals(oldState.getOwner().getId())) {
                                    exists = true;
                                    oldStateTemp = oldState;
                                }
                            }
                            
                            if (exists)
                                domainModel.getPowerStates().add(oldStateTemp);
                            else 
                                domainModel.getPowerStates().add(state);
                        }
                    }
                }
                

                rs.getContents().add(domainModel);
                try {
                    rs.save(InputModelRefresher.this.createSaveOptions());
                    set.getResources().add(rs);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        };
        domain.getCommandStack().execute(c);
    }

    /**
     * Creates a new input model instance.
     *
     * @param isDefault
     *            if parameter is true: a default model will becreated
     * @return return code which button was selected
     */
    public void createNewInputModel(final boolean isDefault) {
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
                return ;
            }
            
            String uriSegments[]= sessionResourceURI.toString().split("/");
            uri = URI.createURI("/"+uriSegments[2]+"/").appendSegment(dialog.getValue() + ".smartgridinput");
        } else {
            String uriSegments[]= sessionResourceURI.toString().split("/");
            uri = URI.createURI("/"+uriSegments[2]+"/").appendSegment("default.smartgridinput");
            if (doesFileExist(uri.path(), shell)) {
                return;
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

                try {
                    rs.save(InputModelRefresher.this.createSaveOptions());
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        };
        domain.getCommandStack().execute(c);
    }
    

    private URI getInputPath(ScenarioState state, SmartGridTopology topology) {

        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        EList<Resource> objects2 = editor.getSession().getSessionResource().getResourceSet().getResources();
        for (Resource resource : objects2) {
            URI uri = resource.getURI();
            if (uri.toString().endsWith(".smartgridinput")) {
                EList<EObject> objects = resource.getContents();
                ScenarioState state2 = (ScenarioState)objects.get(0);
                if (state2 == state && state2.getScenario().getId().equals(topology.getId())) {
                    String pathSegments[]= uri.toString().split("/");
                    String newPath = "/";
                    for (int i=2; i<pathSegments.length; i++) {
                        if (i>2)
                            newPath += "//"+pathSegments[i];
                        else {
                            newPath += pathSegments[i];
                        }
                    }
                    URI newUri = URI.createURI(newPath);
                    return newUri;
                }
            }
           
        }
        
        return null;
    }

}
