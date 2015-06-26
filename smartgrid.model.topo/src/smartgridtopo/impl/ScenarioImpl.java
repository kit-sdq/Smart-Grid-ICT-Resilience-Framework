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
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import smartgridtopo.LogicalCommunication;
import smartgridtopo.NamedEntity;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PhysicalConnection;
import smartgridtopo.PowerGridNode;
import smartgridtopo.Scenario;
import smartgridtopo.SmartgridtopoPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link smartgridtopo.impl.ScenarioImpl#getId <em>Id</em>}</li>
 *   <li>{@link smartgridtopo.impl.ScenarioImpl#getName <em>Name</em>}</li>
 *   <li>{@link smartgridtopo.impl.ScenarioImpl#getContainsNE <em>Contains NE</em>}</li>
 *   <li>{@link smartgridtopo.impl.ScenarioImpl#getContainsPGN <em>Contains PGN</em>}</li>
 *   <li>{@link smartgridtopo.impl.ScenarioImpl#getContainsC <em>Contains C</em>}</li>
 *   <li>{@link smartgridtopo.impl.ScenarioImpl#getContainsLC <em>Contains LC</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScenarioImpl extends MinimalEObjectImpl.Container implements Scenario {
    /**
     * The default value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected static final int ID_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected int id = ID_EDEFAULT;

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The cached value of the '{@link #getContainsNE() <em>Contains NE</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getContainsNE()
     * @generated
     * @ordered
     */
    protected EList<NetworkEntity> containsNE;

    /**
     * The cached value of the '{@link #getContainsPGN() <em>Contains PGN</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getContainsPGN()
     * @generated
     * @ordered
     */
    protected EList<PowerGridNode> containsPGN;

    /**
     * The cached value of the '{@link #getContainsC() <em>Contains C</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getContainsC()
     * @generated
     * @ordered
     */
    protected EList<PhysicalConnection> containsC;

    /**
     * The cached value of the '{@link #getContainsLC() <em>Contains LC</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getContainsLC()
     * @generated
     * @ordered
     */
    protected EList<LogicalCommunication> containsLC;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ScenarioImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SmartgridtopoPackage.Literals.SCENARIO;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getId() {
        return id;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setId(int newId) {
        int oldId = id;
        id = newId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SmartgridtopoPackage.SCENARIO__ID, oldId, id));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SmartgridtopoPackage.SCENARIO__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<NetworkEntity> getContainsNE() {
        if (containsNE == null) {
            containsNE = new EObjectContainmentEList<NetworkEntity>(NetworkEntity.class, this, SmartgridtopoPackage.SCENARIO__CONTAINS_NE);
        }
        return containsNE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<PowerGridNode> getContainsPGN() {
        if (containsPGN == null) {
            containsPGN = new EObjectContainmentEList<PowerGridNode>(PowerGridNode.class, this, SmartgridtopoPackage.SCENARIO__CONTAINS_PGN);
        }
        return containsPGN;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<PhysicalConnection> getContainsC() {
        if (containsC == null) {
            containsC = new EObjectContainmentEList<PhysicalConnection>(PhysicalConnection.class, this, SmartgridtopoPackage.SCENARIO__CONTAINS_C);
        }
        return containsC;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<LogicalCommunication> getContainsLC() {
        if (containsLC == null) {
            containsLC = new EObjectContainmentEList<LogicalCommunication>(LogicalCommunication.class, this, SmartgridtopoPackage.SCENARIO__CONTAINS_LC);
        }
        return containsLC;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case SmartgridtopoPackage.SCENARIO__CONTAINS_NE:
                return ((InternalEList<?>)getContainsNE()).basicRemove(otherEnd, msgs);
            case SmartgridtopoPackage.SCENARIO__CONTAINS_PGN:
                return ((InternalEList<?>)getContainsPGN()).basicRemove(otherEnd, msgs);
            case SmartgridtopoPackage.SCENARIO__CONTAINS_C:
                return ((InternalEList<?>)getContainsC()).basicRemove(otherEnd, msgs);
            case SmartgridtopoPackage.SCENARIO__CONTAINS_LC:
                return ((InternalEList<?>)getContainsLC()).basicRemove(otherEnd, msgs);
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
            case SmartgridtopoPackage.SCENARIO__ID:
                return getId();
            case SmartgridtopoPackage.SCENARIO__NAME:
                return getName();
            case SmartgridtopoPackage.SCENARIO__CONTAINS_NE:
                return getContainsNE();
            case SmartgridtopoPackage.SCENARIO__CONTAINS_PGN:
                return getContainsPGN();
            case SmartgridtopoPackage.SCENARIO__CONTAINS_C:
                return getContainsC();
            case SmartgridtopoPackage.SCENARIO__CONTAINS_LC:
                return getContainsLC();
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
            case SmartgridtopoPackage.SCENARIO__ID:
                setId((Integer)newValue);
                return;
            case SmartgridtopoPackage.SCENARIO__NAME:
                setName((String)newValue);
                return;
            case SmartgridtopoPackage.SCENARIO__CONTAINS_NE:
                getContainsNE().clear();
                getContainsNE().addAll((Collection<? extends NetworkEntity>)newValue);
                return;
            case SmartgridtopoPackage.SCENARIO__CONTAINS_PGN:
                getContainsPGN().clear();
                getContainsPGN().addAll((Collection<? extends PowerGridNode>)newValue);
                return;
            case SmartgridtopoPackage.SCENARIO__CONTAINS_C:
                getContainsC().clear();
                getContainsC().addAll((Collection<? extends PhysicalConnection>)newValue);
                return;
            case SmartgridtopoPackage.SCENARIO__CONTAINS_LC:
                getContainsLC().clear();
                getContainsLC().addAll((Collection<? extends LogicalCommunication>)newValue);
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
            case SmartgridtopoPackage.SCENARIO__ID:
                setId(ID_EDEFAULT);
                return;
            case SmartgridtopoPackage.SCENARIO__NAME:
                setName(NAME_EDEFAULT);
                return;
            case SmartgridtopoPackage.SCENARIO__CONTAINS_NE:
                getContainsNE().clear();
                return;
            case SmartgridtopoPackage.SCENARIO__CONTAINS_PGN:
                getContainsPGN().clear();
                return;
            case SmartgridtopoPackage.SCENARIO__CONTAINS_C:
                getContainsC().clear();
                return;
            case SmartgridtopoPackage.SCENARIO__CONTAINS_LC:
                getContainsLC().clear();
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
            case SmartgridtopoPackage.SCENARIO__ID:
                return id != ID_EDEFAULT;
            case SmartgridtopoPackage.SCENARIO__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case SmartgridtopoPackage.SCENARIO__CONTAINS_NE:
                return containsNE != null && !containsNE.isEmpty();
            case SmartgridtopoPackage.SCENARIO__CONTAINS_PGN:
                return containsPGN != null && !containsPGN.isEmpty();
            case SmartgridtopoPackage.SCENARIO__CONTAINS_C:
                return containsC != null && !containsC.isEmpty();
            case SmartgridtopoPackage.SCENARIO__CONTAINS_LC:
                return containsLC != null && !containsLC.isEmpty();
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
        if (baseClass == NamedEntity.class) {
            switch (derivedFeatureID) {
                case SmartgridtopoPackage.SCENARIO__NAME: return SmartgridtopoPackage.NAMED_ENTITY__NAME;
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
        if (baseClass == NamedEntity.class) {
            switch (baseFeatureID) {
                case SmartgridtopoPackage.NAMED_ENTITY__NAME: return SmartgridtopoPackage.SCENARIO__NAME;
                default: return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
        result.append(" (id: ");
        result.append(id);
        result.append(", name: ");
        result.append(name);
        result.append(')');
        return result.toString();
    }

} //ScenarioImpl
