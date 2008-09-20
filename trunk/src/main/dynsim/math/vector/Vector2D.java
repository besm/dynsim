package dynsim.math.vector;

public class Vector2D extends VectorN {

	public Vector2D(double x, double y) {
		super(new double[] { x, y });
	}

	public Vector2D(VectorN v) {
		this(v.get(0), v.get(1));
	}

	public Vector2D() {
		this(0, 0);
	}

	public double getY() {
		return get(1);
	}

	public double getX() {
		return get(0);
	}
}
