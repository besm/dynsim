package dynsim.math.analysis.nonlinear.test;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import junit.framework.TestCase;
import dynsim.math.analysis.local.Stability;
import dynsim.simulator.ode.system.impl.interp.InterpretedOde;
import dynsim.simulator.system.interp.ParserWrapper;
import edu.hws.jcm.data.Variable;

public class InterpretedJacobianTest extends TestCase {
	public void testJacobian() throws Exception {
		InterpretedOde sys = getSys();
		// (4,3) punto espiral estable
		Matrix jac = sys.getJacobianMatrix(new double[] { 4, 3 });
		EigenvalueDecomposition eig = jac.eig();
		double[] imag = eig.getImagEigenvalues();
		double[] real = eig.getRealEigenvalues();
		print(real, imag);
		assertEquals("as. stable spiral point ", Stability.getPointType(real, imag));
		System.out.println(Stability.getPointType(real, imag));
	}

	private void print(double[] re, double[] im) {
		for (int i = 0; i < re.length; i++) {
			System.out.println("re[" + re[i] + "]im[" + im[i] + "]");
		}
	}

	private InterpretedOde getSys() {
		InterpretedOde sys = new InterpretedOde();
		setUpExpressions(sys);

		sys.setInitialCondition("x0", .1);
		sys.setInitialCondition("x1", .1);

		return sys;
	}

	private void setUpExpressions(InterpretedOde sys) {
		ParserWrapper parser = sys.getWrapper();

		parser.add(new Variable("x0"));
		parser.add(new Variable("x1"));

		parser.addExpression("33-10*x0-3*x1+x0^2");
		parser.addExpression("-18+6*x0+2*x1-x0*x1");
	}
}
