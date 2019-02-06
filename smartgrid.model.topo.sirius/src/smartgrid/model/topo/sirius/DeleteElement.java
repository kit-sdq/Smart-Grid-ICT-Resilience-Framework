package smartgrid.model.topo.sirius;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import smartgridinput.ScenarioState;
import smartgridtopo.SmartGridTopology;

public class DeleteElement implements IExternalJavaAction {

    public DeleteElement() {
    }

    @Override
    public boolean canExecute(Collection<? extends EObject> arg0) {
        return true;
    }

    @Override
    public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        SmartGridTopology topology = (SmartGridTopology)((DSemanticDiagram) editor.getRepresentation()).getTarget();
        CheckInput checkInput = new CheckInput();
        LinkedList<SmartGridTopology> resulTopologies = checkInput.getTopologies();
        resulTopologies.add(topology);
        for ( SmartGridTopology topology1 : resulTopologies) {
            LinkedList<ScenarioState> states = checkInput.getAndCheckAllScnearioState(topology);
            if (states != null) {
                for ( ScenarioState scenarioState : states) {
                    checkInput.updateInput(topology, scenarioState);
                }
            }
        }

    }

}
