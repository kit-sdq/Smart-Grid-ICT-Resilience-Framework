package smartgrid.newsimcontrol.rcp;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import couplingToICT.ISimulationController;
import smartgrid.newsimcontrol.controller.LocalController;

public class Activator implements BundleActivator {

	private static Activator activator;
	
	private LocalController controller;
	
	@Override
	public void start(BundleContext context) throws Exception {
		Activator.activator = this;
		var reference = context.getServiceReference(LocalController.class);
		controller = context.getService(reference);
	}
	
	public static Activator getInstance() {
		return Activator.activator;
	}
	
	public ISimulationController getController() {
	    return controller;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		//same as parent
		
	}


}
