/**
 */
package smartgridoutput;

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
 * 
 * @see smartgridoutput.SmartgridoutputFactory
 * @model kind="package"
 * @generated
 */
public interface SmartgridoutputPackage extends EPackage {
    /**
     * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNAME = "smartgridoutput";

    /**
     * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNS_URI = "http://sdq.ipd.uka.de/smartgridoutput/1.0";

    /**
     * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNS_PREFIX = "smartgridoutput";

    /**
     * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    SmartgridoutputPackage eINSTANCE = smartgridoutput.impl.SmartgridoutputPackageImpl.init();

    /**
     * The meta object id for the '{@link smartgridoutput.impl.ScenarioResultImpl <em>Scenario
     * Result</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see smartgridoutput.impl.ScenarioResultImpl
     * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getScenarioResult()
     * @generated
     */
    int SCENARIO_RESULT = 0;

    /**
     * The feature id for the '<em><b>States</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCENARIO_RESULT__STATES = 0;

    /**
     * The feature id for the '<em><b>Clusters</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCENARIO_RESULT__CLUSTERS = 1;

    /**
     * The feature id for the '<em><b>Scenario</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCENARIO_RESULT__SCENARIO = 2;

    /**
     * The number of structural features of the '<em>Scenario Result</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCENARIO_RESULT_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Scenario Result</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SCENARIO_RESULT_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link smartgridoutput.impl.EntityStateImpl <em>Entity
     * State</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see smartgridoutput.impl.EntityStateImpl
     * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getEntityState()
     * @generated
     */
    int ENTITY_STATE = 1;

    /**
     * The feature id for the '<em><b>Owner</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ENTITY_STATE__OWNER = 0;

    /**
     * The number of structural features of the '<em>Entity State</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ENTITY_STATE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Entity State</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ENTITY_STATE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link smartgridoutput.impl.OnImpl <em>On</em>}' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see smartgridoutput.impl.OnImpl
     * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getOn()
     * @generated
     */
    int ON = 8;

    /**
     * The feature id for the '<em><b>Owner</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ON__OWNER = ENTITY_STATE__OWNER;

    /**
     * The feature id for the '<em><b>Belongs To Cluster</b></em>' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ON__BELONGS_TO_CLUSTER = ENTITY_STATE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Is Hacked</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ON__IS_HACKED = ENTITY_STATE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>On</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ON_FEATURE_COUNT = ENTITY_STATE_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>On</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ON_OPERATION_COUNT = ENTITY_STATE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link smartgridoutput.impl.OnlineImpl <em>Online</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see smartgridoutput.impl.OnlineImpl
     * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getOnline()
     * @generated
     */
    int ONLINE = 2;

    /**
     * The feature id for the '<em><b>Owner</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ONLINE__OWNER = ON__OWNER;

    /**
     * The feature id for the '<em><b>Belongs To Cluster</b></em>' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ONLINE__BELONGS_TO_CLUSTER = ON__BELONGS_TO_CLUSTER;

    /**
     * The feature id for the '<em><b>Is Hacked</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ONLINE__IS_HACKED = ON__IS_HACKED;

    /**
     * The number of structural features of the '<em>Online</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ONLINE_FEATURE_COUNT = ON_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Online</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ONLINE_OPERATION_COUNT = ON_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link smartgridoutput.impl.DefectImpl <em>Defect</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see smartgridoutput.impl.DefectImpl
     * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getDefect()
     * @generated
     */
    int DEFECT = 3;

    /**
     * The feature id for the '<em><b>Owner</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int DEFECT__OWNER = ENTITY_STATE__OWNER;

    /**
     * The number of structural features of the '<em>Defect</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int DEFECT_FEATURE_COUNT = ENTITY_STATE_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Defect</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int DEFECT_OPERATION_COUNT = ENTITY_STATE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link smartgridoutput.impl.NoPowerImpl <em>No Power</em>}'
     * class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see smartgridoutput.impl.NoPowerImpl
     * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getNoPower()
     * @generated
     */
    int NO_POWER = 4;

