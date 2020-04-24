package smartgrid.newsimcontrol.application.commands;


import couplingToICT.SimcontrolException;
import couplingToICT.SmartGridTopoContainer;
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
		if (! (obj instanceof LocalController)) 
			return false;
		obj = ReadObjectFromFile(args[1]);
		if (! (obj instanceof SmartGridTopoContainer)) 
			return false;
		return true;
	}

	@Override
	public void doCommand(String[] args) throws SimcontrolException {
		this.controller = (LocalController) ReadObjectFromFile(args[0]);
		SmartGridTopoContainer topologyContainer = (SmartGridTopoContainer)ReadObjectFromFile(args[1]);
		WriteObjectToFile(controller.initTopo(topologyContainer), args[2]);
		WriteObjectToFile(controller, args[0]);
	}

}
