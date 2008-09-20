package dynsim.simulator.ode.system.impl;

import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.ode.system.AbstractOdeSystem;

public class ChenUeta extends AbstractOdeSystem {
	// private double a = 35, b = 3, c = 28;
	private double a = 35, b = 3, c = 28;

	private RealFunction fx, fy, fz;

	public ChenUeta() {
		setInitialCondition("x", .0);
		setInitialCondition("y", .1);
		setInitialCondition("z", .0);

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

		// a*(y-x)
		// (c-a)*x-x*z+c*y
		// x*y-b*z

		double[] vars = new double[] { x[1], x[2], x[3] };

		// dxt,dxy,dxz
		rhs[0] = 1.; // tiempo
		rhs[1] = fx.eval(vars);
		rhs[2] = fy.eval(vars);
		rhs[3] = fz.eval(vars);

		return rhs;
	}

	class fx implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			return a * (y - x);
		}
	}

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			double z = vars[2];

			return (c - a) * x - x * z + c * y;
		}
	}

	class fz implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			double z = vars[2];

			return x * y - b * z;
		}
	}

}
