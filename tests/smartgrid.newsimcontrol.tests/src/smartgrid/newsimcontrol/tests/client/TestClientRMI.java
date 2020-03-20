package smartgrid.newsimcontrol.tests.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;

import couplingToICT.ISimulationControllerRemote;
import couplingToICT.PowerAssigned;
import couplingToICT.SimcontrolInitializationException;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.newsimcontrol.tests.helpers.InitHelpers;

public class TestClientRMI {
	private static final String hostName = "localhost";
	private static final String lookupName = "ISimulationController";

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Wrong parameter count. Only one parameter with the output path is allowed");
			System.exit(1);
		}
		var powerSpec = InitHelpers.createPowerSpecContainer();
		var topoContainer = new SmartGridTopoContainer(InitHelpers.createTopoMap(), null);
		var powerAssigned = new PowerAssigned(InitHelpers.assignPower());
		try {
			var connector = initRMIConnection(InitHelpers.createDTOMAP(args[0]));
			connector.initTopo(topoContainer);
			var test = connector.getModifiedPowerSpec(powerSpec, powerAssigned);
			connector.getModifiedPowerSpec(test, powerAssigned);
			connector.terminate();
			System.out.println("Programm sucessfully executed");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private static ISimulationControllerRemote initRMIConnection(HashMap<InitializationMapKeys, String> exchangeMap)
			throws RemoteException, NotBoundException, SimcontrolInitializationException {
		System.setProperty("java.security.policy", "file:java.policy");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		System.setProperty("java.rmi.server.hostname", "localhost");		
		var registry = LocateRegistry.getRegistry(hostName);
		var connector = (ISimulationControllerRemote) registry.lookup(lookupName);
		connector.initConfiguration(exchangeMap);
		return connector;
	}
}
