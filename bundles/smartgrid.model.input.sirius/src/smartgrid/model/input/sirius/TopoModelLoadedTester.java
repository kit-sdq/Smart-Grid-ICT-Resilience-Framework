package smartgrid.model.input.sirius;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import smartgridtopo.SmartGridTopology;

public class TopoModelLoadedTester extends PropertyTester {

	public TopoModelLoadedTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		
		if (property.equals("isTopoLoaded")) {
			IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			DDiagramEditor editor = (DDiagramEditor) part;
			DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
			EObject target = rep.getTarget();
			if (target instanceof SmartGridTopology) {
				return true;
			}
		}
		return false;
	}

}
