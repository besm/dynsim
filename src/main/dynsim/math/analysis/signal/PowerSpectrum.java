package dynsim.math.analysis.signal;

public class PowerSpectrum {
	private double[] datum;

	private int N;

	private double[] signal;

	private double[] A;

	private double[] B;

	private double[] P;

	// TODO transformada inversa
	public PowerSpectrum(double[] signal) {
		this.signal = signal;
	}

	public void firstDiference(double[] signal) {
		datum[0] = 0;
		for (int i = 1; i < N; i++) {
			datum[i] = signal[i] - signal[i - 1];
		}
	}

	public void compute() {
		int i, k, p, N, L;
		double avg, sum, psmax;
		double[] ts;

		int len = signal.length;
		ts = new double[len];
		A = new double[len];
		B = new double[len];
		P = new double[len];

		/* read in and scale data points */
		for (i = 0; i < signal.length; i++) {
			ts[i] = signal[i] / 1000.0;
		}
		/*
		 * get rid of last point and make sure # of data points is even
		 */
		if ((i % 2) == 0)
			i -= 2;
		else
			i -= 1;
		L = i;
		N = L / 2;
		/* subtract out dc component from time series */
		for (i = 0, avg = 0; i < L; ++i) {
			avg += ts[i];
		}
		avg = avg / L;
		/* now subtract out the mean value from the time series */
		for (i = 0; i < L; ++i) {
			ts[i] = ts[i] - avg;
		}
		/* o.k. guys, ready to do Fourier transform */
		/* first do cosine series */
		for (k = 0; k <= N; ++k) {
			for (p = 0, sum = 0; p < 2 * N; ++p) {
				sum += ts[p] * Math.cos(Math.PI * k * p / N);
			}
			A[k] = sum / N;
		}
		/* now do sine series */
		for (k = 0; k < N; ++k) {
			for (p = 0, sum = 0; p < 2 * N; ++p) {
				sum += ts[p] * Math.sin(Math.PI * k * p / N);
			}
			B[k] = sum / N;
		}
		/* lastly, calculate the power spectrum */
		for (i = 0; i <= N; ++i) {
			P[i] = Math.sqrt(A[i] * A[i] + B[i] * B[i]);
		}
		/* find the maximum of the power spectrum to normalize */
		for (i = 0, psmax = 0; i <= N; ++i) {
			if (P[i] > psmax)
				psmax = P[i];
		}
		for (i = 0; i <= N; ++i) {
			P[i] = P[i] / psmax;
		}
		/* o.k., print out the results: k, P(k) */
	}

	public double[] getSpectrum() {
		return P;
	}

	public double[] getRealFreq() {
		return A;
	}
}
