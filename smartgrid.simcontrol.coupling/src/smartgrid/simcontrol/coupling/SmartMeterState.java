package smartgrid.simcontrol.coupling;

public enum SmartMeterState implements ISmartMeterState {
    ONLINE, ONLINE_HACKED, NO_UPLINK, NO_UPLINK_HACKED, NO_POWER, DEFECT;
}
