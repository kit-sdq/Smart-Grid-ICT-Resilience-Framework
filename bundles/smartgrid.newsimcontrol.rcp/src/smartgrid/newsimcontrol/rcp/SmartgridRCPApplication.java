package smartgrid.newsimcontrol.rcp;

import java.util.Arrays;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import couplingToICT.SimcontrolException;
import smartgrid.newsimcontrol.application.commands.ControllerCommand;
import smartgrid.newsimcontrol.application.commands.GetModifiedPowerspecsCommand;
import smartgrid.newsimcontrol.application.commands.InitTopoCommand;
import smartgrid.newsimcontrol.controller.LocalController;

/**
 * This class controls all aspects of the application's execution
 */
public class SmartgridRCPApplication implements IApplication {

	String test = "";
	
	@Override
	public Object start(IApplicationContext context) throws Exception {
		
		System.out.println("RCP starting");
		
		String[] arguments = (String[] )context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		
		System.out.println("");
		
		if (arguments.length == 0) {
			System.out.println("The name of the method to be run should be given with its arguments.");
			return IApplication.EXIT_OK;
		} else {
			ControllerCommand cCommand = getCommand(arguments[0]);
			applyArguments(cCommand, arguments);
		}
		
		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
	}
	
	private ControllerCommand getCommand(String commandString) throws SimcontrolException, InterruptedException {
		SimControlCommands command = null;
		ControllerCommand cCommand = null;
		
		try {
			command = SimControlCommandFromValue(commandString);
		} catch(IllegalArgumentException e) {
			System.out.println("The entered command can't be recognized. The program will end.");
			return null;
		}
		var controller = Activator.getInstance().getController();
		switch (command) {
			case INIT_TOPO:
				cCommand = new InitTopoCommand(controller);
				break;
			case GET_MODIFIED_POWERSPECS:
				cCommand = new GetModifiedPowerspecsCommand(controller);
				break;
			default:
				cCommand = null;
				System.out.println("The entered command can't be recognized. The program will end.");
		}
		return cCommand;
	}
	
	private void applyArguments(ControllerCommand cCommand, String[] arguments) throws SimcontrolException, InterruptedException {
		String[] arguments2 = Arrays.copyOfRange(arguments, 1, arguments.length);

		if (cCommand.allow())
			cCommand.execute(arguments2);
	}
	
	public static SimControlCommands SimControlCommandFromValue(String text) { 
     for (SimControlCommands comm : SimControlCommands.values()) { 
       if (String.valueOf(comm).equals(text)) { 
         return comm; 
       } 
     } 
     throw new IllegalArgumentException();
    } 
	
	/**
	 * Test Method
	 * would be removed
	 * @param commandArg
	 * @throws SimcontrolException
	 * @throws InterruptedException
	 */
	public void startTest(String commandArg) throws SimcontrolException, InterruptedException {
		
		System.out.println("RCP starting");
		
		String[] arguments = commandArg.split(" ");
		
		System.out.println("");
		
		if (arguments.length == 0) {
			System.out.println("Please write the name of the method to be run.");
		} else {
			ControllerCommand cCommand = getCommand(arguments[0]);
			applyArguments(cCommand, arguments);
		}
	}
	

}
