/**
 */
package smartgridtopo;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scenario</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link smartgridtopo.Scenario#getContainsNE <em>Contains NE</em>}</li>
 *   <li>{@link smartgridtopo.Scenario#getContainsPGN <em>Contains PGN</em>}</li>
 *   <li>{@link smartgridtopo.Scenario#getContainsC <em>Contains C</em>}</li>
 *   <li>{@link smartgridtopo.Scenario#getContainsLC <em>Contains LC</em>}</li>
 * </ul>
 *
 * @see smartgridtopo.SmartgridtopoPackage#getScenario()
 * @model
 * @generated
 */
public interface Scenario extends NamedIdentifier {
    /**
     * Returns the value of the '<em><b>Contains NE</b></em>' containment reference list.
     * The list contents are of type {@link smartgridtopo.NetworkEntity}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Contains NE</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Contains NE</em>' containment reference list.
     * @see smartgridtopo.SmartgridtopoPackage#getScenario_ContainsNE()
     * @model containment="true"
     * @generated
     */
    EList<NetworkEntity> getContainsNE();

    /**
     * Returns the value of the '<em><b>Contains PGN</b></em>' containment reference list.
     * The list contents are of type {@link smartgridtopo.PowerGridNode}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Contains PGN</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Contains PGN</em>' containment reference list.
     * @see smartgridtopo.SmartgridtopoPackage#getScenario_ContainsPGN()
     * @model containment="true"
     * @generated
     */
    EList<PowerGridNode> getContainsPGN();

    /**
     * Returns the value of the '<em><b>Contains C</b></em>' containment reference list.
     * The list contents are of type {@link smartgridtopo.PhysicalConnection}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Contains C</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Contains C</em>' containment reference list.
     * @see smartgridtopo.SmartgridtopoPackage#getScenario_ContainsC()
     * @model containment="true"
     * @generated
     */
    EList<PhysicalConnection> getContainsC();

    /**
     * Returns the value of the '<em><b>Contains LC</b></em>' containment reference list.
     * The list contents are of type {@link smartgridtopo.LogicalCommunication}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Contains LC</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Contains LC</em>' containment reference list.
     * @see smartgridtopo.SmartgridtopoPackage#getScenario_ContainsLC()
     * @model containment="true"
     * @generated
     */
    EList<LogicalCommunication> getContainsLC();

} // Scenario
