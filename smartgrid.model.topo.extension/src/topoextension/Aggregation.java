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
 * <li>{@link topoextension.Aggregation#getSmartMeterReplicas <em>Smart Meter Replicas</em>}</li>
 * </ul>
 *
 * @see topoextension.TopoextensionPackage#getAggregation()
 * @model
 * @generated
 */
public interface Aggregation extends EObject {
    /**
     * Returns the value of the '<em><b>Smart Meter Replicas</b></em>' reference list. The list
     * contents are of type {@link smartgridtopo.NetworkEntity}. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Smart Meter Replicas</em>' reference list isn't clear, there
     * really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Smart Meter Replicas</em>' reference list.
     * @see topoextension.TopoextensionPackage#getAggregation_SmartMeterReplicas()
     * @model required="true"
     * @generated
     */
    EList<NetworkEntity> getSmartMeterReplicas();

} // Aggregation
