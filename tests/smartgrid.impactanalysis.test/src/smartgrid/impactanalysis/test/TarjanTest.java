
package smartgrid.impactanalysis.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import smartgrid.impactanalysis.Tarjan;

/**
 * This class to test the tarjan-get-clusters algorithms presented in the Tarjan Class
 *
 * @author mazenebada
 *
 */
public class TarjanTest {
    private double[][] adjacentMatrix;
    private Map<Integer, String> internalToExternal;
    private List<List<Integer>> clusters;

    /**
     * setup data
     */
    @Before
    public void setUp() {
        this.initializeMatrix();
        this.initializeMap();
    }

    /**
     * tests the cluster forming
     */
    @Test
    public void getClustersTest() {

        this.clusters = Tarjan.getClusters(this.adjacentMatrix, this.internalToExternal);

        // first expected cluster
        final List<Integer> expectedCluster1 = new LinkedList<Integer>();
        expectedCluster1.add(0);
        expectedCluster1.add(1);
        expectedCluster1.add(3);

        // second expected cluster
        final List<Integer> expectedCluster2 = new LinkedList<Integer>();
        expectedCluster2.add(2);
        expectedCluster2.add(4);

        // expected list of clusters
        final List<List<Integer>> expectedClusters = new LinkedList<List<Integer>>();
        expectedClusters.add(expectedCluster1);
        expectedClusters.add(expectedCluster2);

        // asserting the correctness
        Assert.assertEquals(expectedClusters, this.clusters);
    }

    /**
     * initialize the map to be used in testing
     */
    private void initializeMap() {
        this.internalToExternal = new HashMap<Integer, String>();
        this.internalToExternal.put(0, "id1");
        this.internalToExternal.put(1, "id2");
        this.internalToExternal.put(2, "id3");
        this.internalToExternal.put(3, "id4");
        this.internalToExternal.put(4, "id5");

    }

    /**
     * initialize the matrix of the model
     */
    private void initializeMatrix() {

        // forming an adjacent matrix with two clusters : 0,1,3 and 2,4

        this.adjacentMatrix = new double[5][5];

        this.adjacentMatrix[0][0] = 0;
        this.adjacentMatrix[0][1] = 1;
        this.adjacentMatrix[0][2] = 0;
        this.adjacentMatrix[0][3] = 0;
        this.adjacentMatrix[0][4] = 0;

        this.adjacentMatrix[1][0] = 1;
        this.adjacentMatrix[1][1] = 0;
        this.adjacentMatrix[1][2] = 0;
        this.adjacentMatrix[1][3] = 1;
        this.adjacentMatrix[1][4] = 0;

        this.adjacentMatrix[2][0] = 0;
        this.adjacentMatrix[2][1] = 0;
        this.adjacentMatrix[2][2] = 0;
        this.adjacentMatrix[2][3] = 0;
        this.adjacentMatrix[2][4] = 1;

        this.adjacentMatrix[3][0] = 0;
        this.adjacentMatrix[3][1] = 1;
        this.adjacentMatrix[3][2] = 0;
        this.adjacentMatrix[3][3] = 0;
        this.adjacentMatrix[3][4] = 0;

        this.adjacentMatrix[4][0] = 0;
        this.adjacentMatrix[4][1] = 0;
        this.adjacentMatrix[4][2] = 1;
        this.adjacentMatrix[4][3] = 0;
        this.adjacentMatrix[4][4] = 0;

    }
}
