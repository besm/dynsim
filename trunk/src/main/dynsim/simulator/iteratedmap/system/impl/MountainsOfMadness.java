package dynsim.simulator.iteratedmap.system.impl;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class MountainsOfMadness extends AbstractIteratedMap {

	/*
	 * set up some constants. To see a chaotic attractor, try: A=2.24, B=0.43,
	 * C=-0.65, D=-2.43, E=1.0
	 */
	double A = 2.25;

	double B = 0.43;

	double C = -0.72;

	double D = -2.86;

	double E = 1.0;

	public MountainsOfMadness() {
		setInitialCondition("x", 0.0);
		setInitialCondition("y", 0.0);
		setInitialCondition("z", 0.0);
	}

	public double[] eval(double[] v) {
		double[] r = new double[getIndependentVarsNum()];

		double x = v[0];
		double y = v[1];
		double z = v[2];

		r[0] = Math.sin(A * y) - z * Math.cos(B * x);
		r[1] = z * Math.sin(C * x) - Math.cos(D * y);
		r[2] = E * Math.sin(x);

		return r;
	}

}
