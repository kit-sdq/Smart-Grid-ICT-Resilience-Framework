package smartgrid.model.topo.sirius;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.componentization.ViewpointRegistry;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import smartgrid.model.helper.input.LoadInputModelConformityHelper;
import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputFactory;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;

public class CheckInput implements IExternalJavaAction {

    private static Session session;
    
    public CheckInput() {
    }

    @Override
    public boolean canExecute(Collection<? extends EObject> selections) {
        return true;
    }

    @Override
    public void execute(Collection<? extends EObject> selections, Map<String, Object> parameters) {
        EObject eObject =  selections.iterator().next() ;
        SmartGridTopology topology1 = (SmartGridTopology) eObject;
        LinkedList<SmartGridTopology> resulTopologies = getTopologies();
        resulTopologies.add(topology1);
        for ( SmartGridTopology topology : resulTopologies) {
//            ScenarioState scenarioState = getAndCheckScnearioState(topology);
//            if (scenarioState != null) {
//              updateInput(topology, scenarioState);
//            }
            LinkedList<ScenarioState> states = getAndCheckAllScnearioState(topology);
            if (states != null) {
                for ( ScenarioState scenarioState : states) {
                    updateInput(topology, scenarioState);
                }
            }
        }

    }
    
    protected void updateInput(SmartGridTopology topology, ScenarioState scenarioState) {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
        
        URI sessionResourceURI = getCurrentUri();
        Session createdSession = SessionManager.INSTANCE.getExistingSession(sessionResourceURI);
        final TransactionalEditingDomain domain = createdSession.getTransactionalEditingDomain();

        String uriSegments[]= sessionResourceURI.toString().split("/");
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
                
                for (final NetworkEntity obj : topology.getContainsNE()) {
                            final EntityState state = SmartgridinputFactory.eINSTANCE.createEntityState();
                            state.setOwner((NetworkEntity) obj);
                            String id = ((NetworkEntity) obj).getId();
                            boolean exists = false;
                            EntityState oldStateTemp = null;
                            for (EntityState oldState : scenarioState.getEntityStates()) {
                                if (id.equals(oldState.getOwner().getId())) {
                                    exists = true;
                                    oldStateTemp = oldState;
                                }
                            }
                            
                            if (exists)
                                domainModel.getEntityStates().add(oldStateTemp);
                            else 
                                domainModel.getEntityStates().add(state);
                 }
                for (final PowerGridNode obj : topology.getContainsPGN()) {
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
                

                rs.getContents().add(domainModel);
                try {
                    rs.save(CheckInput.this.createSaveOptions());

                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            domain.getCommandStack().execute(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    

    public String findFile(String name,File file)
    {
        String returnString = "";
        File[] list = file.listFiles();
        if(list!=null)
        for (File fil : list)
        {
            if (fil.isDirectory())
            {
                returnString = findFile(name,fil);
                if (returnString != "")
                    break;
            }
            else if (fil.getPath().endsWith(name))
            {
                returnString = fil.getPath();
                break;
            }
        }
        return returnString;
    }
    
    /**
     * Retrieves the current uri of the project.
     *
     * @return the current uri
     */
    private URI getCurrentUri() {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        Collection<Viewpoint> selectedViewpoints = session.getSelectedViewpoints(false);
        final Set<Viewpoint> registry = ViewpointRegistry.getInstance().getViewpoints();
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
    
    protected LinkedList<SmartGridTopology> getTopologies(){
        LinkedList<SmartGridTopology> resulTopologies = new LinkedList<SmartGridTopology>();
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
//        resulTopologies.add((SmartGridTopology)rep.getTarget());
        final EList<DDiagramElement> boList = rep.getDiagramElements();
        for (final EObject tmp : boList) {
            if (tmp instanceof SmartGridTopology) {
                resulTopologies.add((SmartGridTopology) tmp);
            }
        }
        return resulTopologies;
    }

    public ScenarioState getAndCheckScnearioState(SmartGridTopology topo) {
        ScenarioState state = null;

        // Needs sync execution, otherwise editor can not be found
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                        .getActiveEditor();
                DDiagramEditor editor = (DDiagramEditor) part;
                session = editor.getSession();
            }
        });

        Collection<Resource> coll = session.getSemanticResources();
        Iterator<Resource> it = coll.iterator();
        while (it.hasNext()) {
            Resource sr = it.next();
            if (sr.getContents().get(0) instanceof ScenarioState) {
                state = (ScenarioState) sr.getContents().get(0);
                if (LoadInputModelConformityHelper.checkInputModelConformity(state, topo)) {
                    break;
                }
            }
        }
        if (state == null || !LoadInputModelConformityHelper.checkInputModelConformitySimple(state, topo)) {
            return null;
        }
        return state;
    }
    
    private URI getInputPath(ScenarioState state, SmartGridTopology topology) {

        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        EList<Resource> objects2 = editor.getSession().getSessionResource().getResourceSet().getResources();
        
        URI sessionResourceURI = getCurrentUri();
        Session createdSession = SessionManager.INSTANCE.getExistingSession(sessionResourceURI);

        for(Resource resource : createdSession.getSemanticResources()) {
            URI uri = resource.getURI();
            if (!objects2.contains(resource)) {
                boolean found = false;
                for (Resource objectsResource : objects2){
                    if (objectsResource.getURI().toString().equals(uri))
                        found = true;
                }
                if (!found) {
                //objects2.add(resource);
                }
            }
        }
        
        for (Resource resource : objects2) {
            URI uri = resource.getURI();
            if (uri.toString().contains("User")) {
                String temp = uri.toString();
                uri=uri;
            }
            
            if (uri.toString().endsWith(".smartgridinput")) {
                if (!resource.getContents().isEmpty()){
                    EList<EObject> objects = resource.getContents();
                    ScenarioState state2 = (ScenarioState)objects.get(0);
                    if (state2 == state && state2.getScenario().getId().equals(topology.getId())) {
                        String pathSegments[]= uri.toString().split("/");
                        String newPath = "/";
                        int start = 2;
                        for (int i=start; i<pathSegments.length; i++) {
                            if (i>start)
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
           
        }
        
        return null;
    }
    
    private URI getURI(ScenarioState state, SmartGridTopology topology) {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        EList<Resource> objects2 = editor.getSession().getSessionResource().getResourceSet().getResources();
        for (Resource resource : objects2) {
            URI uri = resource.getURI();
            if (uri.toString().endsWith(".smartgridinput")) {
                EList<EObject> objects = resource.getContents();
                ScenarioState state2 = (ScenarioState)objects.get(0);
                if (state2 == state && state2.getScenario().getId().equals(topology.getId())) {
                    return uri;
                }
            }
        }
        return null;
    }
    public LinkedList<ScenarioState> getAndCheckAllScnearioState(SmartGridTopology topo) {
        LinkedList<ScenarioState> list = new LinkedList<ScenarioState>();
        ScenarioState state = null;

        // Needs sync execution, otherwise editor can not be found
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                        .getActiveEditor();
                DDiagramEditor editor = (DDiagramEditor) part;
                session = editor.getSession();
            }
        });

        Collection<Resource> coll = session.getSemanticResources();
        Iterator<Resource> it = coll.iterator();
        while (it.hasNext()) {
            Resource sr = it.next();
            if (sr.getContents().get(0) instanceof ScenarioState) {
                state = (ScenarioState) sr.getContents().get(0);
                if (LoadInputModelConformityHelper.checkInputModelConformitySimple(state, topo)) {
                    list.add(state);
                }
            }
        }
        if (state == null || !LoadInputModelConformityHelper.checkInputModelConformitySimple(state, topo)) {
            return null;
        }
        return list;
    }

}
