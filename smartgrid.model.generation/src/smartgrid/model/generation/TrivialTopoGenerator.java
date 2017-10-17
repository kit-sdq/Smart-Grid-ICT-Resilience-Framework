package smartgrid.model.generation;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import smartgridtopo.ControlCenter;
import smartgridtopo.NetworkNode;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;
import smartgridtopo.SmartgridtopoFactory;
import smartgridtopo.impl.SmartgridtopoPackageImpl;

public class TrivialTopoGenerator extends AbstractTopoGenerator {

    private static final Logger LOG = Logger.getLogger(TrivialTopoGenerator.class);

    @Override
    public SmartGridTopology generateTopo(Object _smartMeterGeoData) {

        LOG.error("Starting generation of trivial ICT topology.");

        //TODO hack: remove
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
            setNameAndId(nodeEntry, powerGridNode);
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
                setNameAndId(smartMeterEntry, smartMeter);
                topo.getContainsNE().add(smartMeter);

                // connect to power
                smartMeter.getConnectedTo().add(powerGridNode);

                // connect the smart meter
                createPhysicalConnection(topoFactory, topo, networkNode, smartMeter);
                createLogicalConnection(topoFactory, topo, controlCenter, smartMeter);
            }
        }

        if (topo.getContainsPGN().size() > 0) {
            // connect command center and its network node to power
            PowerGridNode firstNode = topo.getContainsPGN().get(0);
            controlCenter.getConnectedTo().add(firstNode);
            controlCenterNetworkNode.getConnectedTo().add(firstNode);
        } else {
            LOG.error("The generated topology does not have any power nodes. No meaningful results will be produced.");
        }

        LOG.info("Trivial ICT topology was successfully generated.");

        return topo;
    }
}
