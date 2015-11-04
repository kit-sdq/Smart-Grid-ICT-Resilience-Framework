package smartgrid.graphiti.actions;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.editor.IDiagramContainerUI;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import smartgrid.graphiti.StringProvider;
import smartgrid.graphiti.customfeatures.ManageNodeAppearances;
import smartgrid.model.helper.input.LoadInputModelConformityHelper;
import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridsecurity.graphiti.actions.ToolbarButtonAction;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;

/**
 * This class defines an action which loads a specific input model.
 * 
 * @author mario
 *
 */
public class LoadToolbarButtonAction extends ToolbarButtonAction {

	private boolean loadSuccessful = true;

	public LoadToolbarButtonAction(IPropertyChangeListener listener) {
		super();
		ACTION_ID = "LoadToolbarButtonActionId";
		TOOL_TIP = "Load a State Scenario";
		setToolTipText(TOOL_TIP);
		setId(ACTION_ID);
		addPropertyChangeListener(listener);
		setImageDescriptor(createImage("icons" + File.separator + "load_input.png", "smartgrid.model.input"));
	}

	@Override
	public void setDiagramContainer(IDiagramContainerUI container) {
		diagramContainer = container;
	}

	@Override
	public void run() {

		// Retrieve the shell
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		// check if an output model is loaded
		for (EObject obj : diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects()) {
			if (!(obj instanceof ScenarioState) && !(obj instanceof SmartGridTopology)) {
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
				messageBox.setMessage("Cannot open new input model while output model is loaded");
				messageBox.open();
				return;
			}
		}

		// open file dialog
		final FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.smartgridinput" });
		dialog.setFilterNames(new String[] { "Smart Grid Input" });
		String fileSelected = dialog.open();

		if (fileSelected == null) {
			return;
		} else {
			// Perform Action, like save the file.
			System.out.println("Selected file: " + fileSelected);
			final TransactionalEditingDomain domain = diagramContainer.getDiagramBehavior().getEditingDomain();

			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap()
					.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

			// load the resource and resolve the proxies
			// TODO use resource set of diagram
			File source = new File(fileSelected);
			ResourceSet rs = new ResourceSetImpl();

			Resource r = rs.createResource(URI.createFileURI(source.getAbsolutePath()));

			try {
				r.load(null);
				EcoreUtil.resolveAll(rs);
				// r.setTrackingModification(true);
				final EObject obj = r.getContents().get(0);

				RecordingCommand cmd = new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						EList<EObject> boList = diagramContainer.getDiagramTypeProvider().getDiagram().getLink()
								.getBusinessObjects();

						// Conformity check
						for (final EObject current : boList) {
							if (current instanceof SmartGridTopology) {
								if (!(LoadInputModelConformityHelper.checkInputModelConformitySimple(
										(ScenarioState) obj, (SmartGridTopology) current))) {
									MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
									messageBox.setMessage("Input model is not conform to the current topo model");
									messageBox.open();
									loadSuccessful = false;
									return;
								}
							}
						}

						for (final EObject tmp : boList) {
							if (tmp instanceof ScenarioState) {
								TransactionalEditingDomain domain = diagramContainer.getDiagramBehavior()
										.getEditingDomain();
								RecordingCommand c = new RecordingCommand(domain) {
									@Override
									protected void doExecute() {
										diagramContainer.getDiagramTypeProvider().getDiagram().getLink()
												.getBusinessObjects().remove(tmp);
									}
								};
								domain.getCommandStack().execute(c);
								break;
							}
						}

						Shape[] shapes = (Shape[]) diagramContainer.getDiagramTypeProvider().getDiagram().getChildren()
								.toArray();

						ManageNodeAppearances manager = new ManageNodeAppearances(diagramContainer);
						for (int i = 0; i < shapes.length; i++) {
							Shape containerShape = shapes[i];

							Object o = containerShape.getLink().getBusinessObjects().get(0);

							if (o instanceof PowerGridNode) {
								Object ob = resolveBOfromPowergridNode(
										(PowerGridNode) containerShape.getLink().getBusinessObjects().get(0),
										((ScenarioState) obj).getPowerStates());

								containerShape.getGraphicsAlgorithm().setForeground(manager.manageForeground());
								if (ob instanceof PowerState && ((PowerState) ob).isPowerOutage()) {
									containerShape.getGraphicsAlgorithm().setBackground(manager.manageBackground(true));

								} else if (ob instanceof PowerState) {
									containerShape.getGraphicsAlgorithm()
											.setBackground(manager.manageBackground(false));
								}
							} else if (o instanceof NetworkEntity) {
								Object ob = resolveBOfromNetworkEntity(
										(NetworkEntity) containerShape.getLink().getBusinessObjects().get(0),
										((ScenarioState) obj).getEntityStates());
								if (ob != null && ob instanceof EntityState && ((EntityState) ob).isIsDestroyed()
										&& containerShape instanceof ContainerShape) {
									manager.drawDestroyed((ContainerShape) containerShape);
								} else if (ob != null && ob instanceof EntityState
										&& containerShape instanceof ContainerShape) {
									manager.removeChildren((ContainerShape) containerShape);
								}
							}
						}

						boList.add(obj);

					}

				};
				domain.getCommandStack().execute(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (loadSuccessful) {
			firePropertyChange(StringProvider.ENABLE_CLEAR_BUTTON, null, null);
		}
		loadSuccessful = true;
	}

	/**
	 * Resolve input bo from NetworkEnitity bo.
	 * 
	 * @param toResolve
	 *            bo to resolve
	 * @param loadedBO
	 *            lis of all input bo's
	 * @return the resolve bo
	 */
	private Object resolveBOfromNetworkEntity(NetworkEntity toResolve, EList<?> loadedBO) {
		for (Object obj : loadedBO) {
			if (obj instanceof EntityState && ((EntityState) obj).getOwner().getId() == toResolve.getId()) {
				return obj;
			}
			if (obj instanceof PowerState && ((PowerState) obj).getOwner().getId() == toResolve.getId()) {
				return obj;
			}
		}
		return null;
	}

	/**
	 * Resolve input bo from PowerGridNode bo.
	 * 
	 * @param toResolve
	 *            bo to resolve
	 * @param loadedBO
	 *            lis of all input bo's
	 * @return the resolve bo
	 */
	private Object resolveBOfromPowergridNode(PowerGridNode toResolve, EList<?> loadedBO) {
		for (Object obj : loadedBO) {
			if (obj instanceof EntityState && ((EntityState) obj).getOwner().getId() == toResolve.getId()) {
				return obj;
			}
			if (obj instanceof PowerState && ((PowerState) obj).getOwner().getId() == toResolve.getId()) {
				return obj;
			}
		}
		return null;
	}

}
