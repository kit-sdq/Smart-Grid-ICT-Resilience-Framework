package smartgrid.newsimcontrol.application.commands;

import java.util.Map;

import couplingToICT.SimcontrolException;
import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.newsimcontrol.controller.LocalController;

public class InitConfigurationCommand extends ControllerCommand {

	public InitConfigurationCommand(LocalController controller) {
		super(controller);
	}

	@Override
	public boolean allow() {
		return true;
	}

	@Override
	public boolean checkArguments(String[] args) {
		if (args.length != 2) {
			System.out.println("The correct number of arguments for the command init_config is 2.");
			return false;
		}
		Object obj = ReadObjectFromFile(args[1]);
		if (obj instanceof Map<?,?>) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void doCommand(String[] args) throws SimcontrolException {
		System.out.println("Doing the command: INIT_CONFIG");
		Map<InitializationMapKeys, String> topologyContainer = (Map<InitializationMapKeys, String>) ReadObjectFromFile(args[1]);
		controller.initConfiguration(topologyContainer);
		WriteObjectToFile(controller, args[0]);
	}

}
