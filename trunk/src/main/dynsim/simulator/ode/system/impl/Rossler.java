package dynsim.simulator.ode.system.impl;

import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.ode.system.AbstractOdeSystem;

/**
 * Rössler In 1976 Otto Rössler found the inspiration from a Taffy-pulling
 * machine for his Non-linear three-dimensional deterministic dynamical system.
 * 
 * exhibits a strange attractor for a=b=0.2 and c=5.7.
 */
public class Rossler extends AbstractOdeSystem {
	// a = 0.15; b = 0.2; c = 10.0
	// a = 0.15, b = 0.35, c = 9.8;
	// private double a = 0.2, b = 0.2, c = 5.7;
	// private double a = 0.2, b = 0.2, c = 9.8;

	private RealFunction fx, fy, fz;

	public Rossler() {
		setInitialCondition("x", 0.);
		setInitialCondition("y", 0.);
		setInitialCondition("z", 0.);

		setParameter("a", 0.2);
		setParameter("b", 0.2);
		setParameter("c", 5.7);

		fx = new fx();
		fy = new fy();
		fz = new fz();
		addFunction(fx);
		addFunction(fy);
		addFunction(fz);
	}

	public double[] eval(double[] x, double t) {
		double[] rhs = new double[x.length];
		double[] vars = new double[] { x[1], x[2], x[3] };

		rhs[0] = 1.;
		rhs[1] = fx.eval(vars);
		rhs[2] = fy.eval(vars);
		rhs[3] = fz.eval(vars);
		return rhs;
	}

	class fx implements RealFunction {
		public double eval(double[] vars) {
			// double x = vars[0];
			double y = vars[1];
			double z = vars[2];
			return -y - z;
		}
	}

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			// double z = vars[2];

			double a = getParameter("a");

			return x + a * y;
		}
	}

	class fz implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			// double y = vars[1];
			double z = vars[2];

			double b = getParameter("b");
			double c = getParameter("c");

			return b + z * (x - c);
		}
	}
}
