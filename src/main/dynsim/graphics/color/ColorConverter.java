/**
 * 
 */
package dynsim.graphics.color;

import Jama.Matrix;

/**
 * @author maf83
 * 
 */
public class ColorConverter {
	static final  Matrix YCC = new Matrix(new double[][] { { 0.2989, 0.5866, 0.1145 }, { -0.1687, -0.3312, 0.5000 },
			{ 0.5000, 0.4183, 0.0816 } });

	static final  Matrix iYCC = new Matrix(new double[][] { { 1.0, 0.0, 1.4022 }, { 1.0, -0.3456, -0.7145 },
			{ 1.0, 1.7710, 0.0 } });

	public static float[] RGBtoYCC(final float[] rgb) {
		return matToFloatArr(YCC.times(floatArrToMat(rgb)));
	}

	private static Matrix floatArrToMat(final float[] rgb) {
		return new Matrix(new double[][] { { rgb[0] }, { rgb[1] }, { rgb[2] } });
	}

	public static float[] YCCtoRGB(final float[] rgb) {
		return matToFloatArr(iYCC.times(floatArrToMat(rgb)));
	}

	private static float[] matToFloatArr(final Matrix mc) {
		return new float[] { (float) mc.get(0, 0), (float) mc.get(1, 0), (float) mc.get(2, 0) };
	}

	public static float[] HSVtoRGB(final float h, final float s, final float v) {
		// H is given on [0->6] or -1. S and V are given on [0->1].
		// RGB are each returned on [0->1].
		float m, n, f;
		int i;

		final float[] hsv = new float[3];
		final float[] rgb = new float[3];

		hsv[0] = h;
		hsv[1] = s;
		hsv[2] = v;

		if (hsv[0] == -1) {
			rgb[0] = rgb[1] = rgb[2] = hsv[2];
			return rgb;
		}
		i = (int) (Math.floor(hsv[0]));
		f = hsv[0] - i;
		if (i % 2 == 0)
			f = 1 - f; // if i is even
		m = hsv[2] * (1 - hsv[1]);
		n = hsv[2] * (1 - hsv[1] * f);
		switch (i) {
		case 6:
		case 0:
			rgb[0] = hsv[2];
			rgb[1] = n;
			rgb[2] = m;
			break;
		case 1:
			rgb[0] = n;
			rgb[1] = hsv[2];
			rgb[2] = m;
			break;
		case 2:
			rgb[0] = m;
			rgb[1] = hsv[2];
			rgb[2] = n;
			break;
		case 3:
			rgb[0] = m;
			rgb[1] = n;
			rgb[2] = hsv[2];
			break;
		case 4:
			rgb[0] = n;
			rgb[1] = m;
			rgb[2] = hsv[2];
			break;
		case 5:
			rgb[0] = hsv[2];
			rgb[1] = m;
			rgb[2] = n;
			break;
		}

		return rgb;

	}

}
