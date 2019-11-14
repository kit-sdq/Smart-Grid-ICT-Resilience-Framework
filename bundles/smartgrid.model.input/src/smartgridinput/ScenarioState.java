/**
 */
package smartgridinput;

import org.eclipse.emf.common.util.EList;
import smartgridtopo.SmartGridTopology;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Scenario State</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link smartgridinput.ScenarioState#getEntityStates <em>Entity States</em>}</li>
 *   <li>{@link smartgridinput.ScenarioState#getPowerStates <em>Power States</em>}</li>
 *   <li>{@link smartgridinput.ScenarioState#getScenario <em>Scenario</em>}</li>
 * </ul>
 *
 * @see smartgridinput.SmartgridinputPackage#getScenarioState()
 * @model
 * @generated
 */
public interface ScenarioState extends Identifier {
    /**
     * Returns the value of the '<em><b>Entity States</b></em>' containment reference list.
     * The list contents are of type {@link smartgridinput.EntityState}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Entity States</em>' containment reference list isn't clear, there
     * really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Entity States</em>' containment reference list.
     * @see smartgridinput.SmartgridinputPackage#getScenarioState_EntityStates()
     * @model containment="true"
     * @generated
     */
    EList<EntityState> getEntityStates();

    /**
     * Returns the value of the '<em><b>Power States</b></em>' containment reference list.
     * The list contents are of type {@link smartgridinput.PowerState}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Power States</em>' containment reference list isn't clear, there
     * really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Power States</em>' containment reference list.
     * @see smartgridinput.SmartgridinputPackage#getScenarioState_PowerStates()
     * @model containment="true"
     * @generated
     */
    EList<PowerState> getPowerStates();

    /**
     * Returns the value of the '<em><b>Scenario</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Scenario</em>' reference isn't clear, there really should be more
     * of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Scenario</em>' reference.
     * @see #setScenario(SmartGridTopology)
     * @see smartgridinput.SmartgridinputPackage#getScenarioState_Scenario()
     * @model
     * @generated
     */
    SmartGridTopology getScenario();

    /**
     * Sets the value of the '{@link smartgridinput.ScenarioState#getScenario <em>Scenario</em>}' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Scenario</em>' reference.
     * @see #getScenario()
     * @generated
     */
    void setScenario(SmartGridTopology value);

} // ScenarioState
