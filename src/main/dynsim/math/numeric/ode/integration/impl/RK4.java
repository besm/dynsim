package dynsim.math.numeric.ode.integration.impl;

import dynsim.math.numeric.ode.integration.AbstractIntegrator;
import dynsim.simulator.ode.system.OdeSystem;

/**
 * x(n+1) = x(n) + (h/6) * (k(n1) + 2k(n2) + 2k(n3) + k(n4))
 * 
 * k(n1)...
 * 
 * Runge Kutta de cuarto orden
 * 
 * @author m83
 * 
 */
public class RK4 extends AbstractIntegrator {
	private int dim;

	double[] xt;

	double[] k1, k2, k3, k4;

	private OdeSystem sys;

	public RK4(OdeSystem target) {
		this.sys = target;
		dim = sys.getIndependentVarsNum();
		xt = new double[dim];
		k1 = new double[dim];
		k2 = new double[dim];
		k3 = new double[dim];
		k4 = new double[dim];
	}

	public double timeStep(double[] x, double t, double h) throws ArithmeticException {
		k1 = sys.eval(x, t);
		for (int n = 0; n < dim; n++)
			xt[n] = x[n] + 0.5 * h * k1[n];
		k2 = sys.eval(xt, t + 0.5 * h);
		for (int n = 0; n < dim; n++)
			xt[n] = x[n] + 0.5 * h * k2[n];
		k3 = sys.eval(xt, t + 0.5 * h);
		for (int n = 0; n < dim; n++)
			xt[n] = x[n] + h * k3[n];
		k4 = sys.eval(xt, t + h);

		for (int i = 0; i < dim; i++) {
			x[i] = x[i] + (h / 6) * (k1[i] + 2 * k2[i] + 2 * k3[i] + k4[i]);

			checkBounds(x[i]);
		}

		t += h;

		return t;
	}

	public int getDimension() {
		return dim;
	}
}
