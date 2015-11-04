/**
 */
package smartgridoutput.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import smartgridoutput.Cluster;
import smartgridoutput.On;
import smartgridoutput.SmartgridoutputPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cluster</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link smartgridoutput.impl.ClusterImpl#getControlCenterCount <em>Control Center Count</em>}</li>
 *   <li>{@link smartgridoutput.impl.ClusterImpl#getSmartMeterCount <em>Smart Meter Count</em>}</li>
 *   <li>{@link smartgridoutput.impl.ClusterImpl#getHasEntities <em>Has Entities</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ClusterImpl extends MinimalEObjectImpl.Container implements Cluster {
	/**
	 * The default value of the '{@link #getControlCenterCount() <em>Control Center Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getControlCenterCount()
	 * @generated
	 * @ordered
	 */
	protected static final int CONTROL_CENTER_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getControlCenterCount() <em>Control Center Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getControlCenterCount()
	 * @generated
	 * @ordered
	 */
	protected int controlCenterCount = CONTROL_CENTER_COUNT_EDEFAULT;

	/**
	 * The default value of the '{@link #getSmartMeterCount() <em>Smart Meter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSmartMeterCount()
	 * @generated
	 * @ordered
	 */
	protected static final int SMART_METER_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSmartMeterCount() <em>Smart Meter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSmartMeterCount()
	 * @generated
	 * @ordered
	 */
	protected int smartMeterCount = SMART_METER_COUNT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getHasEntities() <em>Has Entities</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHasEntities()
	 * @generated
	 * @ordered
	 */
	protected EList<On> hasEntities;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ClusterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SmartgridoutputPackage.Literals.CLUSTER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getControlCenterCount() {
		return controlCenterCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setControlCenterCount(int newControlCenterCount) {
		int oldControlCenterCount = controlCenterCount;
		controlCenterCount = newControlCenterCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmartgridoutputPackage.CLUSTER__CONTROL_CENTER_COUNT, oldControlCenterCount, controlCenterCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSmartMeterCount() {
		return smartMeterCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSmartMeterCount(int newSmartMeterCount) {
		int oldSmartMeterCount = smartMeterCount;
		smartMeterCount = newSmartMeterCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmartgridoutputPackage.CLUSTER__SMART_METER_COUNT, oldSmartMeterCount, smartMeterCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<On> getHasEntities() {
		if (hasEntities == null) {
			hasEntities = new EObjectWithInverseResolvingEList<On>(On.class, this, SmartgridoutputPackage.CLUSTER__HAS_ENTITIES, SmartgridoutputPackage.ON__BELONGS_TO_CLUSTER);
		}
		return hasEntities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SmartgridoutputPackage.CLUSTER__HAS_ENTITIES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getHasEntities()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SmartgridoutputPackage.CLUSTER__HAS_ENTITIES:
				return ((InternalEList<?>)getHasEntities()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SmartgridoutputPackage.CLUSTER__CONTROL_CENTER_COUNT:
				return getControlCenterCount();
			case SmartgridoutputPackage.CLUSTER__SMART_METER_COUNT:
				return getSmartMeterCount();
			case SmartgridoutputPackage.CLUSTER__HAS_ENTITIES:
				return getHasEntities();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SmartgridoutputPackage.CLUSTER__CONTROL_CENTER_COUNT:
				setControlCenterCount((Integer)newValue);
				return;
			case SmartgridoutputPackage.CLUSTER__SMART_METER_COUNT:
				setSmartMeterCount((Integer)newValue);
				return;
			case SmartgridoutputPackage.CLUSTER__HAS_ENTITIES:
				getHasEntities().clear();
				getHasEntities().addAll((Collection<? extends On>)newValue);
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
			case SmartgridoutputPackage.CLUSTER__CONTROL_CENTER_COUNT:
				setControlCenterCount(CONTROL_CENTER_COUNT_EDEFAULT);
				return;
			case SmartgridoutputPackage.CLUSTER__SMART_METER_COUNT:
				setSmartMeterCount(SMART_METER_COUNT_EDEFAULT);
				return;
			case SmartgridoutputPackage.CLUSTER__HAS_ENTITIES:
				getHasEntities().clear();
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
			case SmartgridoutputPackage.CLUSTER__CONTROL_CENTER_COUNT:
				return controlCenterCount != CONTROL_CENTER_COUNT_EDEFAULT;
			case SmartgridoutputPackage.CLUSTER__SMART_METER_COUNT:
				return smartMeterCount != SMART_METER_COUNT_EDEFAULT;
			case SmartgridoutputPackage.CLUSTER__HAS_ENTITIES:
				return hasEntities != null && !hasEntities.isEmpty();
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
		result.append(" (ControlCenterCount: ");
		result.append(controlCenterCount);
		result.append(", SmartMeterCount: ");
		result.append(smartMeterCount);
		result.append(')');
		return result.toString();
	}

} //ClusterImpl
