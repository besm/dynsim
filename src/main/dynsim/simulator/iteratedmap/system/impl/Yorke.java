package dynsim.simulator.iteratedmap.system.impl;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class Yorke extends AbstractIteratedMap {

	private double a = 3.;

	private double b = 0.25;

	public Yorke() {
		setInitialCondition("x", .1);
		setInitialCondition("y", .1);
	}

	public double[] eval(double[] x) {
		double[] r = new double[getIndependentVarsNum()];

		r[0] = (a * x[0]) % a;
		r[1] = b * x[1] + Math.cos(2 * Math.PI * x[0]);

		return r;
	}
}
