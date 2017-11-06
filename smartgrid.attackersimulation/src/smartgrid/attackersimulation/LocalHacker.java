package smartgrid.attackersimulation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.helper.ScenarioModelHelper;
import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.HackingStyle;
import smartgrid.simcontrol.baselib.coupling.IAttackerSimulation;
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
    /* End Hacking Root Variables */

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

    /**
     * Constructs an Local Hacker with the desired Hacking Style and hacking Speed.
     *
     * The Root Node will be set by Random
     *
     * @param desiredHackingStyle
     *            See {@link HackingStyle} description of the behavior
     * @param hackingSpeed
     *            how many Nodes will be hacked in the same Time Step. Minimum Value is 1.
     */
    public LocalHacker(final HackingStyle desiredHackingStyle, final int hackingSpeed) {

        usedHackingStyle = desiredHackingStyle;

        // It's 0 Based --> 0 means 1 Node will be hacked. No negative Value allowed !

        assert hackingSpeed >= 0 : "HackingSpeed is 0 Based --> No negative Value allowed !";
        setHackingSpeed(hackingSpeed - 1);

        rootNodeID = null; // Means No Root provided
    }

    /**
     * Constructs an Local Hacker with the desired Hacking Style, hacking Speed and root Node (the
     * Node where the Hacker is connected to).
     *
     * @param usedHackingStyle
     * @param rootNodeID
     *            Set the Node by {@link smartgridtopo.Identifier} from which the Hacker operates
     * @param hackingSpeed
     *            how many Nodes will be hacked in the same Time Step. Minimum Value is 1.
     */
    public LocalHacker(final HackingStyle desiredHackingStyle, final String rootNodeID, final int hackingSpeed) {

        // Call other Constructor
        this(desiredHackingStyle, hackingSpeed);

        this.rootNodeID = rootNodeID;
    }

    @Override
    public ErrorCodeEnum init(final ILaunchConfiguration config) throws CoreException {

        HackingStyle desiredHackingStyle = null;
        String rootNodeID = null;

        ErrorCodeEnum errorCode = ErrorCodeEnum.NOT_SET;

        String desiredHackingStyleString;
        String rootNodeIDString;
        String hackingSpeedString;

        // Extracting Parameters from Config
        desiredHackingStyleString = config.getAttribute(Constants.HACKING_STYLE_KEY, Constants.FAIL);

        rootNodeIDString = config.getAttribute(Constants.ROOT_NODE_ID_KEY, Constants.FAIL);

        hackingSpeedString = config.getAttribute(Constants.HACKING_SPEED_KEY, Constants.FAIL);

        if (desiredHackingStyleString.equals(Constants.FAIL) || rootNodeIDString.equals(Constants.FAIL)) {

            errorCode = ErrorCodeEnum.DEFAULT_VALUES_USED;

            if (desiredHackingStyleString.equals(Constants.FAIL)) {
                desiredHackingStyle = HackingStyle.valueOf(Constants.DEFAULT_HACKING_STYLE);
            }
            if (rootNodeIDString.equals(Constants.FAIL)) {
                rootNodeID = Constants.DEFAULT_ROOT_NODE_ID;
            }
            if (hackingSpeedString.equals(Constants.FAIL)) {
                hackingSpeed = Integer.parseInt(Constants.DEFAULT_HACKING_SPEED);
            }
        } else {
            desiredHackingStyle = HackingStyle.valueOf(desiredHackingStyleString);

            rootNodeID = rootNodeIDString;

            hackingSpeed = Integer.parseInt(hackingSpeedString);

            errorCode = ErrorCodeEnum.SUCCESS;
        }

        // Adding extracted Parameters
        this.rootNodeID = rootNodeID;
        usedHackingStyle = desiredHackingStyle;

        LOG.info("Infection root node ID is: " + rootNodeID);
        LOG.info("Hacking style is: " + desiredHackingStyle);
        LOG.info("Hacking speed is: " + hackingSpeed);

        initDone = true;

        LOG.debug("Init done");

        return errorCode;
    }

    /**
     * @see smartgrid.simcontrol.interfaces.IAttackerSimulation#run(smartgridtopo .Scenario,
     *      smartgridoutput.ScenarioResult)
     */
    @Override
    public ScenarioResult run(final SmartGridTopology smartGridTopo, final ScenarioResult impactAnalysisOutput) {

        assert initDone : "Init was not run! Run init() first!";

        // Copy Input in own Variables
        mySmartGridTopo = smartGridTopo;
        myScenarioResult = impactAnalysisOutput;

        if (firstRun) {
            setHackingRootOnEntityState();
            firstRun = false;
        } else {
            // Find Root On Entity using ID I already know
            rootNodeState = ScenarioModelHelper.findEntityOnStateFromID(rootNodeID, myScenarioResult);
        }

        if (rootNodeState != null) {
            // At this Point we have valid RootNodeID and rootNode
            assert rootNodeState.getOwner().getId() == rootNodeID : "Root Node Not Valid !";
            // Hack Root just in case its not hacked
            // this.rootNode.setIsHacked(true);

            // Starting hacking according to the desired hacking Style
            hackNext(rootNodeState.getBelongsToCluster());
        }

        myScenarioResult.setScenario(smartGridTopo);

        LOG.info("Hacking done");
        return myScenarioResult;
    }

    private void hackNext(final Cluster clusterToHack) {

        switch (usedHackingStyle) {
        case BFS_HACKING:
            bfsHacking(clusterToHack);
            break;
        case DFS_HACKING:
            dfsHacking(clusterToHack);
            break;
        case FULLY_MESHED_HACKING:
            fullMeshedHacking(clusterToHack);
            break;
        }
    }

    /*
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
     * } Modified from Sanders Algorithm 1 Lecture BFS Algorithm @KIT. Also on Book
     * "Algorithms and Data Structures" ISBN: 978-3-540-77977-3 (Print) 978-3-540-77978-0 (Online)
     */
    private void bfsHacking(final Cluster clusterToHack) {

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
        final Map<String, LinkedList<String>> IDtoHisNeighborLinks = ScenarioModelHelper.genNeighborMapbyID(mySmartGridTopo);

        // Root is in cluster to hack --> so Root is StartNode
        currentLayer.add(rootNodeState);

        // Starting BFS Algorithm
        hackingDone: for (layerCount = 0; !currentLayer.isEmpty(); layerCount++) {
            for (final On Node : currentLayer) {

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
                    final LinkedList<On> neighborOnList = ScenarioModelHelper.getNeighborsFromCluster(clusterToHack, neighborIDList);

                    // Now I have my alive (in my Cluster) Neighbor OnState List
                    for (final On neighbor : neighborOnList) {

                        if (!(neighbor.getOwner() instanceof NetworkNode) && !neighbor.isIsHacked()) {
                            LOG.debug("Hacked with BFS node " + neighbor.getOwner().getId());
                            neighbor.setIsHacked(true);
                            hackedNodesCount++;

                            if (hackedNodesCount > hackingSpeed) {
                                break hackingDone;
                            }

                            nextLayer.add(neighbor);

                        }
                        // Found an hacked Node that can hack in the next Layer
                        else {
                            nextLayer.add(neighbor);
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

    /*
     * Modified from Wikipedia http://en.wikipedia.org/wiki/Depth-first_search
     */
    private void dfsHacking(final Cluster clusterToHack) {

        final int hackCount = 0;

        final On Node = rootNodeState;

        /*
         * Reads from Scenario so this List don't respects changes in States of the Entities -->
         * It's only the "hardwired" logical Connection neighbors
         */
        final Map<String, LinkedList<String>> IDtoHisNeighborLinks = ScenarioModelHelper.genNeighborMapbyID(mySmartGridTopo);

        dfs(clusterToHack, Node, IDtoHisNeighborLinks, hackCount);

    }

    /**
     * Helper Method for the DFS Hacking
     *
     * @param clusterToHack
     *
     * @param Node
     *
     * @param IDtoHisNeighborLinks
     */
    private void dfs(final Cluster clusterToHack, final On Node, final Map<String, LinkedList<String>> IDtoHisNeighborLinks, int hackCount) {
        // getting Neighbors
        final String nodeID = ScenarioModelHelper.getIDfromEntityOnState(Node);

        // Getting Neighbor List with ID from Node
        // These Nodes could be in my Cluster but not sure
        final LinkedList<String> neighborIDList = IDtoHisNeighborLinks.get(nodeID);

        if (neighborIDList != null) {
            /*
             * Here it Filters the hardwired Logical Connections of the Neighbors out that because
             * of their State (e.g. destroyed) don't functions
             */
            final LinkedList<On> neighborOnList = ScenarioModelHelper.getNeighborsFromCluster(clusterToHack, neighborIDList);

            /* Now I have my alive (in my Cluster) Neighbor OnState List */
            for (final On neighbor : neighborOnList) {

                if (hackCount > hackingSpeed) {
                    break; // Stopp hacking for this run
                }

                else if (neighbor.isIsHacked()) {
                    dfs(clusterToHack, neighbor, IDtoHisNeighborLinks, ++hackCount);
                } else {
                    if (!(neighbor.getOwner() instanceof NetworkNode)) {
                        LOG.debug("Hacked with BFS node " + neighbor.getOwner().getId());
                        neighbor.setIsHacked(true);
                    }
                    hackCount++;
                    dfs(clusterToHack, neighbor, IDtoHisNeighborLinks, hackCount);
                }

            }
        }
        LOG.info("Done hacking with DFS");
    }

    /**
     * hack every Node in the the given Cluster without respecting logical Connections
     */
    private void fullMeshedHacking(final Cluster clusterToHack) {

        final Iterator<On> myIter = clusterToHack.getHasEntities().iterator();
        int hackedNodesCount = 0;

        do {

            final On currentNode = myIter.next();

            if (!(currentNode.getOwner() instanceof NetworkNode) && !currentNode.isIsHacked()) {
                LOG.debug("Hacked with Full Meshed Hacking node " + currentNode.getOwner().getId());
                currentNode.setIsHacked(true);
                hackedNodesCount++;
            }

        } while (hackedNodesCount < hackingSpeed && myIter.hasNext());

    }

    private void chooseRootIDByRandom() {
        Cluster myCluster;
        final Random myRandom = new Random();

        final int clusterCount = myScenarioResult.getClusters().size();

        // Choose Random Cluster with Entries
        do {
            // [0 - clusterCount) Exclusive upper bound
            final int myClusterNumber = myRandom.nextInt(clusterCount);
            myCluster = myScenarioResult.getClusters().get(myClusterNumber);

        } while (myCluster.getHasEntities().isEmpty()); // Or threshold of Entities in Cluster ?

        // Get the Count of ON EntityStates in choosen Cluster
        final int entityCount = myCluster.getHasEntities().size();

        // Choose one by Random and make it the hacking Root Node

        final int myEntityNumber = myRandom.nextInt(entityCount); // [0 - entityCount)

        rootNodeState = myCluster.getHasEntities().get(myEntityNumber);
        rootNodeID = rootNodeState.getOwner().getId();
        if (rootNodeState.getOwner() instanceof NetworkNode) { //To-do: this could be improved
            chooseRootIDByRandom();
        } else {
            LOG.info("Using random root with ID: " + rootNodeID);
        }
    }

    private void setHackingRootOnEntityState() {

        if (rootNodeID == null) {
            chooseRootIDByRandom();
            LOG.info("No root node set.");
        } else {
            rootNodeState = ScenarioModelHelper.findEntityOnStateFromID(rootNodeID, myScenarioResult);

            if (rootNodeState != null) {
                rootNodeID = rootNodeState.getOwner().getId();
            } else {
                LOG.warn("Could not find root node with ID " + rootNodeID);
                chooseRootIDByRandom();
            }
        }

        // set root node infected if not already
        if (!rootNodeState.isIsHacked()) {
            rootNodeState.setIsHacked(true);
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
        return usedHackingStyle;
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
        return rootNodeID;
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
        return hackingSpeed;
    }

    /**
     * @param hackingSpeed
     *            the hackingSpeed to set
     */
    public void setHackingSpeed(final int hackingSpeed) {
        this.hackingSpeed = hackingSpeed;
    }
}