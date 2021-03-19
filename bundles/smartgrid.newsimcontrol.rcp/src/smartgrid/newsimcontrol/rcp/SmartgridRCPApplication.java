package smartgrid.newsimcontrol.rcp;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;


import couplingToICT.SimcontrolException;
import smartgrid.newsimcontrol.controller.LocalController;
import smartgrid.newsimcontrol.rcp.commands.ControllerCommand;
import smartgrid.newsimcontrol.rcp.commands.GetModifiedPowerspecsCommand;
import smartgrid.newsimcontrol.rcp.commands.InitTopoCommand;
import smartgrid.newsimcontrol.rcp.commands.SimControlCommands;

/**
 * This class controls all aspects of the application's execution
 */
public class SmartgridRCPApplication implements IApplication {
	protected static final Logger LOG = Logger.getLogger(SmartgridRCPApplication.class);
	String test = "";
	
	@Override
	public Object start(IApplicationContext context) throws Exception {
		
		LOG.info("RCP starting");
		
		String[] arguments = (String[] )context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		
		
		if (arguments.length == 0) {
			LOG.error("The name of the method to be run should be given with its arguments.");
			return IApplication.EXIT_OK;
		} else {
			ControllerCommand cCommand = getCommand(arguments[0]);
			applyArguments(cCommand, arguments);
		}
		
		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		//same as parent
	}
	
	private ControllerCommand getCommand(String commandString) {
		SimControlCommands command = null;
		ControllerCommand cCommand = null;
		
		try {
			command = simControlCommandFromValue(commandString);
		} catch(IllegalArgumentException e) {
			LOG.error("The entered command can't be recognized. The program will end.");
			return null;
		}
		var controller = new LocalController(); //(LocalController) Activator.getInstance().getController();
		switch (command) {
			case INIT_TOPO:
				cCommand = new InitTopoCommand(controller);
				break;
			case GET_MODIFIED_POWERSPECS:
				cCommand = new GetModifiedPowerspecsCommand(controller);
				break;
			default:
				cCommand = null;
				LOG.error("The entered command can't be recognized. The program will end.");
		}
		return cCommand;
	}
	
	private void applyArguments(ControllerCommand cCommand, String[] arguments) throws SimcontrolException, InterruptedException {
		String[] arguments2 = Arrays.copyOfRange(arguments, 1, arguments.length);

		if (cCommand.allow())
			cCommand.execute(arguments2);
	}
	
	public static SimControlCommands simControlCommandFromValue(String text) { 
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
		
		LOG.info("RCP starting");
		
		String[] arguments = commandArg.split(" ");
				
		if (arguments.length == 0) {
			LOG.error("Please write the name of the method to be run.");
		} else {
			ControllerCommand cCommand = getCommand(arguments[0]);
			applyArguments(cCommand, arguments);
		}
	}
	

}
