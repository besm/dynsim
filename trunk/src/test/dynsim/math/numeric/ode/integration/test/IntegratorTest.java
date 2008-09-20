package dynsim.math.numeric.ode.integration.test;

import java.math.BigDecimal;

import dynsim.math.numeric.ode.integration.Integrator;
import dynsim.math.numeric.ode.integration.impl.Euler;
import dynsim.math.numeric.ode.integration.impl.Heun;
import dynsim.math.numeric.ode.integration.impl.RK4;
import dynsim.math.numeric.ode.integration.impl.RK5;
import dynsim.math.numeric.ode.integration.impl.Verlet;
import dynsim.simulator.ode.system.OdeSystem;

public class IntegratorTest {

	OdeSystem sys;

	double[] x;

	double[][] exactRes;

	int steps = 40;

	int exactSteps = 10;

	double t;

	double h = 0.025;

	public IntegratorTest() {
		sys = new DummySystem();
		exactRes = new double[sys.getIndependentVarsNum()][exactSteps];
		setExactRes();
	}

	public void test() {
		test(new Euler(sys));
		test(new Verlet(sys));
		test(new Heun(sys));
		test(new RK4(sys));
		test(new RK5(sys));
	}

	public void test(Integrator intr) {
		t = 0;
		x = sys.getInitialValues();
		System.out.println("** Testing " + intr.getClass() + " **");
		System.out.println("h=" + h);
		double[][] r = compute(intr);
		errorPct(r);
		print(r);

	}

	public static void main(String[] args) {
		IntegratorTest test = new IntegratorTest();
		test.test();
	}

	private void errorPct(double[][] a) {
		double diff, err;
		for (int i = 0; i < exactSteps; i++) {
			diff = Math.abs(1 - (exactRes[1][i] / a[1][i]));
			err = trunc(diff * 100, 0);
			System.out.println("err=" + err + "%");
		}

	}

	private double trunc(double d, int s) {
		BigDecimal t = new BigDecimal(d);
		t = t.setScale(s, BigDecimal.ROUND_HALF_UP);
		return t.doubleValue();
	}

	private void print(double[][] a) {
		for (int i = 0; i < exactSteps; i++)
			System.out.println("time=" + a[0][i] + " value=" + a[1][i]);
	}

	private double[][] compute(Integrator integra) {
		double[][] res = new double[x.length][exactSteps];
		int ratio = steps / exactSteps;
		int r = ratio;
		int c = 0;

		for (int i = 0; i < steps; i++) {
			r--;

			integra.timeStep(x, t, h);

			if (r == 0) {
				for (int n = 0; n < x.length; n++)
					res[n][c] = trunc(x[n], 7);

				r = ratio;
				c++;
			}

			t += h;
		}

		return res;
	}

	private void setExactRes() {
		// Para y(0) = 1.
		// f(t) = 1/4*t-3/16+19/16*exp(4*t)
		for (int i = 0; i < exactSteps; i++)
			exactRes[0][i] = i * 0.1;

		exactRes[1][0] = 1.6090418;
		exactRes[1][1] = 2.5053299;
		exactRes[1][2] = 3.8301388;
		exactRes[1][3] = 5.7942260;
		exactRes[1][4] = 8.7120041;
		exactRes[1][5] = 13.052522;
		exactRes[1][6] = 19.515518;
		exactRes[1][7] = 29.144880;
		exactRes[1][8] = 43.497903;
		exactRes[1][9] = 64.897803;
	}

}
