package smartgrid.attackersimulation.strategies;

import java.util.ArrayDeque;
import java.util.HashMap;

import smartgridoutput.On;

public class BFSStrategy extends SingleStepAttackStrategies {

	public BFSStrategy(boolean ignoreLogicalConnections, int hackingSpeed) {
		super(ignoreLogicalConnections, hackingSpeed);
	}

	/**
	 * Hacks next Node using BFS as expansion algorithms
	 * 
	 * @see https://de.wikipedia.org/wiki/Breitensuche
	 * 
	 * @param cluster Cluster for searching next Attackpath
	 */
	@Override
	public void hackNextNode(On rootNodeState) {
		final var cluster = rootNodeState.getBelongsToCluster();
		int counterHackoperations = 0;
		final var visited = new HashMap<On, Boolean>();
		final var queue = new ArrayDeque<On>();
		queue.push(rootNodeState);
		visited.put(rootNodeState, true);
		while (!queue.isEmpty() && checkMaxHackingOperations(counterHackoperations)) {
			final var node = queue.pop();
			for (final var next : getConnected(cluster, node)) {
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

}
