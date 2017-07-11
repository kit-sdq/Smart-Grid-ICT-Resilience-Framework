/**
 */
package topoextension;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each
 * non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see topoextension.TopoextensionPackage
 * @generated
 */
public interface TopoextensionFactory extends EFactory {
    /**
     * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    TopoextensionFactory eINSTANCE = topoextension.impl.TopoextensionFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Extension Repository</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return a new object of class '<em>Extension Repository</em>'.
     * @generated
     */
    ExtensionRepository createExtensionRepository();

    /**
     * Returns a new object of class '<em>Replication</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return a new object of class '<em>Replication</em>'.
     * @generated
     */
    Replication createReplication();

    /**
     * Returns a new object of class '<em>Completion</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return a new object of class '<em>Completion</em>'.
     * @generated
     */
    Completion createCompletion();

    /**
     * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the package supported by this factory.
     * @generated
     */
    TopoextensionPackage getTopoextensionPackage();

} //TopoextensionFactory
