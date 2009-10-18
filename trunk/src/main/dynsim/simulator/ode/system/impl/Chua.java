package dynsim.simulator.ode.system.impl;

import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.ode.system.AbstractOdeSystem;

public class Chua extends AbstractOdeSystem {
	// critical gamma = 60.81
	// 23
	// private double alpha = 15.6, beta = 1, gamma = 23.5181;
	// private double alpha = 15.6, beta = 1, gamma = 31.15;
	// gamma 31...34

	// 23.4-5
	// private double alpha = 15.6, beta = 1, gamma = 28;
	// private double alpha = 15.6, beta = 1, gamma = 27.5;

	private double a = -1.143, b = -0.714;

	private fx fx;

	private fy fy;

	private fz fz;

	public Chua() {
		setInitialCondition("x", 1);
		setInitialCondition("y", 0.2);
		setInitialCondition("z", 0);

		setParameter("a", 15.6);
		setParameter("b", 1);
		setParameter("c", 31.15);

		fx = new fx();
		fy = new fy();
		fz = new fz();
		addFunction(fx);
		addFunction(fy);
		addFunction(fz);
	}

	public double[] eval(double[] x, double t) {
		int dim = getIndependentVarsNum();

		double[] rhs = new double[dim];

		// rhs[0] = 1.; // tiempo
		// rhs[1] = alpha * (x[2] - x[1] - f(x[1]));
		// rhs[2] = beta * (x[1] - x[2] + x[3]);
		// rhs[3] = -gamma * x[2];

		double[] p = new double[] { x[1], x[2], x[3] };

		rhs[0] = 1.; // tiempo
		rhs[1] = fx.eval(p);
		rhs[2] = fy.eval(p);
		rhs[3] = fz.eval(p);

		return rhs;
	}

	private double f(double x) {
		return b * x + ((a - b) / 2) * (Math.abs(x + 1) - Math.abs(x - 1));
	}

	class fx implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			// double z = vars[2];
			double alpha = getParameter("a");

			return alpha * (y - x - f(x));
		}
	}

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			double z = vars[2];
			double beta = getParameter("b");

			return beta * (x - y + z);
		}
	}

	class fz implements RealFunction {
		public double eval(double[] vars) {
			// double x = vars[0];
			double y = vars[1];
			// double z = vars[2];
			double gamma = getParameter("c");

			return -gamma * y;
		}
	}

}
