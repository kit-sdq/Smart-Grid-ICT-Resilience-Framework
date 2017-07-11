/**
 *
 */
package smartgrid.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import smartgridinput.ScenarioState;
import smartgridinput.impl.SmartgridinputPackageImpl;
import smartgridoutput.Cluster;
import smartgridoutput.EntityState;
import smartgridoutput.On;
import smartgridoutput.ScenarioResult;
import smartgridtopo.LogicalCommunication;
import smartgridtopo.NetworkNode;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.impl.SmartgridtopoPackageImpl;

/**
 * @author Christian
 *
 */
public final class ScenarioModelHelper {

    private static Logger LOG = Logger.getLogger(ScenarioModelHelper.class);

    public static SmartGridTopology loadScenario(final String path) {
        SmartGridTopology s = null;
        SmartgridtopoPackageImpl.init();

        final ResourceSet resSet = new ResourceSetImpl();
        final Resource resource = resSet.getResource(URI.createFileURI(path), true);

        final EObject r = resource.getContents().get(0);
        LOG.debug("Class: " + r.getClass());
        s = (SmartGridTopology) resource.getContents().get(0);
        return s;
    }

    public static ScenarioState loadInput(final String path) {
        ScenarioState input = null;
        SmartgridinputPackageImpl.init();

        // Code Removed - Christian

        final ResourceSet resSet = new ResourceSetImpl();
        final Resource resource = resSet.getResource(URI.createFileURI(path), true);

        final EObject r = resource.getContents().get(0);
        LOG.debug("Class: " + r.getClass());
        input = (ScenarioState) resource.getContents().get(0);

        return input;
    }

    /**
     * Generates a Map the Keys are one certain NodeId and the LinkedList <Integer> Element are all
     * Nodes to which Key has a logical Connection in the Scenario (Topology) Model @see
     * {@link smartgridtopo.Scenario}
     *
     * Attention these are the hardwired Connections ! the current EntityState of the Network Entity
     * is respected !!
     *
     *
     * @param mySmartGridTopo
     *            The Topology of the Model from the Analysis
     * @return a Map with NodeID as Key and LinkedList<"NodeID"> of Keys logical Neighbor as Value
     */
    public static Map<Integer, LinkedList<Integer>> genNeighborMapbyID(final SmartGridTopology mySmartGridTopo) {
        /*
         * Maps one Node (by ID) to his List of neighbor Nodes (also by ID)
         */
        final Map<Integer, LinkedList<Integer>> IDLinks = new HashMap<Integer, LinkedList<Integer>>();

        for (final LogicalCommunication myLCom : mySmartGridTopo.getContainsLC()) {

            // Links 2! NetworkEntities together
            // Node u <--Link--> Node v
            final int key = myLCom.getLinks().get(0).getId();

            final int value = myLCom.getLinks().get(1).getId();

            // Add Node u --Link--> Node v
            addNeighbors(IDLinks, key, value);

            // Get Neighbors from other side of the Link
            // Add Node u <--Link-- Node v
            addNeighbors(IDLinks, value, key);

        }

        return IDLinks;
    }

    /**
     * Searches in all EntityStates that On State that has the given NetworkEntity ID
     *
     *
     * @param entityID
     *            The Id for that to search the On State
     * @param myScenarioResult
     *            In that "Container" are the States to search for
     * @return the On State with given ID
     */
    public static On findEntityOnStateFromID(final int entityID, final ScenarioResult myScenarioResult) {

        boolean foundNodeID = false;

        On foundEntity = null;

        // Alternative with foreach Cluster foreach On Entities ?? (Double
        // foreach Loop!)
        for (final smartgridoutput.EntityState currentNode : myScenarioResult.getStates()) {
            // Using only Entities that is On
            if (currentNode.getOwner().getId() == entityID && currentNode instanceof On && !(currentNode.getOwner() instanceof NetworkNode)) {
                foundEntity = (On) currentNode;
                foundNodeID = true;

                break;
            }
        }
        // ID don't exists or found Entity has no On State
        if (!foundNodeID) {
            LOG.info("[Hacker] provided ID not found or found Entity has no On State");
        }

        return foundEntity;

    }

    /**
     * Returns the NetworkEntity Id from given On State
     *
     * @param eintityOnState
     *            The State
     * @return Id of the Network Entity
     */
    public static int getIDfromEntityOnState(final On eintityOnState) {

        final int id = eintityOnState.getOwner().getId();

        return id;
    }

    /**
     * Filters the hardwired Logical Connections of the Neighbors out that because of their State
     * (e.g. destroyed) don't functions
     *
     *
     *
     * @param clusterToHack
     *            the Target Cluster in which are all Nodes located that have a (transitive)
     *            Connection between them
     * @param neighborIDList
     *            the hardwired Logical Connections List
     * @return Nodes that are in the intersection of the above Container --> These are my direct
     *         alive Neighbors
     */
    public static LinkedList<On> getNeighborsFromCluster(final Cluster clusterToHack, final LinkedList<Integer> neighborIDList) {

        final LinkedList<On> neighborOnList = new LinkedList<On>();

        // Check whether this Neighbor is in my Cluster
        for (final On clusterNode : clusterToHack.getHasEntities()) {

            // Are my Neighbors at my Cluster ? Otherwise they are gone
            // (Destroyed or something)
            if (neighborIDList.contains(ScenarioModelHelper.getIDfromEntityOnState(clusterNode))) {

                neighborOnList.add(clusterNode);
            }

        } // Check neighbor loop
        return neighborOnList;
    }

    /* *** Private Methods ***** */

    /*
     * Add another Value (Neighbor) to keys (Node) list
     *
     * @param IDLinks Reference to the List Nodes and their Neighbors
     *
     * @param key the Node
     *
     * @param value the Neighbor from "Key" Node
     */
    private static void addNeighbors(final Map<Integer, LinkedList<Integer>> IDLinks, final int key, final int value) {
        // Search "Key" --> Neighbor List "Values"
        LinkedList<Integer> myNeighbor = IDLinks.get(key);

        // Key already in Map
        if (myNeighbor != null) {

            myNeighbor.add(value);

        }

        // Key not in Map (myNeighbor List == null)
        else {
            // Construct New Linked List
            myNeighbor = new LinkedList<Integer>();

            // Add fresh Neighbor "Value"
            myNeighbor.add(value);

            // Add Key (Node) and his first Neighbor in Map
            IDLinks.put(key, myNeighbor);

        }
    }

    public static List<On> getHackedNodes(final List<EntityState> states) {
        final List<On> on = new ArrayList<On>();
        for (final EntityState state : states) {
            if (state instanceof On && ((On) state).isIsHacked()) {
                on.add((On) state);
            }
        }
        return on;
    }

}
