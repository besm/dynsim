package dynsim.math.numeric.nonlinear.solver.test;

import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.TestCase;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import dynsim.math.analysis.local.Stability;
import dynsim.math.numeric.solver.IterativeSolver;
import dynsim.math.numeric.solver.nonlinear.Broyden;
import dynsim.math.numeric.solver.nonlinear.NewtonHomotopy;
import dynsim.simulator.ode.system.impl.*;
import dynsim.simulator.ode.system.impl.interp.InterpretedOde;
import dynsim.simulator.system.DynamicalSystem;
import dynsim.simulator.system.interp.ParserWrapper;
import edu.hws.jcm.data.Variable;

public class HomotopyTest extends TestCase {
	double delta = 1E-4;

	// public void testAproxRoot() {
	// InterpretedOde sys = getSys();
	// NewtonHomotopy hom = new NewtonHomotopy(sys);
	// hom.setStartSearch(new double[] { 5, 5 });
	// hom.solve();
	// // double[] res = br.getResults();
	// //
	// // assertEquals(br.getStatus(), IterativeSolver.OK);
	// // assertEquals(0.6666, res[0], delta);
	// // assertEquals(0.5000, res[1], delta);
	// }
	//
	// public void testAproxRootSimple() {
	// OdeSystem sys = new SimpleTest();
	// NewtonHomotopy hom = new NewtonHomotopy(sys);
	// hom.setStartSearch(new double[] {6, 7});
	// hom.solve();
	// double[] res = hom.getResults();
	// // assertEquals(1.9999, res[0], delta);
	// // assertEquals(2.0000, res[1], delta);
	// }

	public void testAproxOde() {
		DynamicalSystem sys = new Rikitake();

		double[] res = null;
		ArrayList<double[]> pts = new ArrayList<double[]>();

		for (int i = 0; i < 250; i++) {
			NewtonHomotopy hom = new NewtonHomotopy(sys);
			hom.setStartSearch(new double[] { i, i, i });
			hom.solve();
			res = hom.getResults();

			assertEquals(hom.getStatus(), IterativeSolver.OK);
			if (hom.getStatus() != IterativeSolver.OK) {
				continue;
			}

			Broyden br = new Broyden(sys);
			br.setStartSearch(res);
			br.solve();
			res = br.getResults();
			if (br.getStatus() == IterativeSolver.OK) {
				if (notContains(pts, res)) {
					pts.add(res);
					for (int n = 0; n < res.length; n++) {
						System.out.println("FOUND " + res[n]);
					}
				}
			}
		}

		assertEquals(3, pts.size());

		Iterator<double[]> itor = pts.iterator();

		while (itor.hasNext()) {
			double[] ep = itor.next();
			Matrix jac = sys.getJacobianMatrix(ep);
			EigenvalueDecomposition eig = jac.eig();
			double[] imag = eig.getImagEigenvalues();
			double[] real = eig.getRealEigenvalues();

			System.out.println(ep + " " + Stability.getPointType(real, imag));
		}
	}

	private boolean notContains(ArrayList<double[]> pts, double[] res) {
		Iterator<double[]> it = pts.iterator();
		if (pts.isEmpty())
			return true;

		while (it.hasNext()) {
			double[] c = it.next();

			boolean eq = false;

			for (int i = 0; i < c.length; i++) {
				double diff = Math.abs(c[i] - res[i]);

				if (diff <= 1E-2) {
					eq = true;
				} else {
					eq = false;
					break;
				}
			}

			if (eq) {
				return false;
			}
		}

		return true;
	}

	private InterpretedOde getSys() {
		InterpretedOde sys = new InterpretedOde();
		setUpExpressions(sys);

		sys.setInitialCondition("x0", 0);
		sys.setInitialCondition("x1", 0);

		return sys;
	}

	private void setUpExpressions(InterpretedOde sys) {
		ParserWrapper parser = sys.getWrapper();

		parser.add(new Variable("x0"));
		parser.add(new Variable("x1"));

		parser.addExpression("x0*(5-10*x1)");
		parser.addExpression("-x1*(2-3*x0)");
	}
}
