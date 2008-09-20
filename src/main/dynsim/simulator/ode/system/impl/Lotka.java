/**
 * 
 */
package dynsim.simulator.ode.system.impl;

import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.ode.system.AbstractOdeSystem;

/**
 * @author maf83
 * 
 */
public class Lotka extends AbstractOdeSystem {
	private double k0 = 0.05, k1 = 0.07, k2 = 0.1;

	private RealFunction fx, fy;

	public Lotka() {
		setInitialCondition("x", 1);
		setInitialCondition("y", 2);

		fx = new fx();
		fy = new fy();

		addFunction(fx);
		addFunction(fy);
	}

	public double[] eval(double[] x, double t) {
		double[] rhs = new double[x.length];
		double[] vars = new double[] { x[1], x[2] };

		rhs[0] = 1.; // tiempo
		rhs[1] = fx.eval(vars);
		rhs[2] = fy.eval(vars);

		return rhs;
	}

	class fx implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];

			return k0 - k1 * x * y;
		}
	}

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];

			return k1 * x * y - k2 * y;
		}
	}
}
