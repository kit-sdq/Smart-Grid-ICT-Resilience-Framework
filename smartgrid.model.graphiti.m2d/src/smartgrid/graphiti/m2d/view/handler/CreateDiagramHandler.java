package smartgrid.graphiti.m2d.view.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.File;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import smartgrid.graphiti.m2d.transformation.ModelToDiagram;
import smartgridtopo.SmartGridTopology;

@SuppressWarnings("restriction")
public class CreateDiagramHandler extends AbstractHandler {

    public CreateDiagramHandler() {
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);
        File emfFile = (File) selection.getFirstElement();
        ResourceSet impl = new ResourceSetImpl();

        Resource resource = impl.getResource(URI.createFileURI(emfFile.getFullPath().toString()), true);

        new ModelToDiagram(emfFile.getProjectRelativePath().toString(), emfFile.getProject())
                .initializeDiagram((SmartGridTopology) resource.getContents().get(0));
        return null;
    }

}
