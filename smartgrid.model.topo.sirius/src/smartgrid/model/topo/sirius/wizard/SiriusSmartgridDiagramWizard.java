package smartgrid.model.topo.sirius.wizard;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.sirius.business.api.componentization.ViewpointRegistry;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.sirius.ui.business.api.viewpoint.ViewpointSelectionCallback;
import org.eclipse.sirius.ui.tools.api.project.ModelingProjectManager;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import smartgrid.model.topo.sirius.wizard.page.SiriusCreateNewSmartgridDiagramPage;
import smartgrid.model.topo.sirius.wizard.util.SiriusCustomUtil;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputFactory;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoFactory;

/**
 * Wizard used to create smartgrid diagrams.
 *
 */
public class SiriusSmartgridDiagramWizard extends Wizard implements INewWizard {

    private IProject project;
    private IConfigurationElement config;
    private ISelection selection;
    private final String editorId;
    private final IWorkspace workspace;
    private final IWorkbench workbench;
    private Resource classModelResource;
    private Resource classInputResource;
    //protected SiriusCreateNewSmartgridDiagramPage newDiagramPage;
    protected WizardNewProjectCreationPage newProjectPage;
    protected SiriusCreateNewSmartgridDiagramPage newTopologyPage;
    protected SmartGridTopology topology;
    
    public SiriusSmartgridDiagramWizard() {
        super();
        setNeedsProgressMonitor(true);
        workspace = ResourcesPlugin.getWorkspace();
        editorId = "SmartGridSecurityDiagramType";
        workbench = PlatformUI.getWorkbench();
    }
    
