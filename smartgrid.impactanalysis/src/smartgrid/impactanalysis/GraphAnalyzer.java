package smartgrid.impactanalysis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
// import org.eclipse.emf.common.util.URI;
// import org.eclipse.emf.ecore.resource.Resource;
// import org.eclipse.emf.ecore.resource.ResourceSet;
// import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.helper.FileSystemHelper;
import smartgrid.model.helper.input.LoadInputModelConformityHelper;
import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.coupling.IImpactAnalysis;
import smartgridinput.EntityState;
import smartgridinput.PowerState;
import smartgridinput.ScenarioState;
import smartgridoutput.Cluster;
import smartgridoutput.NoUplink;
import smartgridoutput.Online;
import smartgridoutput.ScenarioResult;
import smartgridoutput.SmartgridoutputFactory;
import smartgridoutput.impl.SmartgridoutputPackageImpl;
import smartgridtopo.ControlCenter;
import smartgridtopo.LogicalCommunication;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PhysicalConnection;
import smartgridtopo.PowerGridNode;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;

/**
 * This Class does an Impact Analysis.
 *
 * The Target of this Analysis is to get to know which SmartMeters can communicate with which
 * ControlCenters respectively other SmartMeters For more Details see the Model Description in
 * the @see smartgrid.model.* packages
 *
 * @author Torsten
 * @author Christian (modified)
 * @implements IImpactAnalysis
 */
public class GraphAnalyzer implements IImpactAnalysis {

    private static final Logger LOG = Logger.getLogger(GraphAnalyzer.class);

    private Map<String, PowerState> powerStates;
    private Map<String, EntityState> entityStates;

    private Map<String, Integer> externalToInternalID;
    private Map<Integer, String> internalToExternalID;
    private Map<Integer, Cluster> internalToCluster;
    private Map<String, Double[]> controlCenterConnectivity;

    private String outputPath;
    private boolean initDone = false;
    private boolean ignoreLogicalConnections;

    List<List<Integer>> physicalClusters;
    List<List<Integer>> logicalClusters;

    // Die Adjazenzmatrizen waren noch vom vorherigen Ansatz
    double[][] adjacentMatrix;
    double[][] logicalAdjacentMatrix;
    List<String> logicalNodes;
    List<String> controlCenters;
    int internalMaxID;

    /**
     * For ExtensionPoints .. use this together with the init() Method
     */
    public GraphAnalyzer() {
    }

    /**
     * The Constructor of the Class. For use with the old Impact Analysy only Version
     * {@link smartgrid.impactanalysis.ui}
     *
     * @deprecated It's now integrated in the SimControl Approach
     * @param outputPath
     *            In case of using {@code public void analyze(...)} Method it is the Path to the
     *            Outputfile
     */
    @Deprecated
    public GraphAnalyzer(final String outputPath) {

        // Attention cloned from init() to be downward compatible
        this.internalMaxID = 0;
        this.powerStates = new HashMap<>();
        this.entityStates = new HashMap<>();
        this.controlCenters = new LinkedList<>();
        this.internalToExternalID = new HashMap<>();
        this.externalToInternalID = new HashMap<>();
        this.internalToCluster = new HashMap<>();
        this.logicalNodes = new LinkedList<>();
        this.controlCenterConnectivity = new HashMap<>();

        this.outputPath = outputPath;
        this.initDone = true;
        // Do it always with logical Connection
        this.ignoreLogicalConnections = false;
    }

    @Override
    public void init(final ILaunchConfiguration config) throws CoreException {

        this.internalMaxID = 0;
        this.powerStates = new HashMap<>();
        this.entityStates = new HashMap<>();
        this.controlCenters = new LinkedList<>();
        this.internalToExternalID = new HashMap<>();
        this.externalToInternalID = new HashMap<>();
        this.internalToCluster = new HashMap<>();
        this.logicalNodes = new LinkedList<>();
        this.controlCenterConnectivity = new HashMap<>();

        final String ignoreLogicalConnectionsString = config.getAttribute(Constants.IGNORE_LOC_CON_KEY, Constants.FAIL);

        if (ignoreLogicalConnectionsString.equals(Constants.FAIL)) {
            // Checks whether DEFAULT_IGNORE_LOC_CON_KEY is true and assigns it
            this.ignoreLogicalConnections = Constants.TRUE.equals(Constants.DEFAULT_IGNORE_LOC_CON);

        } else {
            // checks whether ignoreLogicalConnectionsString is true and assigns it
            this.ignoreLogicalConnections = Constants.TRUE.equals(ignoreLogicalConnectionsString);
        }

        LOG.info("Ignoring logical connections: " + this.ignoreLogicalConnections);

        LOG.debug("Init done");
        this.initDone = true;
    }

