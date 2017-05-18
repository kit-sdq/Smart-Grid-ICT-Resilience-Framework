package smartgrid.simcontrol.coupling;

public class PowerSpec {
	private String ciType;     //CI, -component ID
	private String ciName;     //CI, -component name
	private String ciSmartID;  //Smart meter agent-ID
    private double optDemand; //optimal demand
    private double minReqDemand; //Minimum required power
    private double criticality; //1 = human casualties (has to be supplied)


    public PowerSpec(String ciType, String ciName, String ciSmartID, double optDemand, double minReqDemand, double criticality) {
        this.ciType = ciType;
        this.ciName = ciName;
        this.ciSmartID = ciSmartID;    	
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
	 * @return the baseDemand
	 */
	public double getBaseDemand() {
		return optDemand;
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
	
}



