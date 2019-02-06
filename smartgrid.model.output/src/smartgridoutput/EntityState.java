/**
 */
package smartgridoutput;

import org.eclipse.emf.ecore.EObject;

import smartgridtopo.NetworkEntity;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Entity State</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link smartgridoutput.EntityState#getOwner <em>Owner</em>}</li>
 * </ul>
 *
 * @see smartgridoutput.SmartgridoutputPackage#getEntityState()
 * @model abstract="true"
 * @generated
 */
public interface EntityState extends EObject {
    /**
     * Returns the value of the '<em><b>Owner</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Owner</em>' reference isn't clear, there really should be more of
     * a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owner</em>' reference.
     * @see #setOwner(NetworkEntity)
     * @see smartgridoutput.SmartgridoutputPackage#getEntityState_Owner()
     * @model required="true"
     * @generated
     */
    NetworkEntity getOwner();

    /**
     * Sets the value of the '{@link smartgridoutput.EntityState#getOwner <em>Owner</em>}' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Owner</em>' reference.
     * @see #getOwner()
     * @generated
     */
    void setOwner(NetworkEntity value);

} // EntityState
