package smartgrid.simcontrol.test.rmi;

import java.util.Map;

import org.apache.log4j.Logger;

import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SmartComponentStateContainer;
import couplingToICT.SmartGridTopoContainer;

/**
 * This is basically an exchanger for two different data types.
 * 
 * @author Mazen
 */
public class BlockingKritisDataExchanger {

    private static final Logger LOG = Logger.getLogger(BlockingKritisDataExchanger.class);

    
    private static Thread kritisThread;

    private static Map<String, Map<String, Double>> bufferedPower;
    
    private static Thread couplingThread;

    private static Throwable storedException;

    private static SmartComponentStateContainer scsc;
    
    private static PowerAssigned bufferedPowerAssigned;
    
    private static PowerSpecContainer bufferedPowerSpecs;
    
    private static PowerSpecContainer modifiedPowerSpecs;
    
   
    
    /**
     * @author Mazen Ebada
     * @return
     * @throws Throwable
     */
    public synchronized static void bufferPSAndPA(PowerSpecContainer powerSpecs, PowerAssigned powerAssigned) throws Throwable {
        
        assert bufferedPower == null;
        assert powerAssigned != null;
        
        assert bufferedPowerSpecs == null;
        assert powerSpecs != null;
        
        bufferedPowerAssigned = powerAssigned;
        bufferedPowerSpecs = powerSpecs;
        
        couplingThread = Thread.currentThread();
        BlockingKritisDataExchanger.class.notifyAll();
        

    }
    
    public static PowerAssigned getPowerAssigned() throws Throwable {
        
        couplingThread = Thread.currentThread();
        
        // wait for data
        while (bufferedPowerAssigned == null) {
            LOG.info("SimControl is waiting for the PowerAssigned.");
            BlockingKritisDataExchanger.class.wait();
        }
        couplingThread = null;
        
        return bufferedPowerAssigned;
        
    }
    
	public static PowerSpecContainer getBufferedPowerSpecs() throws Throwable {
	        
	        couplingThread = Thread.currentThread();
	        
	        // wait for data
	        while (bufferedPowerSpecs == null) {
	            LOG.info("SimControl is waiting for the bufferedPowerSpecs.");
	            BlockingKritisDataExchanger.class.wait();
	        }
	        couplingThread = null;
	        
	        return bufferedPowerSpecs;
	        
	    }
	
	public static PowerSpecContainer getModifiedPowerSpecs() throws Throwable {
	    
	    couplingThread = Thread.currentThread();
	    
	    // wait for data
	    while (modifiedPowerSpecs == null) {
	        LOG.info("SimControl is waiting for the modifiedPowerSpecs.");
	        BlockingKritisDataExchanger.class.wait();
	    }
	    couplingThread = null;
	    
	    return modifiedPowerSpecs;
	    
	}
    
	public static SmartComponentStateContainer getSCSC() throws Throwable {
	    
	    couplingThread = Thread.currentThread();
	    
	    // wait for data
	    while (scsc == null) {
	        LOG.info("SimControl is waiting for the scsc.");
	        BlockingKritisDataExchanger.class.wait();
	    }
	    couplingThread = null;
	    
	    return scsc;
	    
	}

    public static void storeSCSC (SmartComponentStateContainer smartComponentStateContainer) {
        couplingThread = Thread.currentThread();
        scsc = smartComponentStateContainer;
        BlockingKritisDataExchanger.class.notifyAll();

    }
    
    public static void storeModifiedPowerSpecs (PowerSpecContainer powerSpecContainer) {
        couplingThread = Thread.currentThread();
        modifiedPowerSpecs = powerSpecContainer;
        BlockingKritisDataExchanger.class.notifyAll();

    }

    public static synchronized void storeException(Throwable e) {
        storedException = e;
        BlockingKritisDataExchanger.class.notifyAll();
    }

    private static SmartGridTopoContainer bufferedTopoData;

    public static synchronized void storeGeoData(SmartGridTopoContainer topoData) {
        if (bufferedTopoData != null) {
            LOG.error("There was already geo data present. This data is now overwritten.");
        }
        BlockingKritisDataExchanger.bufferedTopoData = topoData;
        BlockingKritisDataExchanger.class.notifyAll();
    }

    public static synchronized SmartGridTopoContainer getTopoData() throws InterruptedException {
        couplingThread = Thread.currentThread();

        // wait for data
        while (bufferedTopoData == null) {
            LOG.info("SimControl is waiting for topo data from the Kritis simulation.");
            BlockingKritisDataExchanger.class.wait();
        }

        // consume data
        SmartGridTopoContainer tempGeoData = bufferedTopoData;
        bufferedTopoData = null;
        couplingThread = null;
        LOG.info("SimControl got geo data from the Kritis simulation.");
        return tempGeoData;
    }

    public static synchronized boolean freeAll() {

        boolean threadFreed = couplingThread != null || kritisThread != null;

        // free coupling thread
        if (couplingThread != null) {
            couplingThread.interrupt();
            couplingThread = null;
            LOG.info("The SimControl thread was interrupted and freed from the data exchange sync.");
        }

        if (kritisThread != null) {
            // only free kritisThread if there was no exception (the KRITIS thread will be freed by the exception throw)
            if (storedException == null) {
                kritisThread.interrupt();
                LOG.info("The Kritis simulation thread was interrupted and freed from the data exchange sync.");
            }
            kritisThread = null;
        }

        // clear all buffered data
        bufferedPowerSpecs = null;
        modifiedPowerSpecs = null;
        scsc = null;
        bufferedPowerAssigned = null;
        bufferedPower = null;
        bufferedTopoData = null;
        storedException = null;
        LOG.info("All data was cleared.");

        return threadFreed;
    }
}
