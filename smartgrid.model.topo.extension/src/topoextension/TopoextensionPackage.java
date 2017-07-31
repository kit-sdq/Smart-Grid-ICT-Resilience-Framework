/**
 */
package topoextension;

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
 * @see topoextension.TopoextensionFactory
 * @model kind="package"
 * @generated
 */
public interface TopoextensionPackage extends EPackage {
    /**
     * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNAME = "topoextension";

    /**
     * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNS_URI = "http://sdq.ipd.uka.de/smartgridtopo/topoextension";

    /**
     * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNS_PREFIX = "topoextension";

    /**
     * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    TopoextensionPackage eINSTANCE = topoextension.impl.TopoextensionPackageImpl.init();

    /**
     * The meta object id for the '{@link topoextension.impl.ExtensionRepositoryImpl <em>Extension
     * Repository</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see topoextension.impl.ExtensionRepositoryImpl
     * @see topoextension.impl.TopoextensionPackageImpl#getExtensionRepository()
     * @generated
     */
    int EXTENSION_REPOSITORY = 0;

    /**
     * The feature id for the '<em><b>Replications</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int EXTENSION_REPOSITORY__REPLICATIONS = 0;

    /**
     * The feature id for the '<em><b>Aggregations</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int EXTENSION_REPOSITORY__AGGREGATIONS = 1;

    /**
     * The number of structural features of the '<em>Extension Repository</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int EXTENSION_REPOSITORY_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Extension Repository</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int EXTENSION_REPOSITORY_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link topoextension.impl.ReplicationImpl <em>Replication</em>}'
     * class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see topoextension.impl.ReplicationImpl
     * @see topoextension.impl.TopoextensionPackageImpl#getReplication()
     * @generated
     */
    int REPLICATION = 1;

    /**
     * The feature id for the '<em><b>Nr Of Replicas</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int REPLICATION__NR_OF_REPLICAS = 0;

    /**
     * The number of structural features of the '<em>Replication</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int REPLICATION_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Replication</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int REPLICATION_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link topoextension.impl.AggregationImpl <em>Aggregation</em>}'
     * class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see topoextension.impl.AggregationImpl
     * @see topoextension.impl.TopoextensionPackageImpl#getAggregation()
     * @generated
     */
    int AGGREGATION = 2;

    /**
     * The feature id for the '<em><b>Networkentity</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AGGREGATION__NETWORKENTITY = 0;

    /**
     * The number of structural features of the '<em>Aggregation</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AGGREGATION_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Aggregation</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int AGGREGATION_OPERATION_COUNT = 0;

    /**
     * Returns the meta object for class '{@link topoextension.ExtensionRepository <em>Extension
     * Repository</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Extension Repository</em>'.
     * @see topoextension.ExtensionRepository
     * @generated
     */
    EClass getExtensionRepository();

    /**
     * Returns the meta object for the containment reference list
     * '{@link topoextension.ExtensionRepository#getReplications <em>Replications</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Replications</em>'.
     * @see topoextension.ExtensionRepository#getReplications()
     * @see #getExtensionRepository()
     * @generated
     */
    EReference getExtensionRepository_Replications();

    /**
     * Returns the meta object for the containment reference list
     * '{@link topoextension.ExtensionRepository#getAggregations <em>Aggregations</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Aggregations</em>'.
     * @see topoextension.ExtensionRepository#getAggregations()
     * @see #getExtensionRepository()
     * @generated
     */
    EReference getExtensionRepository_Aggregations();

    /**
     * Returns the meta object for class '{@link topoextension.Replication <em>Replication</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Replication</em>'.
     * @see topoextension.Replication
     * @generated
     */
    EClass getReplication();

    /**
     * Returns the meta object for the attribute '{@link topoextension.Replication#getNrOfReplicas
     * <em>Nr Of Replicas</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the attribute '<em>Nr Of Replicas</em>'.
     * @see topoextension.Replication#getNrOfReplicas()
     * @see #getReplication()
     * @generated
     */
    EAttribute getReplication_NrOfReplicas();

    /**
     * Returns the meta object for class '{@link topoextension.Aggregation <em>Aggregation</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Aggregation</em>'.
     * @see topoextension.Aggregation
     * @generated
     */
    EClass getAggregation();

    /**
     * Returns the meta object for the containment reference list
     * '{@link topoextension.Aggregation#getNetworkentity <em>Networkentity</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Networkentity</em>'.
     * @see topoextension.Aggregation#getNetworkentity()
     * @see #getAggregation()
     * @generated
     */
    EReference getAggregation_Networkentity();

    /**
     * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the factory that creates the instances of the model.
     * @generated
     */
    TopoextensionFactory getTopoextensionFactory();

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
         * The meta object literal for the '{@link topoextension.impl.ExtensionRepositoryImpl
         * <em>Extension Repository</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see topoextension.impl.ExtensionRepositoryImpl
         * @see topoextension.impl.TopoextensionPackageImpl#getExtensionRepository()
         * @generated
         */
        EClass EXTENSION_REPOSITORY = eINSTANCE.getExtensionRepository();

        /**
         * The meta object literal for the '<em><b>Replications</b></em>' containment reference list
         * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference EXTENSION_REPOSITORY__REPLICATIONS = eINSTANCE.getExtensionRepository_Replications();

        /**
         * The meta object literal for the '<em><b>Aggregations</b></em>' containment reference list
         * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference EXTENSION_REPOSITORY__AGGREGATIONS = eINSTANCE.getExtensionRepository_Aggregations();

        /**
         * The meta object literal for the '{@link topoextension.impl.ReplicationImpl
         * <em>Replication</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see topoextension.impl.ReplicationImpl
         * @see topoextension.impl.TopoextensionPackageImpl#getReplication()
         * @generated
         */
        EClass REPLICATION = eINSTANCE.getReplication();

        /**
         * The meta object literal for the '<em><b>Nr Of Replicas</b></em>' attribute feature. <!--
         * begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EAttribute REPLICATION__NR_OF_REPLICAS = eINSTANCE.getReplication_NrOfReplicas();

        /**
         * The meta object literal for the '{@link topoextension.impl.AggregationImpl
         * <em>Aggregation</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see topoextension.impl.AggregationImpl
         * @see topoextension.impl.TopoextensionPackageImpl#getAggregation()
         * @generated
         */
        EClass AGGREGATION = eINSTANCE.getAggregation();

        /**
         * The meta object literal for the '<em><b>Networkentity</b></em>' containment reference
         * list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference AGGREGATION__NETWORKENTITY = eINSTANCE.getAggregation_Networkentity();

    }

} //TopoextensionPackage
