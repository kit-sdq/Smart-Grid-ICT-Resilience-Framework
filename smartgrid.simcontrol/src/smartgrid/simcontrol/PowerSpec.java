package smartgrid.simcontrol;

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
        super();
        this.defaultDemand = defaultDemand;
        this.actualDemand = actualDemand;
    }

}
