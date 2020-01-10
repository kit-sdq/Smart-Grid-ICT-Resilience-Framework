package smartgrid.model.topo.generator.standard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import smartgrid.model.topo.generator.standard.data.MicroGrid;
import smartgrid.model.topo.generator.standard.data.PGNWithConnection;
import smartgrid.model.topo.generator.standard.data.SmartMeter;

/**
 * A Class representing a MicroGrid Generator, includes Value Checking of the MicroGrids
 * @author Eric Klappert
 *
 */
public abstract class AbstractMicroGridsGenerator implements IMicroGridsGenerator {

	private int powerWeight;
	private int critDuplicateWeight;
	private int critPowerWeight;
	
	public AbstractMicroGridsGenerator() {
		//set defaultWeights
		powerWeight = 1;
		critDuplicateWeight = 0;
		critPowerWeight = 0;
	}
	
	
	public abstract List<MicroGrid> generateMicrogrids(LinkedHashMap<String, PGNWithConnection> pgns);
	
	/**
	 * Calculates a Value representing the quality of a Microgrid
	 * @return value of the MicroGrid
	 */
	public int getMicroGridValue(MicroGrid grid) {
		//TODO: add additional parameters like critical Infrastructures etc
		//difference between power consumption and power creation
		//number of critical infrastructures, multiple of the same time are not allowed
		int value = 0;
		double power = grid.getPowerBalance();
		if (power < 0)
			value += power * powerWeight;
		

		Set<String> types = new HashSet<String>();
		int duplicatesCounter = 0;
		double critpower = 0;
		int critsum = 0;
		double noncritPowerProduction = 0;
		for (PGNWithConnection pgn : grid.getPGNs().values()) {
			for (SmartMeter sm : pgn.getConnectedSM()) {
				if (sm.getCritValue() > -1) {
					//get sum of critical infrastructes criticality and power
					critsum += sm.getCritValue();
					critpower += sm.getPower();
					//Find out if there are two critical infrastructures with the same type
					if (types.contains(sm.getCritType())) {
						duplicatesCounter++;
					}
					else {
						types.add(sm.getCritType());
					}
				}
				else {
					if (sm.getPower() > 0) {
						noncritPowerProduction += sm.getPower();
					}
				}
			}
		}
		value += duplicatesCounter * critDuplicateWeight;
		//optional: use critsum to increase this value if criticality is high by adding * critsum
		value += (noncritPowerProduction + critpower) * critPowerWeight; 
		
		
		return value;
	}
	
	/**
	 * Calculates a Value representing the quality of a List of microGrids
	 * @param microGrids List of micro Grids to check
	 * @return sum of Values of the microGrids
	 */
	public int getMicroGridsValue(List<MicroGrid> microGrids) {
		int value = 0;
		for (MicroGrid grid : microGrids) {
			value += getMicroGridValue(grid);
		}
		return value;
	}
	
	public void setPowerWeight(int value) {
		powerWeight = value;
	}
	
	public void setCritDuplicatesWeight(int value) {
		critDuplicateWeight = value;
	}
	
	public void setCritPowerWeight(int value) {
		critPowerWeight = value;
	}
}
