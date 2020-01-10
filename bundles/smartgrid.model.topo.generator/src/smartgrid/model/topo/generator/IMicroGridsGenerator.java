package smartgrid.model.topo.generator;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import smartgrid.model.topo.generator.data.MicroGrid;
import smartgrid.model.topo.generator.data.PGNWithConnection;

/**
 * Interface for the MicroGridsGenerator
 * @author Eric Klappert
 *
 */
public interface IMicroGridsGenerator {

	/**
	 * Generates Microgrids for based on the Network
	 * @param pgns Power Grid nodes with connections and smart meter information
	 * @return a list of Microgrids containing all pgns
	 */
	public List<MicroGrid> generateMicrogrids(LinkedHashMap<String, PGNWithConnection> pgns);
}
