package dynsim.math.analysis.nonlinear.test;

import junit.framework.TestCase;
import dynsim.math.analysis.local.LyapunovExponent;
import dynsim.simulator.iteratedmap.system.impl.Henon;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.Lorentz;
import dynsim.simulator.system.DynamicalSystem;

public class LyapunovExpTest extends TestCase {
	/*
	 * Exponents aprox. values from J. C. Sprott, Chaos and Time-Series Analysis
	 * Dimension values: Henon~1.26 Lorentz~2.06 from Mandelbrot[83]
	 */
	public void testHenon() {
		DynamicalSystem sys = new Henon();
		LyapunovExponent le = new LyapunovExponent(sys);
		le.compute(5000);
		double[] exps = le.getLyapunovExponents();

		assertEquals(0.426, exps[0], 0.001);
		assertEquals(-1.630, exps[1], 0.001);

		assertEquals(1.261, le.getKaplanYorkeDim(), 0.001);
		printStability(exps[0]);
	}

	public void testLorentz() {
		OdeSystem sys = new Lorentz();

		LyapunovExponent le = new LyapunovExponent(sys);
		le.compute(5000);

		double[] exps = le.getLyapunovExponents();

		assertTrue(exps[0] > 0);
		assertEquals(2.055, le.getKaplanYorkeDim(), 0.001);
		printStability(exps[0]);
	}

	private void printStability(double exp) {
		if (exp > 0.01) {
			System.out.println("chaotic " + exp);
		} else if (exp < 0) {
			System.out.println("periodic " + exp);
		} else {
			System.out.println("stable " + exp);
		}
	}
}
