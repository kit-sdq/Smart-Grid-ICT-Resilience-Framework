package smartgrid.newsimcontrol.application.commands;

import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SimcontrolException;
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
		if (args.length != 4)
			return false;
		Object obj1 = ReadObjectFromFile(args[0]);
		Object obj2 = ReadObjectFromFile(args[1]);
		Object obj3 = ReadObjectFromFile(args[2]);
		if (obj1 instanceof LocalController && obj2 instanceof PowerSpecContainer && obj3 instanceof PowerAssigned) 
			return true;
		else
			return false;
	}

	@Override
	public void doCommand(String[] args) throws SimcontrolException, InterruptedException {
		this.controller = (LocalController)ReadObjectFromFile(args[0]);
		PowerSpecContainer powerSpecs = (PowerSpecContainer)ReadObjectFromFile(args[0]);
		PowerAssigned SMPowerAssigned = (PowerAssigned)ReadObjectFromFile(args[1]);
		WriteObjectToFile(controller.getModifiedPowerSpec(powerSpecs, SMPowerAssigned), args[3]);
		
	}

}