    /**
     * The feature id for the '<em><b>Owner</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int NO_POWER__OWNER = ENTITY_STATE__OWNER;

    /**
     * The number of structural features of the '<em>No Power</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int NO_POWER_FEATURE_COUNT = ENTITY_STATE_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>No Power</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int NO_POWER_OPERATION_COUNT = ENTITY_STATE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link smartgridoutput.impl.OfflineImpl <em>Offline</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see smartgridoutput.impl.OfflineImpl
     * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getOffline()
     * @generated
     */
    int OFFLINE = 6;

    /**
     * The number of structural features of the '<em>Offline</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int OFFLINE_FEATURE_COUNT = 0;

    /**
     * The number of operations of the '<em>Offline</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int OFFLINE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link smartgridoutput.impl.NoUplinkImpl <em>No Uplink</em>}'
     * class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see smartgridoutput.impl.NoUplinkImpl
     * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getNoUplink()
     * @generated
     */
    int NO_UPLINK = 5;

    /**
     * The feature id for the '<em><b>Owner</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int NO_UPLINK__OWNER = OFFLINE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Belongs To Cluster</b></em>' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int NO_UPLINK__BELONGS_TO_CLUSTER = OFFLINE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Is Hacked</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int NO_UPLINK__IS_HACKED = OFFLINE_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>No Uplink</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int NO_UPLINK_FEATURE_COUNT = OFFLINE_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>No Uplink</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int NO_UPLINK_OPERATION_COUNT = OFFLINE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link smartgridoutput.impl.ClusterImpl <em>Cluster</em>}' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see smartgridoutput.impl.ClusterImpl
     * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getCluster()
     * @generated
     */
    int CLUSTER = 7;

    /**
     * The feature id for the '<em><b>Control Center Count</b></em>' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CLUSTER__CONTROL_CENTER_COUNT = 0;

    /**
     * The feature id for the '<em><b>Smart Meter Count</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CLUSTER__SMART_METER_COUNT = 1;

    /**
     * The feature id for the '<em><b>Has Entities</b></em>' reference list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CLUSTER__HAS_ENTITIES = 2;

    /**
     * The number of structural features of the '<em>Cluster</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CLUSTER_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Cluster</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CLUSTER_OPERATION_COUNT = 0;

    /**
     * Returns the meta object for class '{@link smartgridoutput.ScenarioResult <em>Scenario
     * Result</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Scenario Result</em>'.
     * @see smartgridoutput.ScenarioResult
     * @generated
     */
    EClass getScenarioResult();

    /**
     * Returns the meta object for the containment reference list
     * '{@link smartgridoutput.ScenarioResult#getStates <em>States</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>States</em>'.
     * @see smartgridoutput.ScenarioResult#getStates()
     * @see #getScenarioResult()
     * @generated
     */
    EReference getScenarioResult_States();

    /**
     * Returns the meta object for the containment reference list
     * '{@link smartgridoutput.ScenarioResult#getClusters <em>Clusters</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Clusters</em>'.
     * @see smartgridoutput.ScenarioResult#getClusters()
     * @see #getScenarioResult()
     * @generated
     */
    EReference getScenarioResult_Clusters();

    /**
     * Returns the meta object for the reference '{@link smartgridoutput.ScenarioResult#getScenario
     * <em>Scenario</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Scenario</em>'.
     * @see smartgridoutput.ScenarioResult#getScenario()
     * @see #getScenarioResult()
     * @generated
     */
    EReference getScenarioResult_Scenario();

    /**
     * Returns the meta object for class '{@link smartgridoutput.EntityState <em>Entity
     * State</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Entity State</em>'.
     * @see smartgridoutput.EntityState
     * @generated
     */
    EClass getEntityState();

