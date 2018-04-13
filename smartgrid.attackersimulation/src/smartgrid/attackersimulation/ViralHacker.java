package smartgrid.attackersimulation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.helper.ScenarioModelHelper;
import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.HackingStyle;
import smartgrid.simcontrol.baselib.coupling.IAttackerSimulation;
import smartgridoutput.Cluster;
import smartgridoutput.On;
import smartgridoutput.ScenarioResult;
import smartgridtopo.NetworkNode;
import smartgridtopo.SmartGridTopology;

/**
 * This Class represents an viral operating Hacker.
 *
 * Viral hacking means that every Node that has been hacked can hack other Nodes that have a logical
 * Connection to them.
 */
public class ViralHacker implements IAttackerSimulation {

    private static final Logger LOG = Logger.getLogger(ViralHacker.class);

    // state variables
    private boolean initDone;

    // config variables
    private int hackingSpeed;
    private HackingStyle usedHackingStyle;

    private LinkedList<On> seedNodes;

    /**
     * For ExtensionPoints .. use this together with the init() Method
     */
    public ViralHacker() {
        initDone = false;
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * Remark Root NodeIDs {@link smartgrid.simcontrol.baselib.Constants} have to be List of String!
     */
    @Override
    public void init(final ILaunchConfiguration config) throws CoreException {

        hackingSpeed = Integer.parseInt(config.getAttribute(Constants.HACKING_SPEED_KEY, Constants.DEFAULT_HACKING_SPEED));

        // Getting Hacking Style
        String hackingStyleString = config.getAttribute(Constants.HACKING_STYLE_KEY, Constants.FAIL);
        if (hackingStyleString.equals(Constants.FAIL)) {
            hackingStyleString = Constants.DEFAULT_HACKING_STYLE;
        }
        usedHackingStyle = HackingStyle.valueOf(hackingStyleString);

        LOG.info("Hacking speed is: " + hackingSpeed);
        LOG.info("Hacking style is: " + usedHackingStyle);
        LOG.debug("Init done");

        initDone = true;
    }

    @Override
    public ScenarioResult run(final SmartGridTopology topo, final ScenarioResult impactAnalysisOutput) {

        assert initDone : "Init wasn't run! Run init first!";

        LOG.debug("Start Hacking with Viral Hacker");
        if (usedHackingStyle == HackingStyle.BFS_HACKING) {
            bfsHacking(topo);
        } else if (usedHackingStyle == HackingStyle.DFS_HACKING) {
            dfsHacking(topo);
        } else if (usedHackingStyle == HackingStyle.FULLY_MESHED_HACKING) {
            fullMeshedHacking(impactAnalysisOutput);
        } else {
            throw new RuntimeException("Unknown hacking style: " + usedHackingStyle);
        }

        return impactAnalysisOutput;
    }

    private void dfsHacking(SmartGridTopology topo) {

        final List<On> freshHackedNodes = new LinkedList<>();
        final int hackCount = 0;

        for (int i = 0; i < seedNodes.size(); i++) {
            final On node = seedNodes.get(i);
            final Cluster clusterToHack = node.getBelongsToCluster();

            /*
             * Reads from Scenario so this List don't respects changes in States of the Entities -->
             * It's only the "hardwired" logical Connection neighbors
             */
            final Map<String, LinkedList<String>> IDtoHisNeighborLinks = ScenarioModelHelper.genNeighborMapbyID(topo);

            dfs(clusterToHack, node, IDtoHisNeighborLinks, hackCount, freshHackedNodes);
        }
        LOG.debug("Done Hacking with DFS");
    }

    /*
     * Helper Method for the DFS Hacking
     *
     * @param clusterToHack
     *
     * @param Node
     *
     * @param IDtoHisNeighborLinks
     */
    private int dfs(final Cluster clusterToHack, final On node, final Map<String, LinkedList<String>> iDtoHisNeighborLinks, int hackCount, final List<On> freshHackedNodes) {

        // getting Neighbors
        final String nodeID = ScenarioModelHelper.getIDfromEntityOnState(node);

        // Getting Neighbor List with ID from Node
        // These Nodes could be in my Cluster but not sure
        final LinkedList<String> neighborIDList = iDtoHisNeighborLinks.get(nodeID);

        if (neighborIDList == null) {
            return hackingSpeed;
        }

        /*
         * Here it Filters the hardwired Logical Connections of the Neighbors out that because of
         * their State (e.g. destroyed) don't functions
         */
        final LinkedList<On> neighborOnList = ScenarioModelHelper.getNeighborsFromCluster(clusterToHack, neighborIDList);

        /* Now I have my alive (in my Cluster) Neighbor OnState List */
        for (final On neighbor : neighborOnList) {

            if (hackCount >= hackingSpeed) {
                break; // Stopp hacking for this run
            } else if (neighbor.isIsHacked()) {
                dfs(clusterToHack, neighbor, iDtoHisNeighborLinks, hackCount + 1, freshHackedNodes);
            } else if (neighbor.getOwner() instanceof NetworkNode) {
                return dfs(clusterToHack, neighbor, iDtoHisNeighborLinks, hackCount, freshHackedNodes);
            } else {
                LOG.debug("Hacked with DFS node " + neighbor.getOwner().getId());
                neighbor.setIsHacked(true);
                // Add fresh hacked node
                freshHackedNodes.add(neighbor);

                hackCount++;
                return dfs(clusterToHack, neighbor, iDtoHisNeighborLinks, hackCount, freshHackedNodes);
            }
        }
        return hackCount;
    }

    /*
     * Does hacking the bright first search way
     */
    private void bfsHacking(SmartGridTopology topo) {

        final List<On> freshHackedNodes = new LinkedList<>();

        for (final On hackedNode : seedNodes) {
            int hackedNodesCount = 0;

            // For The BFS Algo
            List<On> currentLayer = new LinkedList<>();
            List<On> nextLayer = new LinkedList<>();
            final Cluster clusterToHack = hackedNode.getBelongsToCluster();

            @SuppressWarnings("unused")
            int layerCount; // Not used atm but just in case

            /*
             * Reads from Scenario so this List don't respects changes in States of the Entities -->
             * It's only the "hardwired" logical Connection neighbors
             */
            final Map<String, LinkedList<String>> IDtoHisNeighborLinks = ScenarioModelHelper.genNeighborMapbyID(topo);

            // Root is in cluster to hack --> so Root is StartNode
            currentLayer.add(hackedNode);

            // Starting BFS Algorithm
            hackingDone: for (layerCount = 0; !currentLayer.isEmpty(); layerCount++) {
                for (final On Node : currentLayer) {

                    // getting Neighbors
                    final String nodeID = ScenarioModelHelper.getIDfromEntityOnState(Node);

                    // Getting Neighbor List with ID from Node
                    // These Nodes could be in my Cluster but not sure
                    final LinkedList<String> neighborIDList = IDtoHisNeighborLinks.get(nodeID);

                    if (neighborIDList == null) {
                        return;
                    }
                    /*
                     * Here it Filters the hardwired Logical Connections of the Neighbors out that
                     * because of their State (e.g. destroyed) don't functions
                     */
                    final LinkedList<On> neighborOnList = ScenarioModelHelper.getNeighborsFromCluster(clusterToHack, neighborIDList);

                    /*
                     * Now I have my alive (in my Cluster) Neighbor OnState List
                     */
                    for (final On neighbor : neighborOnList) {
                        if (!neighbor.isIsHacked() && !(neighbor.getOwner() instanceof NetworkNode)) {
                            LOG.debug("Hacked with BFS node " + neighbor.getOwner().getId());
                            neighbor.setIsHacked(true);

                            // Add new hacked one to seed Nodes
                            freshHackedNodes.add(neighbor);
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

                /*
                 * Q := Q' Q' := Clear
                 */
                currentLayer = nextLayer;
                nextLayer = new LinkedList<>();

            } // Layer Loop most outer

            LOG.debug("Done hacking with BFS");

        } // End For hacked seedNodes

        // Now add new hacked Nodes to seed List
        seedNodes.addAll(freshHackedNodes);
    }

    /**
     * Each already hacked Node hacks hackingSpeed others and because of fully meshed --> every node
     * in Cluster can be a victim
     * 
     * @param impactAnalysisOutput
     */
    private void fullMeshedHacking(ScenarioResult impactAnalysisOutput) {
        // foreach Cluster in the ScenarioResult
        for (final Cluster myCluster : impactAnalysisOutput.getClusters()) {

            int hackedNodesinCluster = 0;
            final List<On> notHackedNodes = new LinkedList<>();

            // Count hacked Node in that certain Cluster
            for (final On myNode : myCluster.getHasEntities()) {

                // Check if Node is already hacked --> So he is able to hack
                if (myNode.isIsHacked()) {
                    hackedNodesinCluster++;
                } else if (!(myNode.getOwner() instanceof NetworkNode)) {
                    notHackedNodes.add(myNode);
                }

            } // End Nodes in certain Cluster

            // Each Hacked Nodes in the Cluster hacks "hackingSpeed" other Nodes
            int howManyToHack = hackedNodesinCluster * hackingSpeed;
            final Random rand = new Random();

            while (howManyToHack > 0 && !notHackedNodes.isEmpty()) {
                final int nextID = rand.nextInt(notHackedNodes.size());
                notHackedNodes.get(nextID).setIsHacked(true);
                LOG.debug("Hacked with Full Meshed Hacking node " + notHackedNodes.get(nextID).getOwner().getId());
                notHackedNodes.remove(nextID);
                howManyToHack--;
            }
        }
        LOG.debug("Done hacking with Full Meshed Hacking");
    }

//    private boolean seedNodeIdsValid() {
//
//        boolean iDsValid;
//
//        for (final String seedNodeId : seedNodeIDs) {
//
//            for (final Cluster myCluster : myResult.getClusters()) {
//                for (final On myNode : myCluster.getHasEntities()) {
//                    iDsValid = myNode.getOwner().getId() == seedNodeId;
//
//                    if (!iDsValid) {
//                        return false;
//                    }
//                }
//            }
//        }
//        return true;
//    }

    @Override
    public String getName() {
        return "Viral Hacker";
    }

    @Override
    public boolean enableHackingSpeed() {
        return true;
    }

    @Override
    public boolean enableRootNode() {
        return false;
    }

    @Override
    public boolean enableLogicalConnections() {
        return true;
    }
}
