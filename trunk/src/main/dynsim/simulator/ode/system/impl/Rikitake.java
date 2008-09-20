package dynsim.simulator.ode.system.impl;

import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.ode.system.AbstractOdeSystem;

/**
 * Oscillations of a system of disk dynamos One model which attempts to explain
 * the reversal of the Earth's magnetic Field is the Rikitake system [Rikitake].
 * This system describes the currents of two coupled dynamo disks.
 * 
 * @author m83
 * 
 */
public class Rikitake extends AbstractOdeSystem {
	// a = 2, b = 5
	// a < 2.461
	private double a = 2, b = 5;

	private RealFunction fx, fy, fz;

	public Rikitake() {
		// 0.,0.1,0.1,1.
		setInitialCondition("x", .1);
		setInitialCondition("y", .1);
		setInitialCondition("z", 1);

		// cotas
		// x{6,-6} y{4,-3} z{10, 0}

		fx = new fx();
		fy = new fy();
		fz = new fz();
		addFunction(fx);
		addFunction(fy);
		addFunction(fz);
	}

	/**
	 * @param r
	 *            Resistencia
	 * @param v1
	 *            Coeficiente rozamiento
	 */
	public Rikitake(double a, double b) {
		this();
		this.a = a;
		this.b = b;
	}

	class fx implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			double z = vars[2];

			return -a * x + y * z;
		}
	}

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			double z = vars[2];

			return -a * y + (z - b) * x;
		}
	}

	class fz implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];

			return 1 - x * y;
		}
	}

	public double[] eval(double[] x, double t) {
		double[] rhs = new double[getIndependentVarsNum()];

		/*
		 * ?x = ?µx + zy ?y = ?µy + (z ? a)x ?z = 1 ? xy
		 */

		double[] vars = new double[] { x[1], x[2], x[3] };

		rhs[0] = 1.; // tiempo
		rhs[1] = fx.eval(vars);
		rhs[2] = fy.eval(vars);
		rhs[3] = fz.eval(vars);

		return rhs;
	}

}
