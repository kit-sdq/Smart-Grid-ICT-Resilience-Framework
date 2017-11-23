package smartgrid.model.generation;

import smartgrid.simcontrol.coupling.TopologyContainer;
import smartgridtopo.SmartGridTopology;

public interface ITopoGenerator {
    SmartGridTopology generateTopo(TopologyContainer topoData);
}