    /**
     * Returns the meta object for the reference '{@link smartgridoutput.EntityState#getOwner
     * <em>Owner</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Owner</em>'.
     * @see smartgridoutput.EntityState#getOwner()
     * @see #getEntityState()
     * @generated
     */
    EReference getEntityState_Owner();

    /**
     * Returns the meta object for class '{@link smartgridoutput.Online <em>Online</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Online</em>'.
     * @see smartgridoutput.Online
     * @generated
     */
    EClass getOnline();

    /**
     * Returns the meta object for class '{@link smartgridoutput.Defect <em>Defect</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Defect</em>'.
     * @see smartgridoutput.Defect
     * @generated
     */
    EClass getDefect();

    /**
     * Returns the meta object for class '{@link smartgridoutput.NoPower <em>No Power</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>No Power</em>'.
     * @see smartgridoutput.NoPower
     * @generated
     */
    EClass getNoPower();

    /**
     * Returns the meta object for class '{@link smartgridoutput.NoUplink <em>No Uplink</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>No Uplink</em>'.
     * @see smartgridoutput.NoUplink
     * @generated
     */
    EClass getNoUplink();

    /**
     * Returns the meta object for class '{@link smartgridoutput.Offline <em>Offline</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Offline</em>'.
     * @see smartgridoutput.Offline
     * @generated
     */
    EClass getOffline();

    /**
     * Returns the meta object for class '{@link smartgridoutput.Cluster <em>Cluster</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Cluster</em>'.
     * @see smartgridoutput.Cluster
     * @generated
     */
    EClass getCluster();

    /**
     * Returns the meta object for the attribute
     * '{@link smartgridoutput.Cluster#getControlCenterCount <em>Control Center Count</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Control Center Count</em>'.
     * @see smartgridoutput.Cluster#getControlCenterCount()
     * @see #getCluster()
     * @generated
     */
    EAttribute getCluster_ControlCenterCount();

    /**
     * Returns the meta object for the attribute '{@link smartgridoutput.Cluster#getSmartMeterCount
     * <em>Smart Meter Count</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Smart Meter Count</em>'.
     * @see smartgridoutput.Cluster#getSmartMeterCount()
     * @see #getCluster()
     * @generated
     */
    EAttribute getCluster_SmartMeterCount();

    /**
     * Returns the meta object for the reference list '{@link smartgridoutput.Cluster#getHasEntities
     * <em>Has Entities</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference list '<em>Has Entities</em>'.
     * @see smartgridoutput.Cluster#getHasEntities()
     * @see #getCluster()
     * @generated
     */
    EReference getCluster_HasEntities();

    /**
     * Returns the meta object for class '{@link smartgridoutput.On <em>On</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>On</em>'.
     * @see smartgridoutput.On
     * @generated
     */
    EClass getOn();

    /**
     * Returns the meta object for the reference '{@link smartgridoutput.On#getBelongsToCluster
     * <em>Belongs To Cluster</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Belongs To Cluster</em>'.
     * @see smartgridoutput.On#getBelongsToCluster()
     * @see #getOn()
     * @generated
     */
    EReference getOn_BelongsToCluster();

    /**
     * Returns the meta object for the attribute '{@link smartgridoutput.On#isIsHacked <em>Is
     * Hacked</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Is Hacked</em>'.
     * @see smartgridoutput.On#isIsHacked()
     * @see #getOn()
     * @generated
     */
    EAttribute getOn_IsHacked();

    /**
     * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the factory that creates the instances of the model.
     * @generated
     */
    SmartgridoutputFactory getSmartgridoutputFactory();

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
     * 
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link smartgridoutput.impl.ScenarioResultImpl
         * <em>Scenario Result</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see smartgridoutput.impl.ScenarioResultImpl
         * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getScenarioResult()
         * @generated
         */
        EClass SCENARIO_RESULT = eINSTANCE.getScenarioResult();

