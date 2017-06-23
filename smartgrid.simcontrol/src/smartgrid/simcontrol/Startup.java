package smartgrid.simcontrol;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.eclipse.ui.IStartup;

public class Startup implements IStartup {

    private static final Logger LOG = Logger.getLogger(Startup.class);
    private static RmiServer rmiServer;

    @Override
    public void earlyStartup() {
        ensureServerRunning();
    }

    public static void ensureServerRunning() {
        synchronized (Startup.class) {
            if (rmiServer == null) {
                rmiServer = new RmiServer();
                rmiServer.initServer();
            }
        }
    }

    public void shutDown() {
        try {
            synchronized (Startup.class) {
                rmiServer.shutDown();
                rmiServer = null;
            }
        } catch (RemoteException e) {
            LOG.error("Error while shutting down the RMI server.");
            e.printStackTrace();
        }
    }
}