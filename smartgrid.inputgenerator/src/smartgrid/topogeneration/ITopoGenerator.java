package smartgrid.topogeneration;

import java.util.Map;

import smartgrid.simcontrol.coupling.SmartMeterGeoData;
import smartgridtopo.SmartGridTopology;

public interface ITopoGenerator {
    SmartGridTopology generateTopo(Map<String, Map<String, SmartMeterGeoData>> smartMeterGeoData);
}
