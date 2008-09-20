package dynsim.simulator.ode.system.impl;

import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.ode.system.AbstractOdeSystem;

public class WeirdTest extends AbstractOdeSystem {
	private RealFunction fx, fy, fz;

	public WeirdTest() {
		// (0.5,0,-0.5235988)
		setInitialCondition("x", 1.5);
		setInitialCondition("y", 1.5);
		setInitialCondition("z", 1.);

		fx = new fx();
		fy = new fy();
		fz = new fz();

		addFunction(fx);
		addFunction(fy);
		addFunction(fz);
	}

	public double[] eval(double[] x, double t) {
		double[] rhs = new double[getIndependentVarsNum()];
		double[] p = new double[] { x[1], x[2], x[3] };

		rhs[0] = 1.;
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
			return 3 * x - Math.cos(y * z) - 0.5;

		}
	}

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			double z = vars[2];
			return (x * x) - 81 * (y + 0.1) * (y + 0.1) + Math.sin(z) + 1.06;
		}
	}

	class fz implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			double z = vars[2];
			return Math.exp(-x * y) + 20 * z + 0.333 * (10 * Math.PI - 3);
		}
	}
}
