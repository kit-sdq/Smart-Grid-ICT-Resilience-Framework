package smartgrid.simcontrol.coupling;

import java.io.Serializable;

/*
 Smart meter geo information for constructing reasonable ICT-topology
 */
public class SmartMeterGeoData
  implements Serializable {

	private static final long serialVersionUID = -1219989950279411992L;

	private String ciType; // CI, -component ID
	private String ciName; // CI, -component name
	private String ciSmartID; // Smart meter agent-ID
	private double lat;
	private double lon;


	public String getCiType() {
		return ciType;
	}

	public void setCiType(String ciType) {
		this.ciType = ciType;
	}

	public String getCiName() {
		return ciName;
	}

	public void setCiName(String ciName) {
		this.ciName = ciName;
	}

	public String getCiSmartID() {
		return ciSmartID;
	}

	public void setCiSmartID(String ciSmartID) {
		this.ciSmartID = ciSmartID;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double _lat) {
		lat = _lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double _lon) {
		lon = _lon;
	}

    
}
