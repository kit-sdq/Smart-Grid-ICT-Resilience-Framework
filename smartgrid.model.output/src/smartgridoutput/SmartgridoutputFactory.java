/**
 */
package smartgridoutput;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each
 * non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see smartgridoutput.SmartgridoutputPackage
 * @generated
 */
public interface SmartgridoutputFactory extends EFactory {
    /**
     * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    SmartgridoutputFactory eINSTANCE = smartgridoutput.impl.SmartgridoutputFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Scenario Result</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return a new object of class '<em>Scenario Result</em>'.
     * @generated
     */
    ScenarioResult createScenarioResult();

    /**
     * Returns a new object of class '<em>Online</em>'. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @return a new object of class '<em>Online</em>'.
     * @generated
     */
    Online createOnline();

    /**
     * Returns a new object of class '<em>Defect</em>'. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @return a new object of class '<em>Defect</em>'.
     * @generated
     */
    Defect createDefect();

    /**
     * Returns a new object of class '<em>No Power</em>'. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @return a new object of class '<em>No Power</em>'.
     * @generated
     */
    NoPower createNoPower();

    /**
     * Returns a new object of class '<em>No Uplink</em>'. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @return a new object of class '<em>No Uplink</em>'.
     * @generated
     */
    NoUplink createNoUplink();

    /**
     * Returns a new object of class '<em>Cluster</em>'. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @return a new object of class '<em>Cluster</em>'.
     * @generated
     */
    Cluster createCluster();

    /**
     * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the package supported by this factory.
     * @generated
     */
    SmartgridoutputPackage getSmartgridoutputPackage();

} //SmartgridoutputFactory
