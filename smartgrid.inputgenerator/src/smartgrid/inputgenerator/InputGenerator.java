/**
 * 
 */
package smartgrid.inputgenerator;

import smartgrid.simcontrol.baselib.GenerationStyle;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputFactory;
import smartgridinput.impl.SmartgridinputPackageImpl;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoFactory;
import smartgridtopo.impl.SmartgridtopoPackageImpl;

/**
 * @author Christian
 *
 */
public class InputGenerator implements IInputGenerator {

    /**
     * 
     */
    public InputGenerator() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see smartgrid.simcontrol.interfaces.IInputGenerator#init()
     */
    @Override
    public void init() {
    }

    @Override
    public InputModelDTO generate(GenerationStyle desiredStyle, int SmartMeterCount, int ControlCenterCount) {

        // Init Scenario State
        SmartgridinputPackageImpl.init();
        SmartgridinputFactory scenarioStateFactory = SmartgridinputFactory.eINSTANCE;
        ScenarioState myScenarioStates = scenarioStateFactory.createScenarioState();

        // Init Scenario Topo
        SmartgridtopoPackageImpl.init();
        SmartgridtopoFactory scenarioTopoFactore = SmartgridtopoFactory.eINSTANCE;
        SmartGridTopology myScenarioTopo = scenarioTopoFactore.createSmartGridTopology();

        InputModelDTO myDTO = new InputModelDTO(myScenarioTopo, myScenarioStates);

        return myDTO;
    }
}
