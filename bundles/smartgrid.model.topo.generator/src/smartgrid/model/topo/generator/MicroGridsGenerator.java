package smartgrid.model.topo.generator;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import smartgrid.model.topo.generator.data.MicroGrid;
import smartgrid.model.topo.generator.data.PGNWithConnection;

/**
 * A Class used to partition a List of Power Grid Nodes with Connections into MicroGrids
 * 
 * Uses a Greedy Algorithm to try and create a good solution
 * 
 * @author Eric Klappert
 *
 */
public class MicroGridsGenerator extends AbstractMicroGridsGenerator{
	
	static final int defaultMaxSize = 10;
	static final int defaultMinSize = 1;
	
	private int maxSize;
	private int minSize;
	
	public MicroGridsGenerator() {
		super();
		maxSize = defaultMaxSize;
		minSize = defaultMinSize;
	}
	
	public List<MicroGrid> generateMicrogrids(LinkedHashMap<String, PGNWithConnection> pgns) {
		PriorityQueue<MicroGrid> microgrids = new PriorityQueue<MicroGrid>(new Comparator<MicroGrid>() {
			@Override
			public int compare(MicroGrid first, MicroGrid second) {
				return getMicroGridValue(second) - getMicroGridValue(first);
			}
		});
		LinkedList<MicroGrid> completeMicrogrids = new LinkedList<>();
		//create a Microgrid for each PGN
		for (PGNWithConnection pgn : pgns.values()) {
			MicroGrid microgrid = new MicroGrid();
			microgrid.addPGN(pgn);
			microgrids.add(microgrid);
		}
		
		while (microgrids.size() > 1) {
			MicroGrid grid = microgrids.poll();
			List<MicroGrid> neighbours = grid.getNeighbours(microgrids);
			if (neighbours.size() < 1) {
				completeMicrogrids.add(grid);
				continue;
			}
			MicroGrid bestNeighbour = null;
			MicroGrid bestResult = null;
			int bestValue = 0;
			//finds the best Neighbour
			for(MicroGrid neighbour : neighbours) {
				MicroGrid result = MicroGrid.mergeMicroGrids(grid, neighbour);
				//if the result would be larger than maxSize than it should not be considered, continue with next neighbour
				if (result.getPGNs().size()  > maxSize)
					continue;
				//if no bestNeighbour has been set, set it to this one
				//if the bestNeighbour is not null, than check if the new result is better than the current one
				if (bestNeighbour == null || getMicroGridValue(result) > bestValue) {
					bestNeighbour = neighbour;
					bestResult = result;
					bestValue = getMicroGridValue(bestResult);
				}
			}
			//if there is no neighbour that can be merged with this grid without being larger than maxSize, this grid is complete
			if (bestNeighbour == null) {
				completeMicrogrids.add(grid);
				continue;
			}
			//same if the Value of the bestResult would be lower than the value of the current grid,
			//but minSize has been reached/surpassed
			if (bestResult.getPGNs().size() > minSize && bestValue < getMicroGridValue(grid)) {
				completeMicrogrids.add(grid);
				continue;
			}
			microgrids.remove(bestNeighbour);
			microgrids.add(bestResult);
		}
		if (microgrids.size() > 0)
			completeMicrogrids.add(microgrids.poll());
		return completeMicrogrids;
	}
	
	public void setMaxSize(int size) {
		maxSize = size;
	}
	
	public void setMinSize(int size) {
		minSize = size;
	}
	
}
