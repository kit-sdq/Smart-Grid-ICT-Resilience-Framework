package smartgrid.model.topo.generator.standard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import smartgrid.model.topo.generator.standard.data.MicroGrid;
import smartgrid.model.topo.generator.standard.data.PGNWithConnection;

/**
 * A Microgrid generator using a brute Force approach.
 * It tests all possible combinations and chooses the optimal one.
 * 
 * Not optimized, expected Runtime is 2^N with N being the number of connections
 * Should not be used, exists mainly for evaluation purposes and comparison with other Generators
 * @author Eric Klappert
 *
 */
public class MicroGridsGeneratorBruteforce extends AbstractMicroGridsGenerator{
	
	public List<MicroGrid> generateMicrogrids(LinkedHashMap<String, PGNWithConnection> pgns) {
		//first make an array list of all connections and a hashmap to map microgrids to pgns
		ArrayList<Connection> connections = new ArrayList<Connection>();
		for(PGNWithConnection pgn : pgns.values()) {
			for (PGNWithConnection connectedPGN : pgn.getConnectedPGN()) {
				//Check all connections if connection already exists
				boolean exists = false;
				for (Connection con : connections) {
					if ((con.pgnA == pgn && con.pgnB == connectedPGN) || (con.pgnB == pgn && con.pgnA == connectedPGN)) {
						exists = true;
						break;
					}
				}
				if (!exists) {
					connections.add(new Connection(pgn, connectedPGN));
				}
			}
			
		}

		int bestValue = 0;
		int smallestSize = 0;
		LinkedList<MicroGrid> bestMicroGrids = null;
	
		//iterate through all possible combinations
		boolean[] connected = new boolean[connections.size() + 1];
		while (!connected[connected.length - 1]) {
			//Generate MicroGrids and link their PGNs to them
			LinkedHashMap<PGNWithConnection,MicroGrid> pgnToMicroGrid = new LinkedHashMap<PGNWithConnection, MicroGrid>();
			for(PGNWithConnection pgn : pgns.values()) {
				MicroGrid grid = new MicroGrid();
				grid.addPGN(pgn);
				pgnToMicroGrid.put(pgn, grid);
			}
			
			//Iterate all connections and if they are true, merge the microgrids of the pgns
			for(int i = 0; i < connected.length - 1; i++) {
				if (connected[i]) {
					MicroGrid gridA = pgnToMicroGrid.get(connections.get(i).pgnA);
					MicroGrid gridB = pgnToMicroGrid.get(connections.get(i).pgnB);
					MicroGrid result = MicroGrid.mergeMicroGrids(gridA, gridB);
					for (PGNWithConnection pgn : result.getPGNs().values()) {
						pgnToMicroGrid.put(pgn, result);
					}
				}
			}
			
			LinkedList<MicroGrid> microGrids = new LinkedList<MicroGrid>();
			int biggestGrid = 0;
			int value = 0;
			for (MicroGrid grid : pgnToMicroGrid.values()) {
				if (!microGrids.contains(grid)) {
					microGrids.add(grid);
					//grids producing less power than consuming result in lower value
					if (grid.getPowerBalance() < 0)
						value--;
					if (biggestGrid < grid.getPGNs().size())
						biggestGrid = grid.getPGNs().size();
				}
			}
			if (bestMicroGrids == null  || bestValue < value || (bestValue == value && biggestGrid < smallestSize)) {
				bestMicroGrids = microGrids;
				bestValue = value;
				smallestSize = biggestGrid;
			}
			//Increment connection array to represent next combination
			for (int i = 0; i < connected.length; i++) {
				if (connected[i] == false) {
					connected[i] = true;
					break;
				}
				connected[i] = false;
			}
		}
		return bestMicroGrids;
	}

	
	/**
	 * Helper Class representing a connection between two PGNs
	 * @author Eric Klappert
	 *
	 */
	class Connection {
		public PGNWithConnection pgnA;
		public PGNWithConnection pgnB;
		
		public Connection(PGNWithConnection pPgnA, PGNWithConnection pPgnB) {
			pgnA = pPgnA;
			pgnB = pPgnB;
		}
	}
}
