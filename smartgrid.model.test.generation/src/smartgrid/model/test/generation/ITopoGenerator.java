package smartgrid.model.test.generation;

import couplingToICT.SmartGridTopoContainer;
import smartgridtopo.SmartGridTopology;

public interface ITopoGenerator {
    SmartGridTopology generateTopo(SmartGridTopoContainer topoData);
}
