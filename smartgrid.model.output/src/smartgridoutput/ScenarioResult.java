/**
 */
package smartgridoutput;

import org.eclipse.emf.common.util.EList;
import smartgridinput.ScenarioState;
import smartgridtopo.SmartGridTopology;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Scenario Result</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link smartgridoutput.ScenarioResult#getStates <em>States</em>}</li>
 *   <li>{@link smartgridoutput.ScenarioResult#getClusters <em>Clusters</em>}</li>
 *   <li>{@link smartgridoutput.ScenarioResult#getScenario <em>Scenario</em>}</li>
 *   <li>{@link smartgridoutput.ScenarioResult#getInput <em>Input</em>}</li>
 * </ul>
 *
 * @see smartgridoutput.SmartgridoutputPackage#getScenarioResult()
 * @model
 * @generated
 */
public interface ScenarioResult extends Identifier {
    /**
     * Returns the value of the '<em><b>States</b></em>' containment reference list.
     * The list contents are of type {@link smartgridoutput.EntityState}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>States</em>' containment reference list isn't clear, there really
     * should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>States</em>' containment reference list.
     * @see smartgridoutput.SmartgridoutputPackage#getScenarioResult_States()
     * @model containment="true"
     * @generated
     */
    EList<EntityState> getStates();

    /**
     * Returns the value of the '<em><b>Clusters</b></em>' containment reference list.
     * The list contents are of type {@link smartgridoutput.Cluster}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Clusters</em>' containment reference list isn't clear, there
     * really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Clusters</em>' containment reference list.
     * @see smartgridoutput.SmartgridoutputPackage#getScenarioResult_Clusters()
     * @model containment="true"
     * @generated
     */
    EList<Cluster> getClusters();

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
     * @see smartgridoutput.SmartgridoutputPackage#getScenarioResult_Scenario()
     * @model
     * @generated
     */
    SmartGridTopology getScenario();

    /**
     * Sets the value of the '{@link smartgridoutput.ScenarioResult#getScenario <em>Scenario</em>}' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Scenario</em>' reference.
     * @see #getScenario()
     * @generated
     */
    void setScenario(SmartGridTopology value);

    /**
     * Returns the value of the '<em><b>Input</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Input</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Input</em>' reference.
     * @see #setInput(ScenarioState)
     * @see smartgridoutput.SmartgridoutputPackage#getScenarioResult_Input()
     * @model
     * @generated
     */
    ScenarioState getInput();

    /**
     * Sets the value of the '{@link smartgridoutput.ScenarioResult#getInput <em>Input</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Input</em>' reference.
     * @see #getInput()
     * @generated
     */
    void setInput(ScenarioState value);

} // ScenarioResult
