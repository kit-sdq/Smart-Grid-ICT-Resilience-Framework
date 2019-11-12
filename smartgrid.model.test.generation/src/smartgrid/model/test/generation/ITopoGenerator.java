package smartgrid.model.test.generation;

import smartgrid.coupling.ICT.SmartGridTopoContainer;
import smartgridtopo.SmartGridTopology;

public interface ITopoGenerator {
    SmartGridTopology generateTopo(SmartGridTopoContainer topoData);
}
