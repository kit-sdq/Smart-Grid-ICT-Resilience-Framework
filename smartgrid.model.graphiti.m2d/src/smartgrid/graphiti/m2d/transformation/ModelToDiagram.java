package smartgrid.graphiti.m2d.transformation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.AreaContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.PictogramsFactory;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import smartgrid.graphiti.m2d.layout.ZestLayoutDiagramFeature;
import smartgridsecurity.graphiti.SGSDiagramEditor;
import smartgridsecurity.graphiti.SGSDiagramTypeProvider;
import smartgridsecurity.graphiti.helper.GraphitiHelper;
import smartgridsecurity.graphiti.patterns.connections.AddPowerConnectionFeature;
import smartgridtopo.LogicalCommunication;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PhysicalConnection;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;

public final class ModelToDiagram {

	private static final Logger LOG = Logger.getLogger(ModelToDiagram.class);

	private static final String F_DIAGRAM = ".sgdiagram";

	private String pathToModel;
	private IProject project;
	private IFile diagramPathFile;
	private SGSDiagramEditor part;

	private TransactionalEditingDomain editingDomain;

	public ModelToDiagram(String pathToModel, IProject project) {
		this.pathToModel = pathToModel;
		this.project = project;

		if (pathToModel.contains(File.separator)) {
			this.pathToModel = pathToModel.replaceAll(File.separator, "/");
		}
	}

