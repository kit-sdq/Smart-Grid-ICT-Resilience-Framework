/**
 */
package smartgridoutput.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import smartgridoutput.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see smartgridoutput.SmartgridoutputPackage
 * @generated
 */
public class SmartgridoutputAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static SmartgridoutputPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SmartgridoutputAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = SmartgridoutputPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SmartgridoutputSwitch<Adapter> modelSwitch =
        new SmartgridoutputSwitch<Adapter>() {
            @Override
            public Adapter caseScenarioResult(ScenarioResult object) {
                return createScenarioResultAdapter();
            }
            @Override
            public Adapter caseEntityState(EntityState object) {
                return createEntityStateAdapter();
            }
            @Override
            public Adapter caseOnline(Online object) {
                return createOnlineAdapter();
            }
            @Override
            public Adapter caseDestroyed(Destroyed object) {
                return createDestroyedAdapter();
            }
            @Override
            public Adapter caseNoPower(NoPower object) {
                return createNoPowerAdapter();
            }
            @Override
            public Adapter caseNoUplink(NoUplink object) {
                return createNoUplinkAdapter();
            }
            @Override
            public Adapter caseOffline(Offline object) {
                return createOfflineAdapter();
            }
            @Override
            public Adapter caseCluster(Cluster object) {
                return createClusterAdapter();
            }
            @Override
            public Adapter caseOn(On object) {
                return createOnAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link smartgridoutput.ScenarioResult <em>Scenario Result</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see smartgridoutput.ScenarioResult
     * @generated
     */
    public Adapter createScenarioResultAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link smartgridoutput.EntityState <em>Entity State</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see smartgridoutput.EntityState
     * @generated
     */
    public Adapter createEntityStateAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link smartgridoutput.Online <em>Online</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see smartgridoutput.Online
     * @generated
     */
    public Adapter createOnlineAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link smartgridoutput.Destroyed <em>Destroyed</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see smartgridoutput.Destroyed
     * @generated
     */
    public Adapter createDestroyedAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link smartgridoutput.NoPower <em>No Power</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see smartgridoutput.NoPower
     * @generated
     */
    public Adapter createNoPowerAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link smartgridoutput.NoUplink <em>No Uplink</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see smartgridoutput.NoUplink
     * @generated
     */
    public Adapter createNoUplinkAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link smartgridoutput.Offline <em>Offline</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see smartgridoutput.Offline
     * @generated
     */
    public Adapter createOfflineAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link smartgridoutput.Cluster <em>Cluster</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see smartgridoutput.Cluster
     * @generated
     */
    public Adapter createClusterAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link smartgridoutput.On <em>On</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see smartgridoutput.On
     * @generated
     */
    public Adapter createOnAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //SmartgridoutputAdapterFactory
