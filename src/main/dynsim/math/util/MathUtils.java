package dynsim.math.util;

import java.math.BigDecimal;

public class MathUtils {

	private static final double LOG2 = Math.log(2);

	private static final double LOG10 = Math.log(10);

	public static double hypot(double a, double b) {
		double r;
		if (Math.abs(a) > Math.abs(b)) {
			r = b / a;
			r = Math.abs(a) * Math.sqrt(1 + r * r);
		} else if (b != 0) {
			r = a / b;
			r = Math.abs(b) * Math.sqrt(1 + r * r);
		} else {
			r = 0.0;
		}
		return r;
	}

	public static double trunc(double d, int s) {
		if (Double.isInfinite(d) || Double.isNaN(d)) {
			return 0;
		}
		BigDecimal t = new BigDecimal(d);
		t = t.setScale(s, BigDecimal.ROUND_HALF_UP);
		return t.doubleValue();
	}

	public static double log10(double x) {
		return Math.log(x) / LOG10;
	}

	public static double log2(double x) {
		return Math.log(x) / LOG2;
	}

	public static double sgn(double x) {
		if (x < 0)
			return -1;
		if (x > 0)
			return 1;
		return 0;
	}

	public static double percent(double v, float pct) {
		return v * pct;
	}

	public static double percent(double v, int pct) {
		return v * (pct / 100);
	}

	/**
	 * @param ds
	 *            serie de dobles
	 * @return Array en indice 0 min en 1 max
	 */
	public static double[] minMax(double[] ds) {
		double[] r = new double[] { 0, 0 };

		for (int i = 0; i < ds.length; i++) {
			if (ds[i] < r[0])
				r[0] = ds[i];
			if (ds[i] > r[1])
				r[1] = ds[i];
		}

		return r;
	}
}
