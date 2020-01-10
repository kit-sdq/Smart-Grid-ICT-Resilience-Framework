package smartgrid.model.topo.generator.standard.data;

/**
 * A Class representing a Smart Meter
 * this Smart Meter has some extended Properties used for Calculating a Topology
 * @author Eric Klappert
 *
 */
public class SmartMeter {
	
	private double lat;
	private double lon;
	
	private double power;
	private double critValue;
	private String critType;
	
	public SmartMeter() {
		super();
		critType = "";
		critValue = -1;
	}
	
	public void setPosition(double pLat, double pLon) {
		lat = pLat;
		lon = pLon;
	}
	
	public double getLat() {
		return lat;
	}
	
	public double getLon() {
		return lon;
	}
	
	public void setPower(double pPower) {
		power = pPower;
	}
	
	public double getPower() {
		return power;
	}

	public void setCritValue(double value) {
		critValue = value;
	}
	
	public double getCritValue() {
		return critValue;
	}
	
	public void setCritType(String value) {
		critType = value;
	}
	
	public String getCritType() {
		return critType;
	}
}
