package dynsim.simulator.iteratedmap.system.impl;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class Rush extends AbstractIteratedMap {
	// double a = 1.07; .. 1.245
	double a = 1.23;

	public Rush() {
		setInitialCondition("x", .1);
		setInitialCondition("y", .1);
	}

	public double[] eval(double[] x) {
		double[] r = new double[getIndependentVarsNum()];

		r[0] = a * (x[0] - x[1] * Math.sin(x[0]));
		r[1] = x[1] - x[0] * Math.cos(x[1]);

		return r;
	}
}
