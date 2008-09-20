package dynsim.math.vector;

// No indempotent
public class VectorN {
	protected double[] components;

	private static final double tolerance = 0.0001;

	public VectorN(double[] v) {
		components = v;
	}

	public VectorN(VectorN x) {
		this(x.getArrayCopy());
	}

	public VectorN(int n) {
		components = new double[n];
	}

	public double get(int i) {
		return components[i];
	}

	public int componentsNum() {
		return components.length;
	}

	public double norm(VectorN y) {
		double r = 0, t;
		double[] ya = y.getArray();
		checkLength(y);
		for (int i = 0; i < this.componentsNum(); i++) {
			t = components[i] - ya[i];
			r += t * t;
		}
		return Math.sqrt(r);
	}

	public double normMax(VectorN y) {
		double r = 0, t;
		double[] ya = y.getArray();
		checkLength(y);
		for (int i = 0; i < this.componentsNum(); i++) {
			t = Math.abs(components[i] - ya[i]);
			if (t > r) {
				r = t;
			}
		}
		return r;
	}

	public double norm() {
		double r = 0.0;
		for (int i = 0; i < this.componentsNum(); i++) {
			r += components[i] * components[i];
		}

		return Math.sqrt(r);
	}

	public double normMax() {
		double r = 0, t;
		for (int i = 0; i < this.componentsNum(); i++) {
			t = Math.abs(components[i]);
			if (t > r) {
				r = t;
			}
		}

		return r;
	}

	public void normalize() {
		double m = norm();
		if (m <= tolerance)
			m = 1;

		for (int i = 0; i < this.componentsNum(); i++) {
			components[i] /= m;

			if (Math.abs(components[i]) < tolerance) {
				components[i] = .0;
			}
		}
	}

	public VectorN reverse() {
		for (int i = 0; i < this.componentsNum(); i++) {
			components[i] = -components[i];
		}

		return this;
	}

	public VectorN multiply(double s) {
		for (int i = 0; i < this.componentsNum(); i++) {
			components[i] *= s;
		}

		return this;
	}

	public VectorN multiply(VectorN y) {
		double[] ya = y.getArray();
		checkLength(y);
		for (int i = 0; i < this.componentsNum(); i++) {
			components[i] *= ya[i];
		}
		return this;
	}

	public VectorN divide(double s) {
		for (int i = 0; i < this.componentsNum(); i++) {
			components[i] /= s;
		}

		return this;
	}

	public VectorN add(VectorN v) {
		double[] va = v.getArray();
		for (int i = 0; i < this.componentsNum(); i++) {
			components[i] += va[i];
		}

		return this;
	}

	public VectorN substract(VectorN v) {
		double[] va = v.getArray();
		for (int i = 0; i < this.componentsNum(); i++) {
			components[i] -= va[i];
		}

		return this;
	}

	public final double dot(VectorN v1) {
		double[] va = v1.getArray();
		double dot = 0;

		for (int i = 0; i < this.componentsNum(); i++) {
			dot += components[i] * va[i];
		}
		return dot;
	}

	/**
	 * Returns the angle in radians between this vector and the vector
	 * parameter; the return value is constrained to the range [0,PI].
	 * 
	 * @param v1
	 *            the other vector
	 * @return the angle in radians in the range [0,PI]
	 */
	public final double angle(VectorN v1) {
		double vDot = this.dot(v1) / (this.norm() * v1.norm());
		if (vDot < -1.0)
			vDot = -1.0;
		if (vDot > 1.0)
			vDot = 1.0;
		return ((double) (Math.acos(vDot)));

	}

	public double[] getArray() {
		return this.components;
	}

	public double[] getArrayCopy() {
		return (double[]) this.components.clone();
	}

	public void set(int i, double x) {
		components[i] = x;
	}

	public void set(double[] c) {
		this.components = (double[]) c.clone();
	}

	@Override
	public String toString() {
		String s = "[" + VectorN.class + "][";
		for (int i = 0; i < components.length; i++) {
			s += ", " + components[i];
		}
		return s + "]";
	}

	public boolean isZero() {
		for (int i = 0; i < components.length; i++) {
			if (components[i] != 0)
				return false;
		}
		return true;
	}

	public VectorN getNeg() {
		VectorN v = new VectorN(this.getArrayCopy());
		return v.reverse();
	}

	public VectorN getNewCopy() {
		return new VectorN(getArrayCopy());
	}

	private void checkLength(VectorN y) {
		if (this.componentsNum() != y.componentsNum()) {
			throw new IllegalArgumentException("Different num. of components.");
		}
	}
}
