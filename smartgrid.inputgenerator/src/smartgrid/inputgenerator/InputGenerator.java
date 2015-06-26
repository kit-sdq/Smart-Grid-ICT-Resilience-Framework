/**
 * 
 */
package smartgrid.inputgenerator;



import smartgrid.simcontrol.interfaces.IInputGenerator;
import smartgrid.simcontrol.interfaces.InputModelDTO;
import smartgrid.simcontroller.baselibary.GenerationStyle;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputFactory;
import smartgridinput.impl.SmartgridinputPackageImpl;
import smartgridtopo.Scenario;
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
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see smartgrid.simcontrol.interfaces.IInputGenerator#init()
	 */
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public InputModelDTO generate(GenerationStyle desiredStyle,
			int SmartMeterCount, int ControlCenterCount) {

		// Init Scenario State
		SmartgridinputPackageImpl.init();
		SmartgridinputFactory scenarioStateFactory = SmartgridinputFactory.eINSTANCE;
		ScenarioState myScenarioStates = scenarioStateFactory
				.createScenarioState();

		// Init Scenario Topo
		SmartgridtopoPackageImpl.init();
		SmartgridtopoFactory scenarioTopoFactore = SmartgridtopoFactory.eINSTANCE;
		Scenario myScenarioTopo = scenarioTopoFactore.createScenario();

		switch (desiredStyle) {

		case BIG_CLUSTERS:

			break;

		case D3_TORUS_CLUSTER:

			break;

		case HYPERCUBE:

			break;

		case MANY_CLSUTERS:

			break;

		case RANDOM:

			break;

		}

		InputModelDTO myDTO = new InputModelDTO(myScenarioTopo,
				myScenarioStates);

		return myDTO;
	}



}
