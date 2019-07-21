package smartgrid.simcontrol.test.rmi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import couplingToICT.PowerAssigned;
import couplingToICT.PowerDemand;
import couplingToICT.PowerSpec;
import couplingToICT.SmartComponentStateContainer;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.SimcontrolException;

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
    
    private static PowerDemand bufferedDemand;
    
    private static PowerDemand modifiedDemand;
    
   
    
    /**
     * @author Mazen Ebada
     * @param powerAssigned
     * @return
     * @throws Throwable
     */
    public synchronized static void bufferPDAndPA(PowerAssigned powerAssigned, PowerDemand powerDemand) throws Throwable {
        
        assert bufferedPower == null;
        assert powerAssigned != null;
        
        assert bufferedDemand == null;
        assert powerDemand != null;
        
        bufferedPowerAssigned = powerAssigned;
        bufferedDemand = powerDemand;
        
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
    
	public static PowerDemand getBufferedPowerDemand() throws Throwable {
	        
	        couplingThread = Thread.currentThread();
	        
	        // wait for data
	        while (bufferedDemand == null) {
	            LOG.info("SimControl is waiting for the bufferedDemand.");
	            BlockingKritisDataExchanger.class.wait();
	        }
	        couplingThread = null;
	        
	        return bufferedDemand;
	        
	    }
	
	public static PowerDemand getModifiedPowerDemand() throws Throwable {
	    
	    couplingThread = Thread.currentThread();
	    
	    // wait for data
	    while (modifiedDemand == null) {
	        LOG.info("SimControl is waiting for the modifiedDemand.");
	        BlockingKritisDataExchanger.class.wait();
	    }
	    couplingThread = null;
	    
	    return modifiedDemand;
	    
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
    
    public static void storeModifiedDemand (PowerDemand newModifiedPowerDemand) {
        couplingThread = Thread.currentThread();
        modifiedDemand = newModifiedPowerDemand;
        BlockingKritisDataExchanger.class.notifyAll();

    }

    private static void hasExceptionOccured() throws Throwable {
        if (storedException != null) {
            LOG.info("Passing exception to KRITIS simulation.");
            throw storedException;
        }
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
        bufferedDemand = null;
        bufferedPower = null;
        bufferedTopoData = null;
        storedException = null;
        LOG.info("All data was cleared.");

        return threadFreed;
    }
}
