package dynsim.simulator.ifs.system.filter;

import dynsim.math.vector.Vector2D;

public class Sinusoidal implements Filter {
	public Vector2D filter(double x, double y) {
		return new Vector2D(Math.sin(x), Math.sin(y));
	}
}
