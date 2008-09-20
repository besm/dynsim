package dynsim.math.numeric.ode.integration.impl;

import dynsim.math.numeric.ode.integration.AbstractIntegrator;
import dynsim.simulator.ode.system.OdeSystem;

/**
 * x(n+1) = x(n) + h * f(t(n), x(n)) = x(n) + h * x'(n)
 * 
 * Integración de primer orden
 * 
 * @author m83
 * 
 */
public class Euler extends AbstractIntegrator {
	private int dim;

	double[] k1;

	private OdeSystem sys;

	public Euler(OdeSystem target) {
		this.sys = target;
		dim = sys.getIndependentVarsNum();
		k1 = new double[dim];
	}

	public double timeStep(double[] x, double t, double h) throws ArithmeticException {

		k1 = sys.eval(x, t);

		for (int i = 0; i < dim; i++) {
			x[i] = x[i] + h * k1[i];

			checkBounds(x[i]);
		}

		t += h;

		return t;
	}

	public int getDimension() {
		return dim;
	}
}
