package smartgrid.model.input.sirius;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import smartgridinput.EntityState;

public class InputDestroyer{

    public InputDestroyer() {
    }
    
    public int destroyElement(EntityState required) {
        URI sessionResourceURI = getCurrentUri();
        Session createdSession = SessionManager.INSTANCE.getExistingSession(sessionResourceURI);
        final TransactionalEditingDomain domain = createdSession.getTransactionalEditingDomain();
        
        
        final RecordingCommand c = new RecordingCommand(domain) {

            @Override
            protected void doExecute() {
                // Check whether the String has true or false in it and act accordingly
                if ((required.isIsDestroyed())
                        || (!required.isIsDestroyed())) {
                    required.setIsDestroyed(!required.isIsDestroyed());
                }
            }
            
        };
        domain.getCommandStack().execute(c);
       
        return org.eclipse.jface.window.Window.OK;
    }
        
    /**
     * Retrieves the current uri of the project.
     *
     * @return the current uri
     */
    private URI getCurrentUri() {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        String session = editor.getSession().getSessionResource().getURI().toString();
        String[] partsStrings = session.split("/");
        String returnString = "/" + partsStrings[2] + "/" + partsStrings[3]; 
              
     return URI.createPlatformResourceURI(returnString, true);
    }
    

}
