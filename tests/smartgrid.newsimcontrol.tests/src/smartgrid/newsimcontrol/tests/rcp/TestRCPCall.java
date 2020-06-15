package smartgrid.newsimcontrol.tests.rcp;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

public class TestRCPCall {
	
	//inputs
	PowerSpecContainer powerSpec;
	SmartGridTopoContainer topoContainer;
	PowerAssigned powerAssigned;
	HashMap<InitializationMapKeys, String> dtoMap;
	
	//outputs
	Collection<ICTElement> ictElements;
	PowerSpecContainer powerSpecModified;
	SmartComponentStateContainer smartCompStateContainer;
	
	//inputs
	String powerSpecFilepath = System.getProperty("java.io.tmpdir") + File.separator + "powerSpec-" + System.currentTimeMillis();
	String topoContainerFilepath = System.getProperty("java.io.tmpdir") + File.separator + "topoContainer-" + System.currentTimeMillis();
	String powerASsignedFilepath = System.getProperty("java.io.tmpdir") + File.separator + "powerAssigned-" + System.currentTimeMillis();
	String dtoMapFilePath = System.getProperty("java.io.tmpdir") + File.separator + "dtoMap-" + System.currentTimeMillis();
	
	//outputs
	String ictElementsFilePath;
	String powerSpecModifiedFilePath;
	String smartCompStateContainerFilePath;
	
	//controller
	String controllerFilePath;
	
	//count
	int controllerCount = 0; //For every test case a new controller
	
	@BeforeEach
	void init_testCase(){
		

		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		//System.setProperty("java.io.tmpdir", "/users/mazenebada/rcpTest");
		
		//count
		controllerCount ++;
		
		//inputs
		powerSpec = InitHelpers.createPowerSpecContainer();
		topoContainer = new SmartGridTopoContainer(InitHelpers.createTopoMap(), null);
		powerAssigned = new PowerAssigned(InitHelpers.assignPower());
		dtoMap = InitHelpers.createDTOMAP(System.getProperty("java.io.tmpdir") + File.separator + "output-" + System.currentTimeMillis());
		
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
		

		ictElements = (Collection<ICTElement>) TestHelper.ReadObjectFromFile(ictElementsFilePath);
		powerSpecModified = (PowerSpecContainer) TestHelper.ReadObjectFromFile(powerSpecModifiedFilePath);
		smartCompStateContainer = (SmartComponentStateContainer) TestHelper.ReadObjectFromFile(smartCompStateContainerFilePath);
		
	}
	
	
	
	@Test
	@DisplayName("simple Iteration over the commands")
	void runCommands() throws SimcontrolException, InterruptedException {
		String command;
		

	   
		//1. Init Topo
		command = "INIT_TOPO";
		command += " " + dtoMapFilePath + " " + topoContainerFilepath + " " + ictElementsFilePath;
		runCommand(command);
		
		//2. Get Modified PowerSpecs
		command = "GET_MODIFIED_POWERSPECS";
		command += " " + dtoMapFilePath + " " + topoContainerFilepath + " " + powerSpecFilepath + " " + powerASsignedFilepath + " " + powerSpecModifiedFilePath + " " + smartCompStateContainerFilePath;
		runCommand(command);
	}
	
	void runCommand(String commandArguments) throws SimcontrolException, InterruptedException {
		//TODO
		//String command = "-consoleLog -application smartgrid.newsimcontrol.rcp.application";
		//command += " " + commandArguments;
		SmartgridRCPApplication testApp = new SmartgridRCPApplication();
		testApp.startTest(commandArguments);
	}
	

}
