package dynsim.math.analysis.local;

public class Stability {

	public static String getPointType(double re[], double imag[]) {
		String s = "";

		int n = re.length;
		int n0 = getZeros(re);
		int np = getPositives(re);
		int nn = getNegatives(re);
		int rsum = getSum(re);

		if (notComplex(imag)) {
			if (n0 == 0)
				s += "hyperbolic ";
			if (nn == n) {
				s += "as. stable attractor (sink) ";

				if (allEq(re)) {
					s += "spiral point ";
				} else {
					s += "node ";
				}
			}
			if (np == n) {
				s += "unstable repellor (source) ";

				if (allEq(re)) {
					s += "spiral point ";
				} else {
					s += "node ";
				}
			}
			if (np > 0 && nn > 0)
				s += "unstable saddle point ";
		} else {
			if (rsum > 0) {
				s += "unstable spiral point ";
			}
			if (rsum < 0) {
				s += "as. stable spiral point ";
			}
			if (rsum == 0) {
				s += "stable center (non-linear)";
			}
		}

		return s;
	}

	private static int getSum(double[] v) {
		int z = 0;
		for (int n = 0; n < v.length; n++) {
			z += v[n];
		}

		return z;
	}

	private static boolean allEq(double[] v) {
		for (int n = 0; n < v.length; n++) {
			for (int j = 0; j < v.length; j++) {
				if (v[n] != v[j])
					return false;
			}
		}

		return true;
	}

	private static int getNegatives(double[] v) {
		int z = 0;
		for (int n = 0; n < v.length; n++) {
			if (v[n] < 0)
				z++;
		}

		return z;
	}

	private static int getPositives(double[] v) {
		int z = 0;
		for (int n = 0; n < v.length; n++) {
			if (v[n] > 0)
				z++;
		}

		return z;
	}

	private static int getZeros(double[] v) {
		int z = 0;
		for (int n = 0; n < v.length; n++) {
			if (v[n] == 0)
				z++;
		}

		return z;
	}

	private static boolean notComplex(double[] v) {
		for (int n = 0; n < v.length; n++) {
			if (v[n] != 0)
				return false;
		}

		return true;
	}

	public static int isStable(double[] re, double[] imag) {
		int n = re.length;

		int np = getPositives(re);
		int nn = getNegatives(re);
		int rsum = getSum(re);

		if (notComplex(imag)) {
			if (nn == n) {
				return 1;
			}
			if (np == n) {
				return 0;
			}
			if (np > 0 && nn > 0)
				return 0;
		} else {
			if (rsum > 0) {
				return 0;
			}
			if (rsum < 0) {
				return 1;
			}
			if (rsum == 0) {
				return 1;
			}
		}

		return -1;
	}

}
