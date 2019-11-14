/**
 */
package smartgridoutput;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Cluster</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link smartgridoutput.Cluster#getControlCenterCount <em>Control Center Count</em>}</li>
 *   <li>{@link smartgridoutput.Cluster#getSmartMeterCount <em>Smart Meter Count</em>}</li>
 *   <li>{@link smartgridoutput.Cluster#getHasEntities <em>Has Entities</em>}</li>
 * </ul>
 *
 * @see smartgridoutput.SmartgridoutputPackage#getCluster()
 * @model
 * @generated
 */
public interface Cluster extends EObject {
    /**
     * Returns the value of the '<em><b>Control Center Count</b></em>' attribute. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Control Center Count</em>' attribute isn't clear, there really
     * should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Control Center Count</em>' attribute.
     * @see #setControlCenterCount(int)
     * @see smartgridoutput.SmartgridoutputPackage#getCluster_ControlCenterCount()
     * @model
     * @generated
     */
    int getControlCenterCount();

    /**
     * Sets the value of the '{@link smartgridoutput.Cluster#getControlCenterCount <em>Control Center Count</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Control Center Count</em>' attribute.
     * @see #getControlCenterCount()
     * @generated
     */
    void setControlCenterCount(int value);

    /**
     * Returns the value of the '<em><b>Smart Meter Count</b></em>' attribute.
     * <!-- begin-user-doc
     * -->
     * <p>
     * If the meaning of the '<em>Smart Meter Count</em>' attribute isn't clear, there really should
     * be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Smart Meter Count</em>' attribute.
     * @see #setSmartMeterCount(int)
     * @see smartgridoutput.SmartgridoutputPackage#getCluster_SmartMeterCount()
     * @model
     * @generated
     */
    int getSmartMeterCount();

    /**
     * Sets the value of the '{@link smartgridoutput.Cluster#getSmartMeterCount <em>Smart Meter Count</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Smart Meter Count</em>' attribute.
     * @see #getSmartMeterCount()
     * @generated
     */
    void setSmartMeterCount(int value);

    /**
     * Returns the value of the '<em><b>Has Entities</b></em>' reference list. The list contents are
     * of type {@link smartgridoutput.On}. It is bidirectional and its opposite is
     * '{@link smartgridoutput.On#getBelongsToCluster <em>Belongs To Cluster</em>}'. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Has Entities</em>' reference list isn't clear, there really should
     * be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Has Entities</em>' reference list.
     * @see smartgridoutput.SmartgridoutputPackage#getCluster_HasEntities()
     * @see smartgridoutput.On#getBelongsToCluster
     * @model opposite="BelongsToCluster"
     * @generated
     */
    EList<On> getHasEntities();

} // Cluster
