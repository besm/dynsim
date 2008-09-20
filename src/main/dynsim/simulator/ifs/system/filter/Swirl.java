package dynsim.simulator.ifs.system.filter;

import dynsim.math.vector.Vector2D;

public class Swirl implements Filter {

	public Vector2D filter(double x, double y) {
		Vector2D v = new Vector2D(x, y);
		double r = v.norm();
		if (r > 0) {
			x = r * Math.cos(Math.atan(y / x) + r);
			y = r * Math.sin(Math.atan(y / x) + r);
		}
		return new Vector2D(x, y);
	}
}
