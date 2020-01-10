package smartgrid.model.topo.generator;

import java.util.List;

import smartgrid.model.topo.generator.data.MicroGrid;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoFactory;

public class ExternalTopologyGenerator implements IExternalTopologyGenerator {
	public void generateExternalTopology(List<MicroGrid> microgrids, TopologyGenerator topogen, SmartGridTopology topo, SmartgridtopoFactory topoFactory) {
		for (MicroGrid grid : microgrids) {
			List<MicroGrid> neighbours = grid.getNeighbours(microgrids);
			for(MicroGrid neighbour : neighbours) {
				topogen.createPhysicalConnection(topoFactory, topo, grid.getControlCenter(), neighbour.getControlCenter());
			}
		}
	}
}
