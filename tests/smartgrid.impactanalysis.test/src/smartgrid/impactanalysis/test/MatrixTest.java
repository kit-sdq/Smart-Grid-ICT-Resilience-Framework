package smartgrid.impactanalysis.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import smartgrid.impactanalysis.Matrix;

/**
 * This class is to test the matrix-multiplication method in the Matrix Class
 *
 * @author mazenebada
 *
 */

public class MatrixTest {
    private static double[][] m1;
    private static double[][] m2;
    private static double[][] m3;
    private double[][] product;

    /**
     * setup the data
     */
    @BeforeClass
    public static void setUp() {

        m1 = new double[2][2];
        m1[0][0] = 1;
        m1[0][1] = 2;
        m1[1][0] = 2;
        m1[1][1] = 1;

        m2 = new double[2][3];
        m2[0][0] = 2;
        m2[0][1] = 3;
        m2[0][2] = 2;
        m2[1][0] = 3;
        m2[1][1] = 2;
        m2[1][2] = 3;

        m3 = new double[1][1];
        m3[0][0] = 2;
    }

    /**
     * to test the correctness of the product of valid multiplication
     */
    @Test
    public void multiplyByMatrixTest1() {

        this.product = Matrix.multiplyByMatrix(m1, m2);
        Assert.assertEquals("The number of rows of the product is not correct", 2, this.product.length);
        Assert.assertEquals("The number of columns of the product is not correct", 3, this.product[0].length);

        Assert.assertEquals(8.0, this.product[0][0], 0.0);
        Assert.assertEquals(7.0, this.product[0][1], 0.0);
        Assert.assertEquals(8.0, this.product[0][2], 0.0);
        Assert.assertEquals(7.0, this.product[1][0], 0.0);
        Assert.assertEquals(8.0, this.product[1][1], 0.0);
        Assert.assertEquals(7.0, this.product[1][2], 0.0);
    }

    /**
     * to test the correctness of detecting the invalidity of matrix multiplication
     */
    @Test
    public void multiplyByMatrixTest2() {

        Assert.assertNull(Matrix.multiplyByMatrix(m1, m3));
    }

}
