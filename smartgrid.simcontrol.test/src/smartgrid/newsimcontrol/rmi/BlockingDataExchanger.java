package smartgrid.newsimcontrol.rmi;


import org.apache.log4j.Logger;

import smartgrid.coupling.ICT.PowerAssigned;
import smartgrid.coupling.ICT.PowerSpecContainer;
import smartgrid.coupling.ICT.SimcontrolException;
import smartgrid.coupling.ICT.SmartComponentStateContainer;
import smartgrid.coupling.ICT.SmartGridTopoContainer;

/**
 * This is basically an exchanger for two different data types.
 * 
 * @author Mazen
 */
public class BlockingDataExchanger {

    private static final Logger LOG = Logger.getLogger(BlockingDataExchanger.class);

    
    private static Thread kritisThread;
    
    private static Thread couplingThread;

    private static Throwable storedException;

    private static SmartComponentStateContainer scsc;
    
    private static PowerAssigned bufferedPowerAssigned;
    
    private static PowerSpecContainer bufferedPowerSpecs;
    
    private static PowerSpecContainer modifiedPowerSpecs;
    
    
    /**
     * @author Mazen Ebada
     * @return
     * @throws SimcontrolException 
     */
    public synchronized static void bufferPSAndPA(PowerSpecContainer powerSpecs, PowerAssigned powerAssigned) throws InterruptedException, SimcontrolException {
        
    	if (bufferedPowerAssigned != null) {
            LOG.warn("There was already power Assigned present. This data is now overwritten.");
        }

    	if (bufferedPowerSpecs != null) {
            LOG.warn("There was already power Specs present. This data is now overwritten.");
        }

        if (powerAssigned == null) {
        	throw new SimcontrolException("The given power assigned is empty.");
        }
        if (powerSpecs == null) {
        	throw new SimcontrolException("The given power spec container is empty.");
        }
        
        bufferedPowerAssigned = powerAssigned;
        bufferedPowerSpecs = powerSpecs;
        
        couplingThread = Thread.currentThread();
        BlockingDataExchanger.class.notifyAll();
    }
    
    public static synchronized PowerAssigned getPowerAssigned() throws InterruptedException {
        
        couplingThread = Thread.currentThread();
        
        // wait for data
        while (bufferedPowerAssigned == null) {
            LOG.info("SimControl is waiting for the PowerAssigned.");
            BlockingDataExchanger.class.wait();
        }
        PowerAssigned tempPowerAssigned = bufferedPowerAssigned;
        
        bufferedPowerAssigned = null;
        couplingThread = null;
        
        return tempPowerAssigned;
        
    }
    
	public static synchronized PowerSpecContainer getBufferedPowerSpecs() throws InterruptedException {
	        
	        couplingThread = Thread.currentThread();
	        
	        // wait for data
	        while (bufferedPowerSpecs == null) {
	            LOG.info("SimControl is waiting for the bufferedPowerSpecs.");
	            BlockingDataExchanger.class.wait();
	        }
	        PowerSpecContainer tempPowerSpecContainer = bufferedPowerSpecs;
	        bufferedPowerSpecs = null;
	        couplingThread = null;
	        
	        return tempPowerSpecContainer;
	        
	    }
	
	public static synchronized PowerSpecContainer getModifiedPowerSpecs() throws InterruptedException {
	    
	    couplingThread = Thread.currentThread();
	    
	    // wait for data
	    while (modifiedPowerSpecs == null) {
	        LOG.info("SimControl is waiting for the modified PowerSpecs.");
	        BlockingDataExchanger.class.wait();
	    }
	    
	    PowerSpecContainer tempModifiedPowerSpecs = modifiedPowerSpecs;
	    
	    modifiedPowerSpecs = null;
	    couplingThread = null;
	    
	    return tempModifiedPowerSpecs;
	    
	}
    
	public static synchronized SmartComponentStateContainer getSCSC() throws InterruptedException {
	    
	    couplingThread = Thread.currentThread();
	    
	    // wait for data
	    while (scsc == null) {
	        LOG.info("SimControl is waiting for the scsc.");
	        BlockingDataExchanger.class.wait();
	    }
	    SmartComponentStateContainer tempSCSC = scsc;
	    
	    scsc = null;
	    couplingThread = null;
	    
	    return tempSCSC;
	    
	}

    public static synchronized void storeSCSC (SmartComponentStateContainer smartComponentStateContainer) {
    	if (scsc != null) {
            LOG.warn("There was already smart Component State Container present. This data is now overwritten.");
        }
        couplingThread = Thread.currentThread();
        scsc = smartComponentStateContainer;
        BlockingDataExchanger.class.notifyAll();
        

    }
    
    public static synchronized void storeModifiedPowerSpecs (PowerSpecContainer powerSpecContainer) {
    	if (modifiedPowerSpecs != null) {
            LOG.warn("There was already modified Power Specs present. This data is now overwritten.");
        }
        couplingThread = Thread.currentThread();
        modifiedPowerSpecs = powerSpecContainer;
        BlockingDataExchanger.class.notifyAll();

    }

    public static synchronized void storeException(Throwable e) {
        storedException = e;
        BlockingDataExchanger.class.notifyAll();
    }

    private static SmartGridTopoContainer bufferedTopoData;

    public static synchronized void storeTopoData(SmartGridTopoContainer topoData) {
        if (bufferedTopoData != null) {
            LOG.warn("There was already geo data present. This data is now overwritten.");
        }
        BlockingDataExchanger.bufferedTopoData = topoData;
        BlockingDataExchanger.class.notifyAll();
    }

    public static synchronized SmartGridTopoContainer getTopoData() throws InterruptedException {
        couplingThread = Thread.currentThread();

        // wait for data
        while (bufferedTopoData == null) {
            LOG.info("SimControl is waiting for topo data from the Kritis simulation.");
            BlockingDataExchanger.class.wait();
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
        bufferedTopoData = null;
        storedException = null;
        LOG.info("All data was cleared.");

        return threadFreed;
    }
}
