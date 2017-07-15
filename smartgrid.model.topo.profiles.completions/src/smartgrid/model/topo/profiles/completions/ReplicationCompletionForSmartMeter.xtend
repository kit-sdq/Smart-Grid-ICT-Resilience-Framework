package smartgrid.model.topo.profiles.completions

import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EcoreUtil
import smartgridtopo.SmartGridTopology
import smartgridtopo.SmartMeter
import topoextension.Replication

class ReplicationCompletionForSmartMeter extends AbstractCompletion<SmartMeter, Replication> {
	
	private static val String REPLICATION_STEREOTYPE_NAME = "Replication"
	private static val String REPLICATION_REFERENCE_NAME = "replications" 
	
	public new(){
		super(SmartMeter, Replication)
	}
	
	override getStereotypeName(){
		return REPLICATION_STEREOTYPE_NAME
	}
	
	override getReferenceName() {
		return REPLICATION_REFERENCE_NAME
	}
	
	
	override executeCompletion(EObject objectToComplete, EObject completionObject) {
		val smartMeter = objectToComplete as SmartMeter
		val replication = completionObject as Replication
		val smartGridTopology = smartMeter.eContainer as SmartGridTopology
		for(var i = 0; i < replication.nrOfReplicas; i++){
			val smartMeterReplica = smartMeter.duplicateSmartMeter()
			smartGridTopology.containsNE.add(smartMeterReplica) 
		}
	}
	
	def SmartMeter duplicateSmartMeter(SmartMeter smartMeter) {
		//TODO: generate new IDs
		//TODO: reference to original aggregation SmartMeter
		
		val clone = EcoreUtil.copy(smartMeter) as SmartMeter;
		for (element : smartMeter.connectedTo) {
			clone.connectedTo.add(element);
			//if connectedTo is bidirectional adding the other direction is missing
		}
		for (element : smartMeter.linkedBy) {
			clone.linkedBy.add(element)
			element.links.add(clone);
		}
		for (element : smartMeter.communicatesBy) {
			clone.communicatesBy.add(element);
			element.links.add(clone);
		}
		return clone;
	}
	
}

