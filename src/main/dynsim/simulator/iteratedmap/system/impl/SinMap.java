package dynsim.simulator.iteratedmap.system.impl;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class SinMap extends AbstractIteratedMap {
	public SinMap() {
		setInitialCondition("x", 0.1);
		setParameter("a", 1.5);
	}

	public double[] eval(double[] x) {
		double[] r = new double[getIndependentVarsNum()];
		double a = getParameter("a");
		// fn(x)=sin cx
		r[0] = Math.sin(a * x[0]);

		return r;
	}
}
