package smartgrid.model.input.sirius.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl.BasicEMapEntry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.bindings.keys.formatting.EmacsKeyFormatter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.sirius.viewpoint.description.DAnnotation;
import org.eclipse.sirius.viewpoint.description.DescriptionFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;

import smartgridinput.ScenarioState;
import smartgrid.model.input.sirius.SInputModelCreator;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;

/**
 * This class defines a toolbar action to create new input models.
 *
 * @author mazen
 *
 */
public class SNewToolbarButtonAction extends AbstractHandler {
   
    /**
     * Creates an ImageDescriptor for a toolbar action.
     *
     * @param filename
     *            filename of the image
     * @param bundleid
     *            the current osgi plugin id
     * @return new ImageDescriptor
     */
    protected ImageDescriptor createImage(final String filename, final String bundleid) {
        final Bundle bundle = Platform.getBundle(bundleid);
        final URL fullPathString = BundleUtility.find(bundle, filename);
        final ImageDescriptor imageDcr = ImageDescriptor.createFromURL(fullPathString);
        return imageDcr;
    }
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        DDiagramEditor editor = (DDiagramEditor) part;
        DSemanticDiagram rep = (DSemanticDiagram) editor.getRepresentation();
        
        final SInputModelCreator creator = new SInputModelCreator(rep);
        URI inputUri = creator.createNewInputModel(false);
        final String uri = inputUri.toString();
        
        URI sessionResourceURI = getCurrentUri();
        Session createdSession = SessionManager.INSTANCE.getExistingSession(sessionResourceURI);
        final TransactionalEditingDomain domain = createdSession.getTransactionalEditingDomain();
        

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

       String path = "/Users/mazenebada/runtime-New_configuration(2)/Sirius/";
       path += "topology" + ((SmartGridTopology)rep.getTarget()).getId() + ".txt";
//     String path = "topology" + ((SmartGridTopology)rep.getTarget()).getId() + ".txt";
       //path += "topology.rtf";
       String uri2 = rep.getDAnnotation("attached").getDetails().get("input");
       writing(path, uri2); 
        return null;
    }

  
    public void writing(String path, String text) {
        try {
            File statText = new File(path);
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
    
    
    /**
     * Create save options.
     *
     * @return the save options
     */
    public static Map<?, ?> createSaveOptions() {
        final HashMap<String, Object> saveOptions = new HashMap<>();
        saveOptions.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
        saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
        return saveOptions;
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

