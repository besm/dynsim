package dynsim.simulator.iteratedmap.system.impl;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class Eshape extends AbstractIteratedMap {
	double a = 0.127219;

	double b = 0.02246817;

	public Eshape() {
		setInitialCondition("x", 0.3);
		setInitialCondition("y", 0.3);
	}

	public double[] eval(double[] x) {
		double[] r = new double[getIndependentVarsNum()];

		r[0] = -a + x[1];
		r[1] = b * x[1] / x[0] - x[0];

		return r;
	}
}
