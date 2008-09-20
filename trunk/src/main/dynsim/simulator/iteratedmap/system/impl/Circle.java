package dynsim.simulator.iteratedmap.system.impl;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class Circle extends AbstractIteratedMap {
	double a = 0.999776;
	double b = 0.1;

	public Circle() {
		setInitialCondition("x", 0.1);
		setInitialCondition("y", 0.1);
	}

	public double[] eval(double[] x) {
		double[] r = new double[getIndependentVarsNum()];

		r[0] = -a * x[1];
		r[1] = x[0] + (b * x[1]);

		return r;
	}
}
