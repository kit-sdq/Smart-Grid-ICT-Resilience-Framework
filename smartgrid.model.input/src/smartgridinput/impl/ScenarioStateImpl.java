/**
 */
package smartgridinput.impl;

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

import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputPackage;
import smartgridtopo.SmartGridTopology;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario State</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link smartgridinput.impl.ScenarioStateImpl#getEntityStates <em>Entity States</em>}</li>
 *   <li>{@link smartgridinput.impl.ScenarioStateImpl#getPowerStates <em>Power States</em>}</li>
 *   <li>{@link smartgridinput.impl.ScenarioStateImpl#getScenario <em>Scenario</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScenarioStateImpl extends MinimalEObjectImpl.Container implements ScenarioState {
	/**
	 * The cached value of the '{@link #getEntityStates() <em>Entity States</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntityStates()
	 * @generated
	 * @ordered
	 */
	protected EList<EntityState> entityStates;

	/**
	 * The cached value of the '{@link #getPowerStates() <em>Power States</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPowerStates()
	 * @generated
	 * @ordered
	 */
	protected EList<PowerState> powerStates;

	/**
	 * The cached value of the '{@link #getScenario() <em>Scenario</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScenario()
	 * @generated
	 * @ordered
	 */
	protected SmartGridTopology scenario;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScenarioStateImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SmartgridinputPackage.Literals.SCENARIO_STATE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EntityState> getEntityStates() {
		if (entityStates == null) {
			entityStates = new EObjectContainmentEList<EntityState>(EntityState.class, this, SmartgridinputPackage.SCENARIO_STATE__ENTITY_STATES);
		}
		return entityStates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PowerState> getPowerStates() {
		if (powerStates == null) {
			powerStates = new EObjectContainmentEList<PowerState>(PowerState.class, this, SmartgridinputPackage.SCENARIO_STATE__POWER_STATES);
		}
		return powerStates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SmartGridTopology getScenario() {
		if (scenario != null && scenario.eIsProxy()) {
			InternalEObject oldScenario = (InternalEObject)scenario;
			scenario = (SmartGridTopology)eResolveProxy(oldScenario);
			if (scenario != oldScenario) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SmartgridinputPackage.SCENARIO_STATE__SCENARIO, oldScenario, scenario));
			}
		}
		return scenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SmartGridTopology basicGetScenario() {
		return scenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScenario(SmartGridTopology newScenario) {
		SmartGridTopology oldScenario = scenario;
		scenario = newScenario;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmartgridinputPackage.SCENARIO_STATE__SCENARIO, oldScenario, scenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SmartgridinputPackage.SCENARIO_STATE__ENTITY_STATES:
				return ((InternalEList<?>)getEntityStates()).basicRemove(otherEnd, msgs);
			case SmartgridinputPackage.SCENARIO_STATE__POWER_STATES:
				return ((InternalEList<?>)getPowerStates()).basicRemove(otherEnd, msgs);
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
			case SmartgridinputPackage.SCENARIO_STATE__ENTITY_STATES:
				return getEntityStates();
			case SmartgridinputPackage.SCENARIO_STATE__POWER_STATES:
				return getPowerStates();
			case SmartgridinputPackage.SCENARIO_STATE__SCENARIO:
				if (resolve) return getScenario();
				return basicGetScenario();
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
			case SmartgridinputPackage.SCENARIO_STATE__ENTITY_STATES:
				getEntityStates().clear();
				getEntityStates().addAll((Collection<? extends EntityState>)newValue);
				return;
			case SmartgridinputPackage.SCENARIO_STATE__POWER_STATES:
				getPowerStates().clear();
				getPowerStates().addAll((Collection<? extends PowerState>)newValue);
				return;
			case SmartgridinputPackage.SCENARIO_STATE__SCENARIO:
				setScenario((SmartGridTopology)newValue);
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
			case SmartgridinputPackage.SCENARIO_STATE__ENTITY_STATES:
				getEntityStates().clear();
				return;
			case SmartgridinputPackage.SCENARIO_STATE__POWER_STATES:
				getPowerStates().clear();
				return;
			case SmartgridinputPackage.SCENARIO_STATE__SCENARIO:
				setScenario((SmartGridTopology)null);
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
			case SmartgridinputPackage.SCENARIO_STATE__ENTITY_STATES:
				return entityStates != null && !entityStates.isEmpty();
			case SmartgridinputPackage.SCENARIO_STATE__POWER_STATES:
				return powerStates != null && !powerStates.isEmpty();
			case SmartgridinputPackage.SCENARIO_STATE__SCENARIO:
				return scenario != null;
		}
		return super.eIsSet(featureID);
	}

} //ScenarioStateImpl
