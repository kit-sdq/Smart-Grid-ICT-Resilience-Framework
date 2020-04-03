package smartgrid.newsimcontrol.application.commands;

import java.rmi.RemoteException;

import couplingToICT.SimcontrolException;
import couplingToICT.SmartGridTopoContainer;
import smartgrid.newsimcontrol.controller.LocalController;

public class InitTopoCommand extends ControllerComand {

	public InitTopoCommand(LocalController controller) {
		super(controller);
	}

	@Override
	public boolean allow() {
		return true;
	}

	@Override
	public boolean checkArguments(String[] args) {
		if (args.length != 1)
			return false;
		Object obj = ReadObjectFromFile(args[0]);
		if (obj instanceof SmartGridTopoContainer) 
			return true;
		else
			return false;
	}

	@Override
	public Object doCommand(String[] args) throws RemoteException, SimcontrolException {
		SmartGridTopoContainer topologyContainer = (SmartGridTopoContainer)ReadObjectFromFile(args[0]);
		return controller.initTopo(topologyContainer);
		
	}

}
