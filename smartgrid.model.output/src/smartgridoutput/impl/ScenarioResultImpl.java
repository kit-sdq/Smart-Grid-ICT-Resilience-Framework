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

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import smartgridoutput.Cluster;
import smartgridoutput.EntityState;
import smartgridoutput.ScenarioResult;
import smartgridoutput.SmartgridoutputPackage;

import smartgridtopo.SmartGridTopology;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link smartgridoutput.impl.ScenarioResultImpl#getStates <em>States</em>}</li>
 *   <li>{@link smartgridoutput.impl.ScenarioResultImpl#getClusters <em>Clusters</em>}</li>
 *   <li>{@link smartgridoutput.impl.ScenarioResultImpl#getScenario <em>Scenario</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScenarioResultImpl extends MinimalEObjectImpl.Container implements ScenarioResult {
	/**
	 * The cached value of the '{@link #getStates() <em>States</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStates()
	 * @generated
	 * @ordered
	 */
	protected EList<EntityState> states;

	/**
	 * The cached value of the '{@link #getClusters() <em>Clusters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClusters()
	 * @generated
	 * @ordered
	 */
	protected EList<Cluster> clusters;

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
	protected ScenarioResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SmartgridoutputPackage.Literals.SCENARIO_RESULT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EntityState> getStates() {
		if (states == null) {
			states = new EObjectContainmentEList<EntityState>(EntityState.class, this, SmartgridoutputPackage.SCENARIO_RESULT__STATES);
		}
		return states;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Cluster> getClusters() {
		if (clusters == null) {
			clusters = new EObjectContainmentEList<Cluster>(Cluster.class, this, SmartgridoutputPackage.SCENARIO_RESULT__CLUSTERS);
		}
		return clusters;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SmartgridoutputPackage.SCENARIO_RESULT__SCENARIO, oldScenario, scenario));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SmartgridoutputPackage.SCENARIO_RESULT__SCENARIO, oldScenario, scenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SmartgridoutputPackage.SCENARIO_RESULT__STATES:
				return ((InternalEList<?>)getStates()).basicRemove(otherEnd, msgs);
			case SmartgridoutputPackage.SCENARIO_RESULT__CLUSTERS:
				return ((InternalEList<?>)getClusters()).basicRemove(otherEnd, msgs);
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
			case SmartgridoutputPackage.SCENARIO_RESULT__STATES:
				return getStates();
			case SmartgridoutputPackage.SCENARIO_RESULT__CLUSTERS:
				return getClusters();
			case SmartgridoutputPackage.SCENARIO_RESULT__SCENARIO:
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
			case SmartgridoutputPackage.SCENARIO_RESULT__STATES:
				getStates().clear();
				getStates().addAll((Collection<? extends EntityState>)newValue);
				return;
			case SmartgridoutputPackage.SCENARIO_RESULT__CLUSTERS:
				getClusters().clear();
				getClusters().addAll((Collection<? extends Cluster>)newValue);
				return;
			case SmartgridoutputPackage.SCENARIO_RESULT__SCENARIO:
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
			case SmartgridoutputPackage.SCENARIO_RESULT__STATES:
				getStates().clear();
				return;
			case SmartgridoutputPackage.SCENARIO_RESULT__CLUSTERS:
				getClusters().clear();
				return;
			case SmartgridoutputPackage.SCENARIO_RESULT__SCENARIO:
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
			case SmartgridoutputPackage.SCENARIO_RESULT__STATES:
				return states != null && !states.isEmpty();
			case SmartgridoutputPackage.SCENARIO_RESULT__CLUSTERS:
				return clusters != null && !clusters.isEmpty();
			case SmartgridoutputPackage.SCENARIO_RESULT__SCENARIO:
				return scenario != null;
		}
		return super.eIsSet(featureID);
	}

} //ScenarioResultImpl
