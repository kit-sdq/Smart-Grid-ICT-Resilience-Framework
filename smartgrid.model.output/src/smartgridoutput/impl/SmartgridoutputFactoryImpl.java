/**
 */
package smartgridoutput.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import smartgridoutput.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SmartgridoutputFactoryImpl extends EFactoryImpl implements SmartgridoutputFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static SmartgridoutputFactory init() {
        try {
            SmartgridoutputFactory theSmartgridoutputFactory = (SmartgridoutputFactory)EPackage.Registry.INSTANCE.getEFactory(SmartgridoutputPackage.eNS_URI);
            if (theSmartgridoutputFactory != null) {
                return theSmartgridoutputFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new SmartgridoutputFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SmartgridoutputFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case SmartgridoutputPackage.SCENARIO_RESULT: return createScenarioResult();
            case SmartgridoutputPackage.ONLINE: return createOnline();
            case SmartgridoutputPackage.DESTROYED: return createDestroyed();
            case SmartgridoutputPackage.NO_POWER: return createNoPower();
            case SmartgridoutputPackage.NO_UPLINK: return createNoUplink();
            case SmartgridoutputPackage.CLUSTER: return createCluster();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ScenarioResult createScenarioResult() {
        ScenarioResultImpl scenarioResult = new ScenarioResultImpl();
        return scenarioResult;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Online createOnline() {
        OnlineImpl online = new OnlineImpl();
        return online;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Destroyed createDestroyed() {
        DestroyedImpl destroyed = new DestroyedImpl();
        return destroyed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NoPower createNoPower() {
        NoPowerImpl noPower = new NoPowerImpl();
        return noPower;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NoUplink createNoUplink() {
        NoUplinkImpl noUplink = new NoUplinkImpl();
        return noUplink;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Cluster createCluster() {
        ClusterImpl cluster = new ClusterImpl();
        return cluster;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SmartgridoutputPackage getSmartgridoutputPackage() {
        return (SmartgridoutputPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static SmartgridoutputPackage getPackage() {
        return SmartgridoutputPackage.eINSTANCE;
    }

} //SmartgridoutputFactoryImpl
