package smartgrid.model.input.sirius;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import com.oracle.tools.packager.IOUtils;

import smartgrid.model.helper.input.LoadInputModelConformityHelper;
import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;

public final class ScenarioStateHelper {

	private static Session session;

	private ScenarioStateHelper() {
	}

	public static ScenarioState getAndCheckScnearioState(SmartGridTopology topo) {
	    String outString = "";
	    
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
				    if (!outString.equals("") && ( resourceUriString.split("/")[0].contains("platform") && outString.contains(newPath)) ||
				            resourceUriString.equals(outString.replace("//", "/")) || resourceUriString.endsWith(outString.replace("//", "/"))) {
				    found = true;
					break;
				    }
				}
			}
		}
		if (found == false || state == null || !LoadInputModelConformityHelper.checkInputModelConformitySimple(state, topo)) {
			return null;
		}
		return state;
	}

	public static PowerState getCorrectPowerState(PowerGridNode node) {
		ScenarioState state = ScenarioStateHelper.getAndCheckScnearioState((SmartGridTopology) node.eContainer());
		EList<PowerState> powerS = state.getPowerStates();
		PowerState required = null;

		for (PowerState ps : powerS) {
			if (ps.getOwner().getId() == node.getId()) {
				required = ps;
				break;
			}
		}
		return required;
	}

	public static EntityState getCorrectEntityState(NetworkEntity node) {
		ScenarioState state = ScenarioStateHelper.getAndCheckScnearioState((SmartGridTopology) node.eContainer());
		EList<EntityState> entityS = state.getEntityStates();
		EntityState required = null;

		for (EntityState es : entityS) {
			if (es.getOwner().getId().equals(node.getId())) {
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
	
	public static String getAttachedInput() {
	    IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
        String returnString = rep.getDAnnotation("attached").getDetails().get("input");
        
        return returnString;
	}
}
