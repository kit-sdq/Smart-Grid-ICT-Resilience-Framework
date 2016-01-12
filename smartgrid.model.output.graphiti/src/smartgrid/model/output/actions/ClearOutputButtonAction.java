package smartgrid.model.output.actions;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.editor.IDiagramContainerUI;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import smartgrid.model.output.StringProvider;
import smartgrid.model.output.features.ManageNodeAppearances;
import smartgridoutput.NoUplink;
import smartgridoutput.Online;
import smartgridoutput.ScenarioResult;
import smartgridsecurity.graphiti.actions.ToolbarButtonAction;
import smartgridtopo.NetworkEntity;

/**
 * This class defines a toolbar action. If this action is executed, the output
 * model will be reverted.
 * 
 * @author mario
 *
 */
public class ClearOutputButtonAction extends ToolbarButtonAction implements IPropertyChangeListener {

	private static final Logger LOG = Logger.getLogger(ClearOutputButtonAction.class);

	public ClearOutputButtonAction() {
		super(AS_CHECK_BOX);
		ACTION_ID = "EnableOutputButtonActionId";
		TOOL_TIP = "Clear the State Result representation";
		setToolTipText(TOOL_TIP);
		setId(ACTION_ID);
		addPropertyChangeListener(this);
		setImageDescriptor(createImage("icons" + File.separator + "clear_output.png", "smartgrid.model.output"));
	}

	@Override
	public void setDiagramContainer(IDiagramContainerUI container) {
		diagramContainer = container;
		checkOutputModel();
	}

	/**
	 * This method checks if there is an output model. If true, the action will
	 * be enabled.
	 */
	private void checkOutputModel() {
		EList<EObject> list = diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects();
		for (EObject model : list) {
			if (model instanceof ScenarioResult) {
				setEnabled(true);
				return;
			}
		}
		setEnabled(false);
	}

	@Override
	public void run() {
		LOG.info("[ClearOutputButtonAction]: Start clearing output model from diagram");
		final TransactionalEditingDomain domain = diagramContainer.getDiagramBehavior().getEditingDomain();
		RecordingCommand c = new RecordingCommand(domain) {
			@Override
			protected void doExecute() {
				ScenarioResult result = null;
				EObject[] objects = (EObject[]) diagramContainer.getDiagramTypeProvider().getDiagram().getLink()
						.getBusinessObjects().toArray();
				for (int j = 0; j < objects.length; j++) {
					if (objects[j] instanceof ScenarioResult) {
						result = (ScenarioResult) objects[j];
					}
				}

				ManageNodeAppearances manager = new ManageNodeAppearances(diagramContainer);
				// copy list to array -> otherwise you'll get a concurrency
				// exception
				Shape[] shapes = (Shape[]) diagramContainer.getDiagramTypeProvider().getDiagram().getChildren()
						.toArray();
				LOG.debug("[ClearOutputButtonAction]: Clearing all nodes");
				for (int i = 0; i < shapes.length; i++) {
					Shape currentShape = shapes[i];

					// Clears the diagram from all output entities
					if ((currentShape.getLink().getBusinessObjects().get(0) instanceof NetworkEntity)) {
						currentShape.setVisible(true);
						manager.manageGraphicalPatternRepresentation((ContainerShape) currentShape, true);
						Object bo = manager.resolveBOfromNetworkEntity(
								(NetworkEntity) currentShape.getLink().getBusinessObjects().get(0), result.getStates());

						if (bo != null
								&& (bo instanceof NoUplink || (bo instanceof Online && ((Online) bo).isIsHacked()))) {
							manager.removeChildren((ContainerShape) currentShape);
						}
					}
				}
				diagramContainer.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects().remove(result);

			}
		};
		domain.getCommandStack().execute(c);
		setEnabled(false);
		LOG.info("[ClearOutputButtonAction]: Clearing successfully finished");
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(StringProvider.ENABLE_CLEAR_BUTTON)) {
			setEnabled(true);
		}
	}
}
