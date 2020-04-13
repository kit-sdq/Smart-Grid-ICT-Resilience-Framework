package smartgrid.newsimcontrol.application.commands;

import java.rmi.RemoteException;

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
		if (args.length != 0)
			return false;
		else
			return true;
	}

	@Override
	public Object doCommand(String[] args) throws RemoteException, SimcontrolException, InterruptedException {
		return controller.getDysfunctSmartComponents();
		
	}

}
