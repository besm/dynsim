package dynsim.graphics.plot.spline;

/*
 * This code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program; if not, write to the Free 
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA  02111-1307, USA.
 */
//package no.geosoft.cc.geometry.spline;
/**
 * A spline factory instance.
 * 
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public class SplineFactory {
	/**
	 * Create a Bezier spline based on the given control points. The generated
	 * curve starts in the first control point and ends in the last control
	 * point.
	 * 
	 * @param controlPoints
	 *            Control points of spline (x0,y0,z0,x1,y1,z1,...).
	 * @param nParts
	 *            Number of parts to divide each leg into.
	 * @return Spline (x0,y0,z0,x1,y1,z1,...).
	 */
	public static double[] createBezier(double[] controlPoints, int nParts) {
		Spline spline = new BezierSpline(controlPoints, nParts);
		return spline.generate();
	}

	/**
	 * Create a cubic spline based on the given control points. The generated
	 * curve starts in the first control point and ends in the last control
	 * point.
	 * 
	 * @param controlPoints
	 *            Control points of spline (x0,y0,z0,x1,y1,z1,...).
	 * @param nParts
	 *            Number of parts to divide each leg into.
	 * @return Spline (x0,y0,z0,x1,y1,z1,...).
	 */
	public static double[] createCubic(double[] controlPoints, int nParts) {
		Spline spline = new CubicSpline(controlPoints, nParts);
		return spline.generate();
	}

	/**
	 * Create a Catmull-Rom spline based on the given control points. The
	 * generated curve starts in the first control point and ends in the last
	 * control point. Im addition, the curve intersects all the control points.
	 * 
	 * @param controlPoints
	 *            Control points of spline (x0,y0,z0,x1,y1,z1,...).
	 * @param nParts
	 *            Number of parts to divide each leg into.
	 * @return Spline (x0,y0,z0,x1,y1,z1,...).
	 */
	public static double[] createCatmullRom(double[] controlPoints, int nParts) {
		Spline spline = new CatmullRomSpline(controlPoints, nParts);
		return spline.generate();
	}

	/**
	 * Testing the spline package.
	 * 
	 * @param args
	 *            Not used.
	 */
	public static void main(String[] args) {
		double[] c = new double[12];
		c[0] = 0.0; // x0
		c[1] = 0.0; // y0
		c[2] = 0.0; // z0

		c[3] = 1.0; // x1
		c[4] = 1.0; // y1
		c[5] = 0.0; // z1

		c[6] = 2.0; // x2
		c[7] = -1.0; // y2
		c[8] = 0.0; // z2

		c[9] = 10.0; // x3
		c[10] = 0.0; // y3
		c[11] = 0.0; // z3

		double[] spline1 = SplineFactory.createBezier(c, 20);
		double[] spline2 = SplineFactory.createCubic(c, 20);
		double[] spline3 = SplineFactory.createCatmullRom(c, 20);

		System.out.println("-- Bezier");
		for (int i = 0; i < spline1.length; i += 3)
			System.out.println(spline1[i] + "," + spline1[i + 1] + "," + spline1[i + 2]);

		System.out.println("-- Cubic");
		for (int i = 0; i < spline2.length; i += 3)
			System.out.println(spline2[i] + "," + spline2[i + 1] + "," + spline2[i + 2]);

		System.out.println("-- Catmull-Rom");
		for (int i = 0; i < spline3.length; i += 3)
			System.out.println(spline3[i] + "," + spline3[i + 1] + "," + spline3[i + 2]);
	}
}
