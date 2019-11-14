package smartgrid.attackersimulation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.helper.ScenarioModelHelper;
import smartgrid.simcontrol.test.baselib.Constants;
import smartgrid.simcontrol.test.baselib.HackingStyle;
import smartgrid.simcontrol.test.baselib.coupling.IAttackerSimulation;
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

    /**
     * For ExtensionPoints .. use this together with the init() Method
     */
    public ViralHacker() {
        this.initDone = false;
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * Remark Root NodeIDs {@link smartgrid.simcontrol.baselib.Constants} have to be List of String!
     */
    @Override
    public void init(final ILaunchConfiguration config) throws CoreException {

        this.hackingSpeed = Integer
                .parseInt(config.getAttribute(Constants.HACKING_SPEED_KEY, Constants.DEFAULT_HACKING_SPEED));

        // Getting Hacking Style
        String hackingStyleString = config.getAttribute(Constants.HACKING_STYLE_KEY, Constants.FAIL);
        if (hackingStyleString.equals(Constants.FAIL)) {
            hackingStyleString = Constants.DEFAULT_HACKING_STYLE;
        }
        this.usedHackingStyle = HackingStyle.valueOf(hackingStyleString);

        LOG.info("Hacking speed is: " + this.hackingSpeed);
        LOG.info("Hacking style is: " + this.usedHackingStyle);
        LOG.debug("Init done");

        this.initDone = true;
    }

    /**
     * method to make init without a launch configuration for test purposes
     *
     * @param hackingStyle
     * @param hackingspeed
     */
    public void initForTest(final String hackingStyle, final String hackingspeed) {
        this.hackingSpeed = Integer.parseInt(hackingspeed);
        this.usedHackingStyle = HackingStyle.valueOf(hackingStyle);

        this.initDone = true;
    }

    @Override
    public ScenarioResult run(final SmartGridTopology topo, final ScenarioResult impactAnalysisOutput) {

        assert this.initDone : "Init wasn't run! Run init first!";

        LOG.debug("Start Hacking with Viral Hacker");
        if (this.usedHackingStyle == HackingStyle.STANDARD_HACKING) {
            this.standardHacking(topo, impactAnalysisOutput);
        } else if (this.usedHackingStyle == HackingStyle.FULLY_MESHED_HACKING) {
            this.fullMeshedHacking(impactAnalysisOutput);
        } else {
            throw new RuntimeException("Unknown hacking style: " + this.usedHackingStyle);
        }

        return impactAnalysisOutput;
    }

    /**
     * Standard hacking means that every node will search for the nearest node to it to hack
     *
     * @param topo
     *            the topology
     * @param impactAnalysisOutput
     *            the output of the impact analysis
     */
    private void standardHacking(final SmartGridTopology topo, final ScenarioResult impactAnalysisOutput) {

        final List<On> seedNodes = this.getHackedNodes(impactAnalysisOutput);
        if (seedNodes.isEmpty()) {
            LOG.debug("STANDARD_HACKING : There are no hacked nodes");
            return;
        }

        for (final On hackedNode : seedNodes) {
            int hackedNodesCount = 0;
            final List<On> visitedNodes = new LinkedList<On>();
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
                    // System.out.println("Node to start hacking is " + Node.getOwner().getId());
                    if (!visitedNodes.contains(Node)) {
                        visitedNodes.add(Node);
                    }

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
                    final LinkedList<On> neighborOnList = ScenarioModelHelper.getNeighborsFromCluster(clusterToHack,
                            neighborIDList);

                    // randomly arranging the neighbors of every node
                    Collections.shuffle(neighborOnList);
                    /*
                     * Now I have my alive (in my Cluster) Neighbor OnState List
                     */
                    for (final On neighbor : neighborOnList) {
                        if (!visitedNodes.contains(neighbor)) {
                            if (!neighbor.isIsHacked() && !(neighbor.getOwner() instanceof NetworkNode)) {
                                LOG.debug("Hacked with BFS node " + neighbor.getOwner().getId());
                                neighbor.setIsHacked(true);
                                visitedNodes.add(neighbor);
                                hackedNodesCount++;
                                // System.out.println("hacked " + neighbor.getOwner().getId());
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

                /*
                 * Q := Q' Q' := Clear
                 */
                currentLayer = nextLayer;
                nextLayer = new LinkedList<>();

            } // Layer Loop most outer

            LOG.debug("Done hacking with BFS");

        } // End For hacked seedNodes

    }

    /**
     * Each already hacked Node hacks hackingSpeed others and because of fully meshed --> every node
     * in Cluster can be a victim
     *
     * @param impactAnalysisOutput
     */
    private void fullMeshedHacking(final ScenarioResult impactAnalysisOutput) {
        // foreach Cluster in the ScenarioResult
        final List<On> seedNodes = this.getHackedNodes(impactAnalysisOutput);

        if (seedNodes.isEmpty()) {
            LOG.debug("STANDARD_HACKING : There are no hacked nodes");
            return;
        }

        for (final Cluster myCluster : impactAnalysisOutput.getClusters()) {
            int hackedNodesinCluster = 0;
            final List<On> notHackedNodes = new LinkedList<>();

            // Count hacked Node in that certain Cluster
            for (final On myNode : myCluster.getHasEntities()) {

                // Check if Node is already hacked --> So he is able to hack
                if (seedNodes.contains(myNode)) {
                    hackedNodesinCluster++;
                } else if (!(myNode.getOwner() instanceof NetworkNode)) {
                    notHackedNodes.add(myNode);
                }

            } // End Nodes in certain Cluster

            // Each Hacked Nodes in the Cluster hacks "hackingSpeed" other Nodes
            int howManyToHack = hackedNodesinCluster * this.hackingSpeed;
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

    /**
     * helper method to get the hacked nodes before every timeStep
     *
     * @param impactAnalysisOutput
     * @return list of hacked nodes in the model
     */
    private List<On> getHackedNodes(final ScenarioResult impactAnalysisOutput) {
        final List<On> hackedNodes = new LinkedList<>();
        for (final Cluster myCluster : impactAnalysisOutput.getClusters()) {
            for (final On myNode : myCluster.getHasEntities()) {
                // Check if Node is already hacked --> So he is able to hack
                if (myNode.isIsHacked()) {
                    hackedNodes.add(myNode);
                }
            }
        }
        return hackedNodes;
    }

    // private boolean seedNodeIdsValid() {
    //
    // boolean iDsValid;
    //
    // for (final String seedNodeId : seedNodeIDs) {
    //
    // for (final Cluster myCluster : myResult.getClusters()) {
    // for (final On myNode : myCluster.getHasEntities()) {
    // iDsValid = myNode.getOwner().getId() == seedNodeId;
    //
    // if (!iDsValid) {
    // return false;
    // }
    // }
    // }
    // }
    // return true;
    // }

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
