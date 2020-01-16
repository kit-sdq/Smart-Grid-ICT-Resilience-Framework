package smartgrid.model.topo.generator.standard.data;

/**
 * This Class Represents additional Data for the Smart Meters
 * This Data should be part of the TopologyContainer but currently is not, so for testing this was created.
 * @author Eric Klappert
 *
 */
public class SmartMeterAddonData {
	private double power;
	private double critValue;
	private String critType;
	
	public SmartMeterAddonData() {
		critType = "";
		critValue = -1;
	}
	
	public void setPower(double value) {
		power = value;
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
