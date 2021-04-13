package smartgrid.newsimcontrol.tests.rcp;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;

import couplingToICT.ICTElement;
import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SimcontrolException;
import couplingToICT.SmartComponentStateContainer;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.newsimcontrol.tests.helpers.InitHelpers;
import smartgrid.newsimcontrol.tests.helpers.TestHelper;

public class RCPCall {

	//Directory
	//TODO: The correct working directory should be given here
	static String path = "/home/majuwa/tmp/workspace-helmholtz-new/workdir";
	
	//inputs
	static PowerSpecContainer powerSpec;
	static SmartGridTopoContainer topoContainer;
	static PowerAssigned powerAssigned;
	static HashMap<InitializationMapKeys, String> dtoMap;
	
	//outputs
	static Collection<ICTElement> ictElements;
	static PowerSpecContainer powerSpecModified;
	static SmartComponentStateContainer smartCompStateContainer;
	
	//inputs
	static String powerSpecFilepath = path + File.separator + "powerSpec-" + System.currentTimeMillis();
	static String topoContainerFilepath = path + File.separator + "topoContainer-" + System.currentTimeMillis();
	static String powerASsignedFilepath = path + File.separator + "powerAssigned-" + System.currentTimeMillis();
	static String dtoMapFilePath = path + File.separator + "dtoMap-" + System.currentTimeMillis();
	
	//outputs
	static String ictElementsFilePath;
	static String powerSpecModifiedFilePath;
	static String smartCompStateContainerFilePath;
	static String topoPath;
	static String inputPath;
	
	//controller
	static String controllerFilePath;
	
	//count
	static int controllerCount = 0; //For every test case a new controller
	
	//helper
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static String rcpPath = "/home/majuwa/tmp/workspace-helmholtz-new/eclipse/eclipse";

	public static void main(String[] args) throws SimcontrolException, InterruptedException, IOException {
		
		init_objects();
		init_values();
		runCommands();
		collectObjects();
	}

	public static void init_objects() {
		//inputs
		//use an instance instead of the class to track the ids and the districts created
		InitHelpers initHelper = new InitHelpers();
		powerSpec = initHelper.createPowerSpecContainer();
		topoContainer = new SmartGridTopoContainer(initHelper.createTopoMap(), null);
		powerAssigned = new PowerAssigned(initHelper.assignPower());
		dtoMap = initHelper.createDTOMAP(path + File.separator + "output-" + System.currentTimeMillis());
				
	}
	
	public static void init_values(){
		//Shouldn't be changed
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		
		//count
		controllerCount ++;
		
		//inputs
		TestHelper.WriteObjectToFile(powerSpec, powerSpecFilepath);	
		TestHelper.WriteObjectToFile(topoContainer, topoContainerFilepath);
		TestHelper.WriteObjectToFile(powerAssigned, powerASsignedFilepath);
		TestHelper.WriteObjectToFile(dtoMap, dtoMapFilePath);
				
		//outputs
		ictElementsFilePath = path + File.separator + "icts" + controllerCount + "-" + System.currentTimeMillis();
		powerSpecModifiedFilePath = path + File.separator + "powerSpecModified" + controllerCount + "-" + System.currentTimeMillis();
		smartCompStateContainerFilePath = path + File.separator + "smartCompStateContainer" + controllerCount +"-" + System.currentTimeMillis();
		topoPath = path + File.separator + "topo" + controllerCount + "-" + System.currentTimeMillis();
		inputPath = path + File.separator + "input" + controllerCount + "-" + System.currentTimeMillis();
		
		//controller
		controllerFilePath = path + File.separator + "controller" + controllerCount + "-" + System.currentTimeMillis();

	}
	
	public static void runCommands() throws SimcontrolException, InterruptedException, IOException {
		String command;
		
		//1. Init Topo
		command = "INIT_TOPO";
		command += " " + dtoMapFilePath + " " + topoContainerFilepath + " " + ictElementsFilePath + " " + topoPath + " " + inputPath;
		runCommand(command);
		
		//2. Get Modified PowerSpecs
		command = "GET_MODIFIED_POWERSPECS";
		command += " " + dtoMapFilePath + " " + topoPath + " " + inputPath + " " + powerSpecFilepath + " " + powerASsignedFilepath + " " + powerSpecModifiedFilePath + " " + smartCompStateContainerFilePath;
		runCommand(command);

	}
	
	public static void runCommand(String commandArguments) throws SimcontrolException, InterruptedException, IOException {
		
		//TODO: to be tested
		String command = "";
		if (OS.contains("mac")) {
			command= rcpPath + " " + commandArguments;
		} else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
			command = rcpPath + " " + commandArguments;
		} else if (OS.contains("win")) {
			command = "cmd /c " + rcpPath + " " + commandArguments;
		}
		
		var process = Runtime.getRuntime().exec(command);
		while(process.isAlive()) {
		      try {
		            Thread.sleep(1000); //the runtime call is executed in parralel it might take some time to 
		        } catch (InterruptedException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }
		}		
		//eclipse Datei exportiert in einem "Eclipse" Order 
		//TODO: Test method: to be removed
		//SmartgridRCPApplication testApp = new SmartgridRCPApplication();
		//testApp.startTest(commandArguments);
	}
	
	@SuppressWarnings("unchecked")
	public static void collectObjects() {

		ictElements = (Collection<ICTElement>) TestHelper.ReadObjectFromFile(ictElementsFilePath);
		powerSpecModified = (PowerSpecContainer) TestHelper.ReadObjectFromFile(powerSpecModifiedFilePath);
		smartCompStateContainer = (SmartComponentStateContainer) TestHelper.ReadObjectFromFile(smartCompStateContainerFilePath);
	}
}
