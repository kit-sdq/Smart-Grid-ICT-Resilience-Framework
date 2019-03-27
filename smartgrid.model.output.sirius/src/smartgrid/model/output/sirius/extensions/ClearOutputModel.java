package smartgrid.model.output.sirius.extensions;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.sirius.viewpoint.description.DAnnotation;
import org.eclipse.sirius.viewpoint.description.DescriptionFactory;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

public class ClearOutputModel extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
        
        
        URI sessionResourceURI = getCurrentUri();
        Session createdSession = SessionManager.INSTANCE.getExistingSession(sessionResourceURI);
        final TransactionalEditingDomain domain = createdSession.getTransactionalEditingDomain();

        final String emptyID = "";
            final RecordingCommand c = new RecordingCommand(domain) {
                @Override
                protected void doExecute() {
                    
                    DAnnotation outputModel = rep.getDAnnotation("attached");
                    if (outputModel == null) {
                        outputModel = DescriptionFactory.eINSTANCE.createDAnnotation();
                        outputModel.setSource("attached");
                        rep.getEAnnotations().add(outputModel);
                        outputModel.getDetails().put("output", emptyID);
                    } else {
                        if (outputModel.getDetails().containsKey("output")) {
                            outputModel.getDetails().removeKey("output");
                        }
                        outputModel.getDetails().put("output", emptyID);
                    }
                }
            };
            domain.getCommandStack().execute(c);
        return null;
    }

    
    
    private URI getCurrentUri() {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        String session = editor.getSession().getSessionResource().getURI().toString();
        String[] partsStrings = session.split("/");
        String returnString = "/" + partsStrings[2] + "/" + partsStrings[3]; 
              
     return URI.createPlatformResourceURI(returnString, true);
    }
}
