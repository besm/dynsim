/**
 * 
 */
package dynsim.simulator.ode.system.impl;

import dynsim.simulator.ode.system.AbstractOdeSystem;

/**
 * @author maf83
 * 
 */
public class StableCycle extends AbstractOdeSystem {

	private int dim;

	public StableCycle() {
		setInitialCondition("x", .1);
		setInitialCondition("y", .1);
		dim = getIndependentVarsNum();
	}

	public double[] eval(double[] x, double t) {
		double[] rhs = new double[dim];
		for (int i = 0; i < dim; i++)
			rhs[i] = 0.;

		double tmp = (1 - (x[1] * x[1]) - (x[2] * x[2]));

		rhs[0] = 1.; // tiempo
		rhs[1] = x[2] + x[1] * tmp;
		rhs[2] = -x[1] + x[2] * tmp;

		return rhs;
	}
}
