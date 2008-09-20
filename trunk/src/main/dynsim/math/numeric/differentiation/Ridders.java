// JTEM - Java Tools for Experimental Mathematics
// Copyright (C) 2001 JEM-Group
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package dynsim.math.numeric.differentiation;

import dynsim.math.numeric.function.RealFunction;

/**
 * Ridders' method of polynomial extrapolation to compute derivative
 * numerically.
 * 
 * @author schmies
 * 
 */
public class Ridders {

	private double error;

	public Ridders() {
	}

	final static double CON = 1.4;

	final static double CON2 = (CON * CON);

	final static double SAFE = 2.0;

	/**
	 * Computes the derivative of f at x by Ridders' method of polynomial
	 * extrapolation. The value h the estimated initial stepsize, which need not
	 * to be small, but should be an increment such that f substantially chanes.
	 * 
	 * @param f
	 *            function to deferentiate
	 * @param x
	 *            point
	 * @param var
	 *            respect var
	 * @param h
	 *            initial stepsize
	 * @param maxTableLength
	 *            maximal length of extrapolation table
	 * @return the derivative of f at x by Ridders' method
	 */
	public double compute(RealFunction f, double[] x, int var, double h, int maxTableLength) {

		double[][] table = new double[maxTableLength][maxTableLength];

		if (h == 0.0)
			throw new IllegalArgumentException("h must be nonzero.");

		double df = Double.NaN, hh = h;
		double[] dxp = x.clone();
		dxp[var] = x[var] + hh;
		double[] dxm = x.clone();
		dxm[var] = x[var] - hh;

		table[0][0] = (f.eval(dxp) - f.eval(dxm)) / (2.0 * hh);

		double err = Double.MAX_VALUE;

		for (int i = 1; i < maxTableLength; i++) {
			hh /= CON;
			table[0][i] = (f.eval(dxp) - f.eval(dxm)) / (2.0 * hh);
			double fac = CON2;
			for (int j = 1; j < i; j++) {
				table[j][i] = (table[j - 1][i] * fac - table[j - 1][i - 1]) / (fac - 1.0);
				fac = CON2 * fac;
				double errt = Math.max(Math.abs(table[j][i] - table[j - 1][i]), Math.abs(table[j][i]
						- table[j - 1][i - 1]));
				if (errt <= err) {
					err = errt;
					df = table[j][i];
				}
			}
			if (Math.abs(table[i][i] - table[i - 1][i - 1]) >= SAFE * (err)) {
				error = err;
				return df;
			}
		}
		error = err;
		return df;
	}

	public double getError() {
		return error;
	}
}
