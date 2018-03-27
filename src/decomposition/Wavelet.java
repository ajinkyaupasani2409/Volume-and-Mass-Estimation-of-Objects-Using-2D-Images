package decomposition;

import org.ejml.data.DenseMatrix64F;

/**
 *
 * @author eadams
 * @author denny
 */
public abstract class Wavelet {

    public DenseMatrix64F [] transform(DenseMatrix64F m, int n) {
        DenseMatrix64F matrix = m.copy();
        DenseMatrix64F [] matrixs = new DenseMatrix64F[n];
        if (matrix.getNumCols() != matrix.getNumRows()) {
            throw new RuntimeException("Matrix must be square!");
        }
//n = BinaryOps.log2(matrix.getNumRows());
        for (int j = 0; j < n; j++) {
            // column transform
            for (int c = 0; c < matrix.getNumCols(); c++) {
                //MatrixIterator iter = matrix.iterator(false, 0, c, matrix.getNumRows() - 1, c);
                double[] col = new double[matrix.getNumRows()];

                for (int rows = 0; rows < matrix.getNumRows(); rows++) {
                    for (int cols = c; cols < c + 1; cols++) {
                        col[rows] = matrix.get(rows, cols);
                    }
                }
                //for (int idx = 0; idx < matrix.getNumRows(); idx++) {
                //    col[idx] = iter.next();
                //}

                col = forwardStep(col);
                for (int r = 0; r < matrix.getNumRows(); r++) {
                    matrix.set(r, c, col[r]);
                }
            }

            // row transform
            for (int r = 0; r < matrix.getNumRows(); r++) {
                //MatrixIterator iter = matrix.iterator(true, r, 0, r, matrix.getNumCols() - 1);
                double[] row = new double[matrix.getNumCols()];

                for (int rows = r; rows < r + 1; rows++) {
                    for (int cols = 0; cols < matrix.getNumCols(); cols++) {
                        row[cols] = matrix.get(rows, cols);
                    }
                }

                //int idx = 0;
                //while (iter.hasNext()) {
                //    row[idx] = iter.next();
                //    idx++;
                //}
                row = forwardStep(row);
                for (int c = 0; c < matrix.getNumCols(); c++) {
                    matrix.set(r, c, row[c]);
                }

            }
            matrixs[j] = matrix;
        }

        return matrixs;
    }

    public DenseMatrix64F [] transform(DenseMatrix64F matrix) {
        return transform(matrix, BinaryOps.log2(matrix.getNumRows()));
    }

    protected abstract double[] forwardStep(double[] signal);
}
