/**
 */
package smartgridoutput.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import smartgridoutput.Cluster;
import smartgridoutput.On;
import smartgridoutput.SmartgridoutputPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>On</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link smartgridoutput.impl.OnImpl#getBelongsToCluster <em>Belongs To Cluster</em>}</li>
 *   <li>{@link smartgridoutput.impl.OnImpl#isIsHacked <em>Is Hacked</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class OnImpl extends EntityStateImpl implements On {
    /**
     * The cached value of the '{@link #getBelongsToCluster() <em>Belongs To Cluster</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBelongsToCluster()
     * @generated
     * @ordered
     */
    protected Cluster belongsToCluster;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OnImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SmartgridoutputPackage.Literals.ON;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Cluster getBelongsToCluster() {
        if (belongsToCluster != null && belongsToCluster.eIsProxy()) {
            InternalEObject oldBelongsToCluster = (InternalEObject)belongsToCluster;
            belongsToCluster = (Cluster)eResolveProxy(oldBelongsToCluster);
            if (belongsToCluster != oldBelongsToCluster) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SmartgridoutputPackage.ON__BELONGS_TO_CLUSTER, oldBelongsToCluster, belongsToCluster));
            }
        }
        return belongsToCluster;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Cluster basicGetBelongsToCluster() {
        return belongsToCluster;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBelongsToCluster(Cluster newBelongsToCluster, NotificationChain msgs) {
        Cluster oldBelongsToCluster = belongsToCluster;
        belongsToCluster = newBelongsToCluster;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SmartgridoutputPackage.ON__BELONGS_TO_CLUSTER, oldBelongsToCluster, newBelongsToCluster);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBelongsToCluster(Cluster newBelongsToCluster) {
        if (newBelongsToCluster != belongsToCluster) {
            NotificationChain msgs = null;
            if (belongsToCluster != null)
                msgs = ((InternalEObject)belongsToCluster).eInverseRemove(this, SmartgridoutputPackage.CLUSTER__HAS_ENTITIES, Cluster.class, msgs);
            if (newBelongsToCluster != null)
                msgs = ((InternalEObject)newBelongsToCluster).eInverseAdd(this, SmartgridoutputPackage.CLUSTER__HAS_ENTITIES, Cluster.class, msgs);
            msgs = basicSetBelongsToCluster(newBelongsToCluster, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SmartgridoutputPackage.ON__BELONGS_TO_CLUSTER, newBelongsToCluster, newBelongsToCluster));
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
            eNotify(new ENotificationImpl(this, Notification.SET, SmartgridoutputPackage.ON__IS_HACKED, oldIsHacked, isHacked));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case SmartgridoutputPackage.ON__BELONGS_TO_CLUSTER:
                if (belongsToCluster != null)
                    msgs = ((InternalEObject)belongsToCluster).eInverseRemove(this, SmartgridoutputPackage.CLUSTER__HAS_ENTITIES, Cluster.class, msgs);
                return basicSetBelongsToCluster((Cluster)otherEnd, msgs);
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
            case SmartgridoutputPackage.ON__BELONGS_TO_CLUSTER:
                return basicSetBelongsToCluster(null, msgs);
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
            case SmartgridoutputPackage.ON__BELONGS_TO_CLUSTER:
                if (resolve) return getBelongsToCluster();
                return basicGetBelongsToCluster();
            case SmartgridoutputPackage.ON__IS_HACKED:
                return isIsHacked();
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
            case SmartgridoutputPackage.ON__BELONGS_TO_CLUSTER:
                setBelongsToCluster((Cluster)newValue);
                return;
            case SmartgridoutputPackage.ON__IS_HACKED:
                setIsHacked((Boolean)newValue);
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
            case SmartgridoutputPackage.ON__BELONGS_TO_CLUSTER:
                setBelongsToCluster((Cluster)null);
                return;
            case SmartgridoutputPackage.ON__IS_HACKED:
                setIsHacked(IS_HACKED_EDEFAULT);
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
            case SmartgridoutputPackage.ON__BELONGS_TO_CLUSTER:
                return belongsToCluster != null;
            case SmartgridoutputPackage.ON__IS_HACKED:
                return isHacked != IS_HACKED_EDEFAULT;
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
        result.append(" (IsHacked: ");
        result.append(isHacked);
        result.append(')');
        return result.toString();
    }

} //OnImpl
