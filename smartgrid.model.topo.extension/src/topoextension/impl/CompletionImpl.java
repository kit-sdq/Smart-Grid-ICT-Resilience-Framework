/**
 */
package topoextension.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import smartgridtopo.NetworkEntity;

import topoextension.Completion;
import topoextension.TopoextensionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Completion</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link topoextension.impl.CompletionImpl#getNetworkentity <em>Networkentity</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CompletionImpl extends MinimalEObjectImpl.Container implements Completion {
	/**
	 * The cached value of the '{@link #getNetworkentity() <em>Networkentity</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNetworkentity()
	 * @generated
	 * @ordered
	 */
	protected EList<NetworkEntity> networkentity;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CompletionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TopoextensionPackage.Literals.COMPLETION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NetworkEntity> getNetworkentity() {
		if (networkentity == null) {
			networkentity = new EObjectContainmentEList<NetworkEntity>(NetworkEntity.class, this, TopoextensionPackage.COMPLETION__NETWORKENTITY);
		}
		return networkentity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TopoextensionPackage.COMPLETION__NETWORKENTITY:
				return ((InternalEList<?>)getNetworkentity()).basicRemove(otherEnd, msgs);
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
			case TopoextensionPackage.COMPLETION__NETWORKENTITY:
				return getNetworkentity();
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
			case TopoextensionPackage.COMPLETION__NETWORKENTITY:
				getNetworkentity().clear();
				getNetworkentity().addAll((Collection<? extends NetworkEntity>)newValue);
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
			case TopoextensionPackage.COMPLETION__NETWORKENTITY:
				getNetworkentity().clear();
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
			case TopoextensionPackage.COMPLETION__NETWORKENTITY:
				return networkentity != null && !networkentity.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //CompletionImpl
