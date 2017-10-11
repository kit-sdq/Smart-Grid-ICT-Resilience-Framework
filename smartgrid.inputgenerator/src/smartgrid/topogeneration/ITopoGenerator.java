package smartgrid.topogeneration;

import java.util.Map;

import smartgrid.simcontrol.coupling.SmartMeterGeoData;

public interface ITopoGenerator {
    void generateTopo(Map<String, Map<String, SmartMeterGeoData>> smartMeterGeoData);
}
