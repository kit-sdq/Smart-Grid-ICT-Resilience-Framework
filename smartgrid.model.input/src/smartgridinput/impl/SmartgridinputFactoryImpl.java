/**
 */
package smartgridinput.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputFactory;
import smartgridinput.SmartgridinputPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class SmartgridinputFactoryImpl extends EFactoryImpl implements SmartgridinputFactory {
    /**
     * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public static SmartgridinputFactory init() {
        try {
            SmartgridinputFactory theSmartgridinputFactory = (SmartgridinputFactory) EPackage.Registry.INSTANCE.getEFactory(SmartgridinputPackage.eNS_URI);
            if (theSmartgridinputFactory != null) {
                return theSmartgridinputFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new SmartgridinputFactoryImpl();
    }

    /**
     * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public SmartgridinputFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
        case SmartgridinputPackage.SCENARIO_STATE:
            return createScenarioState();
        case SmartgridinputPackage.ENTITY_STATE:
            return createEntityState();
        case SmartgridinputPackage.POWER_STATE:
            return createPowerState();
        default:
            throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public ScenarioState createScenarioState() {
        ScenarioStateImpl scenarioState = new ScenarioStateImpl();
        return scenarioState;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EntityState createEntityState() {
        EntityStateImpl entityState = new EntityStateImpl();
        return entityState;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public PowerState createPowerState() {
        PowerStateImpl powerState = new PowerStateImpl();
        return powerState;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public SmartgridinputPackage getSmartgridinputPackage() {
        return (SmartgridinputPackage) getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @deprecated
     * @generated
     */
    @Deprecated
    public static SmartgridinputPackage getPackage() {
        return SmartgridinputPackage.eINSTANCE;
    }

} //SmartgridinputFactoryImpl
