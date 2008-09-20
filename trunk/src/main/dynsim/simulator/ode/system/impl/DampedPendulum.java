/**
 * 
 */
package dynsim.simulator.ode.system.impl;

import dynsim.simulator.ode.system.AbstractOdeSystem;

/**
 * @author maf83
 * 
 */
public class DampedPendulum extends AbstractOdeSystem {
	private double gamma = 0.05;

	public DampedPendulum() {
		setInitialCondition("x", .0);
		setInitialCondition("y", 2);
		setInitialCondition("z", 2);
	}

	public DampedPendulum(double g) {
		this();
		this.gamma = g;
	}

	public DampedPendulum(double[] params) {
		this();
		setParams(params);
	}

	public double[] getParams() {
		return new double[] { gamma };
	}

	public void setParams(double[] params) {
		this.gamma = params[0];
	}

	public String[] getIndependentVarNames() {
		return new String[] { "t", "x", "y" };
	}

	public double[] eval(double[] x, double t) {
		int dim = getIndependentVarsNum();
		double[] rhs = new double[dim];
		for (int i = 0; i < dim; i++)
			rhs[i] = 0.;

		rhs[0] = 1.;
		rhs[1] = x[2];
		rhs[2] = -2 * gamma * x[2] - x[1];

		return rhs;
	}
}
