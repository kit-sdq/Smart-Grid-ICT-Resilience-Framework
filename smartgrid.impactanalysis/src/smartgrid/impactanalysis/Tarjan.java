package smartgrid.impactanalysis;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class Tarjan {
    private static final Logger LOG = Logger.getLogger(Tarjan.class);

    /**
     * expects the graph to be BIDRECTIONAL (simplification of tarjan)
     *
     * @param adjacentMatrix
     * @return
     */
    public static List<List<Integer>> getClusters(final double[][] adjacentMatrix,
            final Map<Integer, Integer> internalToExternal) {
        final List<List<Integer>> result = new LinkedList<List<Integer>>();
        final LinkedList<Integer> unprogressedNodes = new LinkedList<Integer>();
        final boolean[] visited = new boolean[adjacentMatrix.length];
        for (int i = 0; i < adjacentMatrix.length; i++) {
            unprogressedNodes.add(i);
            visited[i] = false;
        }
        LOG.debug("[Tarjan]: Algorithm starts with");

        LOG.debug(printConvertedList(unprogressedNodes, internalToExternal));
        LOG.debug(Arrays.toString(unprogressedNodes.toArray()));
        while (unprogressedNodes.size() > 0) {
            // start building a new cluster
            final LinkedList<Integer> nodesToCheck = new LinkedList<Integer>();
            final int starter = unprogressedNodes.pollFirst();
            nodesToCheck.add(starter);
            visited[starter] = true;
            LOG.debug("[Tarjan]: New cluster with " + internalToExternal.get(starter));

            final LinkedList<Integer> cluster = new LinkedList<Integer>();
            while (nodesToCheck.size() > 0) {
                final int n = nodesToCheck.pollFirst();
                cluster.add(n);
                LOG.debug("[Tarjan]: Find neighbor of: " + internalToExternal.get(n));
                for (int i = 0; i < adjacentMatrix.length; i++) {
                    if (adjacentMatrix[n][i] > 0) {
                        LOG.debug("[Tarjan]: Found neighbor: " + internalToExternal.get(i));
                    }
                    if (adjacentMatrix[n][i] > 0 && !visited[i]) {
                        LOG.debug("[Tarjan]: Unvisited neighbor: " + internalToExternal.get(i));
                        final int m = i;

                        visited[m] = true;
                        nodesToCheck.add(m);

                        unprogressedNodes.remove(new Integer(m));
                    }
                }
            }
            result.add(cluster);
        }
        return result;
    }

    static String printConvertedList(final List<Integer> l, final Map<Integer, Integer> internalToExternal) {
        String s = "";

        for (final Integer i : l) {
            s += internalToExternal.get(i) + " ";
        }

        return s;
    }
}
