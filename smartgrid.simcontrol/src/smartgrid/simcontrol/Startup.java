package smartgrid.simcontrol;

import org.apache.log4j.Logger;
import org.eclipse.ui.IStartup;

import smartgrid.log4j.LoggingInitializer;
import smartgrid.simcontrol.rmi.RmiServer;

public class Startup implements IStartup {

    private static final Logger LOG = Logger.getLogger(Startup.class);
    private static RmiServer rmiServer;

    @Override
    public void earlyStartup() {
        LoggingInitializer.initialize();
        LOG.info("Performing early startup");
        ensureServerRunning();
    }

    public static synchronized void ensureServerRunning() {
        if (rmiServer == null) {
            rmiServer = new RmiServer();
            rmiServer.startServer();
        }
    }

    public static synchronized void shutDown() {
        if (rmiServer != null) {
            rmiServer.shutDownServer();
            rmiServer = null;
        }
    }
}