    @Override
    public void addPages() {
        
        newProjectPage = new WizardNewProjectCreationPage("new Smartgrid Sirius Project");
        this.newProjectPage.setDescription("Create a new Smartgrid Sirius project.");
        this.newProjectPage.setTitle("New Smartgrid Sirius Project");
        
        newTopologyPage = new SiriusCreateNewSmartgridDiagramPage();
        
        addPage(newProjectPage);
        addPage(newTopologyPage);
    }
    
    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
        setNeedsProgressMonitor(true);
    }

    @Override
    public boolean performFinish() {
        final String topologyNameString = this.newTopologyPage.getTopologyName();
        
        final IProject projectHandle = this.newProjectPage.getProjectHandle();
        final java.net.URI projectURI = (!this.newProjectPage.useDefaults())
                ? this.newProjectPage.getLocationURI() : null;
        final IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final IProjectDescription desc = workspace.newProjectDescription(projectHandle.getName());
        desc.setLocationURI(projectURI);
        
        /*
         * Creating the project encapsulated in a workspace operation
         */
        final WorkspaceModifyOperation op = new WorkspaceModifyOperation() {

            @Override
            protected void execute(final IProgressMonitor monitor) throws CoreException {
                SiriusSmartgridDiagramWizard.this.project = createProject(desc, projectHandle, monitor);
            }
        };
        
        /*
         * This isn't as robust as the code in the BasicNewProjectResourceWizard class. Consider
         * beefing this up to improve error handling.
         */
        try {
            getContainer().run(true, true, op);
        } catch (final Exception e) {
            MessageDialog.openError(getShell(), "Error", "An unexpected error occured. See stack trace");
            e.printStackTrace();
            return false;
        }

        if (this.project == null) {
            return false;
        }
        
        BasicNewProjectResourceWizard.updatePerspective(this.config);
        BasicNewProjectResourceWizard.selectAndReveal(this.project, this.workbench.getActiveWorkbenchWindow());



      ////////////////////////////////////////////////////

        final WorkspaceModifyOperation op2 = new WorkspaceModifyOperation() {
            @Override
            protected void execute(final IProgressMonitor monitor) throws CoreException {
                final URI modelUri = URI.createPlatformResourceURI(project.getName() + "/" + topologyNameString + ".smartgridtopo", true);
                final URI inputUri = URI.createPlatformResourceURI(project.getName() + "/" + topologyNameString + ".smartgridinput", true);
                final URI representationsURI = SiriusCustomUtil.getRepresentationsURI(projectHandle);
                final Session session = SessionManager.INSTANCE.getSession(representationsURI, monitor);
                final TransactionalEditingDomain editingDomain = session.getTransactionalEditingDomain();
                final ResourceSet resourceSet = editingDomain.getResourceSet();
                final Resource modelResource = resourceSet.createResource(modelUri);
                classModelResource = modelResource;
                final Resource inputResource = resourceSet.createResource(inputUri);
                classInputResource = inputResource;
                final CommandStack commandStack = editingDomain.getCommandStack();
                commandStack.execute(new RecordingCommand(editingDomain) {
                    @Override
                    protected void doExecute() {
                        // create diagram and business models
                        SiriusSmartgridDiagramWizard.this.topology = createModel(topologyNameString, modelResource, modelUri.lastSegment(),inputResource, inputUri.lastSegment());
                    }
                });
                monitor.worked(1);
                try {
                    // save models
                    modelResource.save(createSaveOptions());
                    inputResource.save(createSaveOptions());
                } catch (final IOException exception) {
                    exception.printStackTrace();
                }
                monitor.done();
                activateViewpoints(projectHandle, SubMonitor.convert(monitor, "Activating Viewpoints", 2000));
            }
        };

        try {
            getContainer().run(false, true, op2);
        } catch (final InterruptedException exception) {
            exception.printStackTrace();
            return false;
        } catch (final InvocationTargetException exception) {
            exception.printStackTrace();
            return false;
        }
        

        ////////////////////////////////////////////////////

        
        final WorkspaceModifyOperation op3 = new WorkspaceModifyOperation() {

            @Override
            protected void execute(final IProgressMonitor monitor) throws CoreException {
                final URI representationsURI = SiriusCustomUtil.getRepresentationsURI(projectHandle);
                final Session session = SessionManager.INSTANCE.getExistingSession(representationsURI);
                final TransactionalEditingDomain editingDomain = session.getTransactionalEditingDomain();
                
                final CommandStack commandStack = editingDomain.getCommandStack();
                commandStack.execute(new RecordingCommand(editingDomain) {
                    @Override
                    protected void doExecute() {
                        final Set<Viewpoint> registry = ViewpointRegistry.getInstance().getViewpoints();
                        for (Viewpoint vp : registry){
                            String vpName = vp.getName();
                            if (vpName.equalsIgnoreCase("topology") || vpName.toLowerCase().contains("smartgridinput"))
                                new ViewpointSelectionCallback().selectViewpoint(vp, session, monitor);
                        }   
                        final URI inputUri = URI.createPlatformResourceURI(project.getName() + "/" + topologyNameString + ".smartgridinput", true);
                        assignInputToModel();
                    }
                    
                    
                });
                
                
                activateViewpoints(projectHandle, SubMonitor.convert(monitor, "Activating Viewpoints", 2000));
            }
        };
        

        try {
            getContainer().run(false, true, op3);
        } catch (final InterruptedException exception) {
            exception.printStackTrace();
            return false;
        } catch (final InvocationTargetException exception) {
            exception.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    private SmartGridTopology createModel( final String diagramName, final Resource modelResource, final String modelName
            ,final Resource inputResource, final String inputName) {
        // create model resource
        modelResource.setTrackingModification(true);
        inputResource.setTrackingModification(true);
        final EObject domainModel = SmartgridtopoFactory.eINSTANCE.createSmartGridTopology();
        final ScenarioState domainInput = SmartgridinputFactory.eINSTANCE.createScenarioState();
        domainInput.setScenario((SmartGridTopology)domainModel);
        modelResource.getContents().add(domainModel);
        inputResource.getContents().add(domainInput);
        
        return (SmartGridTopology)domainModel;

    }
    /**
     * This creates the project in the workspace.
     * 
     * @param description
     *            The description to set for the project.
     * @param projectHandle
     * @param monitor
     * @throws CoreException
     * @throws OperationCanceledException
     */
    private IProject createProject(final IProjectDescription description, final IProject projectHandle,
            final IProgressMonitor monitor) throws CoreException, OperationCanceledException {
        try {
            monitor.beginTask("Creating Project", 8000);
            createAndOpenProject(description, projectHandle, SubMonitor.convert(monitor, "Main Task", 2000));
            convertToModelingProject(projectHandle,
                    SubMonitor.convert(monitor, "Converting to Modeling Project", 2000));
            activateViewpoints(projectHandle, SubMonitor.convert(monitor, "Activating Viewpoints", 2000));
        } finally {
            monitor.done();
        }
        return projectHandle;
    }
    
    private void createAndOpenProject(final IProjectDescription description, final IProject projectHandle,
            final SubMonitor subMonitor) throws CoreException {
        projectHandle.create(description, subMonitor.split(1000));
        if (subMonitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        projectHandle.open(IResource.BACKGROUND_REFRESH, subMonitor.split(1000));
    }
  
    /**
     * Convert to modeling project.
     */
    private void convertToModelingProject(final IProject projectHandle, final SubMonitor subMonitor)
            throws CoreException {
        ModelingProjectManager.INSTANCE.convertToModelingProject(projectHandle, subMonitor);
    }
    
    /**
     * Activate viewpoints.
     */
    private void activateViewpoints(final IProject projectHandle, final SubMonitor subMonitor) {
        final URI representationsURI = SiriusCustomUtil.getRepresentationsURI(projectHandle);
        final Session session = SessionManager.INSTANCE.getSession(representationsURI, subMonitor);
        final Set<Viewpoint> registry = ViewpointRegistry.getInstance().getViewpoints();
        final HashSet<Viewpoint> viewpoints = new HashSet<>();
        final List<String> extensions = getExtensions(session);
        for (final Viewpoint viewpoint : registry) {
            final String ext = viewpoint.getModelFileExtension();
            if (extensions.contains(ext)) {
                viewpoints.add(viewpoint);
            }
        }
        SiriusCustomUtil.selectViewpoints(session, viewpoints, true, subMonitor);
    }
    
    
    private List<String> getExtensions(final Session session) {
        final List<String> extensions = new ArrayList<>();
        for (final Resource r : session.getSemanticResources()) {
            if (r.getClass().getPackage().getName().startsWith("org.palladiosimulator.pcm.")) {
                if (r.getURI().isPlatform()) {
                    extensions.add(r.getURI().fileExtension());
                }
            }
        }
        return extensions;
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
    
    private void assignInputToModel() {
        Resource topoResource = classModelResource;
        TreeIterator<EObject> part = topoResource.getAllContents();
        ResourceSet part2 = topoResource.getResourceSet();
        part2=part2;
        
            
    }

    
}
