package smartgrid.model.input.sirius.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

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
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import smartgrid.mode.input.sirius.help.SiriusHelper;
import smartgrid.model.helper.input.LoadInputModelConformityHelper;
import smartgridinput.ScenarioState;
import smartgridtopo.SmartGridTopology;


public class GenerateOutput extends AbstractHandler{
    
    private static Session session;
    
    public GenerateOutput() {
       
    }
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
        SmartGridTopology topology = (SmartGridTopology) rep.getTarget();
        ScenarioState scenarioState = getAndCheckScnearioState(topology);
        String inputPath = getInputPath(scenarioState,topology);
        
        //String inputPath = "/Users/mazenebada/runtime-New_configuration(2)/Sirius/My.smartgridinput";
        
        String topoPath = "";
        try {
            topoPath = getTopoPath(topology);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //String topoPath="/Users/mazenebada/runtime-New_configuration(2)/Sirius/My.smartgridtopo";
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
                        URI newUri = URI.createURI(newPath);
                        File newFile = new File(newUri.toString());
                        String path = newFile.getAbsolutePath();
                        IWorkspace wokspace = ResourcesPlugin.getWorkspace();
                        File workspaceDirectory = wokspace.getRoot().getLocation().toFile();
                        String pString = workspaceDirectory.getPath();
                        String retString = findFile(newPath, workspaceDirectory);
                        return retString;
                    } else {
                        String uriString = uri.toString();
                        uriString=uriString;
                        String newPath = "/";
                        String pathSegments[]= uri.toString().split("/");
                        for (int i=1; i<pathSegments.length; i++) {
                            if (i!=pathSegments.length-1)
                                newPath += pathSegments[i] + "/" ;
                            else 
                                newPath += pathSegments[i];
                        }
                        newPath = newPath;
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
                    URI newUri = URI.createURI(newPath);
                    
                    
                    File newFile = new File(newUri.toString());
                    String path = newFile.getPath();
                    String path22 = newFile.getCanonicalPath();
                    Path sw = Paths.get(path);
                    String path2 = sw.toAbsolutePath().toString();
                    Path patha = FileSystems.getDefault().getPath(".");
                    String temp = System.getProperty("user.dir");
                    Path paths = FileSystems.getDefault().getPath("My.smartgridtopo");
                    File currentDir = new File("").getAbsoluteFile();
                    String pathaa = currentDir.getAbsolutePath();
                    Path pathss = FileSystems.getDefault().getPath(".");
                    Path pathaas = FileSystems.getDefault().getPath("My.smartgridtopo").toAbsolutePath();
                    String pathcurrentDir= System.getProperty("eclipse.launcher");
                    IWorkspace workspace = ResourcesPlugin.getWorkspace();
                    File workspaceDirectory = workspace.getRoot().getLocation().toFile();
                    String pString = workspaceDirectory.getPath();
                    String retString = findFile(newPath, workspaceDirectory);
                    
                    return retString;
                }
            }
           
        }
        
        return null;
    }

    public ScenarioState getAndCheckScnearioState(SmartGridTopology topo) {
        String outString = "";
        try {
            String path = "/Users/mazenebada/runtime-New_configuration(2)/Sirius/";
            path += "topology" + topo.getId() + ".txt"; 
            Scanner in = new Scanner(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            while(in.hasNext()) {
                sb.append(in.next());
            }
            in.close();
            outString = sb.toString();
        } catch (FileNotFoundException e) {
            
        }
        if (getAttachedInput() == null || getAttachedInput().equals("")) {
            return null;
        } else {
            outString = getAttachedInput();
        }
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
        boolean found = false;
        while (it.hasNext()) {
            Resource sr = it.next();
            if (sr.getContents().get(0) instanceof ScenarioState) {
                state = (ScenarioState) sr.getContents().get(0);
                if (LoadInputModelConformityHelper.checkInputModelConformity(state, topo)) {
                    String resourceUriString = sr.getURI().toString();
                    System.out.println(resourceUriString);
                    System.out.println(outString.replace("//", "/"));
                    if (!outString.equals("") && (resourceUriString.equals(outString.replace("//", "/")) || resourceUriString.endsWith(outString.replace("//", "/")))) {
                    found = true;
                    break;
                    }
                }
            }
        }
        if (state == null || !LoadInputModelConformityHelper.checkInputModelConformitySimple(state, topo)) {
            return null;
        }
        return state;
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
        //if uri.toFileString().be
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
