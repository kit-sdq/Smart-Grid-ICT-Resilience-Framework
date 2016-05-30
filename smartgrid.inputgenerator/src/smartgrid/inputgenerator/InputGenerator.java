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
    public InputModelDTO generate(final GenerationStyle desiredStyle, final int SmartMeterCount,
            final int ControlCenterCount) {

        // Init Scenario State
        SmartgridinputPackageImpl.init();
        final SmartgridinputFactory scenarioStateFactory = SmartgridinputFactory.eINSTANCE;
        final ScenarioState myScenarioStates = scenarioStateFactory.createScenarioState();

        // Init Scenario Topo
        SmartgridtopoPackageImpl.init();
        final SmartgridtopoFactory scenarioTopoFactore = SmartgridtopoFactory.eINSTANCE;
        final SmartGridTopology myScenarioTopo = scenarioTopoFactore.createSmartGridTopology();

        final InputModelDTO myDTO = new InputModelDTO(myScenarioTopo, myScenarioStates);

        return myDTO;
    }
}
