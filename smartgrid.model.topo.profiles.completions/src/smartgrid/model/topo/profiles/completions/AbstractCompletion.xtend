package smartgrid.model.topo.profiles.completions

import org.eclipse.emf.ecore.EObject

abstract class AbstractCompletion<T extends EObject, U extends EObject> implements Completion<T, U> {

	val private Class<T> t
	val private Class<U> u 
	
	 
	public new(Class<T> t, Class<U> u){
		this.t = t
		this.u = u
	}
	
	override getTypeToComplete() {
		return t
	}
	
	override getTypeOfCompletionObject() {
		return u
	}

}
