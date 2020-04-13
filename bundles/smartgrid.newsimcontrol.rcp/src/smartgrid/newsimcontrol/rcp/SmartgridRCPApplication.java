package smartgrid.newsimcontrol.rcp;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Scanner;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import couplingToICT.SimcontrolException;
import smartgrid.newsimcontrol.application.commands.ControllerCommand;
import smartgrid.newsimcontrol.application.commands.GetDysSmartComponentsCommand;
import smartgrid.newsimcontrol.application.commands.GetModifiedPowerspecsCommand;
import smartgrid.newsimcontrol.application.commands.InitConfigurationCommand;
import smartgrid.newsimcontrol.application.commands.InitTopoCommand;
import smartgrid.newsimcontrol.controller.LocalController;

/**
 * This class controls all aspects of the application's execution
 */
public class SmartgridRCPApplication implements IApplication {

	LocalController controller;
	Scanner scan;
	String test = "";
	
	@Override
	public Object start(IApplicationContext context) throws Exception {
		
		System.out.println("RCP starting");
		
//		String[] arguments = (String[] )context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
//		
//		System.out.println("Arguments:");
//		for(var argument : arguments) {
//			System.out.println(argument);
//		}
//		
//		System.out.println("");
//		
//		if (arguments.length == 0) {
//			System.out.println("Please write the name of the method to be run.");
//			return IApplication.EXIT_OK;
//		} else {
//			this.controller = new LocalController();
//			this.scan = new Scanner(System.in);
//			
//			ControllerCommand cCommand = getCommand(arguments[0]);
//			applyArguments(cCommand, arguments);
//		}
		
		this.controller = new LocalController();
		this.scan = new Scanner(System.in);
		return askForInput();
	}

	@Override
	public void stop() {
	}
	
	private Object askForInput() throws RemoteException, SimcontrolException, InterruptedException {
		
		System.out.println("Enter a new command to be run:");
		String commandLine = scan.nextLine();
		
		if (commandLine.toUpperCase().equals("EXIT")){
			scan.close();
			System.out.println("The program will end.");
			return IApplication.EXIT_OK;
		} else {
			System.out.println(commandLine);
			String[] arguments = commandLine.split(" ");
			ControllerCommand cCommand = getCommand(arguments[0]);
			if (cCommand == null) {
				scan.close();
				return IApplication.EXIT_OK;
			}
			applyArguments(cCommand, arguments);
			return askForInput();
		}
	}
	
	private ControllerCommand getCommand(String commandString) throws RemoteException, SimcontrolException, InterruptedException {
		SimControlCommands command = null;
		ControllerCommand cCommand = null;
		
		try {
			command = SimControlCommands.valueOf(commandString);
		} catch(Exception e) {
			System.out.println("The entered command can't be recognized. The program will end.");
			return null;
		}
		switch (command) {
			case INIT_CONFIG:
				cCommand = new InitConfigurationCommand(controller);
				break;
			case INIT_TOPO:
				cCommand = new InitTopoCommand(controller);
				break;
			case GET_DYS_COMPONENTS:
				cCommand = new GetDysSmartComponentsCommand(controller);
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
	
	private void applyArguments(ControllerCommand cCommand, String[] arguments) throws RemoteException, SimcontrolException, InterruptedException {
		String[] arguments2 = Arrays.copyOfRange(arguments, 1, arguments.length);

		if (cCommand.allow())
			cCommand.execute(arguments2);
	}
}
