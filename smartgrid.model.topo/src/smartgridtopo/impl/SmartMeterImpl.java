/**
 */
package smartgridtopo.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import smartgridtopo.CommunicatingEntity;
import smartgridtopo.LogicalCommunication;
import smartgridtopo.SmartMeter;
import smartgridtopo.SmartMeterType;
import smartgridtopo.SmartgridtopoPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Smart Meter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link smartgridtopo.impl.SmartMeterImpl#getCommunicatesBy <em>Communicates By</em>}</li>
 *   <li>{@link smartgridtopo.impl.SmartMeterImpl#getIsA <em>Is A</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SmartMeterImpl extends NetworkEntityImpl implements SmartMeter {
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
     * The cached value of the '{@link #getIsA() <em>Is A</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIsA()
     * @generated
     * @ordered
     */
    protected SmartMeterType isA;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SmartMeterImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SmartgridtopoPackage.Literals.SMART_METER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<LogicalCommunication> getCommunicatesBy() {
        if (communicatesBy == null) {
            communicatesBy = new EObjectWithInverseResolvingEList.ManyInverse<LogicalCommunication>(LogicalCommunication.class, this, SmartgridtopoPackage.SMART_METER__COMMUNICATES_BY, SmartgridtopoPackage.LOGICAL_COMMUNICATION__LINKS);
        }
        return communicatesBy;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SmartMeterType getIsA() {
        if (isA != null && isA.eIsProxy()) {
            InternalEObject oldIsA = (InternalEObject)isA;
            isA = (SmartMeterType)eResolveProxy(oldIsA);
            if (isA != oldIsA) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SmartgridtopoPackage.SMART_METER__IS_A, oldIsA, isA));
            }
        }
        return isA;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SmartMeterType basicGetIsA() {
        return isA;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsA(SmartMeterType newIsA) {
        SmartMeterType oldIsA = isA;
        isA = newIsA;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SmartgridtopoPackage.SMART_METER__IS_A, oldIsA, isA));
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
            case SmartgridtopoPackage.SMART_METER__COMMUNICATES_BY:
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
            case SmartgridtopoPackage.SMART_METER__COMMUNICATES_BY:
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
            case SmartgridtopoPackage.SMART_METER__COMMUNICATES_BY:
                return getCommunicatesBy();
            case SmartgridtopoPackage.SMART_METER__IS_A:
                if (resolve) return getIsA();
                return basicGetIsA();
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
            case SmartgridtopoPackage.SMART_METER__COMMUNICATES_BY:
                getCommunicatesBy().clear();
                getCommunicatesBy().addAll((Collection<? extends LogicalCommunication>)newValue);
                return;
            case SmartgridtopoPackage.SMART_METER__IS_A:
                setIsA((SmartMeterType)newValue);
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
            case SmartgridtopoPackage.SMART_METER__COMMUNICATES_BY:
                getCommunicatesBy().clear();
                return;
            case SmartgridtopoPackage.SMART_METER__IS_A:
                setIsA((SmartMeterType)null);
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
            case SmartgridtopoPackage.SMART_METER__COMMUNICATES_BY:
                return communicatesBy != null && !communicatesBy.isEmpty();
            case SmartgridtopoPackage.SMART_METER__IS_A:
                return isA != null;
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
                case SmartgridtopoPackage.SMART_METER__COMMUNICATES_BY: return SmartgridtopoPackage.COMMUNICATING_ENTITY__COMMUNICATES_BY;
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
                case SmartgridtopoPackage.COMMUNICATING_ENTITY__COMMUNICATES_BY: return SmartgridtopoPackage.SMART_METER__COMMUNICATES_BY;
                default: return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

} //SmartMeterImpl
