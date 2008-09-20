package dynsim.math.numeric.solver.linear;

import dynsim.math.numeric.solver.AbstractSolver;

public class GaussSeidel extends AbstractSolver {
	private int varsNum;

	double[][] A;

	double[] b;

	public GaussSeidel(double[][] A, double[] b) {
		varsNum = A.length;
		this.A = (double[][]) A.clone();
		this.b = (double[]) b.clone();

		status = ERROR;
		etol = 0.001;
		itersNum = 100;
	}

	public void solve() {
		double err;

		for (int k = 0; k < itersNum; k++) {
			err = 0.0;
			for (int I = 1; I <= varsNum; I++) {
				double S = 0.0;

				for (int J = 1; J <= varsNum; J++) {
					S = S - A[I - 1][J - 1] * b[J - 1];
				}

				S = (S + A[I - 1][varsNum]) / A[I - 1][I - 1];

				if (Math.abs(S) > err) {
					err = Math.abs(S);
				}

				b[I - 1] = b[I - 1] + S;
			}
			if (err <= etol) {
				status = OK;
				break;
			} else {
				k++;
			}
		}

		if (status == OK) {
			results = (double[]) b.clone();
		}
	}
}
