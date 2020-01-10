package smartgrid.model.topo.generator.standard.data;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import smartgrid.model.topo.generator.standard.data.MicroGrid;
import smartgrid.model.topo.generator.standard.data.PGNWithConnection;
import smartgridtopo.NetworkNode;

/**
 * Class representing a MicroGrid
 * a Microgrid consists of a hashmap PGNWithConnection and a Control Center
 * 
 * It has methods to determine the value of the Grid
 * @author Eric Klappert
 *
 */
public class MicroGrid {

	private LinkedHashMap<Integer,PGNWithConnection> PGNs;
	private NetworkNode controlCenter;
	
	public MicroGrid() {
		PGNs = new LinkedHashMap<Integer, PGNWithConnection>();
	}
	
	/**
	 * Adds a Power Grid Node to this Microgrid
	 * @param pgn the PGNWithConnection to add
	 */
	public void addPGN(PGNWithConnection pgn) {
		PGNs.put(Integer.parseInt(pgn.getId()), pgn);
	}
	
	/**
	 * Calculates the neighbouring MicroGrids, by checking for Power Grid Node Connections between them.
	 * @param microgrids List of Microgrids to check as potential neighbours
	 * @return List of MicroGrids connected to this microgrid by a Power Grid Node Connection
	 */
	public List<MicroGrid> getNeighbours(Iterable<MicroGrid> microgrids) {
		LinkedList<MicroGrid> neighbours = new LinkedList<MicroGrid>();
		for (PGNWithConnection pgn : PGNs.values()) {
			for (PGNWithConnection connected : pgn.getConnectedPGN()) {
				if (PGNs.containsKey(Integer.parseInt(connected.getId())))
					continue;
				
				//find which Microgrid contains the PGN
				for (MicroGrid otherGrid : microgrids) {
					if (otherGrid.getPGNs().containsKey(Integer.parseInt(connected.getId())))
						neighbours.add(otherGrid);
				}
			}
		}
		return neighbours;
	}
	
	public void setControlCenter(NetworkNode cc) {
		controlCenter = cc;
	}
	
	public NetworkNode getControlCenter() {
		return controlCenter;
	}
	
	public LinkedHashMap<Integer,PGNWithConnection> getPGNs() {
		return PGNs;
	}
	
	/**
	 * Calculates the Power Balance of this single Microgrid
	 * adds up the power of all included smart Meters
	 * @return
	 */
	public double getPowerBalance() {
		double sum = 0;
		for (PGNWithConnection pgn : PGNs.values()) {
			for (SmartMeter sm : pgn.getConnectedSM()) {
				sum += sm.getPower();
			}
		}
		return sum;
	}
	
	/**
	 * Creates a new Microgrid that is the Union of two MicroGrids
	 * Only Copies the Power Grid Nodes from both into a new Empty one.
	 * @param microgridA
	 * @param microgridB
	 * @return new Microgrid containing the Power Grid Nodes of the two input MicroGrids
	 */
	public static MicroGrid mergeMicroGrids(MicroGrid microgridA, MicroGrid microgridB) {
		//Return a microgrid including the nodes of two microgrids
		MicroGrid result = new MicroGrid();
		for (PGNWithConnection pgn : microgridA.getPGNs().values())
			result.addPGN(pgn);
		for (PGNWithConnection pgn : microgridB.getPGNs().values())
			result.addPGN(pgn);
		
		return result;
	}
}
