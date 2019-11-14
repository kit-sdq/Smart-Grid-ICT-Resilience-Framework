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

import smartgrid.model.input.sirius.InputHacker;
import smartgrid.model.input.sirius.ScenarioStateHelper;
import smartgridinput.EntityState;
import smartgridtopo.NetworkEntity;

public class SetHacked extends AbstractHandler {

    public SetHacked() {
    }


    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        
        ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
        final NetworkEntity networkEntity =  getSelectedElement(selection);
        EntityState required = ScenarioStateHelper.getCorrectEntityState(networkEntity);
        
        InputHacker inputHacker = new InputHacker();
        inputHacker.hackElement(required);
        
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
    private NetworkEntity getSelectedElement(ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            Object selectedElement = ((IStructuredSelection) selection).getFirstElement();
            View view = (View) ((EditPart)selectedElement).getModel();
            EObject element = view.getElement();
            EObject element2 = ((DDiagramElement)element).getTarget();
            
            return (NetworkEntity)element2;
           
        }
        return null;
    }
}
