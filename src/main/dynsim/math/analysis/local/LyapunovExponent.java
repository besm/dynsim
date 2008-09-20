package dynsim.math.analysis.local;

import dynsim.math.numeric.ode.integration.Integrator;
import dynsim.math.numeric.ode.integration.impl.RK4;
import dynsim.math.vector.VectorN;
import dynsim.math.vector.VectorOps;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.system.DynamicalSystem;

public class LyapunovExponent {
	public static final double DEF_EPSILON = 1E-8;

	public static final double DEF_STEP_LEN = 0.015;

	public static final int DEF_RENORM_TIME = 6;

	private double stepLength;

	private int renormalizationTime;

	private int numExps;

	private VectorN x, exps, sums;

	private double eps;

	private int n;

	private VectorN[] u, v, perts;

	private boolean isIntegrator;

	private Integrator intg;

	private DynamicalSystem sys;

	private double time;

	private double t = 0;

	public LyapunovExponent(DynamicalSystem sys) {
		this(sys, DEF_EPSILON, DEF_RENORM_TIME);
	}

	public LyapunovExponent(DynamicalSystem sys, double eps, int renormTime) {
		double[] x = sys.getInitialValues();
		this.isIntegrator = false;
		this.sys = sys;
		this.eps = eps;
		this.x = new VectorN(x);
		this.numExps = x.length;
		this.n = x.length;
		this.renormalizationTime = renormTime;
		init();
	}

	public LyapunovExponent(OdeSystem intg) {
		this(intg, DEF_STEP_LEN, DEF_EPSILON, DEF_RENORM_TIME);
	}

	public LyapunovExponent(OdeSystem sys, double stepLength, double eps, int renormTime) {
		isIntegrator = true;
		double[] x = sys.getInitialValues();
		x = unwrapTime(x);
		this.intg = new RK4(sys);
		this.eps = eps;
		this.x = new VectorN(x);
		this.numExps = x.length;
		this.n = x.length;
		this.renormalizationTime = renormTime;
		this.stepLength = stepLength;
		init();
	}

	public VectorN compute(int iters) {
		for (int m = 0; m < iters; m += renormalizationTime) {
			// compute perturbed state vectors
			for (int i = 0; i < numExps; i++) {
				perts[i] = VectorOps.add(x, VectorOps.multiply(u[i], eps));
			}

			// iterate state and perturbed states
			for (int k = 0; k < renormalizationTime; k++) {
				if (isIntegrator) {
					integrate(x);
				} else {
					x.set(sys.eval(x.getArray()));
				}
				for (int i = 0; i < numExps; i++) {
					if (isIntegrator) {
						integrate(perts[i]);
					} else {
						perts[i].set(sys.eval(perts[i].getArray()));
					}
				}
			}

			// compute perturbation result
			for (int i = 0; i < numExps; i++) {
				perts[i] = VectorOps.sub(x, perts[i]);
			}

			// compute Gram-Schmidt reorthonormaliztion of perturbations
			GSR(perts);
			t += renormalizationTime;

			// update sums determining Lyapunov exponents
			for (int i = 0; i < numExps; i++) {
				sums.set(i, sums.get(i) + Math.log(v[i].norm() / eps));
				if (isIntegrator) {
					exps.set(i, sums.get(i) / (t * stepLength));
				} else {
					exps.set(i, sums.get(i) / t);
				}
			}
		}

		return x;
	}

	public double[] getLyapunovExponents() {
		return exps.getArrayCopy();
	}

	public double getKaplanYorkeDim() {
		double sum = 0, ykd = 0;
		// sorting maybe not necessary
		double[] expsAr = ascendentSort(getLyapunovExponents());

		for (int i = 0; i < expsAr.length; i++) {
			sum += expsAr[i];
			if (sum < 0) {
				sum -= expsAr[i];
				ykd = i + sum / Math.abs(expsAr[i]);
			}
		}

		return ykd;
	}

	private void init() {
		u = new VectorN[numExps];
		v = new VectorN[numExps];
		perts = new VectorN[numExps];
		for (int i = 0; i < numExps; i++) {
			u[i] = new VectorN(n);
			u[i].set(i, 1.0);
		}

		exps = new VectorN(numExps);
		sums = new VectorN(numExps);
		t = 0;
	}

	private void integrate(VectorN vec) {
		double[] tmp = wrapTime(vec);
		time = intg.timeStep(tmp, time, stepLength);
		vec.set(unwrapTime(tmp));
	}

	private VectorN[] GSR(VectorN[] x) {
		v[0] = new VectorN(x[0]);
		u[0] = VectorOps.multiply(v[0], 1.0 / v[0].norm());
		for (int i = 1; i < numExps; i++) {
			v[i] = new VectorN(x[i]);
			for (int j = 0; j < i; j++) {
				v[i] = VectorOps.sub(v[i], VectorOps.multiply(u[j], VectorOps.dot(x[i], u[j])));
			}
			u[i] = VectorOps.multiply(v[i], 1.0 / v[i].norm());
		}
		return u;
	}

	private double[] unwrapTime(double[] vec) {
		double[] tmp = new double[vec.length - 1];
		System.arraycopy(vec, 1, tmp, 0, tmp.length);
		return tmp;
	}

	private double[] wrapTime(VectorN vec) {
		double[] tmp = vec.getArray();
		double[] res = new double[tmp.length + 1];
		System.arraycopy(tmp, 0, res, 1, tmp.length);
		return res;
	}

	private double[] ascendentSort(double[] v) {
		for (int i = 0; i < v.length; i++) {
			int max = i;
			for (int j = i + 1; j < v.length; j++)
				if (v[j] > v[max])
					max = j;
			double tmp = v[max];
			v[max] = v[i];
			v[i] = tmp;
		}

		return v;
	}
}
