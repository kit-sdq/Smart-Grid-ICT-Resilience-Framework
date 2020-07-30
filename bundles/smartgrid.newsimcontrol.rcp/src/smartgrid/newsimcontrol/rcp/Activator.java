package smartgrid.newsimcontrol.rcp;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import smartgrid.newsimcontrol.controller.LocalController;

public class Activator implements BundleActivator {

	private static Activator activator;
	
	private LocalController controller;
	
	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		Activator.activator = this;
		var reference = context.getServiceReference(LocalController.class);
		controller = context.getService(reference);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub

	}
	
	public static Activator getInstance() {
		return Activator.activator;
	}
	
	public LocalController getController() {
	    return controller;
	}


}
