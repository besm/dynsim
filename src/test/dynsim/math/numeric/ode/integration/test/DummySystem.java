package dynsim.math.numeric.ode.integration.test;

import dynsim.simulator.ode.system.AbstractOdeSystem;

public class DummySystem extends AbstractOdeSystem {
	private int dim = 2;

	public DummySystem() {
	}

	public int getIndependentVarsNum() {
		return dim;
	}

	public double[] eval(double[] x, double t) {
		double[] rhs = new double[dim];
		for (int i = 0; i < dim; i++)
			rhs[i] = 0.;

		rhs[0] = 1.; // tiempo
		rhs[1] = 1 - t + 4 * x[1];

		return rhs;
	}

	public double[] getParams() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setParams(double[] params) {
		// TODO Auto-generated method stub

	}

	public double[] getInitialValues() {
		return new double[] { 0., 1. };
	}

	public void setInitialConditions(double[] conds) {
		// TODO Auto-generated method stub

	}

}
