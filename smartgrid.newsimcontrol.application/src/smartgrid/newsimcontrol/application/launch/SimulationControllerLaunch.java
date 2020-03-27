package smartgrid.newsimcontrol.application.launch;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import smartgrid.newsimcontrol.controller.LocalController;

public class SimulationControllerLaunch implements IApplication  {

	LocalController controller;
	
	public SimulationControllerLaunch() {
	}

	private final AtomicBoolean terminationFlag = new AtomicBoolean(false);
	private Registry registry = null;

	@Override
	public Object start(IApplicationContext context) throws Exception {
		
		synchronized (terminationFlag) {
			terminationFlag.set(false);
		}
		
		
		
		registry = LocateRegistry.createRegistry(10999);
		
		
		IComponentLoader componentLoader = Activator.getInstance().getComponentLoader();
		for(IComponentInformation component: componentLoader.getComponents()) {
			System.out.println(component.getComponent().getRmiId());
			ILoadMe componentStub = (ILoadMe) UnicastRemoteObject.exportObject(component.getComponent(), 0);
			registry.bind(component.getComponent().getRmiId(), componentStub);
		}
		
		
		

		synchronized (terminationFlag) {
			while (!terminationFlag.get()) {
				terminationFlag.wait();
			}
		}
		
			
		
		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		synchronized (terminationFlag) {
			terminationFlag.set(true);
			terminationFlag.notifyAll();
		}
	}

}
