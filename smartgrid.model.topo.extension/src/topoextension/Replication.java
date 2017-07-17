/**
 */
package topoextension;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Replication</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link topoextension.Replication#getNrOfReplicas <em>Nr Of Replicas</em>}</li>
 * </ul>
 *
 * @see topoextension.TopoextensionPackage#getReplication()
 * @model
 * @generated
 */
public interface Replication extends EObject {
    /**
     * Returns the value of the '<em><b>Nr Of Replicas</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Nr Of Replicas</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Nr Of Replicas</em>' attribute.
     * @see #setNrOfReplicas(int)
     * @see topoextension.TopoextensionPackage#getReplication_NrOfReplicas()
     * @model
     * @generated
     */
    int getNrOfReplicas();

    /**
     * Sets the value of the '{@link topoextension.Replication#getNrOfReplicas <em>Nr Of Replicas</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Nr Of Replicas</em>' attribute.
     * @see #getNrOfReplicas()
     * @generated
     */
    void setNrOfReplicas(int value);

} // Replication
