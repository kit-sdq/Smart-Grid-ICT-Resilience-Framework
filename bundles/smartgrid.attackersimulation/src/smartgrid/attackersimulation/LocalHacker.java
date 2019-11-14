package smartgrid.attackersimulation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.helper.ScenarioModelHelper;
import smartgrid.simcontrol.test.baselib.*;
import smartgrid.simcontrol.test.baselib.coupling.IAttackerSimulation;
import smartgridoutput.Cluster;
import smartgridoutput.On;
import smartgridoutput.ScenarioResult;
import smartgridtopo.NetworkNode;
import smartgridtopo.SmartGridTopology;

/**
 * This Class represents an local Hacker.
 *
 * A Local Hacker operates from one distinct Node and can only hack Node that have a logical
 * Connection to his "root" Node
 *
 * @author Christian
 */
public class LocalHacker implements IAttackerSimulation {

    private static final Logger LOG = Logger.getLogger(LocalHacker.class);

    // private Fields
    private HackingStyle usedHackingStyle;

    /*
     * From this Node the Hacker operates. So he can only attack Nodes that have a Logical
     * Connection to that Node => Same Cluster Only
     */
    private String rootNodeID; // IDs stay the same over the whole Analysis
    private On rootNodeState; // Reference Changes between runs!
    private Map<String, LinkedList<On>> neighborsInClusterMap;

    private int hackingSpeed;

    private boolean firstRun = true;
    private boolean initDone = false;

    private SmartGridTopology mySmartGridTopo;
    private ScenarioResult myScenarioResult;

    /**
     * Default constructor is needed by the OSGi framework to be able to use the extension point
     */
    public LocalHacker() {
    }

    @Override
    public void init(final ILaunchConfiguration config) throws CoreException {

        HackingStyle desiredHackingStyle = null;
        String rootNodeId = null;

        String desiredHackingStyleString;
        String rootNodeIDString;
        String hackingSpeedString;

        // Extracting Parameters from Config
        desiredHackingStyleString = config.getAttribute(Constants.HACKING_STYLE_KEY, Constants.FAIL);

        rootNodeIDString = config.getAttribute(Constants.ROOT_NODE_ID_KEY, Constants.FAIL);

        hackingSpeedString = config.getAttribute(Constants.HACKING_SPEED_KEY, Constants.FAIL);

        if (desiredHackingStyleString.equals(Constants.FAIL) || rootNodeIDString.equals(Constants.FAIL)) {

            if (desiredHackingStyleString.equals(Constants.FAIL)) {
                desiredHackingStyle = HackingStyle.valueOf(Constants.DEFAULT_HACKING_STYLE);
            }
            if (rootNodeIDString.equals(Constants.FAIL)) {
                rootNodeId = Constants.DEFAULT_ROOT_NODE_ID;
            }
            if (hackingSpeedString.equals(Constants.FAIL)) {
                this.hackingSpeed = Integer.parseInt(Constants.DEFAULT_HACKING_SPEED);
            }
        } else {
            desiredHackingStyle = HackingStyle.valueOf(desiredHackingStyleString);

            rootNodeId = rootNodeIDString;

            this.hackingSpeed = Integer.parseInt(hackingSpeedString);
        }

        // Adding extracted Parameters
        this.rootNodeID = rootNodeId;
        this.usedHackingStyle = desiredHackingStyle;
        LOG.info("Infection root node ID is: " + rootNodeId);
        LOG.info("Hacking style is: " + desiredHackingStyle);
        LOG.info("Hacking speed is: " + this.hackingSpeed);

        this.initDone = true;

        LOG.debug("Init done");
    }

    /**
     * Method to make init without using a launch configuration for test purposes
     *
     * @param hackingStyle
     * @param hackingspeed
     * @param rootNode
     */
    public void initForTest(final String hackingStyle, final String hackingspeed, final String rootNode) {
        this.hackingSpeed = Integer.parseInt(hackingspeed);
        this.usedHackingStyle = HackingStyle.valueOf(hackingStyle);
        this.rootNodeID = rootNode;
        this.initDone = true;
    }

