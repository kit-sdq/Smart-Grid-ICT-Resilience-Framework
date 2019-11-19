package smartgrid.attackersimulation;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.helper.ScenarioModelHelper;
import smartgrid.simcontrol.test.baselib.Constants;
import smartgrid.simcontrol.test.baselib.HackingType;
import smartgrid.simcontrol.test.baselib.coupling.IAttackerSimulation;
import smartgridoutput.Cluster;
import smartgridoutput.On;
import smartgridoutput.ScenarioResult;
import smartgridtopo.CommunicatingEntity;
import smartgridtopo.NetworkEntity;
import smartgridtopo.NetworkNode;
import smartgridtopo.SmartGridTopology;

/**
 * This Class represents an local Hacker.
 *
 * A Local Hacker operates from one distinct Node and can only hack Node that
 * have a logical Connection to his "root" Node
 *
 * @author Christian
 */
public class LocalHacker implements IAttackerSimulation {

	private static final Logger LOG = Logger.getLogger(LocalHacker.class);

	private HackingType hackingTypes;
	private String rootNodeID; // IDs stay the same over the whole Analysis
	private On rootNodeState; // Reference Changes between runs!
	private Map<String, LinkedList<On>> neighborsInClusterMap;
	private int hackingSpeed;

	private boolean firstRun = true;
	private boolean initDone = false;
	private boolean ignoreLogicalConnections;

	private SmartGridTopology topo;
	private ScenarioResult scenarioResult;

	/**
	 * Default constructor is needed by the OSGi framework to be able to use the
	 * extension point
	 */
	public LocalHacker() {
	}

	@Override
	public void init(final ILaunchConfiguration config) throws CoreException {
		this.rootNodeID = config.getAttribute(Constants.ROOT_NODE_ID_KEY, Constants.DEFAULT_ROOT_NODE_ID);
		this.hackingSpeed = Integer
				.parseInt(config.getAttribute(Constants.HACKING_SPEED_KEY, Constants.DEFAULT_HACKING_SPEED));
		this.hackingTypes = HackingType
				.valueOf(config.getAttribute(Constants.HACKING_STYLE_KEY, Constants.DEFAULT_HACKING_STYLE));
		this.ignoreLogicalConnections = Boolean
				.valueOf(config.getAttribute(Constants.IGNORE_LOC_CON_KEY, Constants.FALSE));
		LOG.info("Infection root node ID is: " + rootNodeID);
		LOG.info("Hacking style is: " + hackingTypes);
		LOG.info("Hacking speed is: " + this.hackingSpeed);

		this.initDone = true;

		LOG.debug("Init done");
	}

	/**
	 * @see smartgrid.simcontrol.interfaces.IAttackerSimulation#run(smartgridtopo
	 *      .Scenario, smartgridoutput.ScenarioResult)
	 */
	@Override
	public ScenarioResult run(final SmartGridTopology smartGridTopo, final ScenarioResult impactAnalysisOutput) {

		assert this.initDone : "Init was not run! Run init() first!";

		// Copy Input in own Variables
		this.topo = smartGridTopo;
		this.scenarioResult = impactAnalysisOutput;

		if (this.firstRun) {
			this.setHackingRootOnEntityState();
			this.neighborsMapInit();
		} else {
			// Find Root On Entity using ID I already know
			this.rootNodeState = ScenarioModelHelper.findEntityOnStateFromID(this.rootNodeID, this.scenarioResult);
		}

		if (this.rootNodeState != null) {
			// At this Point we have valid RootNodeID and rootNode
			assert this.rootNodeState.getOwner().getId() == this.rootNodeID : "Root Node Not Valid !";
			// Hack Root just in case its not hacked
			// this.rootNode.setIsHacked(true);
			// Starting hacking according to the desired hacking Style

			this.hackNext(this.rootNodeState.getBelongsToCluster());
		}
		this.firstRun = false;
		this.scenarioResult.setScenario(smartGridTopo);

		LOG.debug("Hacking done");
		return this.scenarioResult;
	}

	/**
	 * arranges the neighbours of every node randomly and put them in a list mapped
	 * to the node
	 */
	private void neighborsMapInit() {
		this.neighborsInClusterMap = new HashMap<String, LinkedList<On>>();

		final Map<String, LinkedList<String>> IDtoHisNeighborLinks = ScenarioModelHelper.genNeighborMapbyID(this.topo);
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
		switch (this.hackingTypes) {
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
			throw new IllegalArgumentException("Unknown hacking style: " + this.hackingTypes);

		}

	}

