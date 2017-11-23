package smartgrid.simcontrol.rmi;

import java.util.Map;

import org.apache.log4j.Logger;

import smartgrid.simcontrol.coupling.PowerSpec;
import smartgrid.simcontrol.coupling.TopologyContainer;

/**
 * This is basically an exchanger for two different datatypes.
 * 
 * @author Misha
 */
public class BlockingKritisDataExchanger {

    private static final Logger LOG = Logger.getLogger(BlockingKritisDataExchanger.class);

    private static Map<String, Map<String, PowerSpec>> bufferedDemand;
    private static Thread kritisThread;

    private static Map<String, Map<String, Double>> bufferedPower;
    private static Thread couplingThread;

    public static synchronized Map<String, Map<String, PowerSpec>> passDataToKritisSim(Map<String, Map<String, Double>> power) throws InterruptedException {
        assert bufferedPower == null;

        // provide own data
        bufferedPower = power;
        couplingThread = Thread.currentThread();
        BlockingKritisDataExchanger.class.notifyAll();

        // wait for data
        while (bufferedDemand == null) {
            LOG.info("SimControl is waiting for data of Kritis simulation.");
            BlockingKritisDataExchanger.class.wait();
        }

        // consume data
        Map<String, Map<String, PowerSpec>> tempDemand = bufferedDemand;
        bufferedDemand = null;
        couplingThread = null;
        LOG.info("SimControl has finished its exchange.");
        return tempDemand;
    }

    public static synchronized Map<String, Map<String, Double>> getDataFromCoupling(Map<String, Map<String, PowerSpec>> demand) throws InterruptedException {
        assert bufferedDemand == null;

        // provide own data
        bufferedDemand = demand;
        kritisThread = Thread.currentThread();
        BlockingKritisDataExchanger.class.notifyAll();

        // wait for data
        while (bufferedPower == null) {
            LOG.info("The Kritis simulation is waiting for data of SimControl.");
            BlockingKritisDataExchanger.class.wait();
        }

        // consume data
        Map<String, Map<String, Double>> tempPower = bufferedPower;
        bufferedPower = null;
        kritisThread = null;
        LOG.info("The Kritis simulation has finished its exchange.");
        return tempPower;
    }

    private static TopologyContainer bufferedTopoData;

    public static synchronized void storeGeoData(TopologyContainer topoData) {
        if (bufferedTopoData != null) {
            LOG.error("There was already geo data present. This data is now overwritten.");
        }
        BlockingKritisDataExchanger.bufferedTopoData = topoData;
        BlockingKritisDataExchanger.class.notifyAll();
    }

    public static synchronized TopologyContainer getGeoData() throws InterruptedException {
        couplingThread = Thread.currentThread();

        // wait for data
        while (bufferedTopoData == null) {
            LOG.info("SimControl is waiting for topo data from the Kritis simulation.");
            BlockingKritisDataExchanger.class.wait();
        }

        // consume data
        TopologyContainer tempGeoData = bufferedTopoData;
        bufferedTopoData = null;
        couplingThread = null;
        LOG.info("SimControl got geo data from the Kritis simulation.");
        return tempGeoData;
    }

    public static synchronized boolean freeAll() {

        // free threads
        boolean threadFreed = couplingThread != null || kritisThread != null;
        if (couplingThread != null) {
            couplingThread.interrupt();
            couplingThread = null;
            LOG.info("The SimControl thread was interrupted and freed from the data exchange sync.");
        }
        if (kritisThread != null) {
            kritisThread.interrupt();
            kritisThread = null;
            LOG.info("The Kritis simulation thread was interrupted and freed from the data exchange sync.");
        }

        // clear all buffered data
        bufferedDemand = null;
        bufferedPower = null;
        bufferedTopoData = null;

        return threadFreed;
    }
}
