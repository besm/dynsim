package dynsim.simulator.iteratedmap.system.impl;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class Shell extends AbstractIteratedMap {
	// double a = 0.357057;
	double a = 0.38;

	public Shell() {
		setInitialCondition("x", .3);
		setInitialCondition("y", .3);
	}

	public double[] eval(double[] x) {
		double[] r = new double[getIndependentVarsNum()];

		r[0] = x[0] * x[1] + a * x[0] - x[1];
		r[1] = x[0] + x[1];

		return r;
	}
}
