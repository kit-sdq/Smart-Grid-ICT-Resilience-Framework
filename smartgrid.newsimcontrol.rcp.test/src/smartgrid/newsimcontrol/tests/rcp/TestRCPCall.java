package smartgrid.newsimcontrol.tests.rcp;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
	
	static String dirProperty = "C:\\Users\\mazen\\TestFolder";// + "\\" + System.currentTimeMillis();//System.getProperty("java.io.tmpdir");
	
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
	static String powerSpecFilepath = dirProperty + File.separator + "powerSpec-" + System.currentTimeMillis();
	static String topoContainerFilepath = dirProperty + File.separator + "topoContainer-" + System.currentTimeMillis();
	static String powerASsignedFilepath = dirProperty + File.separator + "powerAssigned-" + System.currentTimeMillis();
	static String dtoMapFilePath = dirProperty + File.separator + "dtoMap-" + System.currentTimeMillis();
	
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
	static int interationCount = 3;
	
	//helper
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static String rcpPath = "/home/majuwa/tmp/workspace-helmholtz-new/eclipse/eclipse";
	
	@Before
	static void initTestCase(){
		

		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		
		//count
		controllerCount ++;
		
		//inputs
		//use an instance instead of the class to track the ids and the districts created
		InitHelpers initHelper = new InitHelpers();
		powerSpec = initHelper.createPowerSpecContainer();
		topoContainer = new SmartGridTopoContainer(initHelper.createTopoMap(), null);
		powerAssigned = new PowerAssigned(initHelper.assignPower());
		dtoMap = initHelper.createDTOMAP(dirProperty + File.separator + "output-" + System.currentTimeMillis());
		
		TestHelper.WriteObjectToFile(powerSpec, powerSpecFilepath);	
		TestHelper.WriteObjectToFile(topoContainer, topoContainerFilepath);
		TestHelper.WriteObjectToFile(powerAssigned, powerASsignedFilepath);
		TestHelper.WriteObjectToFile(dtoMap, dtoMapFilePath);
				
		//outputs
		ictElementsFilePath = dirProperty + File.separator + "icts" + controllerCount + "-" + System.currentTimeMillis();
		powerSpecModifiedFilePath = dirProperty + File.separator + "powerSpecModified" + controllerCount + "-" + System.currentTimeMillis();
		smartCompStateContainerFilePath = dirProperty + File.separator + "smartCompStateContainer" + controllerCount +"-" + System.currentTimeMillis();
		topoPath = dirProperty + File.separator + "topo" + controllerCount + "-" + System.currentTimeMillis() +".smartgridtopo";
		inputPath = dirProperty + File.separator + "input" + controllerCount + "-" + System.currentTimeMillis() +".smartgridinput";
		
		//controller
		controllerFilePath = dirProperty + File.separator + "controller" + controllerCount + "-" + System.currentTimeMillis();
	}
	
	
	
	@Test
	@DisplayName("simple Iteration over the commands")
    void runCommands() throws SimcontrolException, InterruptedException {
		String command;
		
		long start = System.currentTimeMillis();
		
		initTestCase();

		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		System.out.println("Time for initialization:" + elapsedTime);
		
		
		
		//1. Init Topo
		start = System.currentTimeMillis();
		
		command = "INIT_TOPO";
		command += " " + dtoMapFilePath + " " + topoContainerFilepath + " " + ictElementsFilePath + " " + topoPath + " " + inputPath;
		runCommand(command);
		
		end = System.currentTimeMillis();
		elapsedTime = end - start;
		System.out.println("Time for init Topo:" + elapsedTime);
		
		
		//2. Get Modified PowerSpecs
		for (int i=0; i<interationCount; i++) {
			start = System.currentTimeMillis();
			
			command = "GET_MODIFIED_POWERSPECS";
			command += " " + dtoMapFilePath + " " + topoPath + " " + inputPath + " " + powerSpecFilepath + " " + powerASsignedFilepath + " " + powerSpecModifiedFilePath + " " + smartCompStateContainerFilePath;
			runCommand(command);
			
			end = System.currentTimeMillis();
			elapsedTime = end - start;
			System.out.println("Time for iteration 1:" + elapsedTime);
		}
		
		
		collectObjects();
	}
	
    void runCommand(String commandArguments) throws SimcontrolException, InterruptedException {
		SmartgridRCPApplication testApp = new SmartgridRCPApplication();
		testApp.startTest(commandArguments);
	}
	
	@AfterClass
	public static void collectObjects() {

		try {
		ictElements = (Collection<ICTElement>) TestHelper.ReadObjectFromFile(ictElementsFilePath);
		powerSpecModified = (PowerSpecContainer) TestHelper.ReadObjectFromFile(powerSpecModifiedFilePath);
		smartCompStateContainer = (SmartComponentStateContainer) TestHelper.ReadObjectFromFile(smartCompStateContainerFilePath);
		} catch (Exception e) {
			//don't do any thing
		}
		}
}
