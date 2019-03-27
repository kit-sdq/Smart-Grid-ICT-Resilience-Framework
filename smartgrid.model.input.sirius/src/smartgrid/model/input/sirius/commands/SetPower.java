package smartgrid.model.input.sirius.commands;



import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.*;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.ui.PlatformUI;

import smartgrid.model.input.sirius.InputPowerChanger;
import smartgrid.model.input.sirius.ScenarioStateHelper;
import smartgridinput.PowerState;
import smartgridtopo.PowerGridNode;

public class SetPower extends AbstractHandler {

    public SetPower() {
    }


    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        
        ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
        final PowerGridNode powerGridNode =  getSelectedElement(selection);
        PowerState required = ScenarioStateHelper.getCorrectPowerState(powerGridNode);
        
        InputPowerChanger inputPowerChanger = new InputPowerChanger();
        inputPowerChanger.setPower(required);
        
        return null;
    }
    

    /**
     * Get the current selected resource from selection.
     *
     * @param selection
     *            the current selection
     * @return the selected resource if there is one, or <code>null</code>
     *         otherwise
     */
    private PowerGridNode getSelectedElement(ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            Object selectedElement = ((IStructuredSelection) selection).getFirstElement();
            View view = (View) ((EditPart)selectedElement).getModel();
            EObject element = view.getElement();
            EObject element2 = ((DDiagramElement)element).getTarget();
            
            return (PowerGridNode)element2;
           
        }
        return null;
    }
}
