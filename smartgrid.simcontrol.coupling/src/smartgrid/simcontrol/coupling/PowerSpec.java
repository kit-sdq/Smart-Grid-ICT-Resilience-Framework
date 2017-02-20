package smartgrid.simcontrol.coupling;

public class PowerSpec {
    private double baseDemand; //optimal demand
    private double actualDemand; //reduced demand during power failure
    private double criticality; //1 = human casualties (has to be supplied)

    public double getDefaultDemand() {
        return baseDemand;
    }

    public double getActualDemand() {
        return actualDemand;
    }
    
    public double getCriticality() {
        return criticality;
    }

    public PowerSpec(double baseDemand, double actualDemand, double criticality) {
        this.baseDemand = baseDemand;
        this.actualDemand = actualDemand;
        this.criticality = criticality;
    }
}
