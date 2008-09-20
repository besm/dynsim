package dynsim.simulator.iteratedmap.system.impl;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

//TODO mapa tridimensional genérico con búsqueda! (Sprott)
public class Apex extends AbstractIteratedMap {
	/*
	 * a=0.3, b=0.6, c=2, d=0.27. a=0.9, b=-0.6013, c=2, d=0.5.
	 */

	private double a = 0.9, b = -0.6013, c = 2, d = 0.5;

	public Apex() {
		setInitialCondition("x", 0.1);
		setInitialCondition("y", 0.1);
	}

	public double[] eval(double[] v) {
		double[] r = new double[getIndependentVarsNum()];

		double x = v[0];
		double y = v[1];

		r[0] = x * x - y * y + a * x + b * y;
		r[1] = 2 * x * y + c * x + d * y;

		return r;
	}
}
