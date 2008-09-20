package dynsim.math.numeric.differentiation;

import dynsim.math.numeric.function.RealFunction;

/**
 * @author maf83
 * 
 */
public class FiniteDiffs {
	public double compute(RealFunction f, double[] x, int var, double h) {
		double[] dxp = x.clone();
		dxp[var] = x[var] + h;
		double[] dxm = x.clone();
		dxm[var] = x[var] - h;

		return (f.eval(dxp) - f.eval(dxm)) / (2 * h);
	}
}
