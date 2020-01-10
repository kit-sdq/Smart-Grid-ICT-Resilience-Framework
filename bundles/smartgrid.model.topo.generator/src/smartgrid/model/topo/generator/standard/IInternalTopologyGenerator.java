package smartgrid.model.topo.generator.standard;

import smartgrid.model.topo.generator.standard.data.MicroGrid;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoFactory;

/**
 * Interface for the Internal Topology Generator
 * 
 * @author Eric Klappert
 *
 */
public interface IInternalTopologyGenerator {
	/**
	 * Generates a Topology for the given Microgrid, the Topology should consist of a command center and physical and logical connections
	 * to the different smart Meters in the Grid
	 * 
	 * @param grid The Microgrid the internal Topology should be generated for
	 * @param topo The SmartGridTopology the elements should be added to
	 * @param topoFactory Factory to create the elements
	 * @param gen TopologyGenerator giving Access to the CreatePhysicalConnection method
	 */
	public void generateTopologyforMicrogrid(MicroGrid grid, SmartGridTopology topo, SmartgridtopoFactory topoFactory, TopologyGenerator gen);
}
