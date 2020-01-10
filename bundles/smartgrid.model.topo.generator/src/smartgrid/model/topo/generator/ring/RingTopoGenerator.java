package smartgrid.model.topo.generator.ring;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import couplingToICT.SmartComponentGeoData;
import couplingToICT.SmartGridTopoContainer;
import smartgrid.helper.UIDHelper;
import smartgrid.model.test.generation.AbstractTopoGenerator;
import smartgridtopo.ControlCenter;
import smartgridtopo.NetworkEntity;
import smartgridtopo.NetworkNode;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;
import smartgridtopo.SmartgridtopoFactory;
import smartgridtopo.impl.SmartgridtopoPackageImpl;

public class RingTopoGenerator extends AbstractTopoGenerator {

    private static final Logger LOG = Logger.getLogger(RingTopoGenerator.class);

    @Override
    public SmartGridTopology generateTopo(SmartGridTopoContainer topoData) {

        LOG.info("Starting generation of trivial ICT topology.");

        HashMap<String, Map<String, SmartComponentGeoData>> smartMeterGeoData = topoData.getSmartMeterContainer();

        // create root container
        SmartgridtopoPackageImpl.init();
        final SmartgridtopoFactory topoFactory = SmartgridtopoFactory.eINSTANCE;
        final SmartGridTopology topo = topoFactory.createSmartGridTopology();
        topo.setId(UIDHelper.generateUID());

        // create command center
        ControlCenter controlCenter = topoFactory.createControlCenter();
        controlCenter.setId(UIDHelper.generateUID());
        topo.getContainsNE().add(controlCenter);

        // keep track of the last node of last iteration so that network nodes can be chained
        NetworkEntity lastNode = controlCenter;

        // iterate nodes
        for (Entry<String, Map<String, SmartComponentGeoData>> nodeEntry : smartMeterGeoData.entrySet()) {

            PowerGridNode powerGridNode = null;

            // iterate smart meters
            for (Entry<String, ?> smartMeterEntry : nodeEntry.getValue().entrySet()) {

                // create node
                powerGridNode = topoFactory.createPowerGridNode();
                setNameAndId(smartMeterEntry, powerGridNode, "_PGN");
                topo.getContainsPGN().add(powerGridNode);

                // create smart meter
                SmartMeter smartMeter = topoFactory.createSmartMeter();
                setNameAndId(smartMeterEntry, smartMeter);
                topo.getContainsNE().add(smartMeter);

                // connect to power
                smartMeter.getConnectedTo().add(powerGridNode);

                // connect the smart meter
                createLogicalConnection(topoFactory, topo, controlCenter, smartMeter);
                
                if (powerGridNode != null) {
                    // chain the network
                    createPhysicalConnection(topoFactory, topo, lastNode, smartMeter);
                    lastNode = smartMeter;
                } else {
                    LOG.info("Node " + nodeEntry.getKey() + " has no prosumers. I will not create a NetworkNode.");
                }
            }
        }
        
        createLogicalConnection(topoFactory, topo, controlCenter, (SmartMeter)lastNode);
        createPhysicalConnection(topoFactory, topo, controlCenter, lastNode);

        if (topo.getContainsPGN().size() > 0) {
            // connect command center and its network node to power
            PowerGridNode firstNode = topo.getContainsPGN().get(0);
            controlCenter.getConnectedTo().add(firstNode);
        } else {
            LOG.error("The generated topology does not have any power nodes. No meaningful results will be produced.");
        }

        LOG.info("Generation of Ring ICT topology finished.");

        return topo;
    }
}
