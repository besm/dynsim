package dynsim.simulator.iteratedmap.system.impl;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class Spyder extends AbstractIteratedMap {
	private double a, b;

	public Spyder() {
		setInitialCondition("x", 0.1);
		setInitialCondition("y", 0.1);

		// a= 1.105 b = .75
		setParameter("a", 1.35);
		setParameter("b", 0.75);
	}

	public double[] eval(double[] x) {
		double[] r = new double[getIndependentVarsNum()];
		a = params.getValue("a");
		b = params.getValue("b");

		r[0] = -a * (x[0] * x[0]) + x[1];
		r[1] = b * (x[1] * x[1]) - x[0];

		return r;
	}
}