    /**
     * @see smartgrid.simcontrol.interfaces.IAttackerSimulation#run(smartgridtopo .Scenario,
     *      smartgridoutput.ScenarioResult)
     */
    @Override
    public ScenarioResult run(final SmartGridTopology smartGridTopo, final ScenarioResult impactAnalysisOutput) {

        assert this.initDone : "Init was not run! Run init() first!";

        // Copy Input in own Variables
        this.mySmartGridTopo = smartGridTopo;
        this.myScenarioResult = impactAnalysisOutput;

        if (this.firstRun) {
            this.setHackingRootOnEntityState();
            this.neighborsMapInit();
            this.firstRun = false;
        } else {
            // Find Root On Entity using ID I already know
            this.rootNodeState = ScenarioModelHelper.findEntityOnStateFromID(this.rootNodeID, this.myScenarioResult);
        }

        if (this.rootNodeState != null) {
            // At this Point we have valid RootNodeID and rootNode
            assert this.rootNodeState.getOwner().getId() == this.rootNodeID : "Root Node Not Valid !";
            // Hack Root just in case its not hacked
            // this.rootNode.setIsHacked(true);

            // Starting hacking according to the desired hacking Style
            this.hackNext(this.rootNodeState.getBelongsToCluster());
        }

        this.myScenarioResult.setScenario(smartGridTopo);

        LOG.debug("Hacking done");
        return this.myScenarioResult;
    }

    /**
     * arranges the neighbours of every node randomly and put them in a list mapped to the node
     */
    private void neighborsMapInit() {
        this.neighborsInClusterMap = new HashMap<String, LinkedList<On>>();

        final Map<String, LinkedList<String>> IDtoHisNeighborLinks = ScenarioModelHelper
                .genNeighborMapbyID(this.mySmartGridTopo);
        for (final String iD : IDtoHisNeighborLinks.keySet()) {

            final LinkedList<On> neighborOnList = new LinkedList<>();
            for (final On clusterNode : this.rootNodeState.getBelongsToCluster().getHasEntities()) {
                // Are my Neighbors at my Cluster ? Otherwise they are gone
                // (Destroyed or something)
                if (IDtoHisNeighborLinks.get(iD).contains(ScenarioModelHelper.getIDfromEntityOnState(clusterNode))) {
                    neighborOnList.add(clusterNode);
                }
            }
            Collections.shuffle(neighborOnList);
            this.neighborsInClusterMap.put(iD, neighborOnList);
        }
    }

    /**
     * begin hacking ; choose hacking style
     *
     * @param clusterToHack
     */
    private void hackNext(final Cluster clusterToHack) {

        switch (this.usedHackingStyle) {
        case BFS_HACKING:
            this.bfsHacking(clusterToHack);
            break;
        case DFS_HACKING:
            this.dfsHacking(clusterToHack);
            break;
        case FULLY_MESHED_HACKING:
            this.fullMeshedHacking(clusterToHack);
            break;
        default:
            throw new RuntimeException("Unknown hacking style: " + this.usedHackingStyle);

        }

    }

    /**
     * Algo
     *
     * Q = <rootNode>, Q' = < > : Set of Nodes // current, next layer
     *
     * hackedNodesCount=0 : int
     *
     * for(layerCount := 0, Q != Empty; layer++){
     *
     * foreach( node in Q ) {
     *
     * foreach ( neighbor from node) { //Logical Neighbor !
     *
     * if(!neighbor.ishacked) { neighbor --> Hack; hackedNodesCount++;
     *
     * if(hackedNodeCount > HackingSpeed) --> BREAK from All Loops!
     *
     * Add neighbor in Q'; } else { Add neighbor in Q'; }
     *
     *
     * } Q := Q' Q' := Clear
     *
     * } Modified from Sanders Algorithm 1 Lecture BFS Algorithm @KIT. Also on Book "Algorithms and
     * Data Structures" ISBN: 978-3-540-77977-3 (Print) 978-3-540-77978-0 (Online)
     *
     * @param clusterToHack
     */
    private void bfsHacking(final Cluster clusterToHack) {

        // visited nodes in this Time step
        final List<On> visitedNodes = new LinkedList<On>();
        int hackedNodesCount = 0;

        // assert(this.rootNode.getBelongsToCluster() == clusterToHack);

        // --> Trivial cause it was called with root.belongsToCluster

        // For The BFS Algo
        List<On> currentLayer = new LinkedList<>();
        List<On> nextLayer = new LinkedList<>();

        @SuppressWarnings("unused")
        int layerCount; // Not used atm but just in case

        /*
         * Reads from Scenario so this List don't respects changes in States of the Entities -->
         * It's only the "hardwired" logical Connection neighbors
         */
        final Map<String, LinkedList<String>> IDtoHisNeighborLinks = ScenarioModelHelper
                .genNeighborMapbyID(this.mySmartGridTopo);

        // Root is in cluster to hack --> so Root is StartNode
        currentLayer.add(this.rootNodeState);

        // Starting BFS Algorithm
        hackingDone: for (layerCount = 0; !currentLayer.isEmpty(); layerCount++) {

            for (final On Node : currentLayer) {
                if (!visitedNodes.contains(Node)) {
                    visitedNodes.add(Node);
                }

                // getting Neighbors
                final String nodeID = ScenarioModelHelper.getIDfromEntityOnState(Node);

                // Getting Neighbor List with ID from Node
                // These Nodes could be in my Cluster but not sure
                final LinkedList<String> neighborIDList = IDtoHisNeighborLinks.get(nodeID);
                if (neighborIDList != null) {
                    /*
                     * Here it Filters the hardwired Logical Connections of the Neighbors out that
                     * because of their State (e.g. destroyed) don't functions
                     */
                    final LinkedList<On> neighborOnList = this.neighborsInClusterMap.get(nodeID);

                    // Now I have my alive (in my Cluster) Neighbor OnState List
                    for (final On neighbor : neighborOnList) {
                        // check if this node is not visited in this time step
                        if (!visitedNodes.contains(neighbor)) {
                            // TODO why check not being a networknode ?!
                            if (!(neighbor.getOwner() instanceof NetworkNode) && !neighbor.isIsHacked()) {
                                LOG.debug("Hacked with BFS node " + neighbor.getOwner().getId());
                                neighbor.setIsHacked(true);
                                visitedNodes.add(neighbor);
                                hackedNodesCount++;

                                if (hackedNodesCount >= this.hackingSpeed) {
                                    break hackingDone;
                                }

                                nextLayer.add(neighbor);

                            } else {

                                // Found an hacked Node that can hack in the next Layer
                                visitedNodes.add(neighbor);
                                nextLayer.add(neighbor);

                            }
                        }
                    }
                }
            }
            // Q := Q' Q' := Clear
            currentLayer = nextLayer;
            nextLayer = new LinkedList<>();
        }
        LOG.info("Done hacking with BFS");
    }

