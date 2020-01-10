package smartgrid.model.topo.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import couplingToICT.SmartComponentGeoData;
import couplingToICT.SmartGridTopoContainer;
import smartgrid.helper.UIDHelper;
import smartgrid.model.test.generation.AbstractTopoGenerator;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoFactory;
import smartgrid.model.topo.generator.InternalTopologyGenerator;
import smartgrid.model.topo.generator.MicroGridsGenerator;
import smartgrid.model.topo.generator.data.MicroGrid;
import smartgrid.model.topo.generator.data.PGNWithConnection;
import smartgrid.model.topo.generator.data.SmartMeter;
import smartgrid.model.topo.generator.data.SmartMeterAddonData;
import smartgridtopo.impl.SmartgridtopoPackageImpl;

/**
 * Main Topology Geneator Class
 * Extends the AbstractTopologyGenerator by Implementing the generateTopo method
 * @author Eric Klappert
 *
 */
public class TopologyGenerator extends AbstractTopoGenerator {

	private IInternalTopologyGenerator internalTopologyGenerator;
	private IMicroGridsGenerator microGridsGenerator;
	private IExternalTopologyGenerator externalTopologyGenerator;
	
	//This HashMap contains the additional Data for the SmartMeters like Power
	//This should be part of the TopologyContainer (the SmartMeterGeoData actually), but currently is not
	private HashMap<String, SmartMeterAddonData> smartMeterAddonData;
	
	private LinkedHashMap<String,PGNWithConnection> PGNs;
	//Microgrid list is used mainly for validation
	private List<MicroGrid> microgrids;

    @Override
    public SmartGridTopology generateTopo(SmartGridTopoContainer topoData) {
        
        // create root container for the Topology
        SmartgridtopoPackageImpl.init();
        final SmartgridtopoFactory topoFactory = SmartgridtopoFactory.eINSTANCE;
        final SmartGridTopology topo = topoFactory.createSmartGridTopology();
        topo.setId(UIDHelper.generateUID());
        
		PGNs = new LinkedHashMap<String,PGNWithConnection>();
		//TODO:MEB:Auskommentiert um später zu wissen was mit High Voltage nodes zu machen
		//translateInput(topoData, topo);
		
		microgrids = microGridsGenerator.generateMicrogrids(PGNs);
		
		for (MicroGrid grid : microgrids) {
			internalTopologyGenerator.generateTopologyforMicrogrid(grid, topo, topoFactory, this);
		}
		
		externalTopologyGenerator.generateExternalTopology(microgrids, this, topo, topoFactory);
		
        return topo;
    }
	
	public TopologyGenerator() {
		super();
		//set the Generators to the default ones
		microGridsGenerator = new MicroGridsGenerator();
		internalTopologyGenerator = new InternalTopologyGenerator();
		externalTopologyGenerator = new ExternalTopologyGenerator();
	}
	
	/**
	 * Returns the Microgrids generated the last Time generateTopo was called.
	 * Used for evaluation
	 * @return list of microgrids generated with generateTopo
	 */
	public List<MicroGrid> getMicrogrids() {
		return microgrids;
	}
	
	//TODO:MEB:Auskommentiert um später zu wissen was mit High Voltage nodes zu machen
//	/**
//	 * Translates the Data from the TopologyContainer into Data that
//	 * is easier to work with for the purpose of creating the Topology
//	 * @param container TopologyContainer with the Input Data
//	 * @param a SmartGridTopology to put the Data into
//	 */
//	private void translateInput(SmartGridTopoContainer container, SmartGridTopology topo) {
//		ArrayList<HighVoltageNode> uncheckedHVNs = new ArrayList<>();
//		for (HighVoltageNode node : container.getHighVoltageNodes().values()) {
//			uncheckedHVNs.add(node);
//		}
//		while (uncheckedHVNs.size() > 0) {
//			HighVoltageNode node = uncheckedHVNs.get(0);
//			PGNWithConnection pgn = new PGNWithConnection();
//			addPGN(pgn, node, container);
//			uncheckedHVNs.remove(0);
//			recursiveHVNTranslate(uncheckedHVNs, node, pgn, container);
//		}
//	}
	
