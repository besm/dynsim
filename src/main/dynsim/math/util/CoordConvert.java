/**
 * 
 */
package dynsim.math.util;

/**
 * @author maf83
 * 
 */
public class CoordConvert {
	public static double[] polarToCartesian(double rad, double theta) {
		return new double[] { rad * Math.cos(theta), rad * Math.sin(theta) };
	}

	public static double[] cartesianToPolar(double x, double y) {
		return new double[] { Math.sqrt((x * x) + (y * y)), Math.atan(y / x) };
	}

	public static double[] sphericalToCartesian(double rad, double azimut, double polar) {
		return new double[] { rad * Math.cos(azimut) * Math.sin(polar), rad * Math.sin(azimut) * Math.sin(polar),
				rad * Math.cos(polar) };
	}

	public static double[] cartesianToSpherical(double x, double y, double z) {
		double r = Math.sqrt((x * x) + (y * y) + (z * z));
		return new double[] { r, Math.atan(y / x), Math.acos(z / r) };
	}
}
