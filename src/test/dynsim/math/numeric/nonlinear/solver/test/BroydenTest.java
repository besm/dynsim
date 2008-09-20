package dynsim.math.numeric.nonlinear.solver.test;

import java.util.ArrayList;
import java.util.Iterator;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import dynsim.math.analysis.local.Stability;
import dynsim.math.numeric.solver.IterativeSolver;
import dynsim.math.numeric.solver.nonlinear.Broyden;
import dynsim.math.numeric.solver.nonlinear.GradientDescent;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.*;
import dynsim.simulator.ode.system.impl.interp.InterpretedOde;
import dynsim.simulator.system.DynamicalSystem;
import dynsim.simulator.system.interp.ParserWrapper;
import edu.hws.jcm.data.Variable;

import junit.framework.TestCase;

public class BroydenTest extends TestCase {
	double delta = 1E-4;

	public void testFindRoot() {
		InterpretedOde sys = getSys();
		Broyden br = new Broyden(sys);
		br.setStartSearch(new double[] { 1, 1 });
		br.solve();
		double[] res = br.getResults();

		assertEquals(br.getStatus(), IterativeSolver.OK);
		assertEquals(0.6666, res[0], delta);
		assertEquals(0.5000, res[1], delta);
	}

	public void testFindRootSimple() {
		OdeSystem sys = new SimpleTest();
		Broyden br = new Broyden(sys);
		br.setStartSearch(new double[] { 1.5, 2.5 });
		br.solve();
		double[] res = br.getResults();
		assertEquals(1.9999, res[0], delta);
		assertEquals(2.0000, res[1], delta);
	}

	public void testGradientDescSimple() {
		DynamicalSystem sys = new Ueda();

		double[] res = null;
		ArrayList<double[]> pts = new ArrayList<double[]>();

		for (int i = -15; i < 15; i++) {
			GradientDescent gd = new GradientDescent(sys);
			gd.setStartSearch(new double[] { i, i, i });
			gd.solve();
			res = gd.getResults();

			if (gd.getStatus() != IterativeSolver.OK) {
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
