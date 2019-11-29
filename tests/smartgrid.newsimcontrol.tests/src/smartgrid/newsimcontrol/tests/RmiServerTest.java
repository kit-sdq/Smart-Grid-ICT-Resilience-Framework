package smartgrid.newsimcontrol.tests;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.rmi.RemoteException;

import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import couplingToICT.SimcontrolException;
import couplingToICT.SimcontrolInitializationException;
import couplingToICT.SmartGridTopoContainer;
import smartgrid.newsimcontrol.rmi.RmiServer;
import smartgrid.newsimcontrol.tests.helpers.InitHelpers;

public class RmiServerTest {
	private RmiServer server;
	@BeforeEach
	void init_server(){
		server = new RmiServer();
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
	}
	@Test
	@DisplayName("Exception if no init")
	void initTestEmpty() {
		var topo = new SmartGridTopoContainer(InitHelpers.createTopoMap(), null);
		assertThrows(SimcontrolException.class, () -> server.initTopo(topo));
	}
	@Test
	@DisplayName("Sample Init Reactive")
	void initReactive() {
		var dtoMap = InitHelpers.createDTOMAP(System.getProperty("java.io.tmpdir") + File.separator + System.currentTimeMillis());
		try {
			if(server!=null)
				server.initReactive(dtoMap);
		} catch (RemoteException | SimcontrolInitializationException e) {
			fail(e.getMessage());
		}
	}

}
