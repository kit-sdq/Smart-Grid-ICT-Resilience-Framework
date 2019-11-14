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
import org.eclipse.ui.PlatformUI;

import smartgrid.model.helper.output.LoadOutputModelConformityHelper;
import smartgridoutput.EntityState;
import smartgridoutput.ScenarioResult;
import smartgridtopo.NetworkEntity;
import smartgridtopo.SmartGridTopology;

public final class ScenarioResultHelper {

    private static DDiagramEditor editor;
	private static Session session;

	public static ScenarioResult getAndCheckScnearioResult(SmartGridTopology topo) {
	    initalizeEditor();
		ScenarioResult result = null;

		String attachedOutputID = "";
        
        if (getAttachedOutput() == null || getAttachedOutput().equals("")) {
            return null;
        } else {
            attachedOutputID = getAttachedOutput();
        }
		
		Collection<Resource> coll = session.getSemanticResources();
		Iterator<Resource> it = coll.iterator();
		boolean found = false;
		while (it.hasNext()) {
            Resource sr = it.next();
            if (sr.getContents().get(0) instanceof ScenarioResult) {
                result = (ScenarioResult) sr.getContents().get(0);
                if (result.getId().equals(attachedOutputID)) {
                    found = true;
                    break;          
                }
            }
        }
        if (found == false || result == null || !LoadOutputModelConformityHelper.checkOutputModelConformitySimple(result, topo)) {
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
	
	
   private static void initalizeEditor() {
        // Needs sync execution, otherwise editor can not be found
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                        .getActiveEditor();
                DDiagramEditor deditor = (DDiagramEditor) part;
                editor = deditor;
                session = deditor.getSession();

            }
        });
        
   }

    public static String getAttachedOutput() {
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
