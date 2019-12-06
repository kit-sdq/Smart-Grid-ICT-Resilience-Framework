package smartgrid.attackersimulation.strategies;

import java.util.ArrayDeque;

import smartgridoutput.Cluster;
import smartgridoutput.On;

public class DFSStrategy extends SingleStepAttackStrategies {

    public DFSStrategy(final boolean ignoreLogicalConnections, final int hackingSpeed) {
        super(ignoreLogicalConnections, hackingSpeed);
    }

    private void dfsHacking(final Cluster cluster, final On node, int hackCount) {
        if (!node.isIsHacked()) {
            node.setIsHacked(true);
            if (--hackCount <= 0) {
                return;
            }
        }
        final var stack = new ArrayDeque<>(this.getConnected(cluster, node));
        while (!stack.isEmpty()) {
            final var newNode = stack.pop();
            this.dfsHacking(cluster, newNode, hackCount);
        }
    }

    @Override
    public void hackNextNode(final On rootNodeState) {
        this.dfsHacking(rootNodeState.getBelongsToCluster(), rootNodeState, this.getHackingSpeed());
    }

}
