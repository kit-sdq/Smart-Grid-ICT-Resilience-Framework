package smartgrid.simcontrol.coupling;

import java.io.Serializable;

public class SmartMeterGeoData implements Serializable {
	private double lon;
	private double lat;

	public double getLat() {
		return lat;
	}

	public void setLat(double _lat) {
		this.lat = _lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double _lon) {
		this.lon = _lon;
	}
}
