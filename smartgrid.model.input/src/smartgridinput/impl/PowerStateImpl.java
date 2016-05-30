/**
 */
package smartgridinput.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import smartgridinput.PowerState;
import smartgridinput.SmartgridinputPackage;
import smartgridtopo.PowerGridNode;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Power State</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link smartgridinput.impl.PowerStateImpl#getOwner <em>Owner</em>}</li>
 *   <li>{@link smartgridinput.impl.PowerStateImpl#isPowerOutage <em>Power Outage</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PowerStateImpl extends MinimalEObjectImpl.Container implements PowerState {
	/**
	 * The cached value of the '{@link #getOwner() <em>Owner</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwner()
	 * @generated
	 * @ordered
	 */
	protected PowerGridNode owner;

	/**
	 * The default value of the '{@link #isPowerOutage() <em>Power Outage</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPowerOutage()
	 * @generated
	 * @ordered
	 */
	protected static final boolean POWER_OUTAGE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPowerOutage() <em>Power Outage</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPowerOutage()
	 * @generated
	 * @ordered
	 */
	protected boolean powerOutage = POWER_OUTAGE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PowerStateImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SmartgridinputPackage.Literals.POWER_STATE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PowerGridNode getOwner() {
		if (owner != null && owner.eIsProxy()) {
			InternalEObject oldOwner = (InternalEObject)owner;
			owner = (PowerGridNode)eResolveProxy(oldOwner);
			if (owner != oldOwner) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SmartgridinputPackage.POWER_STATE__OWNER, oldOwner, owner));
			}
		}
		return owner;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PowerGridNode basicGetOwner() {
		return owner;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOwner(PowerGridNode newOwner) {
		PowerGridNode oldOwner = owner;
		owner = newOwner;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmartgridinputPackage.POWER_STATE__OWNER, oldOwner, owner));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isPowerOutage() {
		return powerOutage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPowerOutage(boolean newPowerOutage) {
		boolean oldPowerOutage = powerOutage;
		powerOutage = newPowerOutage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmartgridinputPackage.POWER_STATE__POWER_OUTAGE, oldPowerOutage, powerOutage));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SmartgridinputPackage.POWER_STATE__OWNER:
				if (resolve) return getOwner();
				return basicGetOwner();
			case SmartgridinputPackage.POWER_STATE__POWER_OUTAGE:
				return isPowerOutage();
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
			case SmartgridinputPackage.POWER_STATE__OWNER:
				setOwner((PowerGridNode)newValue);
				return;
			case SmartgridinputPackage.POWER_STATE__POWER_OUTAGE:
				setPowerOutage((Boolean)newValue);
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
			case SmartgridinputPackage.POWER_STATE__OWNER:
				setOwner((PowerGridNode)null);
				return;
			case SmartgridinputPackage.POWER_STATE__POWER_OUTAGE:
				setPowerOutage(POWER_OUTAGE_EDEFAULT);
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
			case SmartgridinputPackage.POWER_STATE__OWNER:
				return owner != null;
			case SmartgridinputPackage.POWER_STATE__POWER_OUTAGE:
				return powerOutage != POWER_OUTAGE_EDEFAULT;
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (PowerOutage: ");
		result.append(powerOutage);
		result.append(')');
		return result.toString();
	}

} //PowerStateImpl
