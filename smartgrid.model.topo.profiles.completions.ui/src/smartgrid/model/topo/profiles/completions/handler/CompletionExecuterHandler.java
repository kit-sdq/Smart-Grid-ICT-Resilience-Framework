package smartgrid.model.topo.profiles.completions.handler;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import smartgrid.model.topo.profiles.completions.SmartGridCompletionExecuter;
import smartgridtopo.SmartGridTopology;
import tools.vitruv.framework.util.bridges.EMFBridge;
 
public class CompletionExecuterHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IResource iResource = getResourceFromEvent(event);
		IFile copiedResource = copyIResource(iResource);

		SmartGridTopology smartGridTopology = loadSmartGridResource(copiedResource);
		
		SmartGridCompletionExecuter smartGridCompletionExecuter = new SmartGridCompletionExecuter();
		smartGridCompletionExecuter.executeCompletions(smartGridTopology);
		
		saveEObject(smartGridTopology);

		return null;
	}

	private void saveEObject(SmartGridTopology smartGridTopology) {
		try {
			smartGridTopology.eResource().save(null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private SmartGridTopology loadSmartGridResource(IFile copiedResource) {
		final URI uri = EMFBridge.getEMFPlatformUriForIResource(copiedResource);
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(uri);
		try {
			resource.load(null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (0 == resource.getContents().size() || !(resource.getContents().get(0) instanceof SmartGridTopology)) {
			throw new RuntimeException(
					"Resource " + resource + " is either empty or the root object is not a SmartGridTopology ");
		}
		return (SmartGridTopology) resource.getContents().get(0);
	}

	private IFile copyIResource(IResource resource) throws ExecutionException {
		IPath newPath = (IPath) resource.getFullPath();
		newPath = newPath.removeFileExtension();
		String newLastSegment = newPath.lastSegment() + "_completed.smartgridtopo";
		newPath = newPath.removeLastSegments(1);
		newPath = newPath.append(newLastSegment);

		try {
			resource.copy(newPath, true, null);
			
		} catch (final CoreException e1) {
			throw new ExecutionException("Could copy the resource model.");
		}
		return resource.getProject().getWorkspace().getRoot().getFile(newPath);
	}

	private IResource getResourceFromEvent(ExecutionEvent event) {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

		final Object firstElement = structuredSelection.getFirstElement();
		if (!(firstElement instanceof IResource)) {
			throw new RuntimeException("Selected element is not an IResource");
		}
		return (IResource) firstElement;
	}

}
