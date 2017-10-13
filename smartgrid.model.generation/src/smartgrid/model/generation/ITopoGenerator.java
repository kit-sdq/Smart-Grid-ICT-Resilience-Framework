package smartgrid.model.generation;

import smartgridtopo.SmartGridTopology;

public interface ITopoGenerator {
    SmartGridTopology generateTopo(Object smartMeterGeoData);
}
