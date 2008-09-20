/**
 * 
 */
package dynsim.simulator.ode.system.impl;

import dynsim.simulator.ode.system.AbstractOdeSystem;

/**
 * @author maf83
 * 
 */
public class Lefever extends AbstractOdeSystem {
	private double a = 1, b = 2;

	public Lefever() {
		setInitialCondition("x", .1);
		setInitialCondition("y", .1);
	}

	public double[] eval(double[] x, double t) {
		double[] rhs = new double[getIndependentVarsNum()];
		for (int i = 0; i < rhs.length; i++)
			rhs[i] = 0.;

		rhs[0] = 1.;
		rhs[1] = a - (b + 1) * x[1] + (x[1] * x[1]) * x[2];
		rhs[2] = b * x[1] - (x[1] * x[1]) * x[2];

		return rhs;
	}
}
