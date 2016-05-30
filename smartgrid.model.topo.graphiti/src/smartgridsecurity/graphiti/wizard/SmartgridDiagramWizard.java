package smartgridsecurity.graphiti.wizard;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.PictogramsFactory;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import smartgridsecurity.graphiti.wizard.page.CreateNewSmartgridDiagramPage;
import smartgridtopo.SmartgridtopoFactory;

/**
 * Wizard used to create smartgrid diagrams.
 *
 */
public class SmartgridDiagramWizard extends Wizard implements INewWizard {
    private static final Logger LOG = Logger.getLogger(SmartgridDiagramWizard.class);

    private ISelection selection;
    private final String editorId;
    private final IWorkspace workspace;
    private final CreateNewSmartgridDiagramPage newDiagramPage;
    private final IWorkbench workbench;

    public SmartgridDiagramWizard() {
        this.newDiagramPage = new CreateNewSmartgridDiagramPage();
        this.workspace = ResourcesPlugin.getWorkspace();
        this.editorId = "SmartGridSecurityDiagramType";
        this.workbench = PlatformUI.getWorkbench();
    }

    @Override
    public void init(final IWorkbench workbench, final IStructuredSelection selection) {
        this.selection = selection;
        this.setNeedsProgressMonitor(true);
    }

    @Override
    public void addPages() {
        super.addPages();
        this.addPage(this.newDiagramPage);
    }

    @Override
    public boolean performFinish() {
        final String fileName = this.newDiagramPage.getFileName();
        final URI uri = URI.createPlatformResourceURI(this.getCurrentProjectPath() + "/" + fileName + ".sgdiagram",
                true);
        final URI modelUri = URI
                .createPlatformResourceURI(this.getCurrentProjectPath() + "/" + fileName + ".smartgridtopo", true);

        // create diagram and open it
        final IRunnableWithProgress op = new WorkspaceModifyOperation(null) {

            @Override
            protected void execute(final IProgressMonitor monitor) throws CoreException, InterruptedException {
                final Resource diagramResource = SmartgridDiagramWizard.this.createDiagram(uri, fileName, modelUri,
                        monitor);
                if (diagramResource != null && SmartgridDiagramWizard.this.editorId != null) {
                    try {
                        SmartgridDiagramWizard.this.openDiagram(diagramResource);
                    } catch (final PartInitException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        };

        try {
            this.getContainer().run(false, true, op);
        } catch (final InterruptedException exception) {
            exception.printStackTrace();
            return false;
        } catch (final InvocationTargetException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Creates the diagram
     *
     * @param diagramURI
     *            the diagram uri
     * @param name
     *            the diagram name
     * @param modelURI
     *            the model uri
     * @param progressMonitor
     *            the process monitor
     * @return the created diagram
     */
    private Resource createDiagram(final URI diagramURI, final String name, final URI modelURI,
            final IProgressMonitor progressMonitor) {
        progressMonitor.beginTask("Creating diagram and model files", 2);
        final TransactionalEditingDomain editingDomain = GraphitiUi.getEmfService().createResourceSetAndEditingDomain();
        final ResourceSet resourceSet = editingDomain.getResourceSet();
        final CommandStack commandStack = editingDomain.getCommandStack();
        // create resources for the diagram and domain model files
        final Resource diagramResource = resourceSet.createResource(diagramURI);
        final Resource modelResource = resourceSet.createResource(modelURI);
        if (diagramResource != null && modelResource != null) {
            commandStack.execute(new RecordingCommand(editingDomain) {
                @Override
                protected void doExecute() {
                    // create diagram and business models
                    SmartgridDiagramWizard.this.createModel(diagramResource, name, modelResource,
                            modelURI.lastSegment());
                }
            });
        }
        progressMonitor.worked(1);
        try {
            // save models
            modelResource.save(createSaveOptions());
            diagramResource.save(createSaveOptions());
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        progressMonitor.done();
        return diagramResource;
    }

    /**
     * Create save options.
     *
     * @return the save options
     */
    public static Map<?, ?> createSaveOptions() {
        final HashMap<String, Object> saveOptions = new HashMap<String, Object>();
        saveOptions.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
        saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
        return saveOptions;
    }

    /**
     * Opens the created diagram
     *
     * @param diagramResource
     *            the diagram resource
     * @throws PartInitException
     *             if diagram could not be opened
     */
    private void openDiagram(final Resource diagramResource) throws PartInitException {
        final IFile file = this.workspace.getRoot().getFile(new Path(diagramResource.getURI().toPlatformString(true)));
        this.selectAndReveal(file);
        this.openFileToEdit(this.getShell(), file);
    }

    /**
     * Creates diagram and business models
     *
     * @param diagramResource
     *            the diagram resource
     * @param diagramName
     *            the diagram file name
     * @param modelResource
     *            the model resource
     * @param modelName
     *            the model name
     */
    private void createModel(final Resource diagramResource, final String diagramName, final Resource modelResource,
            final String modelName) {
        // create model resource
        modelResource.setTrackingModification(true);
        final EObject domainModel = SmartgridtopoFactory.eINSTANCE.createSmartGridTopology();
        modelResource.getContents().add(domainModel);

        // create diagram resource
        diagramResource.setTrackingModification(true);
        final Diagram diagram = Graphiti.getPeCreateService().createDiagram(diagramName + this.hashCode(), diagramName,
                10, true);
        diagram.setDiagramTypeId(this.editorId);

        // link model and diagram
        final PictogramLink link = PictogramsFactory.eINSTANCE.createPictogramLink();
        link.setPictogramElement(diagram);
        link.getBusinessObjects().add(domainModel);
        diagramResource.getContents().add(diagram);
    }

    /**
     * Returns the the path of the current selected project. This code was taken from NewDiagramPage
     * class of spray.
     *
     * @return the path
     */
    private String getCurrentProjectPath() {
        if (this.selection == null || this.selection.isEmpty() || !(this.selection instanceof IStructuredSelection)) {
            return null;
        }
        final IStructuredSelection ssel = (IStructuredSelection) this.selection;
        if (ssel.size() > 1) {
            return null;
        }
        final Object obj = ssel.getFirstElement();
        if (!(obj instanceof IAdaptable)) {
            return null;
        }

        IResource resource = null;
        resource = ((IAdaptable) obj).getAdapter(IResource.class);
        IContainer container;
        if (resource instanceof IContainer) {
            container = (IContainer) resource;
        } else {
            container = resource.getParent();
        }
        return container.getFullPath().toString();
    }

    public void selectAndReveal(final IFile file) {
        BasicNewResourceWizard.selectAndReveal(file, this.workbench.getActiveWorkbenchWindow());
    }

    /**
     * @param shell
     *            the parent shell. May not be <code>null</code>
     * @param file
     *            that should be selected. May not be <code>null</code>.
     */
    public void openFileToEdit(final Shell shell, final IFile file) {
        shell.getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                final IWorkbenchPage page = SmartgridDiagramWizard.this.workbench.getActiveWorkbenchWindow()
                        .getActivePage();
                try {
                    IDE.openEditor(page, file, true);
                } catch (final PartInitException e) {
                    LOG.error("[SmartgridDiagramWizard]: Throws PartInitException, message is: " + e.getMessage());
                }
            }
        });
    }

}
