package smartgrid.attackersimulation.strategies;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import smartgridoutput.Cluster;
import smartgridoutput.On;
import smartgridtopo.CommunicatingEntity;
import smartgridtopo.NetworkEntity;

public abstract class AttackStrategies {
	private final boolean ignoreLogicalConnections;
	private final int hackingSpeed;

	public AttackStrategies(boolean ignoreLogicalConnections, int hackingSpeed) {
		this.ignoreLogicalConnections = ignoreLogicalConnections;
		this.hackingSpeed = hackingSpeed;
	}

	protected boolean checkMaxHackingOperations(int operationCount) {
		return operationCount < hackingSpeed;
	}

	protected Set<On> getConnected(Cluster cluster, On node) {
		final var rootNode = node.getOwner();
		Set<NetworkEntity> nextNetworkEntities;
		if (ignoreLogicalConnections)
			nextNetworkEntities = rootNode.getLinkedBy().stream().flatMap(e -> e.getLinks().stream()).distinct()
					.collect(Collectors.toSet());
		else {
			if (rootNode instanceof CommunicatingEntity)
				nextNetworkEntities = ((CommunicatingEntity) rootNode).getCommunicatesBy().stream()
						.flatMap(e -> e.getLinks().stream()).distinct().collect(Collectors.toSet());
			else
				nextNetworkEntities = new HashSet<>();
		}
		nextNetworkEntities.remove(rootNode); // remove rootNode
		return cluster.getHasEntities().stream().filter(nodeState -> nextNetworkEntities.contains(nodeState.getOwner()))
				.collect(Collectors.toSet());
	}

	protected int getHackingSpeed() {
		return hackingSpeed;
	}

	public abstract void hackNextNode(On rootNodeState);
}