	//TODO:MEB:Auskommentiert um später zu wissen was mit High Voltage nodes zu machen
//	private void recursiveHVNTranslate(ArrayList<HighVoltageNode> uncheckedHVNs, HighVoltageNode currentNode, PGNWithConnection currentPGN, TopologyContainer container) {
//		//This adds all nodes connected to the current node and for each adds their connected nodes recursivly
//		//Depth-First search
//		for (int i = 0; i < currentNode.getConnectionsCount(); i++) {
//			String id = "";
//			//connection goes both ways, find the node that is not the current node
//			if (!currentNode.getConnection(i).getFromNodeId().equals(currentNode.getId())) {
//				id = currentNode.getConnection(i).getFromNodeId();
//			} else if (!currentNode.getConnection(i).getToNodeId().equals(currentNode.getId())) {
//				id = currentNode.getConnection(i).getToNodeId();
//			}
//			if (id.equals(""))
//				continue;
//			
//			HighVoltageNode otherHVN = null;
//			PGNWithConnection targetPGN;
//			//is the target node among the unchecked ones? if yes removes it from the unchecked ones
//			otherHVN = findHVNinuncheckedHVNs(uncheckedHVNs, id);
//			if (otherHVN == null) {
//				//if other Node has been checked before it already exists.
//				targetPGN = PGNs.get(id);
//			} else {
//				//if not, create it
//				targetPGN = new PGNWithConnection();
//				addPGN(targetPGN, otherHVN, container);
//			}
//			//set a connection from the target Node to the current one, both ways
//			targetPGN.addConnection(currentPGN);
//			currentPGN.addConnection(targetPGN);
//			//if the target was among the unchecked PGNs than recursivly add all nodes connected to it
//			if (otherHVN != null)
//				recursiveHVNTranslate(uncheckedHVNs, otherHVN, targetPGN, container);
//		}
//	}
	
	//TODO:MEB:Auskommentiert um später zu wissen was mit High Voltage nodes zu machen
//	private void addPGN(PGNWithConnection pgn,HighVoltageNode hvn, TopologyContainer container) {
//		PGNs.put(hvn.getId(), pgn);
//		pgn.setId(hvn.getId());
//		//add SM connected to PGN
//		Collection<SmartComponentGeoData> data = container.getMapCItoHVN().get(hvn.getId()).values();
//		for (SmartComponentGeoData sm : data) {
//			SmartMeter meter = new SmartMeter();
//			meter.setPosition(sm.getLat(), sm.getLon());
//			if (smartMeterAddonData != null) {
//				SmartMeterAddonData addonData = smartMeterAddonData.get(sm.getCompID());
//				meter.setPower(addonData.getPower());
//				meter.setCritValue(addonData.getCritValue());
//				meter.setCritType(addonData.getCritType());
//			}
//			//otherData copy
//			pgn.addConnection(meter);
//		}
//	}
	
	//TODO:MEB:Auskommentiert um später zu wissen was mit High Voltage nodes zu machen
//	private HighVoltageNode findHVNinuncheckedHVNs(ArrayList<HighVoltageNode> uncheckedHVNs, String id) {
//		for (int j = 0; j < uncheckedHVNs.size(); j++) {
//			if (uncheckedHVNs.get(j).getId().equals(id)) {
//				HighVoltageNode node = uncheckedHVNs.get(j);
//				//removes the node from the uncheckedHVNs
//				uncheckedHVNs.remove(j);
//				return node;
//			}
//		}
//		return null;
//	}
	
	public void setInternalTopologyGenerator(IInternalTopologyGenerator generator) {
		internalTopologyGenerator = generator;
	}
	
	public void setMicroGridsGenerator(IMicroGridsGenerator generator) {
		microGridsGenerator = generator;
	}
	
	public void setExternalTopologyGenerator(IExternalTopologyGenerator generator) {
		externalTopologyGenerator = generator;
	}
	
	public void setSmartMeterAddonData(LinkedHashMap<String, SmartMeterAddonData> data) {
		smartMeterAddonData = data;
	}
	
}
