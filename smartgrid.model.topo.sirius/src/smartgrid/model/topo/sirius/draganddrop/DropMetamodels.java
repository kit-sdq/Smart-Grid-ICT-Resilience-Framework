package smartgrid.model.topo.sirius.draganddrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.sirius.viewpoint.DResource;
import smartgrid.helper.ScenarioModelHelper;
import smartgridinput.ScenarioState;
import smartgridtopo.SmartGridTopology;


public class DropMetamodels implements IExternalJavaAction {
    
    private final String inputExtension = "input";
    private final String[] extensions = {inputExtension};
    
    
    @Override
    public void execute(Collection<? extends EObject> selections, Map<String, Object> parameters) {
        Object element = parameters.get("element");
        if (! (element instanceof DResource))
            return;
        
        SmartGridTopology smartgridtopo = (SmartGridTopology) selections.iterator().next();
        TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(smartgridtopo);
        
        File folder = new File(((DResource) element).getPath());
        Collection<File> files = getAllInputFiles(folder);
        for (File file : files) {
            IContainer container = ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(new Path(file.getAbsolutePath()));
            URI uri = URI.createPlatformResourceURI(container.getFullPath().toString(), true);
            
            if (uri.fileExtension().equals(inputExtension)) {
                ScenarioState state = ScenarioModelHelper.loadInput(uri.toString()) ;
                String id = state.getId();
                id = id;
            }   
        }
            
    }

    private Collection<File> getAllInputFiles(File file) {
        Collection<File> files = new ArrayList<File>();
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isFile() && Arrays.asList(extensions).contains((new Path(f.getPath())).getFileExtension())) {
                    files.add(f);
                }
                else if (f.isDirectory())
                    files.addAll(getAllInputFiles(f));
            }
        }
        return files;
    }

    @Override
    public boolean canExecute(Collection<? extends EObject> selections) {
        return true;
    }
    

}