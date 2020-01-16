package smartgrid.model.topo.generator.standard.tester;

import java.util.LinkedHashMap;

import couplingToICT.SmartGridTopoContainer;
import smartgrid.model.topo.generator.standard.data.SmartMeterAddonData;

public class TopologyContainerAddon {
	SmartGridTopoContainer topo;
	LinkedHashMap<String, SmartMeterAddonData> addonData;
	
	public TopologyContainerAddon(SmartGridTopoContainer pTopo, LinkedHashMap<String, SmartMeterAddonData> pAddonData) {
		topo = pTopo;
		addonData = pAddonData;
	}
	
	public SmartGridTopoContainer getTopologyContainer() {
		return topo;
	}
	
	public LinkedHashMap<String, SmartMeterAddonData> getAddonData() {
		return addonData;
	}
	
}