        /**
         * The meta object literal for the '<em><b>States</b></em>' containment reference list
         * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference SCENARIO_RESULT__STATES = eINSTANCE.getScenarioResult_States();

        /**
         * The meta object literal for the '<em><b>Clusters</b></em>' containment reference list
         * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference SCENARIO_RESULT__CLUSTERS = eINSTANCE.getScenarioResult_Clusters();

        /**
         * The meta object literal for the '<em><b>Scenario</b></em>' reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference SCENARIO_RESULT__SCENARIO = eINSTANCE.getScenarioResult_Scenario();

        /**
         * The meta object literal for the '{@link smartgridoutput.impl.EntityStateImpl <em>Entity
         * State</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see smartgridoutput.impl.EntityStateImpl
         * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getEntityState()
         * @generated
         */
        EClass ENTITY_STATE = eINSTANCE.getEntityState();

        /**
         * The meta object literal for the '<em><b>Owner</b></em>' reference feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference ENTITY_STATE__OWNER = eINSTANCE.getEntityState_Owner();

        /**
         * The meta object literal for the '{@link smartgridoutput.impl.OnlineImpl <em>Online</em>}'
         * class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see smartgridoutput.impl.OnlineImpl
         * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getOnline()
         * @generated
         */
        EClass ONLINE = eINSTANCE.getOnline();

        /**
         * The meta object literal for the '{@link smartgridoutput.impl.DefectImpl <em>Defect</em>}'
         * class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see smartgridoutput.impl.DefectImpl
         * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getDefect()
         * @generated
         */
        EClass DEFECT = eINSTANCE.getDefect();

        /**
         * The meta object literal for the '{@link smartgridoutput.impl.NoPowerImpl <em>No
         * Power</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see smartgridoutput.impl.NoPowerImpl
         * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getNoPower()
         * @generated
         */
        EClass NO_POWER = eINSTANCE.getNoPower();

        /**
         * The meta object literal for the '{@link smartgridoutput.impl.NoUplinkImpl <em>No
         * Uplink</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see smartgridoutput.impl.NoUplinkImpl
         * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getNoUplink()
         * @generated
         */
        EClass NO_UPLINK = eINSTANCE.getNoUplink();

        /**
         * The meta object literal for the '{@link smartgridoutput.impl.OfflineImpl
         * <em>Offline</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see smartgridoutput.impl.OfflineImpl
         * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getOffline()
         * @generated
         */
        EClass OFFLINE = eINSTANCE.getOffline();

        /**
         * The meta object literal for the '{@link smartgridoutput.impl.ClusterImpl
         * <em>Cluster</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see smartgridoutput.impl.ClusterImpl
         * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getCluster()
         * @generated
         */
        EClass CLUSTER = eINSTANCE.getCluster();

        /**
         * The meta object literal for the '<em><b>Control Center Count</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute CLUSTER__CONTROL_CENTER_COUNT = eINSTANCE.getCluster_ControlCenterCount();

        /**
         * The meta object literal for the '<em><b>Smart Meter Count</b></em>' attribute feature.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute CLUSTER__SMART_METER_COUNT = eINSTANCE.getCluster_SmartMeterCount();

        /**
         * The meta object literal for the '<em><b>Has Entities</b></em>' reference list feature.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference CLUSTER__HAS_ENTITIES = eINSTANCE.getCluster_HasEntities();

        /**
         * The meta object literal for the '{@link smartgridoutput.impl.OnImpl <em>On</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see smartgridoutput.impl.OnImpl
         * @see smartgridoutput.impl.SmartgridoutputPackageImpl#getOn()
         * @generated
         */
        EClass ON = eINSTANCE.getOn();

        /**
         * The meta object literal for the '<em><b>Belongs To Cluster</b></em>' reference feature.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference ON__BELONGS_TO_CLUSTER = eINSTANCE.getOn_BelongsToCluster();

        /**
         * The meta object literal for the '<em><b>Is Hacked</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute ON__IS_HACKED = eINSTANCE.getOn_IsHacked();

    }

} //SmartgridoutputPackage
