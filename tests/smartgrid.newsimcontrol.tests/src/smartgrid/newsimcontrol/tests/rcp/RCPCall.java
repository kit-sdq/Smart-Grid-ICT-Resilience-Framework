package smartgrid.newsimcontrol.tests.rcp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import smartgrid.newsimcontrol.rcp.SmartgridRCPApplication;
import smartgrid.newsimcontrol.tests.helpers.InitHelpers;
import smartgrid.newsimcontrol.tests.helpers.TestHelper;

public class RCPCall {

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
	static String powerSpecFilepath = System.getProperty("java.io.tmpdir") + File.separator + "powerSpec-" + System.currentTimeMillis();
	static String topoContainerFilepath = System.getProperty("java.io.tmpdir") + File.separator + "topoContainer-" + System.currentTimeMillis();
	static String powerASsignedFilepath = System.getProperty("java.io.tmpdir") + File.separator + "powerAssigned-" + System.currentTimeMillis();
	static String dtoMapFilePath = System.getProperty("java.io.tmpdir") + File.separator + "dtoMap-" + System.currentTimeMillis();
	
	//outputs
	static String ictElementsFilePath;
	static String powerSpecModifiedFilePath;
	static String smartCompStateContainerFilePath;
	
	//controller
	static String controllerFilePath;
	
	//count
	static int controllerCount = 0; //For every test case a new controller


	public static void main(String[] args) throws SimcontrolException, InterruptedException, IOException {
		init_objects();
		init_values();
		initConfig();
	}

	public static void init_objects() {
		//TODO: The correct objects must be given here
		powerSpec = InitHelpers.createPowerSpecContainer();
		topoContainer = new SmartGridTopoContainer(InitHelpers.createTopoMap(), null);
		powerAssigned = new PowerAssigned(InitHelpers.assignPower());
		dtoMap = InitHelpers.createDTOMAP(System.getProperty("java.io.tmpdir") + File.separator + "output-" + System.currentTimeMillis());
		
	}
	
	public static void init_values(){
		
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
		ictElementsFilePath = System.getProperty("java.io.tmpdir") + File.separator + "icts" + controllerCount + "-" + System.currentTimeMillis();
		powerSpecModifiedFilePath = System.getProperty("java.io.tmpdir") + File.separator + "powerSpecModified" + controllerCount + "-" + System.currentTimeMillis();
		smartCompStateContainerFilePath = System.getProperty("java.io.tmpdir") + File.separator + "smartCompStateContainer" + controllerCount +"-" + System.currentTimeMillis();
		
		//controller
		controllerFilePath = System.getProperty("java.io.tmpdir") + File.separator + "controller" + controllerCount + "-" + System.currentTimeMillis();

	}
	
	public static void initConfig() throws SimcontrolException, InterruptedException, IOException {
		String command;
		
		//1. Init Configuration
		command = "INIT_CONFIG";
		command += " " + controllerFilePath + " " + dtoMapFilePath;
		runCommand(command);
		
		//2. Init Topo
		command = "INIT_CONFIG";
		command += " " + controllerFilePath + " " + topoContainerFilepath + " " + smartCompStateContainerFilePath;
		runCommand(command);
		
		//3. Get Modified PowerSpecs
		command = "GET_MODIFIED_POWERSPECS";
		command += " " + controllerFilePath + " " + powerSpecFilepath + " " + powerASsignedFilepath + " " + powerSpecModifiedFilePath;
		runCommand(command);
		
		//4. Get Dysfunctional components
		command = "GET_DYS_COMPONENTS";
		command += " " + controllerFilePath + " " + smartCompStateContainerFilePath;
		runCommand(command);

	}
	
	public static void runCommand(String commandArguments) throws SimcontrolException, InterruptedException, IOException {
		//TODO: to be tested
		String command = "-consoleLog -application smartgrid.newsimcontrol.rcp.application";
		command += " " + commandArguments;
		//Runtime.getRuntime().exec("java -jar product.jar " + commandArguments);
		
		//eclipse Datei exportiert in einem "Eclipse" Order 
		//TODO: Test method: to be removed
		SmartgridRCPApplication testApp = new SmartgridRCPApplication();
		testApp.startTest(commandArguments);
	}
}
