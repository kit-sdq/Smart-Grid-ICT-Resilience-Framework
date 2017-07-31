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

import topoextension.Aggregation;
import topoextension.ExtensionRepository;
import topoextension.Replication;
import topoextension.TopoextensionPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Extension
 * Repository</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link topoextension.impl.ExtensionRepositoryImpl#getReplications <em>Replications</em>}</li>
 * <li>{@link topoextension.impl.ExtensionRepositoryImpl#getAggregations <em>Aggregations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExtensionRepositoryImpl extends MinimalEObjectImpl.Container implements ExtensionRepository {
    /**
     * The cached value of the '{@link #getReplications() <em>Replications</em>}' containment
     * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getReplications()
     * @generated
     * @ordered
     */
    protected EList<Replication> replications;

    /**
     * The cached value of the '{@link #getAggregations() <em>Aggregations</em>}' containment
     * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAggregations()
     * @generated
     * @ordered
     */
    protected EList<Aggregation> aggregations;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected ExtensionRepositoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return TopoextensionPackage.Literals.EXTENSION_REPOSITORY;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EList<Replication> getReplications() {
        if (replications == null) {
            replications = new EObjectContainmentEList<>(Replication.class, this, TopoextensionPackage.EXTENSION_REPOSITORY__REPLICATIONS);
        }
        return replications;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EList<Aggregation> getAggregations() {
        if (aggregations == null) {
            aggregations = new EObjectContainmentEList<>(Aggregation.class, this, TopoextensionPackage.EXTENSION_REPOSITORY__AGGREGATIONS);
        }
        return aggregations;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case TopoextensionPackage.EXTENSION_REPOSITORY__REPLICATIONS:
            return ((InternalEList<?>) getReplications()).basicRemove(otherEnd, msgs);
        case TopoextensionPackage.EXTENSION_REPOSITORY__AGGREGATIONS:
            return ((InternalEList<?>) getAggregations()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case TopoextensionPackage.EXTENSION_REPOSITORY__REPLICATIONS:
            return getReplications();
        case TopoextensionPackage.EXTENSION_REPOSITORY__AGGREGATIONS:
            return getAggregations();
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
        case TopoextensionPackage.EXTENSION_REPOSITORY__REPLICATIONS:
            getReplications().clear();
            getReplications().addAll((Collection<? extends Replication>) newValue);
            return;
        case TopoextensionPackage.EXTENSION_REPOSITORY__AGGREGATIONS:
            getAggregations().clear();
            getAggregations().addAll((Collection<? extends Aggregation>) newValue);
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
        case TopoextensionPackage.EXTENSION_REPOSITORY__REPLICATIONS:
            getReplications().clear();
            return;
        case TopoextensionPackage.EXTENSION_REPOSITORY__AGGREGATIONS:
            getAggregations().clear();
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
        case TopoextensionPackage.EXTENSION_REPOSITORY__REPLICATIONS:
            return replications != null && !replications.isEmpty();
        case TopoextensionPackage.EXTENSION_REPOSITORY__AGGREGATIONS:
            return aggregations != null && !aggregations.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ExtensionRepositoryImpl