    /**
     * Modified from Wikipedia http://en.wikipedia.org/wiki/Depth-first_search
     *
     * @param clusterToHack
     */
    private void dfsHacking(final Cluster clusterToHack) {

        final int hackCount = 0;
        final List<On> visitedNodes = new LinkedList<On>();

        final On Node = this.rootNodeState;
        visitedNodes.add(Node);
        /*
         * Reads from Scenario so this List don't respects changes in States of the Entities -->
         * It's only the "hardwired" logical Connection neighbors
         */
        final Map<String, LinkedList<String>> IDtoHisNeighborLinks = ScenarioModelHelper
                .genNeighborMapbyID(this.mySmartGridTopo);

        this.dfs(clusterToHack, Node, IDtoHisNeighborLinks, hackCount, visitedNodes);

    }

    /**
     * Helper Method for the DFS Hacking
     *
     * @param clusterToHack
     * @param node
     * @param iDtoHisNeighborLinks
     * @param visitedNodes
     * @param hackCount
     * @return number of hackedNodes
     */
    private int dfs(final Cluster clusterToHack, final On node,
            final Map<String, LinkedList<String>> iDtoHisNeighborLinks, int hackCount, final List<On> visitedNodes) {

        // getting Neighbors
        final String nodeID = ScenarioModelHelper.getIDfromEntityOnState(node);

        // Getting Neighbor List with ID from Node
        // These Nodes could be in my Cluster but not sure
        final LinkedList<String> neighborIDList = iDtoHisNeighborLinks.get(nodeID);

        if (neighborIDList != null) {
            /*
             * Here it Filters the hardwired Logical Connections of the Neighbors out that because
             * of their State (e.g. destroyed) don't functions
             */
            final LinkedList<On> neighborOnList = this.neighborsInClusterMap.get(nodeID);

            /* Now I have my alive (in my Cluster) Neighbor OnState List */
            for (final On neighbor : neighborOnList) {
                // check if this node is visited in this time step
                if (!visitedNodes.contains(neighbor)) {

                    if (hackCount >= this.hackingSpeed) {
                        break; // Stopp hacking for this run

                    } else if (neighbor.isIsHacked() || (neighbor.getOwner() instanceof NetworkNode)) {
                        visitedNodes.add(neighbor);
                        hackCount = this.dfs(clusterToHack, neighbor, iDtoHisNeighborLinks, hackCount, visitedNodes);

                    } else {
                        LOG.debug("Hacked with BFS node " + neighbor.getOwner().getId());
                        neighbor.setIsHacked(true);
                        visitedNodes.add(neighbor);
                        hackCount++;
                        hackCount = this.dfs(clusterToHack, neighbor, iDtoHisNeighborLinks, hackCount, visitedNodes);
                    }
                }
            }
        }
        LOG.info("Done hacking with DFS");
        return hackCount;
    }

