package dynsim.simulator.iteratedmap.system.impl;

import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class Insect extends AbstractIteratedMap {

	private RealFunction fx, fy;

	// private double a = 1.09;

	public Insect() {
		setInitialCondition("x", .1);
		setInitialCondition("y", .0);
		setParameter("a", 1); // 1
		// -1..1 flip stability
		// -.91 insect born! -0.9109
		// setParameter("b", -0.937); // -1
		setParameter("b", -1);

		fx = new fx();
		fy = new fy();

		addFunction(fx);
		addFunction(fy);
	}

	public double[] eval(double[] x) {
		double[] r = new double[getIndependentVarsNum()];
		double[] vars = new double[] { x[0], x[1] };

		r[0] = fx.eval(vars);
		r[1] = fy.eval(vars);

		return r;
	}

	class fx implements RealFunction {
		public double eval(double[] vars) {
			double y = vars[1];

			return -y;
		}
	}

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];
			double y = vars[1];

			double a = getParameter("a");
			double b = getParameter("b");

			// (x * x * x - x - y) / a
			return (x * x * x - x + y * b) / a;
		}
	}
	/*
	 * x+1 = -y y+1 = x^3-x-y
	 * 
	 * y = x^3-x-y 2y=x^3-x y=x^3-x/2 y+1=x^3-x-y/a
	 * 
	 * (-1,0) y=1-1/2=0
	 * 
	 * (-5,70) y=-125-5/2=-70
	 * 
	 * (-16,0) 88,0;90,0;91...
	 * 
	 * infinitos puntos críticos
	 * 
	 * a=param biff
	 */
}
