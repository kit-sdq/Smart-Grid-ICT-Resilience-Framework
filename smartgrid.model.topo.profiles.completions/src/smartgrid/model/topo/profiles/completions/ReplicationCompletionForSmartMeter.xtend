package smartgrid.model.topo.profiles.completions

import java.util.ArrayList
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EcoreUtil
import smartgridtopo.SmartGridTopology
import smartgridtopo.SmartMeter
import topoextension.ExtensionRepository
import topoextension.Replication
import topoextension.TopoextensionFactory

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
		
		val replicaList = new ArrayList<SmartMeter>()
		val smartMeter = objectToComplete as SmartMeter
		val replication = completionObject as Replication
		val smartGridTopology = smartMeter.eContainer as SmartGridTopology
		for(var i = 0; i < replication.nrOfReplicas; i++){
			val smartMeterReplica = smartMeter.duplicateSmartMeter()
			smartGridTopology.containsNE.add(smartMeterReplica) 
			replicaList.add(smartMeterReplica);
		}
		val extRepository = replication.eContainer as ExtensionRepository;
		
		//creating a new aggregation
		
		//TODO: fix this line
		val aggregation = TopoextensionFactory.eINSTANCE.createAggregation();
		
		//adding all duplicates to the aggregation
		aggregation.networkentity.addAll(replicaList);
		extRepository.aggregations.add(aggregation)
	}
	
	def SmartMeter duplicateSmartMeter(SmartMeter smartMeter) {
		//TODO: generate new IDs
		
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

