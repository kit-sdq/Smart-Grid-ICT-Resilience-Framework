package smartgrid.simcontrol.coupling;

import java.util.HashMap;
import java.util.Map;

public class AggregatedSmartMeterStates implements ISmartMeterState {
    private final String nodeId;
    private final Map<SmartMeterState, Integer> states;
    private final int totalCount;

    public AggregatedSmartMeterStates(String nodeId, int onlineCount, int onlineHackedCount, int noUplinkCount, int noUplinkHackedCount, int noPowerCount, int defectCount) {
        this.nodeId = nodeId;
        states = new HashMap<SmartMeterState, Integer>(6);
        states.put(SmartMeterState.ONLINE, onlineCount);
        states.put(SmartMeterState.ONLINE_HACKED, onlineHackedCount);
        states.put(SmartMeterState.NO_UPLINK, noUplinkCount);
        states.put(SmartMeterState.NO_UPLINK_HACKED, noUplinkHackedCount);
        states.put(SmartMeterState.NO_POWER, noPowerCount);
        states.put(SmartMeterState.DEFECT, defectCount);
        totalCount = onlineCount + onlineHackedCount + noUplinkCount + noUplinkHackedCount + noPowerCount + defectCount;
    }
    
    public String getId() {
        return nodeId;
    }

    public int getStateCount(SmartMeterState state) {
        return states.get(state);
    }

    public double getStatePercentage(SmartMeterState state) {
        return (double) 100 * getStateCount(state) / this.getTotalCount();
    }

    public int getTotalCount() {
        return totalCount;
    }
}
