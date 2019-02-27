package smartgrid.model.input.sirius.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

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

import smartgridtopo.SmartGridTopology;

public class ClearInputModel extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
        
        String path = "/Users/mazenebada/runtime-New_configuration(2)/Sirius/";
        path += "topology" + ((SmartGridTopology)rep.getTarget()).getId() + ".txt";
        
        URI sessionResourceURI = getCurrentUri();
        Session createdSession = SessionManager.INSTANCE.getExistingSession(sessionResourceURI);
        final TransactionalEditingDomain domain = createdSession.getTransactionalEditingDomain();

        final String uri = "";
            final RecordingCommand c = new RecordingCommand(domain) {
                @Override
                protected void doExecute() {
                    
                    DAnnotation inputModel = rep.getDAnnotation("attached");
                    if (inputModel == null) {
                        inputModel = DescriptionFactory.eINSTANCE.createDAnnotation();
                        inputModel.setSource("attached");
                        rep.getEAnnotations().add(inputModel);
                        inputModel.getDetails().put("input", uri);
                    } else {
                        
                        if (inputModel.getDetails().containsKey("input")) {
                            inputModel.getDetails().removeKey("input");
                        }
                        inputModel.getDetails().put("input", uri);
                    }
                }
            };
            domain.getCommandStack().execute(c);
            
        writing(path, uri);
        return null;
    }

    public void writing(String path, String text) {
        try {
            //Whatever the file path is.
            File statText = new File(path);
//            if (statText.exists()) {
//                statText.delete();
//            }
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            w.write(text);
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
            e.printStackTrace();
        }
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
