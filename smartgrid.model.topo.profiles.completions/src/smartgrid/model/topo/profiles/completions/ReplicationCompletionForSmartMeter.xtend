package smartgrid.model.topo.profiles.completions

import org.eclipse.emf.ecore.util.EcoreUtil
import smartgridtopo.SmartGridTopology
import smartgridtopo.SmartMeter
import topoextension.Replication
import org.eclipse.emf.ecore.EObject

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
		//TODO: also duplicate references to the smart meter
		//TODO: generate new IDs
		return EcoreUtil.copy(smartMeter)
	}
	
}

