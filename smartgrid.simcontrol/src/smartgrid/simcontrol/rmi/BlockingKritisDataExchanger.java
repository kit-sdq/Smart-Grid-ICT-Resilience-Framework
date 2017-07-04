package smartgrid.simcontrol.rmi;

import java.util.Map;

import smartgrid.simcontrol.coupling.PowerSpec;

public class BlockingKritisDataExchanger {
    private static Map<String, Map<String, PowerSpec>> bufferedDemand;
    private static Map<String, Map<String, Double>> bufferedPower;

    public static synchronized Map<String, Map<String, PowerSpec>> passDataToKritisSim(Map<String, Map<String, Double>> power) {
        return null;
    }

    public static synchronized Map<String, Map<String, Double>> getDataFromCoupling(Map<String, Map<String, PowerSpec>> demand) {
        return null;
    }
}