	/**
	 * Hacks next Node using BFS as expansion algorithms
	 * 
	 * @see https://de.wikipedia.org/wiki/Breitensuche
	 * 
	 * @param cluster Cluster for searching next Attackpath
	 */
	private void bfsHacking(final Cluster cluster) {
		var nodes = cluster.getHasEntities();
		int counterHackoperations = 0;
		var visited = new HashMap<On, Boolean>();
		var queue = new ArrayDeque<On>();
		queue.push(rootNodeState);
		visited.put(rootNodeState, true);
		while (!queue.isEmpty() && checkMaxHackingOperations(counterHackoperations)) {
			var node = queue.pop();
			for (var next : getConnected(cluster, node)) {
				if (!visited.getOrDefault(next, false)) {
					if (!next.isIsHacked()) { // hack if not visited
						next.setIsHacked(true);
						counterHackoperations++;
						if (!checkMaxHackingOperations(counterHackoperations))
							break;
					}
					queue.push(next);
					visited.put(next, true);
				}

			}
		}

	}

	private boolean checkMaxHackingOperations(int operationCount) {
		return operationCount < hackingSpeed;
	}

	private Set<On> getConnected(Cluster cluster, On node) {
		var rootNode = node.getOwner();
		Set<NetworkEntity> nextNetworkEntities;
		if (ignoreLogicalConnections)
			nextNetworkEntities = rootNode.getLinkedBy().stream().flatMap(e -> e.getLinks().stream()).distinct()
					.collect(Collectors.toSet());
		else {
			if (rootNode instanceof CommunicatingEntity)
				nextNetworkEntities = ((CommunicatingEntity) rootNode).getCommunicatesBy().stream()
						.flatMap(e -> e.getLinks().stream()).distinct().collect(Collectors.toSet());
			else
				nextNetworkEntities = new HashSet<NetworkEntity>();
		}
		nextNetworkEntities.remove(rootNode); // remove rootNode
		return cluster.getHasEntities().stream().filter(nodeState -> nextNetworkEntities.contains(nodeState.getOwner()))
				.collect(Collectors.toSet());
	}

	/**
	 * Modified from Wikipedia http://en.wikipedia.org/wiki/Depth-first_search
	 *
	 * @param clusterToHack
	 */
	private void dfsHacking(final Cluster clusterToHack) {
//		final int hackCount = 0;
//		final List<On> visitedNodes = new LinkedList<On>();
//
//		final On Node = this.rootNodeState;
//		visitedNodes.add(Node);
//		/*
//		 * Reads from Scenario so this List don't respects changes in States of the
//		 * Entities --> It's only the "hardwired" logical Connection neighbors
//		 */
//		final Map<String, LinkedList<String>> IDtoHisNeighborLinks = ScenarioModelHelper
//				.genNeighborMapbyID(this.topo);
//
//		this.dfs(clusterToHack, Node, IDtoHisNeighborLinks, hackCount, visitedNodes);
		dfs(clusterToHack, rootNodeState, hackingSpeed);

	}

	private void dfs(Cluster cluster, On node, int hackCount) {
		if (!node.isIsHacked()) {
			node.setIsHacked(true);
			if (--hackCount <= 0)
				return;
		}
		var stack = new ArrayDeque<On>(getConnected(cluster, node));
		while (!stack.isEmpty()) {
			var newNode = stack.pop();
			dfs(cluster, newNode, hackCount);
		}
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
			 * Here it Filters the hardwired Logical Connections of the Neighbors out that
			 * because of their State (e.g. destroyed) don't functions
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
	 * hack every Node in the the given Cluster without respecting logical
	 * Connections
	 *
	 * @param cluster clusterToHack
	 */
	private void fullMeshedHacking(final Cluster cluster) {
		var entities = cluster.getHasEntities().stream().unordered().filter(e -> !e.isIsHacked())
				.collect(Collectors.toCollection(ArrayDeque::new));
		int hackCounter = 0;
		while(!entities.isEmpty() && !checkMaxHackingOperations(hackCounter)) {
			var node = entities.pop();
			node.setIsHacked(true);
			hackCounter++;
		}
	}

	/**
	 * choosing randomly a root when none is identified
	 */
	private void chooseRootIDByRandom() {
		Cluster myCluster;
		final Random myRandom = new Random();

		final int clusterCount = this.scenarioResult.getClusters().size();

		// Choose Random Cluster with Entries
		do {
			// [0 - clusterCount) Exclusive upper bound
			final int myClusterNumber = myRandom.nextInt(clusterCount);
			myCluster = this.scenarioResult.getClusters().get(myClusterNumber);

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
			this.rootNodeState = ScenarioModelHelper.findEntityOnStateFromID(this.rootNodeID, this.scenarioResult);

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

}