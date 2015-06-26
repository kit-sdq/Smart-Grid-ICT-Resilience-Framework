/**
 */
package smartgridtopo.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import smartgridtopo.CommunicatingEntity;
import smartgridtopo.ControlCenter;
import smartgridtopo.LogicalCommunication;
import smartgridtopo.SmartgridtopoPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Control Center</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link smartgridtopo.impl.ControlCenterImpl#getCommunicatesBy <em>Communicates By</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ControlCenterImpl extends NetworkEntityImpl implements ControlCenter {
    /**
     * The cached value of the '{@link #getCommunicatesBy() <em>Communicates By</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCommunicatesBy()
     * @generated
     * @ordered
     */
    protected EList<LogicalCommunication> communicatesBy;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ControlCenterImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SmartgridtopoPackage.Literals.CONTROL_CENTER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<LogicalCommunication> getCommunicatesBy() {
        if (communicatesBy == null) {
            communicatesBy = new EObjectWithInverseResolvingEList.ManyInverse<LogicalCommunication>(LogicalCommunication.class, this, SmartgridtopoPackage.CONTROL_CENTER__COMMUNICATES_BY, SmartgridtopoPackage.LOGICAL_COMMUNICATION__LINKS);
        }
        return communicatesBy;
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
            case SmartgridtopoPackage.CONTROL_CENTER__COMMUNICATES_BY:
                return ((InternalEList<InternalEObject>)(InternalEList<?>)getCommunicatesBy()).basicAdd(otherEnd, msgs);
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
            case SmartgridtopoPackage.CONTROL_CENTER__COMMUNICATES_BY:
                return ((InternalEList<?>)getCommunicatesBy()).basicRemove(otherEnd, msgs);
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
            case SmartgridtopoPackage.CONTROL_CENTER__COMMUNICATES_BY:
                return getCommunicatesBy();
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
            case SmartgridtopoPackage.CONTROL_CENTER__COMMUNICATES_BY:
                getCommunicatesBy().clear();
                getCommunicatesBy().addAll((Collection<? extends LogicalCommunication>)newValue);
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
            case SmartgridtopoPackage.CONTROL_CENTER__COMMUNICATES_BY:
                getCommunicatesBy().clear();
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
            case SmartgridtopoPackage.CONTROL_CENTER__COMMUNICATES_BY:
                return communicatesBy != null && !communicatesBy.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == CommunicatingEntity.class) {
            switch (derivedFeatureID) {
                case SmartgridtopoPackage.CONTROL_CENTER__COMMUNICATES_BY: return SmartgridtopoPackage.COMMUNICATING_ENTITY__COMMUNICATES_BY;
                default: return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == CommunicatingEntity.class) {
            switch (baseFeatureID) {
                case SmartgridtopoPackage.COMMUNICATING_ENTITY__COMMUNICATES_BY: return SmartgridtopoPackage.CONTROL_CENTER__COMMUNICATES_BY;
                default: return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

} //ControlCenterImpl
