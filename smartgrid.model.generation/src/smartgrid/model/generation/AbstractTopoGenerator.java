package smartgrid.model.generation;

import java.util.Map.Entry;

import org.apache.log4j.Logger;

import smartgridtopo.ControlCenter;
import smartgridtopo.LogicalCommunication;
import smartgridtopo.NamedIdentifier;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PhysicalConnection;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;
import smartgridtopo.SmartgridtopoFactory;

public abstract class AbstractTopoGenerator {

    static final Logger LOG = Logger.getLogger(AbstractTopoGenerator.class);

    public abstract SmartGridTopology generateTopo(Object _smartMeterGeoData);

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

        String idString = nodeKey;
        if (idString.contains("_")) {
            idString = idString.split("_")[1]; // only take the number of the ID            
        }

        entity.setName(nodeKey);
        int nodeId;
        try {
            nodeId = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            LOG.error("Could not convert node key to int (" + nodeKey + "). Skipping this node.");
            return false;
        }
        entity.setId(nodeId);
        return true;
    }

}