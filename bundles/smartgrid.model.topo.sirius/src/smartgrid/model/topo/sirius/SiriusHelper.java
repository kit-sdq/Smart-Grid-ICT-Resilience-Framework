package smartgrid.model.topo.sirius;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import smartgrid.model.helper.input.LoadInputModelConformityHelper;
import smartgridinput.ScenarioState;
import smartgridtopo.SmartGridTopology;

public class SiriusHelper {
    private static Session session;
    
    public static ScenarioState getAndCheckScnearioState(SmartGridTopology topo) {
        String attachedInputID = "";
        
        if (getAttachedInput() == null || getAttachedInput().equals("")) {
            return null;
        } else {
            attachedInputID = getAttachedInput();
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
                if (state.getId().equals(attachedInputID)) {
                    found = true;
                    break;          
                }
            }
        }
        if (found == false || state == null || !LoadInputModelConformityHelper.checkInputModelConformitySimple(state, topo)) {
            return null;
        }
        return state;
    }
    
    public static String getAttachedInput() {
        try {
            IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            DDiagramEditor editor = (DDiagramEditor) part;
            DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
            String returnString = rep.getDAnnotation("attached").getDetails().get("input");
            
            return returnString;
        } catch (Exception e) {
            return null;
        }
       
    }
    
}
