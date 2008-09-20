/**
 * 
 */
package dynsim.math.vector;

/**
 * @author maf83
 * 
 */
public class Vector3D extends VectorN {

	public Vector3D(double x, double y, double z) {
		super(new double[] { x, y, z });
	}

	public Vector3D(Vector3D v) {
		this(v.get(0), v.get(1), v.get(2));
	}

	public Vector3D() {
		this(0, 0, 0);
	}

	public Vector3D(VectorN vectorN) {
		super(vectorN);
	}

	public double getZ() {
		return get(2);
	}

	public double getY() {
		return get(1);
	}

	public double getX() {
		return get(0);
	}

	public Vector3D cross(Vector3D v1) {
		components[0] = get(1) * v1.get(2) - get(2) * v1.get(1);
		components[1] = get(2) * v1.get(0) - get(0) * v1.get(2);
		components[2] = get(0) * v1.get(1) - get(1) * v1.get(0);

		return this;
	}
}
