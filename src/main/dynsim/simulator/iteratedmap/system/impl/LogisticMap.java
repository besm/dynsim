package dynsim.simulator.iteratedmap.system.impl;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class LogisticMap extends AbstractIteratedMap {
	public LogisticMap() {
		setInitialCondition("x", 0.5);
		setParameter("a", 0.72);
	}

	public double[] eval(double[] x) {
		double[] r = new double[getIndependentVarsNum()];
		double a = getParameter("a");

		r[0] = (x[0] * x[0]) + a;

		return r;
	}
}
