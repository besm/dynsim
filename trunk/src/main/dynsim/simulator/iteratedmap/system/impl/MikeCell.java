package dynsim.simulator.iteratedmap.system.impl;

import dynsim.math.numeric.function.RealFunction;
import dynsim.math.util.MathUtils;
import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;

public class MikeCell extends AbstractIteratedMap {
	// a=3.5;b=0.3;c=1.5;
	double a = 2.5;

	double b = 0.43;

	double c = 5.5;

	private RealFunction fx, fy;

	public MikeCell() {
		setInitialCondition("x", 0);
		setInitialCondition("y", 0);

		fx = new fx();
		fy = new fy();

		addFunction(fx);
		addFunction(fy);
	}

	// xnew=y-SQRT(ABS(b*x-c))*SIGN(x)
	// ynew=a-x

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

			return y - Math.sqrt(Math.abs(b * x - c)) * MathUtils.sgn(x);
		}
	}

	class fy implements RealFunction {
		public double eval(double[] vars) {
			double x = vars[0];

			return a - x;
		}
	}
}
