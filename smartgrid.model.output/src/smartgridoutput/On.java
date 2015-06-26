/**
 */
package smartgridoutput;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>On</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link smartgridoutput.On#getBelongsToCluster <em>Belongs To Cluster</em>}</li>
 *   <li>{@link smartgridoutput.On#isIsHacked <em>Is Hacked</em>}</li>
 * </ul>
 * </p>
 *
 * @see smartgridoutput.SmartgridoutputPackage#getOn()
 * @model abstract="true"
 * @generated
 */
public interface On extends EntityState {
    /**
     * Returns the value of the '<em><b>Belongs To Cluster</b></em>' reference.
     * It is bidirectional and its opposite is '{@link smartgridoutput.Cluster#getHasEntities <em>Has Entities</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Belongs To Cluster</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Belongs To Cluster</em>' reference.
     * @see #setBelongsToCluster(Cluster)
     * @see smartgridoutput.SmartgridoutputPackage#getOn_BelongsToCluster()
     * @see smartgridoutput.Cluster#getHasEntities
     * @model opposite="HasEntities"
     * @generated
     */
    Cluster getBelongsToCluster();

    /**
     * Sets the value of the '{@link smartgridoutput.On#getBelongsToCluster <em>Belongs To Cluster</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Belongs To Cluster</em>' reference.
     * @see #getBelongsToCluster()
     * @generated
     */
    void setBelongsToCluster(Cluster value);

    /**
     * Returns the value of the '<em><b>Is Hacked</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Hacked</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Hacked</em>' attribute.
     * @see #setIsHacked(boolean)
     * @see smartgridoutput.SmartgridoutputPackage#getOn_IsHacked()
     * @model default="false" required="true"
     * @generated
     */
    boolean isIsHacked();

    /**
     * Sets the value of the '{@link smartgridoutput.On#isIsHacked <em>Is Hacked</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Hacked</em>' attribute.
     * @see #isIsHacked()
     * @generated
     */
    void setIsHacked(boolean value);

} // On
