package dynsim.simulator.ifs.system.filter;

import dynsim.math.vector.Vector2D;

public interface Filter {
	public Vector2D filter(double x, double y);
}
