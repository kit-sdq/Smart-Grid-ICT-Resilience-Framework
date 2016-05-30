/**
 * 
 */
package smartgrid.attackersimulation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

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
 *
 */
public class LocalHacker implements IAttackerSimulation {

    private static final Logger LOG = Logger.getLogger(LocalHacker.class);

    // private Fields
    private HackingStyle usedHackingStyle;

    private boolean rootNodeValid = true;

    /*
     * From this Node the Hacker operates. So he can only attack Nodes that have a Logical
     * Connection to that Node => Same Cluster Only
     */
    private int rootNodeID; // IDs stay the same over the whole Analysis

    private On rootNode; // Reference Changes between runs!
    /* End Hacking Root Variables */

    private int hackingSpeed;

    // Flags
    private boolean firstRun = true;
    private boolean initDone = false;

    private SmartGridTopology mySmartGridTopo;
    private ScenarioResult myScenarioResult;

    /**
     * For ExtensionPoints .. use this together with the init() Method
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
    public LocalHacker(HackingStyle desiredHackingStyle, int hackingSpeed) {

        this.usedHackingStyle = desiredHackingStyle;

        // It's 0 Based --> 0 means 1 Node will be hacked. No negative Value
        // allowed !

        assert hackingSpeed >= 0 : "HackingSpeed is 0 Based --> No negative Value allowed !";
        setHackingSpeed(hackingSpeed - 1);

        this.rootNodeID = -1; // Means No Root provided
        this.initDone = true;
        this.rootNodeValid = true;
    }

    /**
     * 
     * Constructs an Local Hacker with the desired Hacking Style, hacking Speed and root Node (the
     * Node where the Hacker is connected to).
     * 
     * @param usedHackingStyle
     * @param rootNodeID
     *            Set the Node by {@link smartgridtopo.Identifier} from which the Hacker operates
     * @param hackingSpeed
     *            how many Nodes will be hacked in the same Time Step. Minimum Value is 1.
     */
    public LocalHacker(HackingStyle desiredHackingStyle, int rootNodeID, int hackingSpeed) {

        // Call other Constructor
        this(desiredHackingStyle, hackingSpeed);

        this.rootNodeID = rootNodeID;

        this.initDone = true;
        this.rootNodeValid = true;
    }