    /**
     * for Test purposes
     *
     * @param ignoreLogicalConnections
     *            whether to ignore logical connections or not
     */
    public void initForTesting(final boolean ignoreLogicalConnections) {
        this.internalMaxID = 0;
        this.powerStates = new HashMap<>();
        this.entityStates = new HashMap<>();
        this.controlCenters = new LinkedList<>();
        this.internalToExternalID = new HashMap<>();
        this.externalToInternalID = new HashMap<>();
        this.internalToCluster = new HashMap<>();
        this.logicalNodes = new LinkedList<>();
        this.controlCenterConnectivity = new HashMap<>();
        this.ignoreLogicalConnections = ignoreLogicalConnections;
        LOG.info("Ignoring logical connections: " + ignoreLogicalConnections);

        LOG.debug("Init done");
        this.initDone = true;
    }

    @Override
    public ScenarioResult run(final SmartGridTopology smartGridTopo, final ScenarioState impactAnalysisInput) {

        assert this.initDone : "Init wasn't run! Run init() first !";

        LOG.debug("Start impact analysis");

        this.clearAll();

        if (!LoadInputModelConformityHelper.checkInputModelConformity(impactAnalysisInput, smartGridTopo)) {
            LOG.error("Input model is not conform to the current topo model");
        }

        // Szenario einlesen
        this.readStates(smartGridTopo, impactAnalysisInput);
        // now slowly build adjacent matrix
        this.adjacentMatrix = new double[this.internalMaxID][this.internalMaxID];
        this.logicalAdjacentMatrix = new double[this.internalMaxID][this.internalMaxID];
        for (int i = 0; i < this.internalMaxID; i++) {
            for (int j = 0; j < this.internalMaxID; j++) {
                this.adjacentMatrix[i][j] = 0;
                this.logicalAdjacentMatrix[i][j] = 0;
            }
        }
        // Es werden insgesamt 2 mal Cluster gebildet. Einmal �ber die
        // physikalischen Verbindungen, dann �ber die logischen
        this.readPhysicalConnections(smartGridTopo, impactAnalysisInput);

        // Run only if Not Ignore LogicalConnection ^= I want logical
        // Connections
        // if (!ignoreLogicalConnections) {
        this.readLogicalConnections(smartGridTopo, impactAnalysisInput);
        // }//TODO Remove if for rollback

        // Generates Result
        final ScenarioResult result = this.genOutputResult();
        result.setScenario(smartGridTopo);

        LOG.debug("Working Directory = " + System.getProperty("user.dir"));

        return result;
    }

    /**
     * This Method does the Impact Analysis.
     *
     * @param scenario
     *            the input scenario
     * @param state
     *            the input state
     */
    public void analyze(final SmartGridTopology scenario, final ScenarioState state) {

        // Generates Result
        final ScenarioResult result = this.run(scenario, state);

        if (result != null) {
            // Saves to File System
            FileSystemHelper.saveToFileSystem(result, this.outputPath);
        }
    }

    /**
     *
     * @return int
     *
     */
    private int getInternID() {
        final int result = this.internalMaxID;
        this.internalMaxID++;
        return result;
    }

    /**
     *
     * @param scenario
     * @param state
     */
    private void readStates(final SmartGridTopology scenario, final ScenarioState state) {
        LOG.debug("Start readStates");

        for (final EntityState s : state.getEntityStates()) {

            LOG.debug("Class " + s.getOwner().getClass() + " ID " + s.getOwner().getId() + " destroyed? "
                    + s.isIsDestroyed() + " powersource " + s.getOwner().getConnectedTo().toString());

            this.entityStates.put(s.getOwner().getId(), s);
            // if ((s.getOwner() instanceof ControlCenter) || (s.getOwner()
            // instanceof SmartMeter))
            // {
            this.logicalNodes.add(s.getOwner().getId());
            // }
            final int internalID = this.getInternID();
            this.externalToInternalID.put(s.getOwner().getId(), internalID);
            this.internalToExternalID.put(internalID, s.getOwner().getId());

            if (s.getOwner() instanceof ControlCenter) {
                this.controlCenters.add(s.getOwner().getId());

                LOG.debug("ControlCenter found: " + s.getOwner().getId());
            }
        }
        for (final PowerState p : state.getPowerStates()) {
            LOG.debug("Entity " + p.getOwner().getName() + " ID " + p.getOwner().getId() + " powerOutage? "
                    + p.isPowerOutage());
            this.powerStates.put(p.getOwner().getId(), p);
        }
        LOG.debug("End readStates");
    }

