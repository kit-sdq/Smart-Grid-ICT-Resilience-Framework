package smartgrid.newsimcontrol.rcp;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static Activator activator;
	
	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		Activator.activator = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub

	}
	
	public static Activator getInstance() {
		return Activator.activator;
	}


}
