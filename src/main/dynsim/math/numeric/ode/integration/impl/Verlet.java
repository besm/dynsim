package dynsim.math.numeric.ode.integration.impl;

import dynsim.math.numeric.ode.integration.AbstractIntegrator;
import dynsim.simulator.ode.system.OdeSystem;

/**
 * x(n+1) = x(n) + (x(n) ? x(n?1) + x'(n) * h^2
 * 
 * x(n-1) = x(n) - x'(n) * h
 * 
 * Integración de primer orden
 * 
 * @author m83
 * 
 */
public class Verlet extends AbstractIntegrator {
	private int dim;

	double[] xt;

	double[] k1;

	private OdeSystem sys;

	public Verlet(OdeSystem target) {
		this.sys = target;
		dim = sys.getIndependentVarsNum();
		k1 = new double[dim];
		xt = new double[dim];
	}

	public double timeStep(double[] x, double t, double h) throws ArithmeticException {

		k1 = sys.eval(x, t);

		for (int i = 0; i < dim; i++)
			xt[i] = x[i] - k1[i] * h; // x(n-1)

		for (int i = 0; i < dim; i++) {
			x[i] = x[i] + (x[i] - xt[i]) + k1[i] * h * h;

			checkBounds(x[i]);
		}

		t += h;

		return t;
	}

	public int getDimension() {
		return dim;
	}
}