    private void readPhysicalConnections(final SmartGridTopology scenario, final ScenarioState state) {

        LOG.debug("Start readPhysicalConnections");

        final List<PhysicalConnection> pConns = scenario.getContainsPC();

        for (final PhysicalConnection p : pConns) {
            final NetworkEntity e1 = p.getLinks().get(0);
            final NetworkEntity e2 = p.getLinks().get(1);

            if (this.externalNodeIsWorking(e1.getId()) && this.externalNodeIsWorking(e2.getId())) {

                final int internal1 = this.externalToInternalID.get(e1.getId());
                final int internal2 = this.externalToInternalID.get(e2.getId());

                this.adjacentMatrix[internal1][internal2] = 1;
                this.adjacentMatrix[internal2][internal1] = 1;
            }
        }

        // Building physical Cluster
        LOG.debug(Matrix.toString(this.adjacentMatrix));
        LOG.debug("Validate clusteralgorithm");

        this.physicalClusters = Tarjan.getClusters(this.adjacentMatrix, this.internalToExternalID);

        LOG.debug("End readPhysicalConnections");
    }

    private void readLogicalConnections(final SmartGridTopology scenario, final ScenarioState state) {

        LOG.debug("Start readLogicalConnections");

        // set logical adjacent
        final List<LogicalCommunication> lConns = scenario.getContainsLC();
        for (final LogicalCommunication l : lConns) {
            final String id1 = l.getLinks().get(0).getId();
            final String id2 = l.getLinks().get(1).getId();
            final int internal1 = this.externalToInternalID.get(id1);
            final int internal2 = this.externalToInternalID.get(id2);

            if (this.areInSameCluster(internal1, internal2, this.physicalClusters)) {
                this.logicalAdjacentMatrix[internal1][internal2] = 1;
                this.logicalAdjacentMatrix[internal2][internal1] = 1;
            }
        }
        // filthy Variant ! Better Solution?
        if (!this.ignoreLogicalConnections) {
            // find logical paths that work
            this.logicalClusters = Tarjan.getClusters(this.logicalAdjacentMatrix, this.internalToExternalID);
        } else {
            this.logicalClusters = this.physicalClusters;
        }

        // remove nodes that are not logical
        final List<List<Integer>> newClusters = new LinkedList<>();

        // Not every time "logical" Clusters see above
        for (final List<Integer> cluster : this.logicalClusters) {
            final List<Integer> newCluster = new LinkedList<>();
            for (final Integer i : cluster) {
                if (this.logicalNodes.contains(this.internalToExternalID.get(i))) {
                    newCluster.add(i);
                }
            }
            if (newCluster.size() > 0) {
                newClusters.add(newCluster);
            }
        }

        this.logicalClusters = newClusters;

        for (final String controlID : this.controlCenters) {
            final int internalControlID = this.externalToInternalID.get(controlID);
            final Double[] connectionAvailable = new Double[this.internalMaxID + 1];
            for (int i = 0; i < connectionAvailable.length; i++) {
                connectionAvailable[i] = 0.0;
            }
            for (final List<Integer> l : this.logicalClusters) {
                if (l.contains(internalControlID)) {
                    for (final Integer n : l) {
                        connectionAvailable[n] = 1.0;
                    }
                }
            }
            this.controlCenterConnectivity.put(controlID, connectionAvailable);
        }
        LOG.debug("End readLogicalConnections");
    }

