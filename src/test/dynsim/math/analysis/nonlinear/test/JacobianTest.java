package dynsim.math.analysis.nonlinear.test;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import dynsim.math.analysis.local.Stability;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.SimpleTest;
import junit.framework.TestCase;

public class JacobianTest extends TestCase {
	public void testJacobian() throws Exception {
		OdeSystem sys = new SimpleTest();
		// (4,0) punto silla
		Matrix jac = sys.getJacobianMatrix(new double[] { 4, 0 });
		EigenvalueDecomposition eig = jac.eig();
		double[] imag = eig.getImagEigenvalues();
		double[] real = eig.getRealEigenvalues();
		print(real, imag);
		// TODO que devuelva numérico!
		assertEquals("hyperbolic unstable saddle point ", Stability.getPointType(real, imag));
		System.out.println(Stability.getPointType(real, imag));
	}

	private void print(double[] re, double[] im) {
		for (int i = 0; i < re.length; i++) {
			System.out.println("re[" + re[i] + "]im[" + im[i] + "]");
		}
	}
}
