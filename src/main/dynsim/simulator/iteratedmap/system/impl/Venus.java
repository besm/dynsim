package dynsim.simulator.iteratedmap.system.impl;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class Venus extends AbstractIteratedMap {

	private double a = -0.00002;

	private double b = -0.240934;

	private double c = 0.096143;

	public Venus() {
		setInitialCondition("x", .1);
		setInitialCondition("y", .1);
	}

	public double[] eval(double[] x) {
		double[] r = new double[getIndependentVarsNum()];

		r[0] = x[1] + a * x[1] * x[1];
		r[1] = b * x[1] * x[1] - x[0] + c;

		return r;
	}
}
