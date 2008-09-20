package dynsim.simulator.iteratedmap.system.impl;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class ZaslavskiiMap extends AbstractIteratedMap {
	private double a, b, c, d;

	public ZaslavskiiMap() {
		setInitialCondition("x", 0.1);
		setInitialCondition("y", 0.1);

		// d = 12.6695, a = 400, c = 3;
		d = 2.6695;

		setParameter("a", 2.5);
		setParameter("b", (1 - Math.exp(-d)) / d);
		setParameter("c", -1.75);
		setParameter("d", d);
	}

	public double[] eval(double[] x) {
		double[] r = new double[getIndependentVarsNum()];
		a = params.getValue("a");
		b = params.getValue("b");
		c = params.getValue("c");
		d = params.getValue("d");

		r[0] = (x[0] + a * (1 + b * x[1]) + c * a * b * Math.cos(2 * Math.PI * x[0])) % 1;
		r[1] = Math.exp(-d) * (x[1] + c * Math.cos(2 * Math.PI * x[0]));

		return r;
	}
}