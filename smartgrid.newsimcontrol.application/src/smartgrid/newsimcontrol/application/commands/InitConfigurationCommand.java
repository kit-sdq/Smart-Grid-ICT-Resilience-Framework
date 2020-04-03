package smartgrid.newsimcontrol.application.commands;

import java.rmi.RemoteException;
import java.util.Map;

import couplingToICT.SimcontrolException;
import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.newsimcontrol.controller.LocalController;

public class InitConfigurationCommand extends ControllerComand {

	public InitConfigurationCommand(LocalController controller) {
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
		if (obj instanceof Map<?,?>) 
			return true;
		else
			return false;
	}

	@Override
	public Object doCommand(String[] args) throws RemoteException, SimcontrolException {
		Map<InitializationMapKeys, String> topologyContainer = (Map<InitializationMapKeys, String>)ReadObjectFromFile(args[0]);
		controller.initConfiguration(topologyContainer);
		return null;
	}

}
