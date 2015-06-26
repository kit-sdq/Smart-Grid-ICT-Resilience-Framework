/**
 * 
 */
package smartgrid.attackersimulation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.helper.ScenarioHelper;
import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.HackingStyle;
import smartgrid.simcontrol.interfaces.ErrorCodeEnum;
import smartgrid.simcontrol.interfaces.IAttackerSimulation;
import smartgridoutput.Cluster;
import smartgridoutput.On;
import smartgridoutput.ScenarioResult;
import smartgridtopo.Scenario;

/**
 * This Class represents an viral operating Hacker.
 * 
 * Viral hacking means that every Node that has been hacked can hack other Nodes that have a logical
 * Connction to them.
 * 
 * @author Christian
 *
 */
public class ViralHacker implements IAttackerSimulation {

    // Private Fields
    private boolean firstRun = true;
    private boolean initDone = false;
    private int hackingSpeed;
    private HackingStyle usedHackingStyle;

    private List<Integer> seedNodeIDs;

    private NodeMode mode;

    // Temp Variables
    private Scenario myScenario;
    private ScenarioResult myResult;
    private List<On> seedNodes;

    // Public Getter Setter

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
        this.hackingSpeed = hackingSpeed - 1;

    }

    /**
     * For ExtensionPoints .. use this together with the init() Method
     */
    public ViralHacker() {

        // Init Lists
        this.seedNodeIDs = new LinkedList<Integer>();
        this.seedNodes = new LinkedList<On>();

    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Remark Root NodeIDs {@link smartgrid.simcontrol.baselib.Constants} have to be List of
     * String !
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     */
    @Override
    public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {

        String myModeString;
        ErrorCodeEnum myError = ErrorCodeEnum.NOT_SET;
        boolean defaultUsed = false;

        myModeString = config.getAttribute(Constants.NODE_MODE, Constants.FAIL);

        if (myModeString.equals(Constants.FAIL)) {
            myModeString = Constants.DEFAULT_NODE_MODE;
            defaultUsed = true;
        }

        // Set nodeMode for Viral Hacker
        this.mode = NodeMode.valueOf(myModeString);

        this.hackingSpeed = Integer.parseInt((String) config.getAttribute(Constants.HACKING_SPEED_KEY,
                Constants.DEFAULT_HACKING_SPEED));

        // Getting Hacking Style
        String hackingStyleString = config.getAttribute(Constants.HACKING_STYLE_KEY, Constants.FAIL);
        if (hackingStyleString.equals(Constants.FAIL)) {
            defaultUsed = true;
            hackingStyleString = Constants.DEFAULT_HACKING_STYLE;
        }

        this.usedHackingStyle = HackingStyle.valueOf(hackingStyleString);

        switch (this.mode) {
        case RandomNode:

            // Nothing else to do here ...

            break;

        case NodeIDs:

            final List<String> fail = new LinkedList<String>();
            fail.add(Constants.FAIL);

            // Getting NodeID
            List<String> nodeIDStrings = config.getAttribute(Constants.ROOT_NODE_ID_KEY, fail);

            for (String idStrings : nodeIDStrings) {
                if (idStrings.equals(Constants.FAIL)) {
                    defaultUsed = true;
                    this.mode = NodeMode.RandomNode;
                    break;
                }
                this.seedNodeIDs.add(Integer.parseInt(idStrings));

            }

            break;

        case Nodes:

            String nodePath = config.getAttribute(Constants.NODE_PATH, Constants.FAIL);

            if (nodePath.equals(Constants.FAIL)) {
                defaultUsed = true;
                this.mode = NodeMode.RandomNode;
                break;
            }

            // TODO Implement in ScenarioHelper !
            // this.seedNodes = ScenarioHelper.loadNodes(nodePath);

            break;

        default:
            myError = ErrorCodeEnum.FAIL;

            break;
        }

        if (defaultUsed) {
            myError = ErrorCodeEnum.DEFAULT_VALUES_USED;
        }

        initDone = true;

        return myError;
    }

    /**
     * 
     * @param hackingSpeed
     */
    public ViralHacker(int hackingSpeed) {

        this.hackingSpeed = hackingSpeed;
        mode = NodeMode.RandomNode;
        initDone = true;
    }

    /**
     * @param hackingSpeed
     * @param seedNodeID
     * 
     */
    public ViralHacker(int hackingSpeed, List<Integer> seedNodeIDs) {

        this(hackingSpeed);

        this.seedNodeIDs = seedNodeIDs;
        this.mode = NodeMode.NodeIDs;
        initDone = true;

    }

    /**
     * @param seedNode
     * @param hackingSpeed
     */
    public ViralHacker(List<On> seedNodes, int hackingSpeed) {

        this(hackingSpeed);

        this.seedNodes = seedNodes;
        this.mode = NodeMode.Nodes;

    }

    /*
     * (non-Javadoc)
     * 
     * @see smartgrid.simcontrol.interfaces.IAttackerSimulation#run(smartgridtopo .Scenario,
     * smartgridoutput.ScenarioResult)
     */
    @Override
    public ScenarioResult run(Scenario smartGridTopo, ScenarioResult impactAnalysisOutput) {

        assert (initDone) : "Init wasn't run! Run init() first !";

        this.myResult = impactAnalysisOutput;
        this.myScenario = smartGridTopo;

        doFirstRun();

        // Rebuild Nodes List for every run because Object References change
        // between runs
        if (!firstRun) {
            buildSeedNodesList();
        }

        /* *** At this point we have valid Node(ID)Lists **** */

        startHacking();

        return myResult;
    }

    /* Private Methods */

    /*
	 * 
	 */
    private void doFirstRun() {
        if (firstRun) {

            switch (this.mode) {

            case RandomNode:

                chooseRandomSeedNodes();// -> Does the buildSeedNodesList during
                                        // Method

                break;

            case NodeIDs: // --> Get Nodes

                buildSeedNodesList();

                break;

            case Nodes: // --> Get NodeIDs

                buildSeedNodeIDsList();

                break;

            }// End Swtich

            // Hack Seed Nodes
            assert (!this.seedNodes.isEmpty());

            for (On node : this.seedNodes) {
                node.setIsHacked(true);
            }

            // Done First Run operations
            firstRun = false;

        }// End FirstRun
    }

    /*
	 * 
	 */
    private void startHacking() {
        // Switch Hacking Modes here

        // Debug
        // usedHackingStyle = HackingStyle.BFS_HACKING;
        // usedHackingStyle = HackingStyle.DFS_HACKING;
        // usedHackingStyle = HackingStyle.FULLY_MESHED_HACKING;

        switch (this.usedHackingStyle) {
        case BFS_HACKING:
            bfsHacking();
            break;
        case DFS_HACKING:
            dfsHacking();
            break;
        case FULLY_MESHED_HACKING:
            fullMeshedHacking();
            break;
        default:
            break;

        }
    }

    /*
     * Does hacking in Deep First Search manner
     */
    private void dfsHacking() {

        List<On> freshHackedNodes = new LinkedList<On>(); // --> Attention in
                                                          // parallel crit.
                                                          // section needed?

        for (On hackedNode : this.seedNodes) {

            On Node = hackedNode;
            int hackCount = 0;
            Cluster clusterToHack = hackedNode.getBelongsToCluster();

            /*
             * Reads from Scenario so this List don't respects changes in States of the Entities -->
             * It's only the "hardwired" logical Connection neighbors
             */
            Map<Integer, LinkedList<Integer>> IDtoHisNeighborLinks = ScenarioHelper.genNeighborMapbyID(this.myScenario);

            dfs(clusterToHack, Node, IDtoHisNeighborLinks, hackCount, freshHackedNodes);

        }// End for hacked seed nodes

        this.seedNodes.addAll(freshHackedNodes); // Attention during if parallel

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
            int hackCount, List<On> freshHackedNodes) {

        // getting Neighbors
        int nodeID = ScenarioHelper.getIDfromEntityOnState(Node);

        // Getting Neighbor List with ID from Node
        // These Nodes could be in my Cluster but not sure
        LinkedList<Integer> neighborIDList = IDtoHisNeighborLinks.get(nodeID);

        if (neighborIDList == null) {
            return;
        }
        
        /*
         * Here it Filters the hardwired Logical Connections of the Neighbors out that because of
         * their State (e.g. destroyed) don't functions
         */
        LinkedList<On> neighborOnList = ScenarioHelper.getNeighborsFromCluster(clusterToHack, neighborIDList);

        /* Now I have my alive (in my Cluster) Neighbor OnState List */
        for (On neighbor : neighborOnList) {

            if (hackCount > this.hackingSpeed) {
                break; // Stopp hacking for this run
            }

            else if (neighbor.isIsHacked()) {
                dfs(clusterToHack, neighbor, IDtoHisNeighborLinks, hackCount + 1, freshHackedNodes);
            } else {
                neighbor.setIsHacked(true);
                // Add fresh hacked node
                freshHackedNodes.add(neighbor);

                hackCount++;
                dfs(clusterToHack, neighbor, IDtoHisNeighborLinks, hackCount, freshHackedNodes);
            }

        }

    }

    /*
     * Does hacking the bright first search way
     */
    private void bfsHacking() {

        List<On> freshHackedNodes = new LinkedList<On>();

        for (On hackedNode : this.seedNodes) {
            // DONE: Make a BFS for every Node
            // DONE: Add all new Hacked one to seedNodes

            int hackedNodesCount = 0;

            // For The BFS Algo
            List<On> currentLayer = new LinkedList<On>();
            List<On> nextLayer = new LinkedList<On>();
            Cluster clusterToHack = hackedNode.getBelongsToCluster();

            @SuppressWarnings("unused")
            int layerCount; // Not used atm but just in case

            /*
             * Reads from Scenario so this List don't respects changes in States of the Entities -->
             * It's only the "hardwired" logical Connection neighbors
             */
            Map<Integer, LinkedList<Integer>> IDtoHisNeighborLinks = ScenarioHelper.genNeighborMapbyID(this.myScenario);

            // Root is in cluster to hack --> so Root is StartNode
            currentLayer.add(hackedNode);

            // Starting BFS Algorithm
            hackingDone: for (layerCount = 0; !currentLayer.isEmpty(); layerCount++) { // Change
                                                                                       // To
                                                                                       // While?

                for (On Node : currentLayer) {

                    // getting Neighbors
                    int nodeID = ScenarioHelper.getIDfromEntityOnState(Node);

                    // Getting Neighbor List with ID from Node
                    // These Nodes could be in my Cluster but not sure
                    LinkedList<Integer> neighborIDList = IDtoHisNeighborLinks.get(nodeID);

                    if (neighborIDList == null) {
                        return;
                    }
                    /*
                     * Here it Filters the hardwired Logical Connections of the Neighbors out that
                     * because of their State (e.g. destroyed) don't functions
                     */
                    LinkedList<On> neighborOnList = ScenarioHelper.getNeighborsFromCluster(clusterToHack,
                            neighborIDList);

                    /* Now I have my alive (in my Cluster) Neighbor OnState List */
                    for (On neighbor : neighborOnList) {

                        if (!neighbor.isIsHacked()) {

                            neighbor.setIsHacked(true);

                            // Add new hacked one to seed Nodes
                            freshHackedNodes.add(neighbor);

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

                /*
                 * Q := Q' Q' := Clear
                 */
                currentLayer = nextLayer;
                nextLayer = new LinkedList<On>();

            }// Layer Loop most outer

            System.out.println("[Viral Hacker]: Done hacking with BFS");

        }// End For hacked seedNodes

        // Now add new hacked Nodes to seed List
        this.seedNodes.addAll(freshHackedNodes); // No Change of fresh list
                                                 // during this operation
                                                 // (keep in mind for
                                                 // parallel)

    }

    /*
     * Uses the seedNodeID List to search for On Entity State in Topo
     */
    private void buildSeedNodesList() {
        this.seedNodes = new LinkedList<On>();

        for (int id : this.seedNodeIDs) {
            On node = ScenarioHelper.findEntityOnStateFromID(id, myResult);
            this.seedNodes.add(node);
        }
    }

    /*
     * Uses the On Entity States List to search for their ID and generates a Int List of Node IDs
     */
    private void buildSeedNodeIDsList() {
        this.seedNodeIDs = new LinkedList<Integer>();

        for (On node : this.seedNodes) {

            int id = ScenarioHelper.getIDfromEntityOnState(node);

            this.seedNodeIDs.add(id);
        }
    }

    /*
     * Each already hacked Node hacks hackingSpeed others and because of fully meshed --> every node
     * in Cluster can be a victim
     */
    private void fullMeshedHacking() {
        // foreach Cluster in the ScenarioResult
        for (Cluster myCluster : this.myResult.getClusters()) {

            int hackedNodesinCluster = 0;
            List<On> notHackedNodes = new LinkedList<On>();

            // Count hacked Node in that certain Cluster
            for (On myNode : myCluster.getHasEntities()) {

                // Check if Node is already hacked --> So he is able to hack
                if (myNode.isIsHacked()) {

                    hackedNodesinCluster++;

                }
                // Build not Hacked List
                else {
                    notHackedNodes.add(myNode);

                }

            }// End Nodes in certain Cluster

            // Each Hacked Nodes in the Cluster hacks "hackingSpeed" other Nodes
            int howManyToHack = hackedNodesinCluster * hackingSpeed;

            // for (int i = 0; i < Math.max(howManyToHack, notHackedNodes.size()-1); i++) {
            //
            // notHackedNodes.get(i).setIsHacked(true);
            // }#

            for (On toHack : notHackedNodes) {
                if (howManyToHack > 0) {
                    toHack.setIsHacked(true);
                    howManyToHack--;
                } else {
                    break;
                }
            }

        }
    }

    /*
     * Expensive operation ! Assert only
     */
    @SuppressWarnings("unused")
    private boolean seedNodeIdsValid() {

        boolean IDsValid;

        // Attention MEthod has two Return Statements !!

        for (Integer seedNodeId : seedNodeIDs) {

            for (Cluster myCluster : this.myResult.getClusters()) {
                for (On myNode : myCluster.getHasEntities()) {
                    IDsValid = myNode.getOwner().getId() == seedNodeId;

                    if (!IDsValid) {

                        return false;
                    }

                }
            }

        }

        return true;
    }

    /*
     * Generates Random Seed Nodes and builds both ID and Node (On Entity State) Lists !
     */
    private void chooseRandomSeedNodes() {
        Cluster myCluster[] = new Cluster[this.hackingSpeed];
        int myClusterNumber;

        Random myRandom = new Random();

        int clusterCount = this.myResult.getClusters().size();

        for (int i = 0; i < myCluster.length; i++) {

            do {
                myClusterNumber = myRandom.nextInt(clusterCount); /*
                                                                   * [0 - clusterCount )
                                                                   * 
                                                                   * Exclusive upper bound
                                                                   */
                myCluster[i] = this.myResult.getClusters().get(myClusterNumber);

            } while (myCluster[i].getHasEntities().isEmpty()); /*
                                                                * Or threshold of Entities in
                                                                * Cluster ?
                                                                */

            // Get the Count of ON EntityStates in choosen Cluster
            int entityCount = myCluster[i].getHasEntities().size();

            // Choose one by Random and make it the hacking Root Node

            int myEntityNumber = myRandom.nextInt(entityCount); // [0 -
                                                                // entityCount)

            On Node = myCluster[i].getHasEntities().get(myEntityNumber);

            this.seedNodes.add(Node);

            this.seedNodeIDs.add(Node.getOwner().getId());

            /*
             * Do while until found Root that is really Online ?? (Random Root case)
             */

        } // End For
    }

    @Override
    public String getName() {
        return "Viral Hacker";
    }

}
