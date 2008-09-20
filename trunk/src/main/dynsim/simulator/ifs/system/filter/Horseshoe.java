package dynsim.simulator.ifs.system.filter;

import dynsim.math.vector.Vector2D;

public class Horseshoe implements Filter {

	public Vector2D filter(double x, double y) {
		Vector2D v = new Vector2D(x, y);
		double r = v.norm();
		if (r > 0) {
			x = r * Math.cos(2 * Math.atan(y / x));
			y = r * Math.sin(2 * Math.atan(y / x));
		}
		return new Vector2D(x, y);
	}
}
