package smartgrid.newsimcontrol.application.commands;


import java.util.Collection;
import java.util.Map;

import couplingToICT.ICTElement;
import couplingToICT.SimcontrolException;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.newsimcontrol.controller.LocalController;

public class InitTopoCommand extends ControllerCommand {

	public InitTopoCommand(LocalController controller) {
		super(controller);
	}

	@Override
	public boolean allow() {
		return true;
	}

	@Override
	public boolean checkArguments(String[] args) {
		if (args.length != 3) {
        	LOG.error("The correct number of arguments isn't correct."
        			+ "For further info see the readme file");
			return false;
		}
		Object obj = ReadObjectFromFile(args[0]);
		if (! (obj instanceof Map<?,?>)) 
			return false;
		obj = ReadObjectFromFile(args[1]);
		if (! (obj instanceof SmartGridTopoContainer)) 
			return false;
		return true;
	}

	@Override
	public void doCommand(String[] args) throws SimcontrolException {
		LOG.info("Initializing the local controller");
		Map<InitializationMapKeys, String> initMap = (Map<InitializationMapKeys, String>) ReadObjectFromFile(args[0]);
		controller.initConfiguration(initMap);
		
		LOG.info("Initializing the topology");
		SmartGridTopoContainer topologyContainer = (SmartGridTopoContainer)ReadObjectFromFile(args[1]);
		Collection<ICTElement> ictElements; 
		ictElements = controller.initTopo(topologyContainer);
		WriteObjectToFile(ictElements, args[2]);
		
	}

}
