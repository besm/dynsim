package dynsim.math.analysis;

import dynsim.math.vector.VectorN;

public class Correlation {
	private double[][] serie;

	private int n;

	double cr;

	public double D2;

	public Correlation(double[][] serie) {
		this.serie = serie;
		this.n = serie[0].length;
	}

	public void compute() {
		// henon eps 0.0001 para 1000 skip 3000 it
		// lorentz eps 0.006 para 1000 skip 3000 it
		double eps = 0.01;
		// double eps = (1d/n)*serie.length;
		double cr = correlationIntegral(eps);
		D2 = Math.log(cr) / Math.log(eps);
	}

	public double correlationIntegral(double eps) {
		double sum = 0;
		double max = -10;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (j == i)
					continue;

				double[] tmpi = new double[serie.length];
				double[] tmpj = new double[serie.length];
				for (int k = 0; k < serie.length; k++) {
					tmpi[k] = serie[k][i];
					tmpj[k] = serie[k][j];
				}

				VectorN vi = new VectorN(tmpi);
				VectorN vj = new VectorN(tmpj);
				double d = vi.norm(vj);

				if (eps - d > max) {
					max = eps - d;
				}

				sum += heaviside(eps - d);
			}
		}

		System.out.println("Max " + max + "sum " + sum);

		sum /= (n * (n - 1));

		System.out.println("Max " + max + "sum " + sum);
		return sum;
	}

	public double heaviside(double v) {
		if (0 <= v) {
			return 1;
		}
		return 0;
	}

	public double mean(double[] data) {
		double sum = 0;
		int n = data.length;

		for (int i = 0; i < n; i++) {
			sum = data[i];
		}
		sum /= n;

		return sum;
	}

	public double stdDev(double[] data) {
		double avg = mean(data);
		double sum = 0.0;
		for (int counter = 0; counter < data.length; counter++) {
			double diff = data[counter] - avg;
			sum = sum + diff * diff;
		}
		return Math.sqrt(sum / (data.length - 1));
	}
}
