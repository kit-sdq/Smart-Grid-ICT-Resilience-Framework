package smartgrid.attackersimulation.strategies;

import java.util.ArrayDeque;
import java.util.stream.Collectors;

import smartgridoutput.On;

public class FullyMeshedStrategy extends SingleStepAttackStrategies {

	public FullyMeshedStrategy(int hackingSpeed) {
		super(true, hackingSpeed);
		// TODO Auto-generated constructor stub
	}

	/**
	 * hack every Node in the the given Cluster without respecting logical
	 * Connections
	 *
	 * @param cluster clusterToHack
	 */
	@Override
	public void hackNextNode(On rootNodeState) {
		final var cluster = rootNodeState.getBelongsToCluster();
		final var entities = cluster.getHasEntities().stream().unordered().filter(e -> !e.isIsHacked())
				.collect(Collectors.toCollection(ArrayDeque::new));
		int hackCounter = 0;
		while (!entities.isEmpty() && !checkMaxHackingOperations(hackCounter)) {
			final var node = entities.pop();
			node.setIsHacked(true);
			hackCounter++;
		}
	}

}
