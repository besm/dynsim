package dynsim.math.numeric.solver.linear;

import dynsim.math.numeric.solver.AbstractSolver;

public class SOR extends AbstractSolver {
	private int n;

	private double w = 1.25;

	double[][] A;

	double[] b;

	public SOR(double[][] A, double[] b) {
		n = A.length;

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
			for (int I = 1; I <= n; I++) {
				double S = 0.0;
				for (int J = 1; J <= n; J++)
					S = S - A[I - 1][J - 1] * b[J - 1];

				S = w * (S + A[I - 1][n]) / A[I - 1][I - 1];
				if (Math.abs(S) > err)
					err = Math.abs(S);

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
