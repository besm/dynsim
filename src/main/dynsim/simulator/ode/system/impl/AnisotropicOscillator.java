package dynsim.simulator.ode.system.impl;

import dynsim.simulator.ode.system.AbstractOdeSystem;

public class AnisotropicOscillator extends AbstractOdeSystem {
	private double w1 = 1, w2 = 3.5;
	double[] rhs;

	public AnisotropicOscillator() {
		setInitialCondition("x", 1.7);
		setInitialCondition("xx", 0);
		setInitialCondition("yy", 0.5);
		setInitialCondition("y", 0);
		// TODO cuando se arregle el tema de par‡metros
		// la ^2 que sea autom‡tica
		w1 *= w1;
		w2 *= w2;

		rhs = new double[getIndependentVarsNum()];
		rhs[0] = 1.;
	}

	public double[] eval(double[] x, double t) {
		// TODO esto ser‡ Callback en super
		rhs[1] = x[2];
		rhs[2] = -w1 * x[1];
		rhs[3] = x[4];
		rhs[4] = -w2 * x[3];
		// esto ser‡ el return en superclase
		return (double[]) rhs.clone();
	}
}
