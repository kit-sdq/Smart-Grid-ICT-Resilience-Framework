/**
 */
package smartgridinput.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import smartgridinput.EntityState;
import smartgridinput.SmartgridinputPackage;

import smartgridtopo.NetworkEntity;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Entity State</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link smartgridinput.impl.EntityStateImpl#isIsDestroyed <em>Is Destroyed</em>}</li>
 *   <li>{@link smartgridinput.impl.EntityStateImpl#isIsHacked <em>Is Hacked</em>}</li>
 *   <li>{@link smartgridinput.impl.EntityStateImpl#getOwner <em>Owner</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EntityStateImpl extends MinimalEObjectImpl.Container implements EntityState {
    /**
     * The default value of the '{@link #isIsDestroyed() <em>Is Destroyed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsDestroyed()
     * @generated
     * @ordered
     */
    protected static final boolean IS_DESTROYED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsDestroyed() <em>Is Destroyed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsDestroyed()
     * @generated
     * @ordered
     */
    protected boolean isDestroyed = IS_DESTROYED_EDEFAULT;

    /**
     * The default value of the '{@link #isIsHacked() <em>Is Hacked</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsHacked()
     * @generated
     * @ordered
     */
    protected static final boolean IS_HACKED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsHacked() <em>Is Hacked</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsHacked()
     * @generated
     * @ordered
     */
    protected boolean isHacked = IS_HACKED_EDEFAULT;

    /**
     * The cached value of the '{@link #getOwner() <em>Owner</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOwner()
     * @generated
     * @ordered
     */
    protected NetworkEntity owner;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EntityStateImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SmartgridinputPackage.Literals.ENTITY_STATE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsDestroyed() {
        return isDestroyed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsDestroyed(boolean newIsDestroyed) {
        boolean oldIsDestroyed = isDestroyed;
        isDestroyed = newIsDestroyed;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SmartgridinputPackage.ENTITY_STATE__IS_DESTROYED, oldIsDestroyed, isDestroyed));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsHacked() {
        return isHacked;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsHacked(boolean newIsHacked) {
        boolean oldIsHacked = isHacked;
        isHacked = newIsHacked;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SmartgridinputPackage.ENTITY_STATE__IS_HACKED, oldIsHacked, isHacked));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NetworkEntity getOwner() {
        if (owner != null && owner.eIsProxy()) {
            InternalEObject oldOwner = (InternalEObject)owner;
            owner = (NetworkEntity)eResolveProxy(oldOwner);
            if (owner != oldOwner) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SmartgridinputPackage.ENTITY_STATE__OWNER, oldOwner, owner));
            }
        }
        return owner;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NetworkEntity basicGetOwner() {
        return owner;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOwner(NetworkEntity newOwner) {
        NetworkEntity oldOwner = owner;
        owner = newOwner;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SmartgridinputPackage.ENTITY_STATE__OWNER, oldOwner, owner));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SmartgridinputPackage.ENTITY_STATE__IS_DESTROYED:
                return isIsDestroyed();
            case SmartgridinputPackage.ENTITY_STATE__IS_HACKED:
                return isIsHacked();
            case SmartgridinputPackage.ENTITY_STATE__OWNER:
                if (resolve) return getOwner();
                return basicGetOwner();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SmartgridinputPackage.ENTITY_STATE__IS_DESTROYED:
                setIsDestroyed((Boolean)newValue);
                return;
            case SmartgridinputPackage.ENTITY_STATE__IS_HACKED:
                setIsHacked((Boolean)newValue);
                return;
            case SmartgridinputPackage.ENTITY_STATE__OWNER:
                setOwner((NetworkEntity)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SmartgridinputPackage.ENTITY_STATE__IS_DESTROYED:
                setIsDestroyed(IS_DESTROYED_EDEFAULT);
                return;
            case SmartgridinputPackage.ENTITY_STATE__IS_HACKED:
                setIsHacked(IS_HACKED_EDEFAULT);
                return;
            case SmartgridinputPackage.ENTITY_STATE__OWNER:
                setOwner((NetworkEntity)null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SmartgridinputPackage.ENTITY_STATE__IS_DESTROYED:
                return isDestroyed != IS_DESTROYED_EDEFAULT;
            case SmartgridinputPackage.ENTITY_STATE__IS_HACKED:
                return isHacked != IS_HACKED_EDEFAULT;
            case SmartgridinputPackage.ENTITY_STATE__OWNER:
                return owner != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (IsDestroyed: ");
        result.append(isDestroyed);
        result.append(", IsHacked: ");
        result.append(isHacked);
        result.append(')');
        return result.toString();
    }

} //EntityStateImpl
