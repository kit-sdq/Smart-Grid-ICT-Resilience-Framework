package smartgrid.newsimcontrol.rcp.commands;


import java.util.Collection;
import java.util.Map;

import couplingToICT.ICTElement;
import couplingToICT.SimcontrolException;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.newsimcontrol.controller.LocalController;
import smartgrid.newsimcontrol.rcp.helper.EObjectsHelper;

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
		if (args.length != 5) {
        	LOG.error("The correct number of arguments isn't correct."
        			+ "For further info see the readme file");
			return false;
		}
		Object obj = readObjectFromFile(args[0]);
		if (! (obj instanceof Map<?,?>)) 
			return false;
		obj = readObjectFromFile(args[1]);
		return (obj instanceof SmartGridTopoContainer);
	}

	@Override
	public void doCommand(String[] args) throws SimcontrolException {
		LOG.info("Initializing the local controller");
		@SuppressWarnings("unchecked")
		Map<InitializationMapKeys, String> initMap = (Map<InitializationMapKeys, String>) readObjectFromFile(args[0]);
		controller.initConfiguration(initMap);
        
		
		LOG.info("Initializing the topology");
		SmartGridTopoContainer topologyContainer = (SmartGridTopoContainer)readObjectFromFile(args[1]);
		Collection<ICTElement> ictElements; 
		try {
            ictElements = controller.initTopo(topologyContainer);
            writeObjectToFile(ictElements, args[2]);
        } catch (SimcontrolException e) {
            e.printStackTrace();
        }
		
		EObjectsHelper.saveToFileSystem(controller.getTopo(), args[3]);
		EObjectsHelper.saveToFileSystem(controller.getInitalState(), args[4]);
		EObjectsHelper.saveToFileSystem(controller.getInitalState(), args[4] + "original");
		
		
	}

}
