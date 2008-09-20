package dynsim.simulator.iteratedmap.system.impl;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class Ikemod extends AbstractIteratedMap {
	// 0.81_0.92
	// -0.93_-1.3355
	private double a = 0.9; // -1.2

	public Ikemod() {
		setInitialCondition("x", 0.1);
		setInitialCondition("y", 0.1);
	}

	public double[] eval(double[] x) {
		double[] r = new double[getIndependentVarsNum()];

		double t = 0.4 - 6 / (1 + (x[0] * x[0]) + (x[1] * x[1]));

		r[0] = 1 + a * (x[0] * Math.cos(t) - x[1] * Math.cos(t));
		r[1] = a * (x[0] * Math.sin(t) + x[1] * Math.sin(t));

		return r;
	}
}
