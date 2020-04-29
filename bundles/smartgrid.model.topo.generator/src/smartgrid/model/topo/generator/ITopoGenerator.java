package smartgrid.model.topo.generator;

import couplingToICT.SmartGridTopoContainer;
import smartgridtopo.SmartGridTopology;

public interface ITopoGenerator {
    SmartGridTopology generateTopo(SmartGridTopoContainer topoData);
}
