package dynsim.simulator.ode.system.impl;

import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.ode.system.AbstractOdeSystem;

public class Crispy extends AbstractOdeSystem {
	// private double a = 0.22, b = -3.32;
	// private double a = 9.599075, b = -3;
	// 9.5 -2.2
	// 9.5 -3
	// a = 9.6, b = -3

	// private double a = 0.22, b = -3.33;

	// a= 0.35 b = -3.35;
	// private double a = 9.99, b = -3.1100000011;
	// private double a = 0.25, b = -3.75;

	// 0.35,-3.5; 0.25,-1.85..-1.95
	// 0.25,-1.9; 1.57,-5.7->1.35,-0.15
	// 1.35,-2.35; a = 1.22, b = -3.33
	// a = 2.5, b = -2.9
	// a = 2.5, b = -3.5; a = 2.5, b = -3.85..4.5;
	// a = 2.5, b = -5; a = 2.5, b = -4.97
	// --> 4.5, b = -2.33
	// a = 2.5, b = -8.79; a = 2.83, b = -8.89
	// a = 2.83...93.., b = -9.9; a = 10.99, b = -11
	// a = 2.99, b = -9.9..10.9;a = 4.99, b = -14.9;
	// a = 9.99, b = -3.23

	// Lor like a = 2.22, b = -3.33;
	// nebula a = 0.22, b = -3.33; a = 0.22, b = -2.33
	// a = 0.22, b = -4.33..40 [4.2..4.44<- homocli
	// a = 0.22, b = -4.438210580020000239999

	public static final String PARAM_B = "b";

	public static final String PARAM_A = "a";

	private fx fx;

	private fy fy;

	private fz fz;

	public Crispy() {
		// setParameter(PARAM_A, 0.15);
		// setParameter(PARAM_B, -3.33);

		setParameter(PARAM_A, 3.5);
		setParameter(PARAM_B, -0.55);

		setInitialCondition("x", 0);
		setInitialCondition("y", 0);
		setInitialCondition("z", 0);

		fx = new fx();
		fy = new fy();
		fz = new fz();
		addFunction(fx);
		addFunction(fy);
		addFunction(fz);
	}

	// NOTA m�s lento que Math.sin
	// /*
	// * sin x = x - x3/3! + x5/5! - x7/7! + . . .
	// * cos x = 1 - x2/2! + x4/4! -
	// * x6/6! + . . .
	// */
	//
	// public double cos(double x) {
	// // convert x to an angle between -2 PI and 2 PI
	// x = x % (2 * Math.PI);
	//
	// // compute the Taylor series approximation
	// double term = 1.0; // ith term = x^i / i!
	// double sum = 0.0; // sum of first i terms in taylor series
	//
	// for (int i = 1; term != 0.0; i++) {
	// term *= (x / i);
	// if (i % 4 == 1)
	// sum += term;
	// if (i % 4 == 2)
	// sum -= term;
	// }
	//
	// return sum;
	// }
	//	
	// public double sin(double x) {
	// // convert x to an angle between -2 PI and 2 PI
	// x = x % (2 * Math.PI);
	//
	// // compute the Taylor series approximation
	// double term = 1.0; // ith term = x^i / i!
	// double sum = 0.0; // sum of first i terms in taylor series
	//
	// for (int i = 1; term != 0.0; i++) {
	// term *= (x / i);
	// if (i % 4 == 1)
	// sum += term;
	// if (i % 4 == 3)
	// sum -= term;
	// }
	//
	// return sum;
	// }

	public double[] eval(double[] x, double t) {
		int dim = getIndependentVarsNum();

		double[] rhs = new double[dim];

		double[] p = new double[] { x[1], x[2], x[3] };

		rhs[0] = 1.; // tiempo
		rhs[1] = fx.eval(p);
		rhs[2] = fy.eval(p);
		rhs[3] = fz.eval(p);

		return rhs;
	}

	class fx implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			double z = vars[2];

			return Math.cos(z) + getParameter(PARAM_A) * (y - x);
		}
	}

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double z = vars[2];

			return (getParameter(PARAM_A) - z) * Math.sin(z) + getParameter(PARAM_B) * x;
		}
	}

	class fz implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			return getParameter(PARAM_A) + (2 * x - y);
		}
	}
}