	/**
	 * Creates a diagram for the given business object
	 * 
	 * @param scenario
	 *            the business object which contents should be added to a new
	 *            diagram
	 */
	public void initializeDiagram(final SmartGridTopology scenario) {
		String diagramPath = pathToModel.substring(0, pathToModel.lastIndexOf("/") + 1);
		String diagramName = pathToModel.substring(pathToModel.lastIndexOf("/") + 1, pathToModel.lastIndexOf("."));
		final Resource diagramRes = createDiagram(diagramPath, diagramName, scenario);

		// First, start with reading and adding the nodes to the diagram
		final List<PowerGridNode> pge = new ArrayList<PowerGridNode>();
		final List<NetworkEntity> ne = new ArrayList<NetworkEntity>();
		final List<LogicalCommunication> lc = new ArrayList<LogicalCommunication>();
		final List<PhysicalConnection> pc = new ArrayList<PhysicalConnection>();

		pge.addAll(scenario.getContainsPGN());
		ne.addAll(scenario.getContainsNE());
		lc.addAll(scenario.getContainsLC());
		pc.addAll(scenario.getContainsPC());

		// Convert PGE's to pictogram elements
		CommandStack commandStack = editingDomain.getCommandStack();
		commandStack.execute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				LOG.info("[ModelToDiagram]: Adding nodes and egdes to the diagram");
				addPEs((Diagram) diagramRes.getContents().get(0), scenario);
				
				LOG.info("[ModelToDiagram]: Applying a layout to the diagram");
				ZestLayoutDiagramFeature layout = new ZestLayoutDiagramFeature(
						GraphitiHelper.getInstance().getFeatureProvider());
				layout.execute(null);
				try {
					diagramRes.save(createSaveOptions());

				} catch (IOException e) {
					LOG.error("[ModelToDiagram]: Saving new diagram went wrong, see StackTrace");
					LOG.error(e.getMessage());
					e.printStackTrace();
				}

				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor((IEditorPart) part,
						false);
			}
		});
	}

	private Resource createDiagram(final String diagramPath, final String diagramName,
			final SmartGridTopology scenario) {
		LOG.info("[ModelToDiagram]: Creating diagram...");
		
		final URI diagramURI = URI.createPlatformResourceURI(
				project.getFullPath().toString() + "/" + diagramPath + diagramName + F_DIAGRAM, true);
		IFile diagramFile = project.getFile(diagramPath + diagramName + F_DIAGRAM);

		try {
			if (diagramFile.exists()) {
				diagramFile.delete(true, null);
			}

		} catch (CoreException e) {
			LOG.error("[ModelToDiagram]: Failed to delete old diagram. Path is "
					+ diagramFile.getFullPath().toOSString());
			e.printStackTrace();
		}

		editingDomain = GraphitiUi.getEmfService().createResourceSetAndEditingDomain();
		ResourceSet resourceSet = editingDomain.getResourceSet();
		CommandStack commandStack = editingDomain.getCommandStack();

		final Resource diagramResource = resourceSet.createResource(diagramURI);

		commandStack.execute(new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				// create resources for the diagram and domain model files

				diagramResource.setTrackingModification(true);
				final Diagram diagram = Graphiti.getPeCreateService().createDiagram(diagramName + hashCode(),
						diagramName, 10, true);
				diagram.setDiagramTypeId("SmartGridSecurityDiagramType");
				// link model and diagram
				PictogramLink link = PictogramsFactory.eINSTANCE.createPictogramLink();
				link.setPictogramElement(diagram);
				link.getBusinessObjects().add(scenario);
				diagramResource.getContents().add(diagram);

			}
		});

		try {
			diagramResource.save(createSaveOptions());
		} catch (IOException e) {
			LOG.error("[ModelToDiagram]: Saving new diagram went wrong, see StackTrace");
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
		GraphitiHelper.getInstance().setDiagram((Diagram) diagramResource.getContents().get((0)));
		Diagram diagram = GraphitiHelper.getInstance().getDiagram();
		diagramPathFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(diagramResource.getURI().toPlatformString(true)));

		// Adding relevant objects to the graphiti helper
		SGSDiagramTypeProvider provider = new SGSDiagramTypeProvider();

		try {
			BasicNewResourceWizard.selectAndReveal(diagramPathFile,
					PlatformUI.getWorkbench().getActiveWorkbenchWindow());
			part = (SGSDiagramEditor) IDE
					.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), diagramPathFile);

			provider.init(diagram, part.getDiagramBehavior());

			GraphitiHelper.getInstance().setFeatureProvider(provider.getFeatureProvider());
			GraphitiHelper.getInstance().setDiagramContainer(part);

		} catch (PartInitException e1) {
			e1.printStackTrace();
			LOG.error("[ModelToDiagram]: Could not assign new diagram to editor part. Message is " + e1.getMessage());
		}

		LOG.info("[ModelToDiagram]: Finished creating diagram");
		return diagramResource;
	}

	/**
	 * Create save options.
	 * 
	 * @return the save options
	 */
	private Map<?, ?> createSaveOptions() {
		HashMap<String, Object> saveOptions = new HashMap<String, Object>();
		saveOptions.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
		saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
		return saveOptions;
	}

	private void addPEs(final Diagram diagram, SmartGridTopology scenario) {
		LOG.debug("[ModelToDiagram]: Adding PowerGridNodes to the diagram");
		for (PowerGridNode power : scenario.getContainsPGN()) {
			addElements(diagram, power);
		}
		LOG.debug("[ModelToDiagram]: Adding NetworkEntities to the diagram");
		for (NetworkEntity network : scenario.getContainsNE()) {
			addElements(diagram, network);
			if (!network.getConnectedTo().isEmpty()) {
				addNetworkEntityWithPowerConnections(diagram, getPeFromBusiness(diagram, network),
						network.getConnectedTo());
			}
		}
		LOG.debug("[ModelToDiagram]: Adding PhysicalConnections to the diagram");
		for (PhysicalConnection connection : scenario.getContainsPC()) {
			addConnection(diagram, connection);

		}
		LOG.debug("[ModelToDiagram]: Adding LogicalCommunications to the diagram");
		for (LogicalCommunication communication : scenario.getContainsLC()) {
			addConnection(diagram, communication);
		}
	}

	private void addNetworkEntityWithPowerConnections(final Diagram diagram, final Shape entity,
			final List<PowerGridNode> connections) {

		CommandStack commandStack = editingDomain.getCommandStack();
		commandStack.execute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {

				for (Shape shape : diagram.getChildren()) {
					if (shape.getLink().getBusinessObjects().get(0) instanceof PowerGridNode) {
						PowerGridNode power = (PowerGridNode) shape.getLink().getBusinessObjects().get(0);
						for (PowerGridNode compare : connections) {
							if (power.getId() == compare.getId()) {

								AddConnectionContext connContext = new AddConnectionContext(entity.getAnchors().get(0),
										shape.getAnchors().get(0));

								// In this case the AddPowerConnectionFeature
								// has to be used to
								// directly add a power connection to the
								// diagram since
								// addIfPossible(context) cannot now the
								// businessObject
								// "PowerConnection"
								AddPowerConnectionFeature feature = new AddPowerConnectionFeature(
										GraphitiHelper.getInstance().getFeatureProvider());
								feature.add(connContext).setVisible(true);
								break;
							}
						}
					}
				}
			}

		});
	}

	private void addElements(final Diagram diagram, Object newObject) {
		AreaContext area = createAreaContext();
		final AddContext add = new AddContext(area, newObject);
		add.setTargetContainer(diagram);
		add.setNewObject(newObject);

		CommandStack commandStack = editingDomain.getCommandStack();
		commandStack.execute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				GraphitiHelper.getInstance().getFeatureProvider().addIfPossible(add);
			}

		});
	}

	/*
	 * Only call this method with connect either instance of PhysicalConnection
	 * or LogicalCommunication
	 */
	private void addConnection(final Diagram diagram, final Object connect) {
		Shape first = null;
		Shape second = null;

		// PhysicalConnection and LogicalCommunication don't have a common
		// basis, getLinks() returns
		// either a NetworkEntity or a CommunicationEntity. In our case the
		// outcome doesn't matter
		// but the cast is still necessary
		if (connect instanceof PhysicalConnection) {
			first = getPeFromBusiness(diagram, ((PhysicalConnection) connect).getLinks().get(0));
			second = getPeFromBusiness(diagram, ((PhysicalConnection) connect).getLinks().get(1));
		} else {
			first = getPeFromBusiness(diagram, ((LogicalCommunication) connect).getLinks().get(0));
			second = getPeFromBusiness(diagram, ((LogicalCommunication) connect).getLinks().get(1));
		}
		final Anchor firstAnchor = first.getAnchors().get(0);
		final Anchor secondAnchor = second.getAnchors().get(0);

		CommandStack commandStack = editingDomain.getCommandStack();
		commandStack.execute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				AddConnectionContext conn = new AddConnectionContext(firstAnchor, secondAnchor);
				conn.setNewObject(connect);
				conn.setTargetContainer(diagram);
				GraphitiHelper.getInstance().getFeatureProvider().addIfPossible(conn);
			}
		});

	}

	/*
	 * Returns the shape corresponding to the given business object
	 */
	private Shape getPeFromBusiness(Diagram d, Object business) {
		Shape result = null;
		for (Shape shape : d.getChildren()) {
			if (shape.getLink().getBusinessObjects().get(0).equals(business)) {
				result = shape;
				break;
			}
		}

		return result;
	}

	/*
	 * Creates a simple AreaContext. In further development, a graphic algorithm
	 * could be plugged in
	 */
	private AreaContext createAreaContext() {

		AreaContext area = new AreaContext();
		area.setLocation(100, 50);

		return area;
	}
}
