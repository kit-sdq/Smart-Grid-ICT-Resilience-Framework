package smartgrid.newsimcontrol.application.launch;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

	private static Activator activator;
	private IComponentLoader componentLoader;
	
	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		Activator.activator = this;
		ServiceReference<IComponentLoader> componentLoaderReference = context.getServiceReference(IComponentLoader.class);
		this.componentLoader = context.getService(componentLoaderReference);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub

	}
	
	public static Activator getInstance() {
		return Activator.activator;
	}

	public IComponentLoader getComponentLoader() {
		return componentLoader;
	}


}
