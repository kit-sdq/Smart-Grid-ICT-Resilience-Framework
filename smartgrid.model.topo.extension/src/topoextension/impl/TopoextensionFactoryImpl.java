/**
 */
package topoextension.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import topoextension.*;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class TopoextensionFactoryImpl extends EFactoryImpl implements TopoextensionFactory {
    /**
     * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public static TopoextensionFactory init() {
        try {
            TopoextensionFactory theTopoextensionFactory = (TopoextensionFactory) EPackage.Registry.INSTANCE.getEFactory(TopoextensionPackage.eNS_URI);
            if (theTopoextensionFactory != null) {
                return theTopoextensionFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new TopoextensionFactoryImpl();
    }

    /**
     * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public TopoextensionFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
        case TopoextensionPackage.EXTENSION_REPOSITORY:
            return createExtensionRepository();
        case TopoextensionPackage.REPLICATION:
            return createReplication();
        case TopoextensionPackage.COMPLETION:
            return createCompletion();
        default:
            throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public ExtensionRepository createExtensionRepository() {
        ExtensionRepositoryImpl extensionRepository = new ExtensionRepositoryImpl();
        return extensionRepository;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Replication createReplication() {
        ReplicationImpl replication = new ReplicationImpl();
        return replication;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Completion createCompletion() {
        CompletionImpl completion = new CompletionImpl();
        return completion;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public TopoextensionPackage getTopoextensionPackage() {
        return (TopoextensionPackage) getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @deprecated
     * @generated
     */
    @Deprecated
    public static TopoextensionPackage getPackage() {
        return TopoextensionPackage.eINSTANCE;
    }

} //TopoextensionFactoryImpl
