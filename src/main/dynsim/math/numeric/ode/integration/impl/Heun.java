package dynsim.math.numeric.ode.integration.impl;

import dynsim.math.numeric.ode.integration.AbstractIntegrator;
import dynsim.simulator.ode.system.OdeSystem;

/**
 * Fórmula mejorada de Euler o fórmula de Heun
 * 
 * x(n+1) = x(n) + (x'(n)+ f(t(n)+h, x(n)+h*x'(n)))/2*h
 * 
 * Runge Kutta de segundo orden
 * 
 * @author m83
 * 
 */
public class Heun extends AbstractIntegrator {
	private int dim;

	double[] k1, k2;

	double[] xt;

	private OdeSystem sys;

	public Heun(OdeSystem target) {
		this.sys = target;
		dim = sys.getIndependentVarsNum();
		k1 = new double[dim];
		k2 = new double[dim];
		xt = new double[dim];
	}

	public double timeStep(double[] x, double t, double h) throws ArithmeticException {

		k1 = sys.eval(x, t);

		for (int n = 0; n < dim; n++)
			xt[n] = x[n] + h * k1[n];

		k2 = sys.eval(xt, t + h);

		for (int i = 0; i < dim; i++) {
			x[i] = x[i] + (k1[i] + k2[i]) / 2 * h;

			checkBounds(x[i]);
		}

		t += h;

		return t;
	}

	public int getDimension() {
		return dim;
	}
}
