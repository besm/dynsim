package dynsim.simulator.ode.system.impl;

import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.ode.system.AbstractOdeSystem;

/**
 * Modelo de E. Lorentz
 * 
 * Describe la convección de una capa de líquido o gas calentada por debajo.
 * 
 * x = -a(x-y); y = -xz + bx -y; z = xy -cz
 * 
 * Para la atmósfera terrestre a=10, c=8/3
 * 
 */
public class Lorentz extends AbstractOdeSystem {
	private fx fx;

	private fy fy;

	private fz fz;

	// Wolf sigma = 16, r = 45.92, b = 4
	// private double a = 10, b = 21.4, c = 8 / 3;
	// private double a = 10, b = 28, c = 8/3;

	public Lorentz() {
		// t; x: componentes de Fourier campo vel.; y, z: campo temp.
		setInitialCondition("x", 1.);
		setInitialCondition("y", 1.);
		setInitialCondition("z", 1.);

		// setParameter("a", -2.6);
		setParameter("a", 10);
		setParameter("b", 21.4); // 180.5, 28
		setParameter("c", 8 / 3);

		fx = new fx();
		fy = new fy();
		fz = new fz();
		addFunction(fx);
		addFunction(fy);
		addFunction(fz);
	}

	/**
	 * @param a
	 *            Número de Prandtl
	 * @param b
	 *            Número de Rayleigh
	 * @param c
	 *            Geometría de la región
	 */

	public double[] eval(double[] x, double t) {
		double[] rhs = new double[getIndependentVarsNum()];
		for (int i = 0; i < rhs.length; i++)
			rhs[i] = 0.;

		// dx/dt= -10x +10y
		// dy/dt= 28x -y -xz
		// dz/dt= -8/3z +xy.

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
			// double z = vars[2];
			double a = getParameter("a");
			return -a * (x - y);
		}
	}

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			double z = vars[2];
			double b = getParameter("b");
			return -x * z + b * x - y;
		}
	}

	class fz implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			double z = vars[2];
			double c = getParameter("c");
			return x * y - c * z;
		}
	}
}
