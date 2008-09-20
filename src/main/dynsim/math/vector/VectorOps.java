package dynsim.math.vector;

public class VectorOps {
	public static VectorN multiply(VectorN a, double b) {
		int dim = a.componentsNum();
		VectorN v = a.getNewCopy();
		for (int i = 0; i < dim; i++) {
			v.set(i, v.get(i) * b);
		}
		return v;
	}

	public static VectorN add(VectorN a, VectorN b) {
		VectorN r = new VectorN(a.componentsNum());
		for (int i = 0; i < r.componentsNum(); i++)
			r.set(i, a.get(i) + b.get(i));
		return r;
	}

	public static VectorN sub(VectorN a, VectorN b) {
		VectorN r = new VectorN(a.componentsNum());
		for (int i = 0; i < r.componentsNum(); i++)
			r.set(i, a.get(i) - b.get(i));
		return r;
	}

	public static double dot(VectorN a, VectorN b) {
		if (a.componentsNum() != b.componentsNum())
			throw new ArithmeticException("using DOT operator, length of Vectors don't match: " + a.componentsNum()
					+ "!=" + b.componentsNum());

		double d = a.get(0) * b.get(0);
		for (int i = 1; i < a.componentsNum(); i++)
			d += a.get(i) * b.get(i);
		return d;
	}

	// TODO generalizar
	public static Vector3D crossProduct(Vector3D u, Vector3D v) {
		double x = u.getY() * v.getZ() - u.getZ() * v.getY();
		double y = u.getZ() * v.getX() - u.getX() * v.getZ();
		double z = u.getX() * v.getY() - u.getY() * v.getX();

		return new Vector3D(x, y, z);
	}

	/**
	 * @param a
	 * @param b
	 * @return
	 */
	public static VectorN mul(VectorN a, VectorN b) {
		int dim = a.componentsNum();
		// TODO log Àexception uncaughtable?
		if (b.componentsNum() != dim)
			return null;

		VectorN v = new VectorN(dim);
		for (int i = 0; i < dim; i++) {
			v.set(i, a.get(i) * b.get(i));
		}
		return v;
	}

	public static VectorN divide(Vector3D a, Vector3D b) {
		int dim = a.componentsNum();
		if (b.componentsNum() != dim)
			return null;

		VectorN v = new VectorN(dim);
		for (int i = 0; i < dim; i++) {
			v.set(i, a.get(i) / b.get(i));
		}
		return v;
	}
}
