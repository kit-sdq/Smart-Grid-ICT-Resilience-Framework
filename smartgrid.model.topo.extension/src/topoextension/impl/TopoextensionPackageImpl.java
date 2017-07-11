/**
 */
package topoextension.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import smartgridtopo.SmartgridtopoPackage;
import smartgridtopo.impl.SmartgridtopoPackageImpl;
import topoextension.Completion;
import topoextension.ExtensionRepository;
import topoextension.Replication;
import topoextension.TopoextensionFactory;
import topoextension.TopoextensionPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class TopoextensionPackageImpl extends EPackageImpl implements TopoextensionPackage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass extensionRepositoryEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass replicationEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass completionEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI
     * value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init
     * init()}, which also performs initialization of the package, or returns the registered
     * package, if one already exists. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see topoextension.TopoextensionPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private TopoextensionPackageImpl() {
        super(eNS_URI, TopoextensionFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others
     * upon which it depends.
     * 
     * <p>
     * This method is used to initialize {@link TopoextensionPackage#eINSTANCE} when that field is
     * accessed. Clients should not invoke it directly. Instead, they should simply access that
     * field to obtain the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static TopoextensionPackage init() {
        if (isInited)
            return (TopoextensionPackage) EPackage.Registry.INSTANCE.getEPackage(TopoextensionPackage.eNS_URI);

        // Obtain or create and register package
        TopoextensionPackageImpl theTopoextensionPackage = (TopoextensionPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof TopoextensionPackageImpl
                ? EPackage.Registry.INSTANCE.get(eNS_URI) : new TopoextensionPackageImpl());

        isInited = true;

        // Obtain or create and register interdependencies
        SmartgridtopoPackageImpl theSmartgridtopoPackage = (SmartgridtopoPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(SmartgridtopoPackage.eNS_URI) instanceof SmartgridtopoPackageImpl
                ? EPackage.Registry.INSTANCE.getEPackage(SmartgridtopoPackage.eNS_URI) : SmartgridtopoPackage.eINSTANCE);

        // Create package meta-data objects
        theTopoextensionPackage.createPackageContents();
        theSmartgridtopoPackage.createPackageContents();

        // Initialize created meta-data
        theTopoextensionPackage.initializePackageContents();
        theSmartgridtopoPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theTopoextensionPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(TopoextensionPackage.eNS_URI, theTopoextensionPackage);
        return theTopoextensionPackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getExtensionRepository() {
        return extensionRepositoryEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getExtensionRepository_Replications() {
        return (EReference) extensionRepositoryEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getExtensionRepository_Completion() {
        return (EReference) extensionRepositoryEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getReplication() {
        return replicationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getReplication_NrOfReplicas() {
        return (EAttribute) replicationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getCompletion() {
        return completionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getCompletion_Networkentity() {
        return (EReference) completionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public TopoextensionFactory getTopoextensionFactory() {
        return (TopoextensionFactory) getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package. This method is guarded to have no affect on
     * any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void createPackageContents() {
        if (isCreated)
            return;
        isCreated = true;

        // Create classes and their features
        extensionRepositoryEClass = createEClass(EXTENSION_REPOSITORY);
        createEReference(extensionRepositoryEClass, EXTENSION_REPOSITORY__REPLICATIONS);
        createEReference(extensionRepositoryEClass, EXTENSION_REPOSITORY__COMPLETION);

        replicationEClass = createEClass(REPLICATION);
        createEAttribute(replicationEClass, REPLICATION__NR_OF_REPLICAS);

        completionEClass = createEClass(COMPLETION);
        createEReference(completionEClass, COMPLETION__NETWORKENTITY);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model. This method is guarded to have
     * no affect on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized)
            return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        SmartgridtopoPackage theSmartgridtopoPackage = (SmartgridtopoPackage) EPackage.Registry.INSTANCE.getEPackage(SmartgridtopoPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes

        // Initialize classes, features, and operations; add parameters
        initEClass(extensionRepositoryEClass, ExtensionRepository.class, "ExtensionRepository", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExtensionRepository_Replications(), getReplication(), null, "replications", null, 0, -1, ExtensionRepository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getExtensionRepository_Completion(), getCompletion(), null, "completion", null, 0, -1, ExtensionRepository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
                !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(replicationEClass, Replication.class, "Replication", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getReplication_NrOfReplicas(), ecorePackage.getEInt(), "nrOfReplicas", null, 0, 1, Replication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(completionEClass, Completion.class, "Completion", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCompletion_Networkentity(), theSmartgridtopoPackage.getNetworkEntity(), null, "networkentity", null, 1, -1, Completion.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Create resource
        createResource(eNS_URI);
    }

} //TopoextensionPackageImpl
