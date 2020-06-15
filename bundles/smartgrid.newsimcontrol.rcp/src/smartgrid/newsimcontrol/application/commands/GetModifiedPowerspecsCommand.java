package smartgrid.newsimcontrol.application.commands;

import java.util.Map;

import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SimcontrolException;
import couplingToICT.SmartComponentStateContainer;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.newsimcontrol.controller.LocalController;

public class GetModifiedPowerspecsCommand extends ControllerCommand {

	public GetModifiedPowerspecsCommand(LocalController controller) {
		super(controller);
	}

	@Override
	public boolean allow() {
		return true;
	}

	@Override
	public boolean checkArguments(String[] args) {
		if (args.length != 6) {
        	LOG.error("The correct number of arguments isn't correct."
        			+ "For further info see the readme file");
			return false;
		}
		Object obj1 = ReadObjectFromFile(args[0]);
		Object obj2 = ReadObjectFromFile(args[1]);
		Object obj3 = ReadObjectFromFile(args[2]);
		Object obj4 = ReadObjectFromFile(args[3]);
		if (obj1 instanceof Map<?,?> && obj2 instanceof SmartGridTopoContainer 
				&& obj3 instanceof PowerSpecContainer && obj4 instanceof PowerAssigned) 
			return true;
		else
			return false;
	}

	@Override
	public void doCommand(String[] args) throws SimcontrolException, InterruptedException {
		LOG.info("Initializing the local controller");
		Map<InitializationMapKeys, String> initMap = (Map<InitializationMapKeys, String>) ReadObjectFromFile(args[0]);
		controller.initConfiguration(initMap);
		
		LOG.info("Initializing the topology");
		SmartGridTopoContainer topologyContainer = (SmartGridTopoContainer)ReadObjectFromFile(args[1]);
		controller.initTopo(topologyContainer);
		
		LOG.info("Running the simulations and saving the modified powerspecContainer");
		PowerSpecContainer powerSpecs = (PowerSpecContainer)ReadObjectFromFile(args[2]);
		PowerAssigned SMPowerAssigned = (PowerAssigned)ReadObjectFromFile(args[3]);
		PowerSpecContainer powerSpecsModified;
		powerSpecsModified = controller.getModifiedPowerSpec(powerSpecs, SMPowerAssigned);
		WriteObjectToFile(powerSpecsModified, args[4]);
		
		LOG.info("Saving the dysfunctional smartcomponents");
		SmartComponentStateContainer scsc;
		scsc = controller.getDysfunctSmartComponents();
		WriteObjectToFile(scsc, args[5]);
		
	}

}
