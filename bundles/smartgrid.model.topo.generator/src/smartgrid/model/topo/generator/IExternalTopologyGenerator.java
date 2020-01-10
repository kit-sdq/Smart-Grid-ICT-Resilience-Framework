package smartgrid.model.topo.generator;

import java.util.List;

import smartgrid.model.topo.generator.data.MicroGrid;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoFactory;

/**
 * Interface for the External Topology Generator
 * 
 * @author Eric Klappert
 *
 */
public interface IExternalTopologyGenerator {
	/**
	 * Generates a Topology for the Microgrids.
	 * 
	 * @param grid The Microgrid the internal Topology should be generated for
	 * @param topo The SmartGridTopology the elements should be added to
	 * @param topoFactory Factory to create the elements
	 * @param gen TopologyGenerator giving Access to the CreatePhysicalConnection method
	 */
	public void generateExternalTopology(List<MicroGrid> microgrids, TopologyGenerator topogen, SmartGridTopology topo, SmartgridtopoFactory topoFactory);
}
