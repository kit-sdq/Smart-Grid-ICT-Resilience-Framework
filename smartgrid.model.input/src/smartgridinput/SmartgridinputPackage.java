/**
 */
package smartgridinput;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta
 * objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see smartgridinput.SmartgridinputFactory
 * @model kind="package"
 * @generated
 */
public interface SmartgridinputPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "smartgridinput";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://sdq.ipd.uka.de/smartgridinput/1.0";

    /**
     * The package namespace name.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "smartgridinput";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    SmartgridinputPackage eINSTANCE = smartgridinput.impl.SmartgridinputPackageImpl.init();

    /**
     * The meta object id for the '{@link smartgridinput.impl.ScenarioStateImpl <em>Scenario State</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see smartgridinput.impl.ScenarioStateImpl
     * @see smartgridinput.impl.SmartgridinputPackageImpl#getScenarioState()
     * @generated
     */
    int SCENARIO_STATE = 0;

    /**
     * The meta object id for the '{@link smartgridinput.impl.EntityStateImpl <em>Entity State</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see smartgridinput.impl.EntityStateImpl
     * @see smartgridinput.impl.SmartgridinputPackageImpl#getEntityState()
     * @generated
     */
    int ENTITY_STATE = 2;

    /**
     * The meta object id for the '{@link smartgridinput.impl.PowerStateImpl <em>Power State</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see smartgridinput.impl.PowerStateImpl
     * @see smartgridinput.impl.SmartgridinputPackageImpl#getPowerState()
     * @generated
     */
    int POWER_STATE = 3;

    /**
     * The meta object id for the '{@link smartgridinput.Identifier <em>Identifier</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see smartgridinput.Identifier
     * @see smartgridinput.impl.SmartgridinputPackageImpl#getIdentifier()
     * @generated
     */
    int IDENTIFIER = 1;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFIER__ID = 0;

    /**
     * The number of structural features of the '<em>Identifier</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFIER_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Identifier</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFIER_OPERATION_COUNT = 0;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCENARIO_STATE__ID = IDENTIFIER__ID;

    /**
     * The feature id for the '<em><b>Entity States</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCENARIO_STATE__ENTITY_STATES = IDENTIFIER_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Power States</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCENARIO_STATE__POWER_STATES = IDENTIFIER_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Scenario</b></em>' reference.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SCENARIO_STATE__SCENARIO = IDENTIFIER_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Scenario State</em>' class.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCENARIO_STATE_FEATURE_COUNT = IDENTIFIER_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>Scenario State</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int SCENARIO_STATE_OPERATION_COUNT = IDENTIFIER_OPERATION_COUNT + 0;

    /**
     * The feature id for the '<em><b>Is Destroyed</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ENTITY_STATE__IS_DESTROYED = 0;

    /**
     * The feature id for the '<em><b>Is Hacked</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ENTITY_STATE__IS_HACKED = 1;

    /**
     * The feature id for the '<em><b>Owner</b></em>' reference.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ENTITY_STATE__OWNER = 2;

    /**
     * The number of structural features of the '<em>Entity State</em>' class.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENTITY_STATE_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Entity State</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int ENTITY_STATE_OPERATION_COUNT = 0;

    /**
     * The feature id for the '<em><b>Owner</b></em>' reference.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int POWER_STATE__OWNER = 0;

    /**
     * The feature id for the '<em><b>Power Outage</b></em>' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int POWER_STATE__POWER_OUTAGE = 1;

    /**
     * The number of structural features of the '<em>Power State</em>' class.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POWER_STATE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Power State</em>' class.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     * @ordered
     */
    int POWER_STATE_OPERATION_COUNT = 0;

    /**
     * Returns the meta object for class '{@link smartgridinput.ScenarioState <em>Scenario State</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Scenario State</em>'.
     * @see smartgridinput.ScenarioState
     * @generated
     */
    EClass getScenarioState();

    /**
     * Returns the meta object for the containment reference list
     * '{@link smartgridinput.ScenarioState#getEntityStates <em>Entity States</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Entity States</em>'.
     * @see smartgridinput.ScenarioState#getEntityStates()
     * @see #getScenarioState()
     * @generated
     */
    EReference getScenarioState_EntityStates();

    /**
     * Returns the meta object for the containment reference list
     * '{@link smartgridinput.ScenarioState#getPowerStates <em>Power States</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Power States</em>'.
     * @see smartgridinput.ScenarioState#getPowerStates()
     * @see #getScenarioState()
     * @generated
     */
    EReference getScenarioState_PowerStates();

    /**
     * Returns the meta object for the reference '{@link smartgridinput.ScenarioState#getScenario <em>Scenario</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Scenario</em>'.
     * @see smartgridinput.ScenarioState#getScenario()
     * @see #getScenarioState()
     * @generated
     */
    EReference getScenarioState_Scenario();

    /**
     * Returns the meta object for class '{@link smartgridinput.EntityState <em>Entity State</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Entity State</em>'.
     * @see smartgridinput.EntityState
     * @generated
     */
    EClass getEntityState();

    /**
     * Returns the meta object for the attribute '{@link smartgridinput.EntityState#isIsDestroyed <em>Is Destroyed</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Is Destroyed</em>'.
     * @see smartgridinput.EntityState#isIsDestroyed()
     * @see #getEntityState()
     * @generated
     */
    EAttribute getEntityState_IsDestroyed();

    /**
     * Returns the meta object for the attribute '{@link smartgridinput.EntityState#isIsHacked <em>Is Hacked</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Is Hacked</em>'.
     * @see smartgridinput.EntityState#isIsHacked()
     * @see #getEntityState()
     * @generated
     */
    EAttribute getEntityState_IsHacked();

    /**
     * Returns the meta object for the reference '{@link smartgridinput.EntityState#getOwner <em>Owner</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Owner</em>'.
     * @see smartgridinput.EntityState#getOwner()
     * @see #getEntityState()
     * @generated
     */
    EReference getEntityState_Owner();

    /**
     * Returns the meta object for class '{@link smartgridinput.PowerState <em>Power State</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for class '<em>Power State</em>'.
     * @see smartgridinput.PowerState
     * @generated
     */
    EClass getPowerState();

    /**
     * Returns the meta object for the reference '{@link smartgridinput.PowerState#getOwner <em>Owner</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Owner</em>'.
     * @see smartgridinput.PowerState#getOwner()
     * @see #getPowerState()
     * @generated
     */
    EReference getPowerState_Owner();

    /**
     * Returns the meta object for the attribute '{@link smartgridinput.PowerState#isPowerOutage <em>Power Outage</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Power Outage</em>'.
     * @see smartgridinput.PowerState#isPowerOutage()
     * @see #getPowerState()
     * @generated
     */
    EAttribute getPowerState_PowerOutage();

    /**
     * Returns the meta object for class '{@link smartgridinput.Identifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Identifier</em>'.
     * @see smartgridinput.Identifier
     * @generated
     */
    EClass getIdentifier();

    /**
     * Returns the meta object for the attribute '{@link smartgridinput.Identifier#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see smartgridinput.Identifier#getId()
     * @see #getIdentifier()
     * @generated
     */
    EAttribute getIdentifier_Id();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    SmartgridinputFactory getSmartgridinputFactory();

    /**
     * <!-- begin-user-doc --> Defines literals for the meta objects that represent
     * <ul>
     * <li>each class,</li>
     * <li>each feature of each class,</li>
     * <li>each operation of each class,</li>
     * <li>each enum,</li>
     * <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link smartgridinput.impl.ScenarioStateImpl <em>Scenario State</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see smartgridinput.impl.ScenarioStateImpl
         * @see smartgridinput.impl.SmartgridinputPackageImpl#getScenarioState()
         * @generated
         */
        EClass SCENARIO_STATE = eINSTANCE.getScenarioState();

        /**
         * The meta object literal for the '<em><b>Entity States</b></em>' containment reference list feature.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @generated
         */
        EReference SCENARIO_STATE__ENTITY_STATES = eINSTANCE.getScenarioState_EntityStates();

        /**
         * The meta object literal for the '<em><b>Power States</b></em>' containment reference list feature.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @generated
         */
        EReference SCENARIO_STATE__POWER_STATES = eINSTANCE.getScenarioState_PowerStates();

        /**
         * The meta object literal for the '<em><b>Scenario</b></em>' reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference SCENARIO_STATE__SCENARIO = eINSTANCE.getScenarioState_Scenario();

        /**
         * The meta object literal for the '{@link smartgridinput.impl.EntityStateImpl <em>Entity State</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see smartgridinput.impl.EntityStateImpl
         * @see smartgridinput.impl.SmartgridinputPackageImpl#getEntityState()
         * @generated
         */
        EClass ENTITY_STATE = eINSTANCE.getEntityState();

        /**
         * The meta object literal for the '<em><b>Is Destroyed</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute ENTITY_STATE__IS_DESTROYED = eINSTANCE.getEntityState_IsDestroyed();

        /**
         * The meta object literal for the '<em><b>Is Hacked</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute ENTITY_STATE__IS_HACKED = eINSTANCE.getEntityState_IsHacked();

        /**
         * The meta object literal for the '<em><b>Owner</b></em>' reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference ENTITY_STATE__OWNER = eINSTANCE.getEntityState_Owner();

        /**
         * The meta object literal for the '{@link smartgridinput.impl.PowerStateImpl <em>Power State</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * @see smartgridinput.impl.PowerStateImpl
         * @see smartgridinput.impl.SmartgridinputPackageImpl#getPowerState()
         * @generated
         */
        EClass POWER_STATE = eINSTANCE.getPowerState();

        /**
         * The meta object literal for the '<em><b>Owner</b></em>' reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference POWER_STATE__OWNER = eINSTANCE.getPowerState_Owner();

        /**
         * The meta object literal for the '<em><b>Power Outage</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute POWER_STATE__POWER_OUTAGE = eINSTANCE.getPowerState_PowerOutage();

        /**
         * The meta object literal for the '{@link smartgridinput.Identifier <em>Identifier</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see smartgridinput.Identifier
         * @see smartgridinput.impl.SmartgridinputPackageImpl#getIdentifier()
         * @generated
         */
        EClass IDENTIFIER = eINSTANCE.getIdentifier();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute IDENTIFIER__ID = eINSTANCE.getIdentifier_Id();

    }

} //SmartgridinputPackage
