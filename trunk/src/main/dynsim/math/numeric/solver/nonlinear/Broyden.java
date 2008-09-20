package dynsim.math.numeric.solver.nonlinear;

import Jama.Matrix;
import dynsim.math.numeric.solver.AbstractRootFinder;
import dynsim.simulator.system.DynamicalSystem;

/**
 * Adapted from Fortran77 Broydn
 * 
 */

public class Broyden extends AbstractRootFinder {
	protected int varsNum;

	private double[][] inverseJac;

	private double[] funcVal;

	protected double[] varsVal;

	private double[] sumJac;

	private double[] mFuncVal;

	private double[] diffFuncVal;

	private double[] mDiffFuncVal;

	protected DynamicalSystem sys;

	public Broyden(DynamicalSystem sys) {
		setSystem(sys);
	}

	public Broyden() {
	}

	public void setSystem(DynamicalSystem sys) {
		varsNum = sys.getIndepVarsNumNoTime();
		this.sys = sys;
		init();
	}

	public void solve() {
		setInitialValues(start);

		funcVal = eval();

		Matrix jacobien = sys.getJacobianMatrix(varsVal);

		if (jacobien.det() == 0) {
			status = JACOBIAN_NO_INVERSE;
			return;
		}

		Matrix jacoInv = jacobien.inverse();

		inverseJac = jacoInv.getArray();
		int curIter = 1;
		double P;
		double[] tmpVal;
		double err;

		err = matMultiply(funcVal, mFuncVal);

		for (int i = 1; i <= varsNum; i++)
			varsVal[i - 1] = varsVal[i - 1] + mFuncVal[i - 1];

		if (procFound(err))
			return;

		curIter++;

		while (curIter < itersNum) {
			tmpVal = eval();

			for (int i = 0; i < varsNum; i++) {
				diffFuncVal[i] = tmpVal[i] - funcVal[i];
			}

			funcVal = tmpVal;

			matMultiply(diffFuncVal, mDiffFuncVal);

			P = 0.0;
			for (int i = 0; i < varsNum; i++)
				P = P - mFuncVal[i] * mDiffFuncVal[i];

			if (P == 0) {
				// to infinity
				status = NO_CONVERGE;
				System.arraycopy(varsVal, 0, results, 0, varsVal.length);
				return;
			}

			for (int i = 0; i < varsNum; i++) {
				sumJac[i] = 0.0D;
				for (int j = 0; j < varsNum; j++)
					sumJac[i] = sumJac[i] + mFuncVal[j] * inverseJac[j][i];
			}

			for (int i = 0; i < varsNum; i++)
				for (int j = 0; j < varsNum; j++)
					inverseJac[i][j] = inverseJac[i][j] + ((mFuncVal[i] + mDiffFuncVal[i]) * sumJac[j]) / P;

			err = matMultiply(funcVal, mFuncVal);

			for (int i = 0; i < varsNum; i++)
				varsVal[i] = varsVal[i] + mFuncVal[i];

			if (procFound(err))
				return;

			curIter++;
		} // end while

		status = NO_CONVERGE;
	}

	private void init() {
		initMats();
		itersNum = 150;
		etol = 1E-4;
		status = NO_CONVERGE;
	}

	private void initMats() {
		inverseJac = new double[varsNum][varsNum];
		funcVal = new double[varsNum];
		varsVal = new double[varsNum];
		diffFuncVal = new double[varsNum];
		sumJac = new double[varsNum];
		funcVal = new double[varsNum];
		mFuncVal = new double[varsNum];
		mDiffFuncVal = new double[varsNum];
		results = new double[varsNum];
	}

	private void setInitialValues(double[] vars) {
		varsVal = vars.clone();
	}

	protected double[] eval() {
		if (sys.getIndependentVarsNum() > varsNum) {
			double[] vtemp = new double[sys.getIndependentVarsNum()];
			System.arraycopy(varsVal, 0, vtemp, 1, varsVal.length);
			vtemp = sys.eval(vtemp);
			double[] rtemp = new double[varsNum];
			System.arraycopy(vtemp, 1, rtemp, 0, rtemp.length);
			return rtemp;
		} else {
			return sys.eval(varsVal);
		}
	}

	private boolean procFound(double err) {
		if (err <= etol) {
			status = OK;
			// Actualizamos a la aprox. actual
			System.arraycopy(varsVal, 0, results, 0, varsVal.length);

			return true;
		}
		return false;
	}

	// TODO calcularlo con algebra lineal de Jama
	private double matMultiply(double[] ad, double[] ad1) {
		double err = 0.0;
		for (int i = 1; i <= varsNum; i++) {
			ad1[i - 1] = 0.0;
			for (int j = 1; j <= varsNum; j++)
				ad1[i - 1] = ad1[i - 1] - inverseJac[i - 1][j - 1] * ad[j - 1];

			err += ad1[i - 1] * ad1[i - 1];
		}

		// euclidean dist
		err = Math.sqrt(err);
		return err;
	}
}
