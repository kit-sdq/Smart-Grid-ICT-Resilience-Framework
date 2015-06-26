package smartgrid.simcontrol.baselib.coupling;

public abstract class AbstractCostFunction {
    private String kritisID;

    protected AbstractCostFunction(String kritisID) {
        this.kritisID = kritisID;
    }

    public String getKritisID() {
        return kritisID;
    }

    public void setKritisID(String kritisID) {
        this.kritisID = kritisID;
    }
}
