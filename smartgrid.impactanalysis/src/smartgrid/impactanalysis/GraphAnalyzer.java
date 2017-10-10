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
import smartgrid.simcontrol.baselib.ErrorCodeEnum;
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

    private Map<Integer, PowerState> powerStates;
    private Map<Integer, EntityState> entityStates;

    private Map<Integer, Integer> externalToInternalID;
    private Map<Integer, Integer> internalToExternalID;
    private Map<Integer, Cluster> internalToCluster;
    private Map<Integer, Double[]> controlCenterConnectivity;

    private String outputPath;
    private boolean initDone = false;
    private boolean ignoreLogicalConnections;

    List<List<Integer>> physicalClusters;
    List<List<Integer>> logicalClusters;

    // Die Adjazenzmatrizen waren noch vom vorherigen Ansatz
    int maxID = -1;
    double[][] adjacentMatrix;
    double[][] logicalAdjacentMatrix;
    List<Integer> logicalNodes;
    List<Integer> controlCenters;
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
        internalMaxID = 0;
        powerStates = new HashMap<>();
        entityStates = new HashMap<>();
        controlCenters = new LinkedList<>();
        internalToExternalID = new HashMap<>();
        externalToInternalID = new HashMap<>();
        internalToCluster = new HashMap<>();
        logicalNodes = new LinkedList<>();
        controlCenterConnectivity = new HashMap<>();

        // TODO Overload Contructor to have 2 one for SimController and one for
        // ImpactAnalysis only?
        this.outputPath = outputPath;
        initDone = true;
        // Do it always with logical Connection
        ignoreLogicalConnections = false;
    }

    @Override
    public ErrorCodeEnum init(final ILaunchConfiguration config) throws CoreException {

        ErrorCodeEnum myError = ErrorCodeEnum.NOT_SET;

        internalMaxID = 0;
        powerStates = new HashMap<>();
        entityStates = new HashMap<>();
        controlCenters = new LinkedList<>();
        internalToExternalID = new HashMap<>();
        externalToInternalID = new HashMap<>();
        internalToCluster = new HashMap<>();
        logicalNodes = new LinkedList<>();
        controlCenterConnectivity = new HashMap<>();

        final String ignoreLogicalConnectionsString = config.getAttribute(Constants.IGNORE_LOC_CON_KEY, Constants.FAIL);

        if (ignoreLogicalConnectionsString.equals(Constants.FAIL)) {
            myError = ErrorCodeEnum.DEFAULT_VALUES_USED;

            // Checks whether DEFAULT_IGNORE_LOC_CON_KEY is true and assigns it
            ignoreLogicalConnections = Constants.TRUE.equals(Constants.DEFAULT_IGNORE_LOC_CON);

        } else {
            // checks whether ignoreLogicalConnectionsString is true and assigns
            // it
            ignoreLogicalConnections = Constants.TRUE.equals(ignoreLogicalConnectionsString);
        }

        LOG.info("Ignoring logical connections: " + ignoreLogicalConnections);

        LOG.debug("Init done");
        initDone = true;

        return myError;
    }

    @Override
    public ScenarioResult run(final SmartGridTopology smartGridTopo, final ScenarioState impactAnalysisInput) {

        assert initDone : "Init wasn't run! Run init() first !";

        LOG.debug("Start impact analysis");

        clearAll();

        if (!LoadInputModelConformityHelper.checkInputModelConformity(impactAnalysisInput, smartGridTopo)) {
            LOG.error("Input model is not conform to the current topo model");
        }

        // Szenario einlesen
        readStates(smartGridTopo, impactAnalysisInput);
        // now slowly build adjacent matrix
        adjacentMatrix = new double[internalMaxID][internalMaxID];
        logicalAdjacentMatrix = new double[internalMaxID][internalMaxID];
        for (int i = 0; i < internalMaxID; i++) {
            for (int j = 0; j < internalMaxID; j++) {
                adjacentMatrix[i][j] = 0;
                logicalAdjacentMatrix[i][j] = 0;
            }
        }
        // Es werden insgesamt 2 mal Cluster gebildet. Einmal �ber die
        // physikalischen Verbindungen, dann �ber die logischen
        readPhysicalConnections(smartGridTopo, impactAnalysisInput);

        // Run only if Not Ignore LogicalConnection ^= I want logical
        // Connections
        // if (!ignoreLogicalConnections) {
        readLogicalConnections(smartGridTopo, impactAnalysisInput);
        // }//TODO Remove if for rollback

        // Generates Result
        final ScenarioResult result = genOutputResult();
        result.setScenario(smartGridTopo);

        // Saves to File System
        // FileSystemHelper.saveToFileSystem(result, this.outputPath); //TODO For
        // Debug purposes only can/should? be removed for SimController later

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
        final ScenarioResult result = run(scenario, state);

        if (result != null) {
            // Saves to File System
            FileSystemHelper.saveToFileSystem(result, outputPath);
        }
    }

    private int getInternID() {
        final int result = internalMaxID;
        internalMaxID++;
        return result;
    }

    private void readStates(final SmartGridTopology scenario, final ScenarioState state) {
        LOG.debug("Start readStates");

        for (final EntityState s : state.getEntityStates()) {

            LOG.debug("Class " + s.getOwner().getClass() + " ID " + s.getOwner().getId() + " destroyed? " + s.isIsDestroyed() + " powersource " + s.getOwner().getConnectedTo().toString());

            entityStates.put(s.getOwner().getId(), s);
            // if ((s.getOwner() instanceof ControlCenter) || (s.getOwner()
            // instanceof SmartMeter))
            // {
            logicalNodes.add(s.getOwner().getId());
            // }
            final int internalID = getInternID();
            externalToInternalID.put(s.getOwner().getId(), internalID);
            internalToExternalID.put(internalID, s.getOwner().getId());

            if (s.getOwner().getId() > maxID) {
                maxID = s.getOwner().getId();
            }
            if (s.getOwner() instanceof ControlCenter) {
                controlCenters.add(s.getOwner().getId());

                LOG.debug("ControlCenter found: " + s.getOwner().getId());
            }
        }
        for (final PowerState p : state.getPowerStates()) {
            LOG.debug("Entity " + p.getOwner().getName() + " ID " + p.getOwner().getId() + " powerOutage? " + p.isPowerOutage());
            powerStates.put(p.getOwner().getId(), p);
        }
        LOG.debug("End readStates");
    }

    private void readPhysicalConnections(final SmartGridTopology scenario, final ScenarioState state) {

        LOG.debug("Start readPhysicalConnections");

        final List<PhysicalConnection> pConns = scenario.getContainsPC();

        for (final PhysicalConnection p : pConns) {
            final NetworkEntity e1 = p.getLinks().get(0);
            final NetworkEntity e2 = p.getLinks().get(1);
            if (externalNodeIsWorking(e1.getId()) && externalNodeIsWorking(e2.getId())) {
                final int internal1 = externalToInternalID.get(e1.getId());
                final int internal2 = externalToInternalID.get(e2.getId());

                adjacentMatrix[internal1][internal2] = 1;
                adjacentMatrix[internal2][internal1] = 1;
            }
        }

        // Building physical Cluster
        LOG.debug(Matrix.toString(adjacentMatrix));
        LOG.debug("Validate clusteralgorithm");
        physicalClusters = Tarjan.getClusters(adjacentMatrix, internalToExternalID);

        LOG.debug("End readPhysicalConnections");
    }

    private void readLogicalConnections(final SmartGridTopology scenario, final ScenarioState state) {

        LOG.debug("Start readLogicalConnections");

        // set logical adjacent
        final List<LogicalCommunication> lConns = scenario.getContainsLC();
        for (final LogicalCommunication l : lConns) {
            final int id1 = l.getLinks().get(0).getId();
            final int id2 = l.getLinks().get(1).getId();
            final int internal1 = externalToInternalID.get(id1);
            final int internal2 = externalToInternalID.get(id2);

            if (areInSameCluster(internal1, internal2, physicalClusters)) {
                logicalAdjacentMatrix[internal1][internal2] = 1;
                logicalAdjacentMatrix[internal2][internal1] = 1;
            }
        }
        // filthy Variant ! Better Solution?
        if (!ignoreLogicalConnections) {
            // find logical paths that work
            logicalClusters = Tarjan.getClusters(logicalAdjacentMatrix, internalToExternalID);
        } else {
            logicalClusters = physicalClusters;
        }

        // remove nodes that are not logical
        final List<List<Integer>> newClusters = new LinkedList<>();

        // Not every time "logical" Clusters see above
        for (final List<Integer> cluster : logicalClusters) {
            final List<Integer> newCluster = new LinkedList<>();
            for (final Integer i : cluster) {
                if (logicalNodes.contains(internalToExternalID.get(i))) {
                    newCluster.add(i);
                }
            }
            if (newCluster.size() > 0) {
                newClusters.add(newCluster);
            }
        }

        logicalClusters = newClusters;

        for (final Integer controlID : controlCenters) {
            final int internalControlID = externalToInternalID.get(controlID);
            final Double[] connectionAvailable = new Double[internalMaxID + 1];
            for (int i = 0; i < connectionAvailable.length; i++) {
                connectionAvailable[i] = 0.0;
            }
            for (final List<Integer> l : logicalClusters) {
                if (l.contains(internalControlID)) {
                    for (final Integer n : l) {
                        connectionAvailable[n] = 1.0;
                    }
                }
            }
            controlCenterConnectivity.put(controlID, connectionAvailable);
        }
        LOG.debug("End readLogicalConnections");
    }

    private ScenarioResult genOutputResult() {
        SmartgridoutputPackageImpl.init();
        final SmartgridoutputFactory factory = SmartgridoutputFactory.eINSTANCE;
        final ScenarioResult result = factory.createScenarioResult();
        //

        // TODO Remove if/else for rollback
        if (ignoreLogicalConnections) {

            clusterCleaning(factory, result, physicalClusters);

        } else {
            clusterCleaning(factory, result, logicalClusters);
        }

        // Generate output for every node depending on connection status
        for (final int nodeID : logicalNodes) {
            LOG.debug("Generate output for node with id " + nodeID);
            smartgridoutput.EntityState state = null;

            final int internalNode = externalToInternalID.get(nodeID);

            final List<Integer> connectedCCs = new LinkedList<>();
            for (final int ccID : controlCenters) {

                if (controlCenterConnectivity.get(ccID)[internalNode] > 0) {
                    connectedCCs.add(ccID);
                }
            } // End Foreach ControlCenters

            if (externalNodeIsDestroyed(nodeID)) {
                state = factory.createDefect();
            } else if (!externalNodeHasPower(nodeID)) {
                state = factory.createNoPower();
            } else if (connectedCCs.size() == 0) {
                final NoUplink n = factory.createNoUplink();
                n.setBelongsToCluster(internalToCluster.get(internalNode));
                internalToCluster.get(internalNode).getHasEntities().add(n);

                // Passthrough IsHacked State from input into Output
                n.setIsHacked(externalNodeIsHacked(nodeID));

                state = n;
            } else {

                final Online s = factory.createOnline();
                s.setBelongsToCluster(internalToCluster.get(internalNode));
                internalToCluster.get(internalNode).getHasEntities().add(s);
                // s.setReachableControlCenters(connectedCCs.size()); - Thorsten

                // Passthrough IsHacked State from input into Output
                s.setIsHacked(externalNodeIsHacked(nodeID));

                state = s;
            }

            state.setOwner(entityStates.get(nodeID).getOwner());

            result.getStates().add(state);
        } // End Foreach LogicalNodes

        return result;

    }

    private void clusterCleaning(final SmartgridoutputFactory factory, final ScenarioResult result, final List<List<Integer>> clusterToClean) {

        for (final List<Integer> c : clusterToClean) {
            final Cluster cluster = factory.createCluster();
            // cluster.setSmartMeterCount(c.size());
            int smCount = 0;
            int controlCentersInCluster = 0;
            for (final Integer i : c) {
                if (i != null) {
                    internalToCluster.put(i, cluster);
                    // check if its a controlCenter and increase
                    // controlCentersInCluster-Count
                    final int externalID = internalToExternalID.get(i);
                    if (externalNodeIsWorking(externalID)) {
                        if (controlCenters.contains(externalID)) {
                            controlCentersInCluster++;
                        } else {
                            smCount++;
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

    private boolean externalNodeIsWorking(final int id) {
        return externalNodeHasPower(id) && !externalNodeIsDestroyed(id);
    }

    private boolean externalNodeHasPower(final int id) {
        boolean connected = false;
        try {
            for (final PowerGridNode pgn : entityStates.get(id).getOwner().getConnectedTo()) {
                if (!powerStates.get(pgn.getId()).isPowerOutage()) {
                    connected = true;
                }
            }
        } catch (final NullPointerException e) {
            LOG.error("Your input model may be not conform to the current topo model but hasn't set its Scenario attribute to a valid value");
        }
        return connected;
    }

    private boolean externalNodeIsDestroyed(final int id) {
        return entityStates.get(id).isIsDestroyed();
    }

    private boolean externalNodeIsHacked(final int id) {
        return entityStates.get(id).isIsHacked();
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
        entityStates.clear();
        powerStates.clear();
        controlCenters.clear();
        internalToCluster.clear();
        externalToInternalID.clear();
        internalToExternalID.clear();
        controlCenterConnectivity.clear();
        logicalNodes.clear();
        internalMaxID = 0;
    }

    @Override
    public String getName() {
        return "Graph Analyzer Impact Analysis";
    }
}