    /**
     * hack every Node in the the given Cluster without respecting logical Connections
     *
     * @param clusterToHack clusterToHack
     */
    private void fullMeshedHacking(final Cluster clusterToHack) {

        // getting the nodes in the cluster to hack and making thier order random
        final LinkedList<On> entitiesInCluster = new LinkedList<On>();
        entitiesInCluster.addAll(clusterToHack.getHasEntities());
        Collections.shuffle(entitiesInCluster);

        final Iterator<On> myIter = entitiesInCluster.iterator();
        int hackedNodesCount = 0;

        do {

            final On currentNode = myIter.next();

            if (!(currentNode.getOwner() instanceof NetworkNode) && !currentNode.isIsHacked()) {
                LOG.debug("Hacked with Full Meshed Hacking node " + currentNode.getOwner().getId());
                currentNode.setIsHacked(true);
                hackedNodesCount++;
            }

        } while (hackedNodesCount < this.hackingSpeed && myIter.hasNext());

    }

    /**
     * choosing randomly a root when none is identified
     */
    private void chooseRootIDByRandom() {
        Cluster myCluster;
        final Random myRandom = new Random();

        final int clusterCount = this.myScenarioResult.getClusters().size();

        // Choose Random Cluster with Entries
        do {
            // [0 - clusterCount) Exclusive upper bound
            final int myClusterNumber = myRandom.nextInt(clusterCount);
            myCluster = this.myScenarioResult.getClusters().get(myClusterNumber);

        } while (myCluster.getHasEntities().isEmpty()); // Or threshold of Entities in Cluster ?

        // Get the Count of ON EntityStates in choosen Cluster
        final int entityCount = myCluster.getHasEntities().size();

        // Choose one by Random and make it the hacking Root Node

        final int myEntityNumber = myRandom.nextInt(entityCount); // [0 - entityCount)

        this.rootNodeState = myCluster.getHasEntities().get(myEntityNumber);
        this.rootNodeID = this.rootNodeState.getOwner().getId();
        if (this.rootNodeState.getOwner() instanceof NetworkNode) { // To-do: this could be improved
            this.chooseRootIDByRandom();
        } else {
            LOG.info("Using random root with ID: " + this.rootNodeID);
        }
    }

    /**
     * setting the Root node
     */
    private void setHackingRootOnEntityState() {

        if (this.rootNodeID == null || this.rootNodeID.equalsIgnoreCase(Constants.NO_ROOT_NODE_ID)) {
            LOG.info("No root node specified.");
            this.chooseRootIDByRandom();
        } else {
            // search root node state (if meaningful ID is specified)
            this.rootNodeState = ScenarioModelHelper.findEntityOnStateFromID(this.rootNodeID, this.myScenarioResult);

            if (this.rootNodeState != null) {
                this.rootNodeID = this.rootNodeState.getOwner().getId();
            } else {
                LOG.warn("Could not find root node with ID " + this.rootNodeID);
                throw new RuntimeException("Falsch root node ID");
            }
        }

        // set root node infected if not already
        if (!this.rootNodeState.isIsHacked()) {
            this.rootNodeState.setIsHacked(true);
        }
    }

    @Override
    public String getName() {
        return "Local Hacker";
    }

    @Override
    public boolean enableHackingSpeed() {
        return true;
    }

    @Override
    public boolean enableRootNode() {
        return true;
    }

    @Override
    public boolean enableLogicalConnections() {
        return true;
    }

    /**
     * @return the usedHackingStyle
     */
    public HackingStyle getUsedHackingStyle() {
        return this.usedHackingStyle;
    }

    /**
     * @param usedHackingStyle
     *            the usedHackingStyle to set
     */
    public void setUsedHackingStyle(final HackingStyle usedHackingStyle) {
        this.usedHackingStyle = usedHackingStyle;
    }

    /**
     * @return the rootNodeID
     */
    public String getRootNodeID() {
        return this.rootNodeID;
    }

    /**
     * @param rootNodeID
     *            the rootNodeID to set
     */
    public void setRootNodeID(final String rootNodeID) {
        this.rootNodeID = rootNodeID;
    }

    /**
     * @return the hackingSpeed
     */
    public int getHackingSpeed() {
        return this.hackingSpeed;
    }

    /**
     * @param hackingSpeed
     *            the hackingSpeed to set
     */
    public void setHackingSpeed(final int hackingSpeed) {
        this.hackingSpeed = hackingSpeed;
    }
}