package dynsim.simulator.ode.system.impl;

import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.ode.system.AbstractOdeSystem;

public class SimpleTest extends AbstractOdeSystem {
	private RealFunction fx, fy;

	public SimpleTest() {
		// (0,0) (2,2) (4.0)
		setInitialCondition("x", .1);
		setInitialCondition("y", .1);

		fx = new fx();
		fy = new fy();
		addFunction(fx);
		addFunction(fy);
	}

	public double[] eval(double[] x, double t) {
		int dim = getIndependentVarsNum();
		double[] rhs = new double[dim];

		double[] p = new double[] { x[1], x[2] };

		rhs[1] = fx.eval(p);
		rhs[2] = fy.eval(p);

		return rhs;
	}

	class fx implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			return x * (4 - x - y);
		}
	}

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			return y * (x - 2);
		}
	}
}
