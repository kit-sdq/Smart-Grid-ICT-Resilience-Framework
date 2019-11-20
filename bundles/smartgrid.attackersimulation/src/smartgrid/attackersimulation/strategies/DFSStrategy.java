package smartgrid.attackersimulation.strategies;

import java.util.ArrayDeque;

import smartgridoutput.Cluster;
import smartgridoutput.On;

public class DFSStrategy extends SingleStepAttackStrategies {

	public DFSStrategy(boolean ignoreLogicalConnections, int hackingSpeed) {
		super(ignoreLogicalConnections, hackingSpeed);
	}

	private void dfsHacking(Cluster cluster, On node, int hackCount) {
		if (!node.isIsHacked()) {
			node.setIsHacked(true);
			if (--hackCount <= 0)
				return;
		}
		final var stack = new ArrayDeque<>(getConnected(cluster, node));
		while (!stack.isEmpty()) {
			final var newNode = stack.pop();
			dfsHacking(cluster, newNode, hackCount);
		}
	}

	@Override
	public void hackNextNode(On rootNodeState) {
		dfsHacking(rootNodeState.getBelongsToCluster(), rootNodeState, getHackingSpeed());
	}

}
