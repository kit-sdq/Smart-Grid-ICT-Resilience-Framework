package smartgrid.model.topo.generator;

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

public abstract class AbstractTopoGenerator implements ITopoGenerator {

    static final Logger LOG = Logger.getLogger(AbstractTopoGenerator.class);

    public void createLogicalConnection(final SmartgridtopoFactory topoFactory, final SmartGridTopology topo, ControlCenter controlCenter, SmartMeter smartMeter) {
        LogicalCommunication logicalCommunication = topoFactory.createLogicalCommunication();
        smartMeter.getCommunicatesBy().add(logicalCommunication);
        controlCenter.getCommunicatesBy().add(logicalCommunication);
        topo.getContainsLC().add(logicalCommunication);
    }

    public void createPhysicalConnection(final SmartgridtopoFactory topoFactory, final SmartGridTopology topo, NetworkEntity networkEntity1, NetworkEntity networkEntity2) {
        PhysicalConnection physicalConnection = topoFactory.createPhysicalConnection();
        networkEntity2.getLinkedBy().add(physicalConnection);
        networkEntity1.getLinkedBy().add(physicalConnection);
        topo.getContainsPC().add(physicalConnection);
    }

    public void setNameAndId(Entry<String, ?> nodeEntry, NamedIdentifier entity) {
        setNameAndId(nodeEntry, entity, "");
    }

    public void setNameAndId(Entry<String, ?> nodeEntry, NamedIdentifier entity, String prefix) {
        String nodeKey = nodeEntry.getKey();
        entity.setName(nodeKey + prefix);
        entity.setId(nodeKey + prefix);
    }
}