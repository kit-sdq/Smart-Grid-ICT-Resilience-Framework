/**
 */
package topoextension;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Extension
 * Repository</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link topoextension.ExtensionRepository#getReplications <em>Replications</em>}</li>
 * <li>{@link topoextension.ExtensionRepository#getAggregations <em>Aggregations</em>}</li>
 * </ul>
 *
 * @see topoextension.TopoextensionPackage#getExtensionRepository()
 * @model
 * @generated
 */
public interface ExtensionRepository extends EObject {
    /**
     * Returns the value of the '<em><b>Replications</b></em>' containment reference list. The list
     * contents are of type {@link topoextension.Replication}. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Replications</em>' containment reference list isn't clear, there
     * really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Replications</em>' containment reference list.
     * @see topoextension.TopoextensionPackage#getExtensionRepository_Replications()
     * @model containment="true"
     * @generated
     */
    EList<Replication> getReplications();

    /**
     * Returns the value of the '<em><b>Aggregations</b></em>' containment reference list. The list
     * contents are of type {@link topoextension.Aggregation}. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Aggregations</em>' containment reference list isn't clear, there
     * really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Aggregations</em>' containment reference list.
     * @see topoextension.TopoextensionPackage#getExtensionRepository_Aggregations()
     * @model containment="true"
     * @generated
     */
    EList<Aggregation> getAggregations();

} // ExtensionRepository
