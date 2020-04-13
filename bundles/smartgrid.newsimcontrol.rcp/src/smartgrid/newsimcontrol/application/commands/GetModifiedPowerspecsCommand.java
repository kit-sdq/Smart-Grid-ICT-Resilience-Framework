package smartgrid.newsimcontrol.application.commands;

import java.rmi.RemoteException;

import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SimcontrolException;
import couplingToICT.SmartGridTopoContainer;
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
		if (args.length != 2)
			return false;
		Object obj1 = ReadObjectFromFile(args[0]);
		Object obj2 = ReadObjectFromFile(args[1]);
		if (obj1 instanceof PowerSpecContainer && obj2 instanceof PowerAssigned) 
			return true;
		else
			return false;
	}

	@Override
	public Object doCommand(String[] args) throws RemoteException, SimcontrolException, InterruptedException {
		PowerSpecContainer powerSpecs = (PowerSpecContainer)ReadObjectFromFile(args[0]);
		PowerAssigned SMPowerAssigned = (PowerAssigned)ReadObjectFromFile(args[1]);
		return controller.getModifiedPowerSpec(powerSpecs, SMPowerAssigned);
		
	}

}
