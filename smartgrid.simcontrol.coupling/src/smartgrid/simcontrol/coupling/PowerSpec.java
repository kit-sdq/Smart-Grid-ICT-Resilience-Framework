package smartgrid.simcontrol.coupling;

import java.io.Serializable;

public class PowerSpec implements Serializable {

    private static final long serialVersionUID = -2011261885321879024L;

    private String ciType; //CI, -component ID
    private String ciName; //CI, -component name
    private String ciSmartID; //Smart meter agent-ID
    private int powerDistrictId;
    private int aggregation;
    private double optDemand; //optimal demand
    private double minReqDemand; //Minimum required power
    private double criticality; //1 = human casualties (has to be supplied)

    public PowerSpec(String ciType, String ciName, String ciSmartID, int _powerDistrictId, double optDemand, double minReqDemand, double criticality) {
      this(ciType, ciName, ciSmartID, _powerDistrictId, 1, optDemand, minReqDemand, criticality);
    }

    public PowerSpec(String ciType, String ciName, String ciSmartID, int _powerDistrictHFId, int aggregation, double optDemand, double minReqDemand, double criticality) {
        this.ciType = ciType;
        this.ciName = ciName;
        this.ciSmartID = ciSmartID;
        this.powerDistrictId = _powerDistrictHFId;
        this.aggregation = aggregation;
        this.optDemand = optDemand;
        this.minReqDemand = minReqDemand;
        this.criticality = criticality;
    }

    /**
     * @return the ciType
     */
    public String getCiType() {
        return ciType;
    }

    /**
     * @return the ciName
     */
    public String getCiName() {
        return ciName;
    }

    /**
     * @return the ciSmartID
     */
    public String getCiSmartID() {
        return ciSmartID;
    }

    /**
     * @return the powerDistrictId
     */
    public int getPowerDistrictId() {
      return powerDistrictId;
    }

    /**
     * @param _powerDistrictId the powerDistrictId to set
     */
    public void setPowerDistrictId(int _powerDistrictId) {
      this.powerDistrictId = _powerDistrictId;
    }

    /**
     * @return the aggregation
     */
    public int getAggregation() {
        return aggregation;
    }

    /**
     * @return the minReqDemand
     */
    public double getMinReqDemand() {
        return minReqDemand;
    }

    /**
     * @return the criticality
     */
    public double getCriticality() {
        return criticality;
    }

    /**
     * @return the optDemand
     */
    public double getOptDemand() {
        return optDemand;
    }

    /**
     * @param optDemand
     *            the optDemand to set
     */
    public void setOptDemand(double optDemand) {
        this.optDemand = optDemand;
    }

    /**
     * @param minReqDemand
     *            the minReqDemand to set
     */
    public void setMinReqDemand(double minReqDemand) {
        this.minReqDemand = minReqDemand;
    }

    /**
     * @param criticality
     *            the criticality to set
     */
    public void setCriticality(double criticality) {
        this.criticality = criticality;
    }

}
