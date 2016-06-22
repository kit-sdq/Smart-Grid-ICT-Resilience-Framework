package smartgrid.simcontrol.baselib.coupling;

public class PowerSpec {
    private double defaultDemand;
    private double actualDemand;

    public double getDefaultDemand() {
        return defaultDemand;
    }

    public double getActualDemand() {
        return actualDemand;
    }

    public PowerSpec(double defaultDemand, double actualDemand) {
        this.defaultDemand = defaultDemand;
        this.actualDemand = actualDemand;
    }

}