    @Override
    public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {

        // They are used in relevant branches perhaps compiler doesn't notice?

        HackingStyle desiredHackingStyle = null;
        int rootNodeID = -1;

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
                rootNodeID = Integer.parseInt(Constants.DEFAULT_ROOT_NODE_ID);
            }
            if (hackingSpeedString.equals(Constants.FAIL)) {
                hackingSpeed = Integer.parseInt(Constants.DEFAULT_HACKING_SPEED);
            }
        } else {
            desiredHackingStyle = HackingStyle.valueOf(desiredHackingStyleString);

            rootNodeID = Integer.parseInt(rootNodeIDString);

            hackingSpeed = Integer.parseInt(hackingSpeedString);

            errorCode = ErrorCodeEnum.SUCCESS;
        }

        // Adding extracted Parameters
        this.rootNodeID = rootNodeID;
        this.usedHackingStyle = desiredHackingStyle;

        LOG.info("Infection root node ID is: " + rootNodeID);
        LOG.info("Hacking style is: " + desiredHackingStyle);
        LOG.info("Hacking speed is: " + hackingSpeed);

        this.initDone = true;

        LOG.debug("Init done");

        return errorCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see smartgrid.simcontrol.interfaces.IAttackerSimulation#run(smartgridtopo .Scenario,
     * smartgridoutput.ScenarioResult)
     */
    @Override
    public ScenarioResult run(SmartGridTopology smartGridTopo, ScenarioResult impactAnalysisOutput) {

        assert (initDone) : "Init wasn't run! Run init() first !";

        // Copy Input in own Variables
        mySmartGridTopo = smartGridTopo;
        myScenarioResult = impactAnalysisOutput;

        if (firstRun) {

            // Uses ID from Constructor (calls findEntityOnStateFromID() in this
            // case) or if not chooses Random Node
            setHackingRootOnEntityState();
            firstRun = false;
        } else {
            // Find Root On Entity using ID I already know
            this.rootNode = ScenarioModelHelper.findEntityOnStateFromID(rootNodeID, myScenarioResult);
        }

        // Each TimeStep Case

        if (rootNode != null) {
            /* ** At this Point we have valid RootNodeID and rootNode !! ** */
            assert (this.rootNode.getOwner().getId() == this.rootNodeID) : "Root Node Not Valid !";
            // Hack Root just in case its not hacked
            // this.rootNode.setIsHacked(true);

            // Starting hacking according to the desired hacking Style
            hackNext(this.rootNode.getBelongsToCluster());
        }
        if (rootNodeValid) {
            myScenarioResult.setScenario(smartGridTopo);
        } else {
            myScenarioResult = null;
            rootNodeValid = true;
        }
        LOG.info("[Local Hacker]: Hacking done");
        return myScenarioResult;
    }

    // Private Methods

    private void hackNext(Cluster clusterToHack) {
        // Debug
        // usedHackingStyle = HackingStyle.BFS_HACKING;
        // usedHackingStyle = HackingStyle.DFS_HACKING;
        // usedHackingStyle = HackingStyle.FULLY_MESHED_HACKING;

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
     * 
     * } Q := Q' Q' := Clear
     * 
     * } Modified from Sanders Algorithm 1 Lecture BFS Algorithm @KIT. Also on Book
     * "Algorithms and Data Structures" ISBN: 978-3-540-77977-3 (Print) 978-3-540-77978-0 (Online)
     */
    private void bfsHacking(Cluster clusterToHack) {

        int hackedNodesCount = 0;

        // assert(this.rootNode.getBelongsToCluster() == clusterToHack);

        // --> Trivial cause it was called with root.belongsToCluster

        // For The BFS Algo
        List<On> currentLayer = new LinkedList<On>();
        List<On> nextLayer = new LinkedList<On>();

        @SuppressWarnings("unused")
        int layerCount; // Not used atm but just in case

        /*
         * Reads from Scenario so this List don't respects changes in States of the Entities -->
         * It's only the "hardwired" logical Connection neighbors
         */
        Map<Integer, LinkedList<Integer>> IDtoHisNeighborLinks = ScenarioModelHelper
                .genNeighborMapbyID(this.mySmartGridTopo);

        // Root is in cluster to hack --> so Root is StartNode
        currentLayer.add(this.rootNode);

        // Starting BFS Algorithm
        hackingDone: for (layerCount = 0; !currentLayer.isEmpty(); layerCount++) {
            for (On Node : currentLayer) {

                // getting Neighbors
                int nodeID = ScenarioModelHelper.getIDfromEntityOnState(Node);

                // Getting Neighbor List with ID from Node
                // These Nodes could be in my Cluster but not sure
                LinkedList<Integer> neighborIDList = IDtoHisNeighborLinks.get(nodeID);
                if (neighborIDList != null) {
                    /*
                     * Here it Filters the hardwired Logical Connections of the Neighbors out that
                     * because of their State (e.g. destroyed) don't functions
                     */
                    LinkedList<On> neighborOnList = ScenarioModelHelper.getNeighborsFromCluster(clusterToHack,
                            neighborIDList);

                    /*
                     * Now I have my alive (in my Cluster) Neighbor OnState List
                     */
                    for (On neighbor : neighborOnList) {

                        if (!(neighbor.getOwner() instanceof NetworkNode) && !neighbor.isIsHacked()) {
                            LOG.debug("Hacked with BFS node " + neighbor.getOwner().getId());
                            neighbor.setIsHacked(true);
                            hackedNodesCount++;

                            if (hackedNodesCount > this.hackingSpeed) {
                                break hackingDone;
                            }

                            nextLayer.add(neighbor);

                        }
                        // Found an hacked Node that can hack in the next Layer
                        else {
                            nextLayer.add(neighbor);

                        }
                    }
                } // Current Layer Loop
            }
            /*
             * Q := Q' Q' := Clear
             */
            currentLayer = nextLayer;
            nextLayer = new LinkedList<On>();
        } // Layer Loop most outer
        LOG.info("[Local Hacker]: Done hacking with BFS");
    }// End Method

    /*
     * 
     * Modified from Wikipedia http://en.wikipedia.org/wiki/Depth-first_search
     */
    private void dfsHacking(Cluster clusterToHack) {

        int hackCount = 0;

        On Node = this.rootNode;

        /*
         * Reads from Scenario so this List don't respects changes in States of the Entities -->
         * It's only the "hardwired" logical Connection neighbors
         */
        Map<Integer, LinkedList<Integer>> IDtoHisNeighborLinks = ScenarioModelHelper
                .genNeighborMapbyID(this.mySmartGridTopo);

        dfs(clusterToHack, Node, IDtoHisNeighborLinks, hackCount);

    }

    /*
     * Helper Method for the DFS Hacking
     * 
     * 
     * @param clusterToHack
     * 
     * @param Node
     * 
     * @param IDtoHisNeighborLinks
     */
    private void dfs(Cluster clusterToHack, On Node, Map<Integer, LinkedList<Integer>> IDtoHisNeighborLinks,
            int hackCount) {
        // getting Neighbors
        int nodeID = ScenarioModelHelper.getIDfromEntityOnState(Node);

        // Getting Neighbor List with ID from Node
        // These Nodes could be in my Cluster but not sure
        LinkedList<Integer> neighborIDList = IDtoHisNeighborLinks.get(nodeID);

        if (neighborIDList != null) {
            /*
             * Here it Filters the hardwired Logical Connections of the Neighbors out that because
             * of their State (e.g. destroyed) don't functions
             */
            LinkedList<On> neighborOnList = ScenarioModelHelper.getNeighborsFromCluster(clusterToHack, neighborIDList);

            /* Now I have my alive (in my Cluster) Neighbor OnState List */
            for (On neighbor : neighborOnList) {

                if (hackCount > this.hackingSpeed) {
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
        LOG.info("[Local Hacker] Done hacking with DFS");
    }

    /*
     * hack every Node in the the given Cluster without respecting logical Connections
     */
    private void fullMeshedHacking(Cluster clusterToHack) {

        Iterator<On> myIter = clusterToHack.getHasEntities().iterator();
        int hackedNodesCount = 0;

        do {

            On currentNode = myIter.next();

            if (!(currentNode.getOwner() instanceof NetworkNode) && !currentNode.isIsHacked()) {
                LOG.debug("Hacked with Full Meshed Hacking node " + currentNode.getOwner().getId());
                currentNode.setIsHacked(true);
                hackedNodesCount++;
            }

        } while (hackedNodesCount < this.hackingSpeed && myIter.hasNext());

    }

    private void chooseRootIDByRandom() {
        Cluster myCluster;
        Random myRandom = new Random();

        int clusterCount = this.myScenarioResult.getClusters().size();

        // Choose Random Cluster with Entries

        do {
            // [0 - clusterCount) Exclusive upper bound
            int myClusterNumber = myRandom.nextInt(clusterCount);
            myCluster = this.myScenarioResult.getClusters().get(myClusterNumber);

        } while (myCluster.getHasEntities().isEmpty()); // Or threshold of
                                                        // Entities in
                                                        // Cluster ?

        // Get the Count of ON EntityStates in choosen Cluster
        int entityCount = myCluster.getHasEntities().size();

        // Choose one by Random and make it the hacking Root Node

        int myEntityNumber = myRandom.nextInt(entityCount); // [0 -
                                                            // entityCount)

        this.rootNode = myCluster.getHasEntities().get(myEntityNumber);
        this.rootNodeID = rootNode.getOwner().getId();
        if (rootNode.getOwner() instanceof NetworkNode) {
            chooseRootIDByRandom();
        }
    }

    private void setHackingRootOnEntityState() {
        switch (this.rootNodeID) {

        // Choose Root - Root ID not set
        case -1:
            chooseRootIDByRandom();
            break;

        default:
            this.rootNode = ScenarioModelHelper.findEntityOnStateFromID(rootNodeID, myScenarioResult);
            if (rootNode == null) {
                invalidRootNodeIdDialog();
                rootNodeValid = false;
            }
            break;

        }// End Swtich

        if (rootNodeValid && !rootNode.isIsHacked()) {
            rootNode.setIsHacked(true);
        }
    }

    private void invalidRootNodeIdDialog() {
        LOG.warn("[Local Hacker]: Root node with ID " + rootNode.getOwner().getId() + " is invalid");
        // TODO: Find better solution than swing Dialog
        JOptionPane.showMessageDialog(null,
                "The root node ID you've entered is not valid. Remember not to use NetworkNodes as root node. Simulation will be aborted");
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
    public void setUsedHackingStyle(HackingStyle usedHackingStyle) {
        this.usedHackingStyle = usedHackingStyle;
    }

    /**
     * @return the rootNodeID
     */
    public int getRootNodeID() {
        return rootNodeID;
    }

    /**
     * @param rootNodeID
     *            the rootNodeID to set
     */
    public void setRootNodeID(int rootNodeID) {
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
    public void setHackingSpeed(int hackingSpeed) {
        this.hackingSpeed = hackingSpeed;
    }
}