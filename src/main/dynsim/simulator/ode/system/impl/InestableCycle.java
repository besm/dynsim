/**
 * 
 */
package dynsim.simulator.ode.system.impl;

import dynsim.math.util.CoordConvert;
import dynsim.simulator.ode.system.AbstractOdeSystem;

/**
 * @author maf83
 * 
 */
public class InestableCycle extends AbstractOdeSystem {

	public InestableCycle() {
		setInitialCondition("x", .01);
		setInitialCondition("y", .01);
	}

	public double[] eval(double[] x, double t) {
		double[] rhs = new double[getIndependentVarsNum()];
		for (int i = 0; i < rhs.length; i++)
			rhs[i] = 0.;

		double[] ro = CoordConvert.cartesianToPolar(x[2], x[1]);

		ro[0] = ro[0] * (ro[0] - 1) * (ro[0] - 2);
		ro[1] = 1;

		ro = CoordConvert.polarToCartesian(ro[1], ro[0]);

		rhs[0] = 1.; // tiempo
		rhs[1] = ro[0];
		rhs[2] = ro[1];

		return rhs;
	}
}
