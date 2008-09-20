package dynsim.simulator.iteratedmap.system.impl;

import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class Henon extends AbstractIteratedMap {
	private RealFunction fx, fy;

	public Henon() {
		setInitialCondition("x", 0.);
		setInitialCondition("y", 0.);
		setParameter("a", 1.4);
		setParameter("b", 0.3);

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
			double x = vars[0];
			double y = vars[1];
			double a = getParameter("a");
			double b = getParameter("b");

			return 1 - a * x * x + b * y;
		}
	}

	/*
	 * x+1 = 1-ax^2+by y+1 = x
	 * 
	 * 1-ax^2+by=x x-1+ax^2 = by x-1../b=y
	 * 
	 * x=0 0-1+0/0.3=-3.3333p -3.3=-1/0.3
	 * 
	 * b=param biff
	 */

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];

			return x;
		}
	}
}
