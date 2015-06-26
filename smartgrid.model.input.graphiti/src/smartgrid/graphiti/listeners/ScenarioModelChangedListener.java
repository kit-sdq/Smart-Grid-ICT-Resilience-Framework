package smartgrid.graphiti.listeners;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;

import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputFactory;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;
import smartgridtopo.Scenario;

/**
 * This listener checks if the ScenarioModel is changed. If there is an input model, the listener will create a new input model bo for every new topo bo..
 * @author mario
 *
 */
public class ScenarioModelChangedListener implements ResourceSetListener {
	private DiagramBehavior behavior;

	public ScenarioModelChangedListener(DiagramBehavior diagramBehavior) {
		behavior = diagramBehavior;
	}

	@Override
	public void resourceSetChanged(ResourceSetChangeEvent event) {
		List<Notification> notifications = event.getNotifications();
		for (final Notification notification : notifications) {
			Object notifier = notification.getNotifier();
			if (notifier instanceof Scenario && notification.getEventType() == Notification.ADD) {
				for (final EObject obj : behavior.getDiagramTypeProvider().getDiagram().getLink().getBusinessObjects()) {
					if (obj instanceof ScenarioState) {
						Runnable myRunnable = new Runnable(){

							public void run(){
								final TransactionalEditingDomain domain = behavior.getEditingDomain();
								RecordingCommand c = new RecordingCommand(domain) {
									@Override
									protected void doExecute() {
										ScenarioState scenarioState = (ScenarioState) obj;
										if (notification.getNewValue() instanceof PowerGridNode) {
											PowerState state = SmartgridinputFactory.eINSTANCE.createPowerState();
											state.setOwner((PowerGridNode) notification.getNewValue());
											scenarioState.getPowerStates().add(state);
										}
										if (notification.getNewValue() instanceof NetworkEntity) {
											EntityState state = SmartgridinputFactory.eINSTANCE.createEntityState();
											state.setOwner((NetworkEntity) notification.getNewValue());
											scenarioState.getEntityStates().add(state);
										}
									}
								};
								domain.getCommandStack().execute(c);

							}
						};
						Thread thread = new Thread(myRunnable);
						thread.start();						
					}
				}
			}
		}
	}

	@Override
	public NotificationFilter getFilter() {
		return NotificationFilter.ANY;
	}

	@Override
	public Command transactionAboutToCommit(ResourceSetChangeEvent event)
			throws RollbackException {
		return null;
	}

	@Override
	public boolean isAggregatePrecommitListener() {
		return false;
	}

	@Override
	public boolean isPrecommitOnly() {
		return false;
	}

	@Override
	public boolean isPostcommitOnly() {
		return false;
	}
	
	

}
