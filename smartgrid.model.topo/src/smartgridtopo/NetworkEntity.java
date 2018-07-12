/**
 */
package smartgridtopo;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Network Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link smartgridtopo.NetworkEntity#getConnectedTo <em>Connected To</em>}</li>
 * <li>{@link smartgridtopo.NetworkEntity#getLinkedBy <em>Linked By</em>}</li>
 * <li>{@link smartgridtopo.NetworkEntity#getXCoord <em>XCoord</em>}</li>
 * <li>{@link smartgridtopo.NetworkEntity#getYCoord <em>YCoord</em>}</li>
 * </ul>
 *
 * @see smartgridtopo.SmartgridtopoPackage#getNetworkEntity()
 * @model abstract="true"
 * @generated
 */
public interface NetworkEntity extends NamedIdentifier {
    /**
     * Returns the value of the '<em><b>Connected To</b></em>' reference list. The list contents are
     * of type {@link smartgridtopo.PowerGridNode}. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Connected To</em>' reference list isn't clear, there really should
     * be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Connected To</em>' reference list.
     * @see smartgridtopo.SmartgridtopoPackage#getNetworkEntity_ConnectedTo()
     * @model required="true"
     * @generated
     */
    EList<PowerGridNode> getConnectedTo();

    /**
     * Returns the value of the '<em><b>Linked By</b></em>' reference list. The list contents are of
     * type {@link smartgridtopo.PhysicalConnection}. It is bidirectional and its opposite is
     * '{@link smartgridtopo.PhysicalConnection#getLinks <em>Links</em>}'. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Linked By</em>' reference list isn't clear, there really should be
     * more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Linked By</em>' reference list.
     * @see smartgridtopo.SmartgridtopoPackage#getNetworkEntity_LinkedBy()
     * @see smartgridtopo.PhysicalConnection#getLinks
     * @model opposite="Links"
     * @generated
     */
    EList<PhysicalConnection> getLinkedBy();

    /**
     * Returns the value of the '<em><b>XCoord</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XCoord</em>' attribute isn't clear, there really should be more of
     * a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>XCoord</em>' attribute.
     * @see #setXCoord(double)
     * @see smartgridtopo.SmartgridtopoPackage#getNetworkEntity_XCoord()
     * @model
     * @generated
     */
    double getXCoord();

    /**
     * Sets the value of the '{@link smartgridtopo.NetworkEntity#getXCoord <em>XCoord</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>XCoord</em>' attribute.
     * @see #getXCoord()
     * @generated
     */
    void setXCoord(double value);

    /**
     * Returns the value of the '<em><b>YCoord</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>YCoord</em>' attribute isn't clear, there really should be more of
     * a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>YCoord</em>' attribute.
     * @see #setYCoord(double)
     * @see smartgridtopo.SmartgridtopoPackage#getNetworkEntity_YCoord()
     * @model
     * @generated
     */
    double getYCoord();

    /**
     * Sets the value of the '{@link smartgridtopo.NetworkEntity#getYCoord <em>YCoord</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>YCoord</em>' attribute.
     * @see #getYCoord()
     * @generated
     */
    void setYCoord(double value);

} // NetworkEntity
