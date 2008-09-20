package dynsim.simulator.ode.system.impl;

import java.util.Random;

import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.ode.system.AbstractOdeSystem;

public class Ueda extends AbstractOdeSystem {
	private RealFunction fx, fy;

	Random rand;

	public Ueda() {
		rand = new Random();
		setInitialCondition("x", 10.5);
		setInitialCondition("y", 10.5);
		// setInitialCondition("z", 10.5);

		// a = 12 chaos
		setParameter("a", 14);
		setParameter("b", 0.1);

		fx = new fx();
		fy = new fy();
		// fz = new fz();
		addFunction(fx);
		addFunction(fy);
		// addFunction(fz);
	}

	public double[] eval(double[] x, double t) {
		double[] rhs = new double[x.length];
		double[] vars = new double[] { x[1], x[2] };

		rhs[0] = 1.;
		rhs[1] = fx.eval(vars);
		rhs[2] = fy.eval(vars);
		// rhs[3] = fz.eval(vars);
		return rhs;
	}

	class fx implements RealFunction {
		public double eval(double[] vars) {
			// double x = vars[0];
			double y = vars[1];
			// double z = vars[2];
			return y;
		}
	}

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];

			double a = getParameter("a");
			double b = getParameter("b");

			return (-b * y) + (-x * -x * -x) + a * Math.sin(x * Math.PI);
		}
	}

	class fz implements RealFunction {
		public double eval(double[] vars) {
			return 1;
		}
	}
}
