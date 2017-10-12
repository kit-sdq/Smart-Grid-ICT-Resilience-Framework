package smartgrid.model.generation;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import smartgridtopo.ControlCenter;
import smartgridtopo.LogicalCommunication;
import smartgridtopo.NamedIdentifier;
import smartgridtopo.NetworkEntity;
import smartgridtopo.NetworkNode;
import smartgridtopo.PhysicalConnection;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;
import smartgridtopo.SmartgridtopoFactory;
import smartgridtopo.impl.SmartgridtopoPackageImpl;

public class TrivialTopoGenerator implements ITopoGenerator {

    private static final Logger LOG = Logger.getLogger(TrivialTopoGenerator.class);

    @Override
    public SmartGridTopology generateTopo(Object _smartMeterGeoData) {

        Map<String, Map<String, ?>> smartMeterGeoData = (Map<String, Map<String, ?>>) _smartMeterGeoData;

        // create root container
        SmartgridtopoPackageImpl.init();
        final SmartgridtopoFactory topoFactory = SmartgridtopoFactory.eINSTANCE;
        final SmartGridTopology topo = topoFactory.createSmartGridTopology();

        // create command center
        ControlCenter controlCenter = topoFactory.createControlCenter();
        topo.getContainsNE().add(controlCenter);

        // create network node for command center and connect
        NetworkNode controlCenterNetworkNode = topoFactory.createNetworkNode();
        topo.getContainsNE().add(controlCenterNetworkNode);
        createPhysicalConnection(topoFactory, topo, controlCenterNetworkNode, controlCenter);

        // keep track of the network node of last iteration so that network nodes can be chained
        NetworkNode lastNetworkNode = controlCenterNetworkNode;

        // iterate nodes
        for (Entry<String, Map<String, ?>> nodeEntry : smartMeterGeoData.entrySet()) {

            // create node
            PowerGridNode powerGridNode = topoFactory.createPowerGridNode();
            if (!setNameAndId(nodeEntry, powerGridNode))
                continue;
            topo.getContainsPGN().add(powerGridNode);

            // create network node
            NetworkNode networkNode = topoFactory.createNetworkNode();
            topo.getContainsNE().add(networkNode);

            // chain the network
            createPhysicalConnection(topoFactory, topo, lastNetworkNode, networkNode);
            lastNetworkNode = networkNode;

            // iterate smart meters
            for (Entry<String, ?> smartMeterEntry : nodeEntry.getValue().entrySet()) {

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

        // connect command center and its network node to power
        PowerGridNode firstNode = topo.getContainsPGN().get(0);
        controlCenter.getConnectedTo().add(firstNode);
        controlCenterNetworkNode.getConnectedTo().add(firstNode);

        return topo;

//        // Init Scenario State
//        SmartgridinputPackageImpl.init();
//        final SmartgridinputFactory inputFactory = SmartgridinputFactory.eINSTANCE;
//        final ScenarioState input = inputFactory.createScenarioState();
    }

    public void createLogicalConnection(final SmartgridtopoFactory topoFactory, final SmartGridTopology topo, ControlCenter controlCenter, SmartMeter smartMeter) {
        LogicalCommunication logicalCommunication = topoFactory.createLogicalCommunication();
        smartMeter.getCommunicatesBy().add(logicalCommunication);
        controlCenter.getCommunicatesBy().add(logicalCommunication);
        topo.getContainsLC().add(logicalCommunication);
    }

    public void createPhysicalConnection(final SmartgridtopoFactory topoFactory, final SmartGridTopology topo, NetworkEntity networkNode, NetworkEntity smartMeter) {
        PhysicalConnection physicalConnection = topoFactory.createPhysicalConnection();
        smartMeter.getLinkedBy().add(physicalConnection);
        networkNode.getLinkedBy().add(physicalConnection);
        topo.getContainsPC().add(physicalConnection);
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
