package smartgrid.model.topo.profiles.smartgridprofiles.api

import org.apache.log4j.Logger
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EObjectResolvingEList

import static extension org.palladiosimulator.mdsdprofiles.api.StereotypeAPI.*
import topoextension.Replication

class SmartGridStereotypesAPI {

	val private static Logger logger = Logger.getLogger(SmartGridStereotypesAPI)

	val public static String REPLICATIONS_REFERENCE_NAME = "replications"
	val public static String REPLICATION_STEREOTYPE_NAME = "Replication"

	private new() {
	}

	def public static <T> T getStereotype(EObject eObject, String referenceName, String stereotypeName, Class<T> t) {
		if(!eObject.hasStereotypeApplications){
			logger.info("EObject " + eObject + " does not contain any stereotypes" )
			return null
		}
		val Object taggedValue = eObject.getTaggedValue(referenceName, stereotypeName)
		if(!(taggedValue instanceof EObjectResolvingEList<?>)){
			logger.warn("The tagged value is not from type " + EObjectResolvingEList)	
			return null
		} 
		return ensureSingleTaggedValueFromType(taggedValue as EObjectResolvingEList<?>, eObject, t)
	}

	def public static getReplicationStereotype(EObject eObject) {
		return getStereotype(eObject, REPLICATIONS_REFERENCE_NAME, REPLICATION_STEREOTYPE_NAME, Replication)
	}

	def public static boolean hasStereotype(EObject eObject, String stereotypeName) {
		return eObject.isStereotypeApplied(stereotypeName)
	}

	def public static boolean hasReplicationStereotype(EObject eObject) {
		return eObject.isStereotypeApplied(REPLICATION_STEREOTYPE_NAME)
	}

	def private static <T> T ensureSingleTaggedValueFromType(EObjectResolvingEList<?> taggedValue, EObject eObject,
		Class<T> t) {
		if (taggedValue.size == 0) {
			logger.info("eObject " + eObject + " does not have the Replication stereotype")
			return null
		}
		if (taggedValue.size > 1) {
			logger.info("eObject " + eObject + " does have multiple stereotypes -- returning the first")
		}
		if (!(t.isInstance(taggedValue.get(0)))) {
			logger.warn("The stereotype of " + eObject + " is not the Replication stereotype")
			return null
		}
		return t.cast(taggedValue.get(0))
	}

}
