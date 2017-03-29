package smartgrid.model.topo.profiles.completions

import org.eclipse.emf.ecore.EObject

/**
 * Base class for completions.
 * T: the type of object to complete
 * U: the type of the object T is completed with
 */
interface Completion<T extends EObject, U extends EObject> {
	 def public String getStereotypeName()
	 def public String getReferenceName() 
	 def public void executeCompletion(EObject objectToComplete, EObject completionObject)
	 def public Class<T> getTypeToComplete()
	 def public Class<U> getTypeOfCompletionObject() 
}