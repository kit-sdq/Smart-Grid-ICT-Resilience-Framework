package smartgrid.simcontrol.baselib.coupling;

public class SigmoidCostFunction extends AbstractCostFunction {
    private double inflectionPoint;

    public SigmoidCostFunction(String kritisID, double inflectionPoint) {
        super(kritisID);
        this.inflectionPoint = inflectionPoint;
    }

    public double getInflectionPoint() {
        return inflectionPoint;
    }

    public void setInflectionPoint(double inflectionPoint) {
        this.inflectionPoint = inflectionPoint;
    }
}
