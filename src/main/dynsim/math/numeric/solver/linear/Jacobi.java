package dynsim.math.numeric.solver.linear;

import dynsim.math.numeric.solver.AbstractSolver;

public class Jacobi extends AbstractSolver {

	private int n;

	double[][] A;

	double[] X1, X2;

	public Jacobi(double[][] A, double[] x0) {
		n = A.length;

		this.A = (double[][]) A.clone();
		X1 = (double[]) x0.clone();
		X2 = new double[n];

		status = ERROR;
		etol = 0.001;
		itersNum = 100;
	}

	public void solve() {
		double err;

		for (int k = 0; k < itersNum; k++) {
			err = 0.0;
			for (int I = 1; I <= n; I++) {
				double S = 0.0;
				for (int J = 1; J <= n; J++)
					S = S - A[I - 1][J - 1] * X1[J - 1];

				S = (S + A[I - 1][n]) / A[I - 1][I - 1];
				if (Math.abs(S) > err)
					err = Math.abs(S);
				/* use X2 for X */
				X2[I - 1] = X1[I - 1] + S;
			}
			if (err <= etol) {
				status = OK;
				break;
			} else {
				k++;
				for (int I = 1; I <= n; I++)
					X1[I - 1] = X2[I - 1];
			}
		}

		if (status == OK) {
			results = (double[]) X2.clone();
		}
	}

}
