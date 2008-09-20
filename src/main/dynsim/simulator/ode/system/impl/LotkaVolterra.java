package dynsim.simulator.ode.system.impl;

import dynsim.simulator.ode.system.AbstractOdeSystem;

/*
 * 
 * It has two variables (P, H) and several parameters:
 H = density of prey
 P = density of predators
 r = intrinsic rate of prey population increase
 a = predation rate coefficient
 b = reproduction rate of predators per 1 prey eaten
 m = predator mortality rate
 */
public class LotkaVolterra extends AbstractOdeSystem {
	// r = 5, a = 10, b = 2, m = 10
	// r = 5, a = 10, b = 2, m = 3
	private double r = 5, a = 10, b = 2, m = 3;

	public LotkaVolterra() {
		// critical point at 0.6667, 0.5
		// private double[] initConds = { 0., 0.6, 0.5 };
		setInitialCondition("x", .1);
		setInitialCondition("y", .1);
	}

	public double[] eval(double[] x, double t) {
		double[] rhs = new double[getIndependentVarsNum()];
		for (int i = 0; i < rhs.length; i++)
			rhs[i] = 0.;

		rhs[0] = 1.; // tiempo
		rhs[1] = x[1] * (r - a * x[2]);
		rhs[2] = -x[2] * (b - m * x[1]);

		return rhs;
	}
}