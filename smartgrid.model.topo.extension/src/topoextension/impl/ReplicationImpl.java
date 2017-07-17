/**
 */
package topoextension.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import topoextension.Replication;
import topoextension.TopoextensionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Replication</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link topoextension.impl.ReplicationImpl#getNrOfReplicas <em>Nr Of Replicas</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReplicationImpl extends MinimalEObjectImpl.Container implements Replication {
    /**
     * The default value of the '{@link #getNrOfReplicas() <em>Nr Of Replicas</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNrOfReplicas()
     * @generated
     * @ordered
     */
    protected static final int NR_OF_REPLICAS_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getNrOfReplicas() <em>Nr Of Replicas</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNrOfReplicas()
     * @generated
     * @ordered
     */
    protected int nrOfReplicas = NR_OF_REPLICAS_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ReplicationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return TopoextensionPackage.Literals.REPLICATION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getNrOfReplicas() {
        return nrOfReplicas;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNrOfReplicas(int newNrOfReplicas) {
        int oldNrOfReplicas = nrOfReplicas;
        nrOfReplicas = newNrOfReplicas;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TopoextensionPackage.REPLICATION__NR_OF_REPLICAS, oldNrOfReplicas, nrOfReplicas));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case TopoextensionPackage.REPLICATION__NR_OF_REPLICAS:
                return getNrOfReplicas();
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
            case TopoextensionPackage.REPLICATION__NR_OF_REPLICAS:
                setNrOfReplicas((Integer)newValue);
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
            case TopoextensionPackage.REPLICATION__NR_OF_REPLICAS:
                setNrOfReplicas(NR_OF_REPLICAS_EDEFAULT);
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
            case TopoextensionPackage.REPLICATION__NR_OF_REPLICAS:
                return nrOfReplicas != NR_OF_REPLICAS_EDEFAULT;
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
        result.append(" (nrOfReplicas: ");
        result.append(nrOfReplicas);
        result.append(')');
        return result.toString();
    }

} //ReplicationImpl
