/**
 */
package smartgridoutput.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import smartgridoutput.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see smartgridoutput.SmartgridoutputPackage
 * @generated
 */
public class SmartgridoutputSwitch<T> extends Switch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static SmartgridoutputPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SmartgridoutputSwitch() {
        if (modelPackage == null) {
            modelPackage = SmartgridoutputPackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @parameter ePackage the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage) {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case SmartgridoutputPackage.SCENARIO_RESULT: {
                ScenarioResult scenarioResult = (ScenarioResult)theEObject;
                T result = caseScenarioResult(scenarioResult);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SmartgridoutputPackage.ENTITY_STATE: {
                EntityState entityState = (EntityState)theEObject;
                T result = caseEntityState(entityState);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SmartgridoutputPackage.ONLINE: {
                Online online = (Online)theEObject;
                T result = caseOnline(online);
                if (result == null) result = caseOn(online);
                if (result == null) result = caseEntityState(online);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SmartgridoutputPackage.DESTROYED: {
                Destroyed destroyed = (Destroyed)theEObject;
                T result = caseDestroyed(destroyed);
                if (result == null) result = caseEntityState(destroyed);
                if (result == null) result = caseOffline(destroyed);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SmartgridoutputPackage.NO_POWER: {
                NoPower noPower = (NoPower)theEObject;
                T result = caseNoPower(noPower);
                if (result == null) result = caseEntityState(noPower);
                if (result == null) result = caseOffline(noPower);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SmartgridoutputPackage.NO_UPLINK: {
                NoUplink noUplink = (NoUplink)theEObject;
                T result = caseNoUplink(noUplink);
                if (result == null) result = caseOffline(noUplink);
                if (result == null) result = caseOn(noUplink);
                if (result == null) result = caseEntityState(noUplink);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SmartgridoutputPackage.OFFLINE: {
                Offline offline = (Offline)theEObject;
                T result = caseOffline(offline);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SmartgridoutputPackage.CLUSTER: {
                Cluster cluster = (Cluster)theEObject;
                T result = caseCluster(cluster);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SmartgridoutputPackage.ON: {
                On on = (On)theEObject;
                T result = caseOn(on);
                if (result == null) result = caseEntityState(on);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Scenario Result</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Scenario Result</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseScenarioResult(ScenarioResult object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Entity State</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Entity State</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEntityState(EntityState object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Online</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Online</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOnline(Online object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Destroyed</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Destroyed</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDestroyed(Destroyed object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>No Power</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>No Power</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNoPower(NoPower object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>No Uplink</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>No Uplink</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNoUplink(NoUplink object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Offline</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Offline</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOffline(Offline object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cluster</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cluster</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCluster(Cluster object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>On</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>On</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOn(On object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object) {
        return null;
    }

} //SmartgridoutputSwitch
