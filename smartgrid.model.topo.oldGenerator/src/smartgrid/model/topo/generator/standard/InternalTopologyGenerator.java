package smartgrid.model.topo.generator.standard;

import java.util.ArrayList;

import smartgrid.helper.UIDHelper;
import smartgridtopo.ControlCenter;
import smartgridtopo.NetworkNode;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;
import smartgridtopo.SmartgridtopoFactory;
import smartgrid.model.topo.generator.standard.Point;
import smartgrid.model.topo.generator.standard.SmallestEnclosingCircle;
import smartgrid.model.topo.generator.standard.TopologyGenerator;
import smartgrid.model.topo.generator.standard.data.MicroGrid;
import smartgrid.model.topo.generator.standard.data.PGNWithConnection;

/**
 * Implementation of IinternalTopology Generator
 * Creates an Internal Topology by creating a controlCenter in the middle of the Microgrid
 * and connecting all Smart Meters directly to it
 * @author Eric Klappert
 *
 */
public class InternalTopologyGenerator implements IInternalTopologyGenerator {
	
	public void generateTopologyforMicrogrid(MicroGrid grid, SmartGridTopology topo, SmartgridtopoFactory topoFactory, TopologyGenerator gen) {
		ArrayList<Point> points = new ArrayList<>();
		
		// create command center
        ControlCenter controlCenter = topoFactory.createControlCenter();
        controlCenter.setId(UIDHelper.generateUID());
        topo.getContainsNE().add(controlCenter);
		
        NetworkNode controlCenterNetworkNode = topoFactory.createNetworkNode();
        controlCenterNetworkNode.setId(UIDHelper.generateUID());
        controlCenterNetworkNode.setName("NetworkOf_ControlCenter");
        topo.getContainsNE().add(controlCenterNetworkNode);
		
		for (PGNWithConnection pgn : grid.getPGNs().values()) {
			// create node
            PowerGridNode powerGridNode = topoFactory.createPowerGridNode();
            //setNameAndId(smartMeterEntry, powerGridNode, "_PGN");
            topo.getContainsPGN().add(powerGridNode);
			for (smartgrid.model.topo.generator.standard.data.SmartMeter sm : pgn.getConnectedSM()) {
				points.add(new Point(sm.getLat(), sm.getLon()));
				

                // create smart meter
                SmartMeter smartMeter = topoFactory.createSmartMeter();
                //setNameAndId(smartMeterEntry, smartMeter);
                topo.getContainsNE().add(smartMeter);
                smartMeter.setXCoord(sm.getLat());
                smartMeter.setYCoord(sm.getLon());

                // connect to power
                smartMeter.getConnectedTo().add(powerGridNode);

                // connect the smart meter
                gen.createPhysicalConnection(topoFactory, topo, controlCenterNetworkNode, smartMeter);
                gen.createLogicalConnection(topoFactory, topo, controlCenter, smartMeter);
				
				
			}
		}
		Point center = SmallestEnclosingCircle.makeCircle(points).c;
		

        controlCenter.setXCoord(center.x);
        controlCenter.setYCoord(center.y);
        
        grid.setControlCenter(controlCenterNetworkNode);
		
        gen.createPhysicalConnection(topoFactory, topo, controlCenterNetworkNode, controlCenter);
	}
}
