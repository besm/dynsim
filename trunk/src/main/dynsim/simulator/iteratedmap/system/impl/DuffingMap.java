package dynsim.simulator.iteratedmap.system.impl;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class DuffingMap extends AbstractIteratedMap {

	private double a = 2.75;

	private double b = 0.15; // .2

	public DuffingMap() {
		setInitialCondition("x", 0.1);
		setInitialCondition("y", 0.1);
	}

	public double[] eval(double[] x) {
		double[] r = new double[getIndependentVarsNum()];

		r[0] = x[1];
		r[1] = -b * x[0] + a * x[1] - (x[1] * x[1] * x[1]);

		return r;
	}
}
