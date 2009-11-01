package dynsim.simulator.ode.system.impl;

import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.ode.system.AbstractOdeSystem;

/*
 * d2x/dt2 - b (1 - x2) dx/dt + x = a cos(ct) 
 In autonomous form with X = x, Y = dx/dt, Z =c t: 

 dX/dt	=	Y	
 dY/dt	=	b(1 - X2)Y - X + a cos(Z)	
 dZ/dt	=	c

 The parameters of the applet are a the strength of 
 the driving, b the coefficient of the negative linear damping,
 and c the frequency of the external driving.
 */

public class VanDerPol extends AbstractOdeSystem {
	// a = 0.32, b = 0.2, g = 1.15
	// a = 23.1, b = 0.5, g = 9.15
	// private double a = 0.32, b = 0.2, g = 1.15;

	private RealFunction fx, fy;

	public VanDerPol() {
		setInitialCondition("x", .0);
		setInitialCondition("y", .0);

		setParameter("a", 0.32);
		setParameter("b", 0.2);
		setParameter("g", 1.15);
		// setInitialCondition("z", .14);
		fx = new fx();
		fy = new fy();
		// fz = new fz();

		addFunction(fx);
		addFunction(fy);
		// addFunction(fz);
	}

	public double[] eval(double[] x, double t) {
		int dim = getIndependentVarsNum();
		double[] rhs = new double[dim];

		double[] vars = new double[] { x[1], x[2] };

		// dxt,dxy
		rhs[0] = 1.; // tiempo
		rhs[1] = fx.eval(vars);
		rhs[2] = fy.eval(vars);
		// rhs[3] = fz.eval(vars);

		return rhs;
	}

	class fx implements RealFunction {
		public double eval(double[] vars) {
			double y = vars[1];
			return y;
		}
	}

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];
			// double z = vars[2];
			double a = getParameter("a");
			double b = getParameter("b");

			return b * (1 - x * x) * y - x + a * Math.cos(y * Math.PI);
		}
	}

	class fz implements RealFunction {
		public double eval(double[] vars) {
			return getParameter("g");
		}
	}
}
