package smartgrid.attackersimulation.strategies;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import smartgridoutput.Cluster;
import smartgridoutput.On;
import smartgridtopo.CommunicatingEntity;
import smartgridtopo.LogicalCommunication;
import smartgridtopo.NetworkEntity;

public abstract class AttackStrategies {
    private final boolean ignoreLogicalConnections;
    private final int hackingSpeed;

    public AttackStrategies(final boolean ignoreLogicalConnections, final int hackingSpeed) {
        this.ignoreLogicalConnections = ignoreLogicalConnections;
        this.hackingSpeed = hackingSpeed;
    }

    protected boolean checkMaxHackingOperations(final int operationCount) {
        return operationCount < this.hackingSpeed;
    }

    protected Set<On> getConnected(final Cluster cluster, final On node) {
        final var rootNode = node.getOwner();
        Set<NetworkEntity> nextNetworkEntities;
        if (this.ignoreLogicalConnections) {
            nextNetworkEntities = rootNode.getLinkedBy().stream().flatMap(e -> e.getLinks().stream()).distinct()
                    .collect(Collectors.toSet());
        } else {
            if (rootNode instanceof CommunicatingEntity) {
            	nextNetworkEntities = new HashSet<>();
            	for (LogicalCommunication logConn : ((CommunicatingEntity) rootNode).getCommunicatesBy()){
            		for (CommunicatingEntity networkEntity: logConn.getLinks()) {
            			nextNetworkEntities.add(networkEntity);
            		}
            	}
            } else {
                nextNetworkEntities = new HashSet<>();
            }
        }
        nextNetworkEntities.remove(rootNode); // remove rootNode
        Set<On> conenctedNodes = new HashSet<On>();
        for (On onEntity : cluster.getHasEntities()) {
        	if (nextNetworkEntities.contains(onEntity.getOwner())) 
        		conenctedNodes.add(onEntity);
        }
        return conenctedNodes;
    }

    protected int getHackingSpeed() {
        return this.hackingSpeed;
    }

    public abstract void hackNextNode(On rootNodeState);
}
