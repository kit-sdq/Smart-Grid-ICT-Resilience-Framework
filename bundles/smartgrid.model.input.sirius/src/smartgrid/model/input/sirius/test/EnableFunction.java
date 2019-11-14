package smartgrid.model.input.sirius.test;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import smartgrid.model.input.sirius.ScenarioStateHelper;
import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridtopo.ControlCenter;
import smartgridtopo.GenericController;
import smartgridtopo.InterCom;
import smartgridtopo.NetworkEntity;
import smartgridtopo.NetworkNode;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;

public class EnableFunction extends PropertyTester {

    public EnableFunction() {
    }

    @Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        
        if (property.equals("isPowerNode")) {
            ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
            if (selection instanceof IStructuredSelection) {
                Object selectedElement = ((IStructuredSelection) selection).getFirstElement();
                View view = (View) ((EditPart)selectedElement).getModel();
                EObject element = view.getElement();
                EObject element2 = ((DDiagramElement)element).getTarget();
                if (element2 instanceof PowerGridNode) {
                    if (inputLoaded())
                    return true;
                }
            } 
        }
        if (property.equals("isCommNode")) {
            ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
            if (selection instanceof IStructuredSelection) {
                Object selectedElement = ((IStructuredSelection) selection).getFirstElement();
                View view = (View) ((EditPart)selectedElement).getModel();
                EObject element = view.getElement();
                EObject element2 = ((DDiagramElement)element).getTarget();
                if (element2 instanceof SmartMeter ||
                        element2 instanceof InterCom ||
                        element2 instanceof ControlCenter ||
                        element2 instanceof GenericController) {
                    if (inputLoaded())
                    return true;
                }
            } 
        }
        if (property.equals("isNetworkNode")) {
            ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
            if (selection instanceof IStructuredSelection) {
                Object selectedElement = ((IStructuredSelection) selection).getFirstElement();
                View view = (View) ((EditPart)selectedElement).getModel();
                EObject element = view.getElement();
                EObject element2 = ((DDiagramElement)element).getTarget();
                if (element2 instanceof SmartMeter ||
                        element2 instanceof InterCom ||
                        element2 instanceof ControlCenter ||
                        element2 instanceof GenericController ||
                        element2 instanceof NetworkNode) {
                if (inputLoaded())
                    return true;
                }
            } 
        }
        if (property.equals("canGenerateOutput")) {
            if (inputLoaded()) {
                IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                DDiagramEditor editor = (DDiagramEditor) part;
                DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
                SmartGridTopology topology = (SmartGridTopology) rep.getTarget();
                ScenarioState state = ScenarioStateHelper.getAndCheckScnearioState(topology);
                if (updated(state, topology))
                    return true;
            }
        }
        return false;
        
    }
    
    private boolean inputLoaded() {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
        ScenarioState scenarioState = ScenarioStateHelper.getAndCheckScnearioState((SmartGridTopology)rep.getTarget());
        if (scenarioState != null) {
            return true;
        }
        return false;
    }
    
    private boolean updated(ScenarioState scenarioState, SmartGridTopology topology) {
        //SmartGridTopology topology = (SmartGridTopology) diagramContainer.getTarget();
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
            for (EntityState state : scenarioState.getEntityStates()) {
                String id = state.getOwner().getId();
                boolean found = false;
                for (NetworkEntity networkEntity : topology.getContainsNE()) {
                    String ownerId = networkEntity.getId();
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
            for (PowerState state : scenarioState.getPowerStates()) {
                String id = state.getOwner().getId();
                boolean found = false;
                for (PowerGridNode powerGridNode : topology.getContainsPGN()) {
                    String ownerId = powerGridNode.getId();
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
}
