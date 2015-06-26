package smartgrid.simcontrol.baselib.coupling;

public class PowerPerNode {
    private String powerNodeID;
    private double absolutePower;

    public PowerPerNode(String powerNodeID, double absolutePower) {
        this.powerNodeID = powerNodeID;
        this.absolutePower = absolutePower;
    }

    public double getAbsolutePower() {
        return absolutePower;
    }

    public String getPowerNodeID() {
        return powerNodeID;
    }

    public void setAbsolutePower(double absolutePower) {
        this.absolutePower = absolutePower;
    }

    public void setPowerNodeID(String powerNodeID) {
        this.powerNodeID = powerNodeID;
    }
}
