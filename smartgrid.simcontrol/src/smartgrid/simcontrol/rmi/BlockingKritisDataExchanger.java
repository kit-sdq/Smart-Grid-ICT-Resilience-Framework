package smartgrid.simcontrol.rmi;

import java.util.Map;

import org.apache.log4j.Logger;

import smartgrid.simcontrol.coupling.PowerSpec;

public class BlockingKritisDataExchanger {

    private static final Logger LOG = Logger.getLogger(BlockingKritisDataExchanger.class);

    private static Map<String, Map<String, PowerSpec>> bufferedDemand;
    private static Map<String, Map<String, Double>> bufferedPower;

    public static synchronized Map<String, Map<String, PowerSpec>> passDataToKritisSim(Map<String, Map<String, Double>> power) {
        assert bufferedPower == null;
        bufferedPower = power;
        while (bufferedDemand == null) {
            try {
                BlockingKritisDataExchanger.class.wait();
            } catch (InterruptedException e) {
                LOG.warn("Interrupted while waiting for data. This is unexpected.", e);
            }
        }
        Map<String, Map<String, PowerSpec>> tempDemand = bufferedDemand;
        bufferedDemand = null;
        return tempDemand;
    }

    public static synchronized Map<String, Map<String, Double>> getDataFromCoupling(Map<String, Map<String, PowerSpec>> demand) {
        assert bufferedDemand == null;
        bufferedDemand = demand;
        while (bufferedPower == null) {
            try {
                BlockingKritisDataExchanger.class.wait();
            } catch (InterruptedException e) {
                LOG.warn("Interrupted while waiting for data. This is unexpected.", e);
            }
        }
        Map<String, Map<String, Double>> tempPower = bufferedPower;
        bufferedPower = null;
        return tempPower;
    }
}
