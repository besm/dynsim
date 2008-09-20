package dynsim.math.numeric.linear.solver.test;

import junit.framework.TestCase;

import dynsim.math.numeric.solver.linear.GaussSeidel;
import dynsim.math.numeric.solver.linear.Jacobi;
import dynsim.math.numeric.solver.linear.SOR;

public class IterativeSolverTest extends TestCase {
	private double[] x0;

	private double[][] A;

	private double[] r;

	protected void setUp() throws Exception {
		super.setUp();
		A = new double[][] { { 10, -1, 2, 0, 6 }, { -1, 11, -1, 3, 25 }, { 2, -1, 10, -1, -11 }, { 0, 3, -1, 8, 15 } };
		// initial aprox.
		x0 = new double[] { 0., 0., 0., 0. };
		r = new double[] { 1, 2, -1, 1 };
	}

	public void testJacobi() {
		Jacobi jac = new Jacobi(A, x0);
		jac.solve();
		double rs[] = jac.getResults();
		for (int i = 0; i < r.length; i++)
			assertEquals(r[i], rs[i], 1E-3);
	}

	public void testGaussSeidel() {
		GaussSeidel gs = new GaussSeidel(A, x0);
		gs.solve();
		double rs[] = gs.getResults();
		for (int i = 0; i < r.length; i++)
			assertEquals(r[i], rs[i], 1E-4);
	}

	public void testSOR() {
		SOR sor = new SOR(A, x0);
		sor.solve();
		double rs[] = sor.getResults();
		for (int i = 0; i < r.length; i++)
			assertEquals(r[i], rs[i], 1E-4);
	}
}