    private ScenarioResult genOutputResult() {
        SmartgridoutputPackageImpl.init();
        final SmartgridoutputFactory factory = SmartgridoutputFactory.eINSTANCE;
        final ScenarioResult result = factory.createScenarioResult();
        //

        // TODO Remove if/else for rollback
        if (this.ignoreLogicalConnections) {

            this.clusterCleaning(factory, result, this.physicalClusters);

        } else {

            this.clusterCleaning(factory, result, this.logicalClusters);
        }

        // Generate output for every node depending on connection status
        for (final String nodeID : this.logicalNodes) {
            LOG.debug("Generate output for node with id " + nodeID);
            smartgridoutput.EntityState state = null;

            final int internalNode = this.externalToInternalID.get(nodeID);

            final List<String> connectedCCs = new LinkedList<>();
            for (final String ccID : this.controlCenters) {

                if (this.controlCenterConnectivity.get(ccID)[internalNode] > 0) {
                    connectedCCs.add(ccID);
                }
            } // End Foreach ControlCenters

            if (this.externalNodeIsDestroyed(nodeID)) {
                state = factory.createDefect();
            } else if (!this.externalNodeHasPower(nodeID)) {
                state = factory.createNoPower();
            } else if (connectedCCs.size() == 0) {
                final NoUplink n = factory.createNoUplink();
                n.setBelongsToCluster(this.internalToCluster.get(internalNode));
                this.internalToCluster.get(internalNode).getHasEntities().add(n);
                // Passthrough IsHacked State from input into Output
                n.setIsHacked(this.externalNodeIsHacked(nodeID));
                state = n;
            } else {

                final Online s = factory.createOnline();
                s.setBelongsToCluster(this.internalToCluster.get(internalNode));
                this.internalToCluster.get(internalNode).getHasEntities().add(s);
                // s.setReachableControlCenters(connectedCCs.size()); - Thorsten

                // Passthrough IsHacked State from input into Output
                s.setIsHacked(this.externalNodeIsHacked(nodeID));
                state = s;
            }

            state.setOwner(this.entityStates.get(nodeID).getOwner());

            result.getStates().add(state);
        } // End Foreach LogicalNodes

        return result;

    }

    private void clusterCleaning(final SmartgridoutputFactory factory, final ScenarioResult result,
            final List<List<Integer>> clusterToClean) {

        for (final List<Integer> c : clusterToClean) {
            final Cluster cluster = factory.createCluster();
            // cluster.setSmartMeterCount(c.size());

            // long smCount = cluster.getHasEntities().stream().filter(s -> s.getOwner() instanceof
            // SmartMeter).count();
            int controlCentersInCluster = 0;
            int smCount = 0;
            for (final Integer i : c) {
                if (i != null) {
                    this.internalToCluster.put(i, cluster);
                    // check if its a controlCenter and increase
                    // controlCentersInCluster-Count
                    final String externalID = this.internalToExternalID.get(i);
                    if (this.externalNodeIsWorking(externalID)) {
                        if (this.controlCenters.contains(externalID)) {
                            controlCentersInCluster++;
                        } else {
                            if (this.entityStates.get(externalID).getOwner() instanceof SmartMeter) {
                                smCount++;
                            }

                        }
                    }
                }
            }
            cluster.setSmartMeterCount(smCount);
            cluster.setControlCenterCount(controlCentersInCluster);
            if (smCount > 0 || controlCentersInCluster > 0) {
                result.getClusters().add(cluster);
            }
        }
    }

    private boolean externalNodeIsWorking(final String id) {
        return this.externalNodeHasPower(id) && !this.externalNodeIsDestroyed(id);
    }

    private boolean externalNodeHasPower(final String id) {
        boolean connected = false;
        try {
            for (final PowerGridNode pgn : this.entityStates.get(id).getOwner().getConnectedTo()) {
                if (!this.powerStates.get(pgn.getId()).isPowerOutage()) {
                    connected = true;

                }
            }
        } catch (final NullPointerException e) {
            LOG.error(
                    "Your input model may be not conform to the current topo model but hasn't set its Scenario attribute to a valid value");
        }
        return connected;
    }

    private boolean externalNodeIsDestroyed(final String id) {
        return this.entityStates.get(id).isIsDestroyed();
    }

    private boolean externalNodeIsHacked(final String id) {
        return this.entityStates.get(id).isIsHacked();
    }

    private boolean areInSameCluster(final int n, final int m, final List<List<Integer>> clusterList) {
        boolean result = false;
        boolean notFound = true;
        for (final List<Integer> l : clusterList) {
            if (notFound && l.contains(n)) {
                if (l.contains(m)) {
                    result = true;
                }
                notFound = false;
            }
        }
        return result;
    }

    private void clearAll() {
        this.entityStates.clear();
        this.powerStates.clear();
        this.controlCenters.clear();
        this.internalToCluster.clear();
        this.externalToInternalID.clear();
        this.internalToExternalID.clear();
        this.controlCenterConnectivity.clear();
        this.logicalNodes.clear();
        this.internalMaxID = 0;
    }

    @Override
    public String getName() {
        return "Graph Analyzer Impact Analysis";
    }
}