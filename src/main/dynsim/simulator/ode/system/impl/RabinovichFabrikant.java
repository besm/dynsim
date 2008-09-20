package dynsim.simulator.ode.system.impl;

import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.ode.system.AbstractOdeSystem;

public class RabinovichFabrikant extends AbstractOdeSystem {
	private fx fx;

	private fy fy;

	private fz fz;

	public RabinovichFabrikant() {
		setInitialCondition("x", -0.1);
		setInitialCondition("y", 1);
		setInitialCondition("z", 0.5);

		setParameter("alpha", 1.1);
		setParameter("gamma", 0.87);

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

			double gamma = getParameter("gamma");

			// return y*z;
			// return (y*(z-1+x*x)+.87*x);
			return y * (z - 1 + x * x) + gamma * x;
		}
	}

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			double z = vars[2];

			double gamma = getParameter("gamma");

			// return -x*z;
			// return (x*(3*z+1-x*x)+.87*y);
			return x * (3 * z + 1 - x * x) + gamma * y;
		}
	}

	class fz implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			double z = vars[2];

			double alpha = getParameter("alpha");

			// return -x*y;
			// return (-2*z*(1.1+x*y));
			return -2 * z * (alpha + x * y);
		}
	}

}
