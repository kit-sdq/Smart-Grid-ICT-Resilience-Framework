package smartgrid.model.output.sirius;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import smartgrid.model.helper.output.LoadOutputModelConformityHelper;
import smartgridoutput.EntityState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.NetworkEntity;
import smartgridtopo.SmartGridTopology;

public final class ScenarioResultHelper {

	private static Session session;

	public static ScenarioResult getAndCheckScnearioResult(SmartGridTopology topo) {
		ScenarioResult result = null;
		String outString="";
		
//		if (getAttachedOutput().equals("")) {
//            return null;
//        } else if (getAttachedOutput() == null) {
//        } 
//		else {
//            outString = getAttachedOutput();
//        }
		
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
			if (sr.getContents().get(0) instanceof ScenarioResult) {
				result = (ScenarioResult) sr.getContents().get(0);
				if (LoadOutputModelConformityHelper.checkOutputModelConformity(result, topo)) {
				    String resourceUriString = sr.getURI().toString();
				    String newPath = "";
				    if (resourceUriString.split("/")[0].contains("platform")) {
                        String pathSegments[]= resourceUriString.toString().split("/");
                        for (int i=2; i<pathSegments.length; i++) {
                            if (i==2)
                                newPath += pathSegments[i];
                            else
                                newPath += "/"+pathSegments[i];
                        }
                    }
				    
				        if ((( resourceUriString.split("/")[0].contains("platform") && outString.contains(newPath)) ||
	                            resourceUriString.equals(outString.replace("//", "/")) || resourceUriString.endsWith(outString.replace("//", "/")))) {
				            break;
				        }
				    
				}
			}
		}
		if (result == null || !LoadOutputModelConformityHelper.checkOutputModelConformitySimple(result, topo)) {
			return null;
		}
		return result;
	}

	public static EntityState getCorrectEntityState(NetworkEntity node) {
		ScenarioResult result = getAndCheckScnearioResult((SmartGridTopology) node.eContainer());
		EList<EntityState> entityS = result.getStates();
		EntityState required = null;

		for (EntityState es : entityS) {
			if (es.getOwner().getId() == node.getId()) {
				required = es;
				break;
			}
		}
		return required;
	}

	public static void refreshDiagram() {
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		DDiagramEditor editor = (DDiagramEditor) part;
		DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
		//rep.refresh();
	}
	
    public static String getAttachedOutput() {
        IWorkbench part1 = PlatformUI.getWorkbench();
        IWorkbenchPage part2 = part1.getActiveWorkbenchWindow().getActivePage();
        IEditorPart part = part2.getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
        String returnString;
        try {
            returnString = rep.getDAnnotation("attached").getDetails().get("output");
        } catch (Exception e) {
            return null;
        }
        
        return returnString;
    }
}
