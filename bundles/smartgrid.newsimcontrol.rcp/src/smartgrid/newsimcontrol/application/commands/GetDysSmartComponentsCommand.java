package smartgrid.newsimcontrol.application.commands;


import couplingToICT.SimcontrolException;
import smartgrid.newsimcontrol.controller.LocalController;

public class GetDysSmartComponentsCommand extends ControllerCommand {

	public GetDysSmartComponentsCommand(LocalController controller) {
		super(controller);
	}

	@Override
	public boolean allow() {
		return true;
	}

	@Override
	public boolean checkArguments(String[] args) {
		if (args.length != 2)
			return false;
		Object obj = ReadObjectFromFile(args[0]);
		if (! (obj instanceof LocalController)) 
			return false;
		return true;
	}

	@Override
	public void doCommand(String[] args) throws SimcontrolException, InterruptedException {
		WriteObjectToFile(controller.getDysfunctSmartComponents(), args[1]);
		
	}

}
