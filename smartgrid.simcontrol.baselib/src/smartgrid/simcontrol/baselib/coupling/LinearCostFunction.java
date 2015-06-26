package smartgrid.simcontrol.baselib.coupling;

public class LinearCostFunction extends AbstractCostFunction {
    private double gradient;

    public LinearCostFunction(String kritisID, double gradient) {
        super(kritisID);
        this.gradient = gradient;
    }

    public double getGradient() {
        return gradient;
    }

    public void setGradient(double gradient) {
        this.gradient = gradient;
    }
}
