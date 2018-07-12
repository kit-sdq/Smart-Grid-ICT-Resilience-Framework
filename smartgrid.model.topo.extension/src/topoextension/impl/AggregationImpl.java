/**
 */
package topoextension.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import smartgridtopo.NetworkEntity;
import topoextension.Aggregation;
import topoextension.TopoextensionPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Aggregation</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link topoextension.impl.AggregationImpl#getSmartMeterReplicas <em>Smart Meter
 * Replicas</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AggregationImpl extends MinimalEObjectImpl.Container implements Aggregation {
    /**
     * The cached value of the '{@link #getSmartMeterReplicas() <em>Smart Meter Replicas</em>}'
     * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getSmartMeterReplicas()
     * @generated
     * @ordered
     */
    protected EList<NetworkEntity> smartMeterReplicas;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected AggregationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return TopoextensionPackage.Literals.AGGREGATION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EList<NetworkEntity> getSmartMeterReplicas() {
        if (smartMeterReplicas == null) {
            smartMeterReplicas = new EObjectResolvingEList<>(NetworkEntity.class, this, TopoextensionPackage.AGGREGATION__SMART_METER_REPLICAS);
        }
        return smartMeterReplicas;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case TopoextensionPackage.AGGREGATION__SMART_METER_REPLICAS:
            return getSmartMeterReplicas();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case TopoextensionPackage.AGGREGATION__SMART_METER_REPLICAS:
            getSmartMeterReplicas().clear();
            getSmartMeterReplicas().addAll((Collection<? extends NetworkEntity>) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case TopoextensionPackage.AGGREGATION__SMART_METER_REPLICAS:
            getSmartMeterReplicas().clear();
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case TopoextensionPackage.AGGREGATION__SMART_METER_REPLICAS:
            return smartMeterReplicas != null && !smartMeterReplicas.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //AggregationImpl
