package smartgrid.topogeneration;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import smartgrid.simcontrol.coupling.SmartMeterGeoData;
import smartgridtopo.ControlCenter;
import smartgridtopo.LogicalCommunication;
import smartgridtopo.NamedIdentifier;
import smartgridtopo.NetworkNode;
import smartgridtopo.PhysicalConnection;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;
import smartgridtopo.SmartgridtopoFactory;
import smartgridtopo.impl.SmartgridtopoPackageImpl;

public class TrivialTopoGenerator implements ITopoGenerator {

    private static final Logger LOG = Logger.getLogger(TrivialTopoGenerator.class);

    public void generateTopo(Map<String, Map<String, SmartMeterGeoData>> smartMeterGeoData) {

        // create root container
        SmartgridtopoPackageImpl.init();
        final SmartgridtopoFactory topoFactory = SmartgridtopoFactory.eINSTANCE;
        final SmartGridTopology topo = topoFactory.createSmartGridTopology();

        // create command center
        ControlCenter controlCenter = topoFactory.createControlCenter();
        topo.getContainsNE().add(controlCenter);

        // create network node for command center and connect
        NetworkNode networkNode = topoFactory.createNetworkNode();

//      NetworkNode lastNetworkNode = topo

        // iterate nodes
        for (Entry<String, Map<String, SmartMeterGeoData>> nodeEntry : smartMeterGeoData.entrySet()) {
            // create node
            PowerGridNode powerGridNode = topoFactory.createPowerGridNode();
            if (!setNameAndId(nodeEntry, powerGridNode))
                continue;
            topo.getContainsPGN().add(powerGridNode);

            // create netork node
            networkNode = topoFactory.createNetworkNode();

            // iterate smart meter
            for (Entry<String, SmartMeterGeoData> smartMeterEntry : nodeEntry.getValue().entrySet()) {
                // create smart meter
                SmartMeter smartMeter = topoFactory.createSmartMeter();
                if (!setNameAndId(smartMeterEntry, smartMeter))
                    continue;
                topo.getContainsNE().add(smartMeter);

                // connect to power
                smartMeter.getConnectedTo().add(powerGridNode);

                // connect the smart meter
                createPhysicalConnection(topoFactory, topo, networkNode, smartMeter);
                createLogicalConnection(topoFactory, topo, controlCenter, smartMeter);

            }
        }

        // connect command center to power
        PowerGridNode firstNode = topo.getContainsPGN().get(0);
        controlCenter.getConnectedTo().add(firstNode);

        // connect command center to the network

//        // Init Scenario State
//        SmartgridinputPackageImpl.init();
//        final SmartgridinputFactory inputFactory = SmartgridinputFactory.eINSTANCE;
//        final ScenarioState input = inputFactory.createScenarioState();
    }

    public LogicalCommunication createLogicalConnection(final SmartgridtopoFactory topoFactory, final SmartGridTopology topo, ControlCenter controlCenter, SmartMeter smartMeter) {
        LogicalCommunication logicalCommunication = topoFactory.createLogicalCommunication();
        smartMeter.getCommunicatesBy().add(logicalCommunication);
        controlCenter.getCommunicatesBy().add(logicalCommunication);
        topo.getContainsLC().add(logicalCommunication);
        return logicalCommunication;
    }

    public PhysicalConnection createPhysicalConnection(final SmartgridtopoFactory topoFactory, final SmartGridTopology topo, NetworkNode networkNode, SmartMeter smartMeter) {
        PhysicalConnection physicalConnection = topoFactory.createPhysicalConnection();
        smartMeter.getLinkedBy().add(physicalConnection);
        networkNode.getLinkedBy().add(physicalConnection);
        topo.getContainsPC().add(physicalConnection);
        return physicalConnection;
    }

    public boolean setNameAndId(Entry<String, ?> nodeEntry, NamedIdentifier entity) {
        String nodeKey = nodeEntry.getKey();
        entity.setName(nodeKey);
        int nodeId;
        try {
            nodeId = Integer.parseInt(nodeKey);
        } catch (NumberFormatException e) {
            LOG.error("Could not convert node key to int (" + nodeKey + "). Skipping this node.");
            return false;
        }
        entity.setId(nodeId);
        return true;
    }
}
