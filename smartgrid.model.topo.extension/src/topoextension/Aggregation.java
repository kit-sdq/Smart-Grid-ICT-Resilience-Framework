/**
 */
package topoextension;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import smartgridtopo.NetworkEntity;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Aggregation</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link topoextension.Aggregation#getNetworkentity <em>Networkentity</em>}</li>
 * </ul>
 *
 * @see topoextension.TopoextensionPackage#getAggregation()
 * @model
 * @generated
 */
public interface Aggregation extends EObject {
    /**
     * Returns the value of the '<em><b>Networkentity</b></em>' containment reference list. The list
     * contents are of type {@link smartgridtopo.NetworkEntity}. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Networkentity</em>' containment reference list isn't clear, there
     * really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Networkentity</em>' containment reference list.
     * @see topoextension.TopoextensionPackage#getAggregation_Networkentity()
     * @model containment="true" required="true"
     * @generated
     */
    EList<NetworkEntity> getNetworkentity();

} // Aggregation
