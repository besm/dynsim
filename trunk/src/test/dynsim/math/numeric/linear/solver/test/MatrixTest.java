package dynsim.math.numeric.linear.solver.test;

import junit.framework.TestCase;

import Jama.Matrix;
import Jama.QRDecomposition;

public class MatrixTest extends TestCase {
	private double[] b1, b2, r1, r2;

	private double[][] A3x3, A5x5;

	protected void setUp() throws Exception {
		A3x3 = new double[][] { { 4, 5, -1 }, { 3, -2, 7 }, { 5, -2, -3 } };
		b1 = new double[] { 1, -1, 3 };
		r1 = new double[] { 0.344595, -0.141892, -0.331081 };

		A5x5 = new double[][] { { 1, -2, 3, -4, 5 }, { 0, 9, -8, 7, -6 }, { 1, -4, 7, 0, 2 }, { 2, 8, -5, 1, 1 },
				{ 3, 9, 4, -6, -1 } };

		b2 = new double[] { 15, -8, 24, 12, 4 };
		r2 = new double[] { 1, 2, 3, 4, 5 };
	}

	public void testMatrixSolve() {
		Matrix A = new Matrix(A3x3);
		Matrix b = new Matrix(b1, 3);
		// LU if square
		Matrix r = A.solve(b);

		for (int i = 0; i < r1.length; i++)
			assertEquals(r1[i], r.get(i, 0), 1E-6);
	}

	public void testQRSolve() {
		Matrix A = new Matrix(A5x5);
		Matrix b = new Matrix(b2, 5);
		QRDecomposition qr = A.qr();
		Matrix r = qr.solve(b);

		for (int i = 0; i < r2.length; i++)
			assertEquals(r2[i], r.get(i, 0), 1E-6);
	}
}
