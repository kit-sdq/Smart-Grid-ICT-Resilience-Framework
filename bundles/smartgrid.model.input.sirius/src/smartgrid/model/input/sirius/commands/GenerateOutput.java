package smartgrid.model.input.sirius.commands;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import smartgrid.mode.input.sirius.help.SiriusHelper;
import smartgrid.model.input.sirius.ScenarioStateHelper;
import smartgridinput.ScenarioState;
import smartgridtopo.SmartGridTopology;


public class GenerateOutput extends AbstractHandler{
    
    
    public GenerateOutput() {
       
    }
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
        SmartGridTopology topology = (SmartGridTopology) rep.getTarget();
        ScenarioState scenarioState = ScenarioStateHelper.getAndCheckScnearioState(topology);
        String inputPath = getInputPath(scenarioState,topology);
        
        
        String topoPath = "";
        try {
            topoPath = getTopoPath(topology);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        String outputPath=new File(inputPath).getParent() + "/";
        try {
            SiriusHelper.generateOutput(inputPath, topoPath, outputPath);
        } catch (CoreException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private String getInputPath(ScenarioState state, SmartGridTopology topology) {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        EList<Resource> objects2 = editor.getSession().getSessionResource().getResourceSet().getResources();
        for (Resource resource : objects2) {
            URI uri = resource.getURI();
            if (uri.toString().endsWith(".smartgridinput")) {
                EList<EObject> objects = resource.getContents();
                ScenarioState state2 = (ScenarioState)objects.get(0);
                if (state2 == state && state2.getScenario().getId().equals(topology.getId())) {
                    if (!isDependency(state, topology)) {
                        String pathSegments[]= uri.toString().split("/");
                        String newPath = "";
                        for (int i=2; i<pathSegments.length; i++) {
                            if (i==2)
                                newPath += pathSegments[i];
                            else
                                newPath += "/"+pathSegments[i];
                        }
                        IWorkspace wokspace = ResourcesPlugin.getWorkspace();
                        File workspaceDirectory = wokspace.getRoot().getLocation().toFile();
                        String retString = findFile(newPath, workspaceDirectory);
                        return retString;
                    } else {
                        String newPath = "/";
                        String pathSegments[]= uri.toString().split("/");
                        for (int i=1; i<pathSegments.length; i++) {
                            if (i!=pathSegments.length-1)
                                newPath += pathSegments[i] + "/" ;
                            else 
                                newPath += pathSegments[i];
                        }
                        return newPath; 
                    }
                }
            }
        }
        return null;
    }
    
    private String getTopoPath(SmartGridTopology topology) throws IOException {

        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        EList<Resource> objects2 = editor.getSession().getSessionResource().getResourceSet().getResources();
        for (Resource resource : objects2) {
            URI uri = resource.getURI();
            if (uri.toString().endsWith(".smartgridtopo")) {
                EList<EObject> objects = resource.getContents();
                SmartGridTopology topology2 = (SmartGridTopology)objects.get(0);
                if (topology == topology2) {
                    String pathSegments[]= uri.toString().split("/");
                    String newPath = "";
                    for (int i=2; i<pathSegments.length; i++) {
                        if (i==2)
                            newPath += pathSegments[i];
                        else
                            newPath += "/"+pathSegments[i];
                    }                
                    IWorkspace workspace = ResourcesPlugin.getWorkspace();
                    File workspaceDirectory = workspace.getRoot().getLocation().toFile();
                    String retString = findFile(newPath, workspaceDirectory);
                    
                    return retString;
                }
            }
           
        }
        
        return null;
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
    
    
    private boolean isDependency(ScenarioState scenarioState, SmartGridTopology smartGridTopology) {
        URI uri = getURI(scenarioState, smartGridTopology);
        if (!uri.scheme().equalsIgnoreCase("platform")) {
            return true;
        }
        return false;
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
    
    public static String getAttachedInput() {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
        String returnString = rep.getDAnnotation("attached").getDetails().get("input");
        
        return returnString;
    }
}
