package dynsim.math.numeric.solver.nonlinear;

import Jama.Matrix;
import dynsim.math.numeric.solver.AbstractRootFinder;
import dynsim.simulator.system.DynamicalSystem;

public class NewtonHomotopy extends AbstractRootFinder {

	private static final int N = 100;

	private double lambda;

	private DynamicalSystem sys;

	public NewtonHomotopy(DynamicalSystem sys) {
		setSystem(sys);
		lambda = 0;
		etol = 1E-4;
		status = NO_CONVERGE;
	}

	private void setSystem(DynamicalSystem sys) {
		this.sys = sys;
	}

	public void solve() {

		double[] p = new double[sys.getIndependentVarsNum()];

		double[] f0, aprox;

		if (sys.getTimeNature() == DynamicalSystem.CONTINOUS) {
			p[0] = 1;
			for (int n = 1; n < p.length; n++) {
				p[n] = start[n - 1];
			}

			f0 = sys.eval(p);
			aprox = new double[f0.length - 1];

		} else {
			for (int n = 0; n < p.length; n++) {
				p[n] = start[n];
			}

			f0 = sys.eval(p);
			aprox = new double[f0.length];
		}

		double h = 1d / N;

		if (sys.getTimeNature() == DynamicalSystem.CONTINOUS) {
			p = new double[sys.getIndepVarsNumNoTime()];
		}

		for (int j = 0; j < N; j++) {
			lambda = j * h;

			for (int n = 0; n < p.length; n++) {
				p[n] = lambda;
			}

			Matrix jac = sys.getJacobianMatrix(p);

			if (jac.det() == 0) {
				status = JACOBIAN_NO_INVERSE;
				continue;
			}

			Matrix jacInv = jac.inverse();
			double[][] inverseJac = jacInv.getArray();

			if (sys.getTimeNature() == DynamicalSystem.CONTINOUS) {
				for (int i = 0; i < f0.length - 1; i++) {
					aprox[i] = h * (-inverseJac[i][i]) * f0[i + 1];
				}
			} else {
				for (int i = 0; i < f0.length; i++) {
					aprox[i] = h * (-inverseJac[i][i]) * f0[i];
				}
			}

			if (isDone(p, aprox)) {
				status = OK;
				results = aprox;
			}
		}

		results = aprox;

		return;

	}

	private boolean isDone(double[] p, double[] aprox) {
		int ok = 0;
		for (int i = 0; i < p.length; i++) {
			double err = Math.abs(p[i] - aprox[i]);
			if (err > etol) {
				ok++;
			}
		}

		return (ok == p.length);
	}

}
