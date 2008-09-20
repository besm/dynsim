package dynsim.simulator.ifs.system.filter;

import dynsim.math.vector.Vector2D;

public class Spherical implements Filter {

	public Vector2D filter(double x, double y) {
		Vector2D v = new Vector2D(x, y);
		double mm = v.norm() * v.norm();
		if (mm > 0) {
			x = v.getX() / mm;
			y = v.getY() / mm;
		}
		return new Vector2D(x, y);
	}

}
