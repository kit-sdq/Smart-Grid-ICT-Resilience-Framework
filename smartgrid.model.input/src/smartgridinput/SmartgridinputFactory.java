/**
 */
package smartgridinput;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each
 * non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see smartgridinput.SmartgridinputPackage
 * @generated
 */
public interface SmartgridinputFactory extends EFactory {
    /**
     * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    SmartgridinputFactory eINSTANCE = smartgridinput.impl.SmartgridinputFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Scenario State</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return a new object of class '<em>Scenario State</em>'.
     * @generated
     */
    ScenarioState createScenarioState();

    /**
     * Returns a new object of class '<em>Entity State</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return a new object of class '<em>Entity State</em>'.
     * @generated
     */
    EntityState createEntityState();

    /**
     * Returns a new object of class '<em>Power State</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return a new object of class '<em>Power State</em>'.
     * @generated
     */
    PowerState createPowerState();

    /**
     * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the package supported by this factory.
     * @generated
     */
    SmartgridinputPackage getSmartgridinputPackage();

} //SmartgridinputFactory
