package smartgrid.impactanalysis;

public class Matrix {
    /**
     * Matrix multiplication method.
     *
     * @param m1
     *            Multiplicand
     * @param m2
     *            Multiplier
     * @return Product
     */
    public static double[][] multiplyByMatrix(final double[][] m1, final double[][] m2) {
        final int m1ColLength = m1[0].length; // m1 columns length
        final int m2RowLength = m2.length; // m2 rows length
        if (m1ColLength != m2RowLength) {
            return null; // matrix multiplication is not possible
        }
        final int mRRowLength = m1.length; // m result rows length
        final int mRColLength = m2[0].length; // m result columns length
        final double[][] mResult = new double[mRRowLength][mRColLength];
        for (int i = 0; i < mRRowLength; i++) { // rows from m1
            for (int j = 0; j < mRColLength; j++) { // columns from m2
                for (int k = 0; k < m1ColLength; k++) { // columns from m1
                    mResult[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return mResult;
    }

    public static String toString(final double[][] m) {
        String result = "";
        for (final double[] element : m) {
            for (final double element2 : element) {
                result += String.format("%11.2f", element2);
            }
            result += "\n";
        }
        return result;
    }
}
