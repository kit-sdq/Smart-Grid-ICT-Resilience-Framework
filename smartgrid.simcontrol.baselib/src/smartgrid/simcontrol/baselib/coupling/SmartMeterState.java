package smartgrid.simcontrol.baselib.coupling;

import smartgridoutput.EntityState;

public class SmartMeterState {
    private String smartMeterID;
    private EntityState smartMeterState;

    public SmartMeterState(String smartMeterID, EntityState smartMeterState) {
        this.smartMeterID = smartMeterID;
        this.smartMeterState = smartMeterState;
    }

    public String getSmartMeterID() {
        return smartMeterID;
    }

    public EntityState getSmartMeterState() {
        return smartMeterState;
    }

    public void setSmartMeterID(String smartMeterID) {
        this.smartMeterID = smartMeterID;
    }

    public void setSmartMeterState(EntityState smartMeterState) {
        this.smartMeterState = smartMeterState;
    }
}
