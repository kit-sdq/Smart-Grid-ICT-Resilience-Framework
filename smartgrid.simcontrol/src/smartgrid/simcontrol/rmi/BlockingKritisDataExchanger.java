package smartgrid.simcontrol.rmi;

import java.util.Map;

import org.apache.log4j.Logger;

import smartgrid.simcontrol.coupling.PowerSpec;

/**
 * This is basically an exchanger for two different datatypes.
 * 
 * @author Misha
 */
public class BlockingKritisDataExchanger {

    private static final Logger LOG = Logger.getLogger(BlockingKritisDataExchanger.class);

    private static Map<String, Map<String, PowerSpec>> bufferedDemand;
    private static Map<String, Map<String, Double>> bufferedPower;

    public static synchronized Map<String, Map<String, PowerSpec>> passDataToKritisSim(Map<String, Map<String, Double>> power) {
        assert bufferedPower == null;

        // provide own data
        bufferedPower = power;
        BlockingKritisDataExchanger.class.notifyAll();

        // wait for data
        while (bufferedDemand == null) {
            LOG.debug("SimControl is waiting to pass data to Kritis simulation.");
            try {
                BlockingKritisDataExchanger.class.wait();
            } catch (InterruptedException e) {
                LOG.warn("Interrupted while waiting for data. This is unexpected.", e);
            }
        }

        // consume data
        Map<String, Map<String, PowerSpec>> tempDemand = bufferedDemand;
        bufferedDemand = null;
        LOG.info("SimControl has finished its exchange.");
        return tempDemand;
    }

    public static synchronized Map<String, Map<String, Double>> getDataFromCoupling(Map<String, Map<String, PowerSpec>> demand) {
        assert bufferedDemand == null;

        // provide own data
        bufferedDemand = demand;
        BlockingKritisDataExchanger.class.notifyAll();

        // wait for data
        while (bufferedPower == null) {
            LOG.debug("The Kritis simulation is waiting to pass data to SimControl.");
            try {
                BlockingKritisDataExchanger.class.wait();
            } catch (InterruptedException e) {
                LOG.warn("Interrupted while waiting for data. This is unexpected.", e);
            }
        }

        // consume data
        Map<String, Map<String, Double>> tempPower = bufferedPower;
        bufferedPower = null;
        LOG.info("The Kritis simulation has finished its exchange.");
        return tempPower;
    }
}
