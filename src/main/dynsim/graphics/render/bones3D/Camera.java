/**
 * 
 */
package dynsim.graphics.render.bones3D;

import dynsim.math.vector.Vector3D;

/**
 * @author maf83 TODO reemplazar por una cámara mejor y pipeline de
 *         transformaciones. (con cuaterniones)
 */
public class Camera {
	// Rotation [-PI..PI]
	double yaw, pitch, roll;

	double cx, cy, cz;

	// Viewpoint
	Vector3D eye;

	double tanthetah, tanthetav;

	static double DTOR = 0.01745329252;

	static double EPSILON = 0.001;

	// Vector3D from;
	// Vector3D to;
	// Vector3D up;
	// Vector3D basisa,basisb,basisc;
	double angleh, anglev;

	float zoom;

	// double front,back;
	private boolean PERSPECTIVE = true;

	public Camera() {
		yaw = 0.;
		pitch = 0.;
		roll = 0.;

		cx = 0;
		cy = 0;
		cz = 0;

		// from = new Vector3D(0,100,-600);
		// to = new Vector3D(0,0,600);
		// up = new Vector3D(0,600,0);
		zoom = 1;
		eye = new Vector3D();

		angleh = .90;
		anglev = .90;
		//
		// eye = new Vector3D();
		//        
		// /* Calculate camera aperture statics, note: angles in degrees */
		tanthetah = Math.tan(angleh * DTOR / 2);
		tanthetav = Math.tan(anglev * DTOR / 2);
		//        
		// basisb = new Vector3D(to.getX() - from.getX(), to.getY() -
		// from.getY(), to.getZ() - from.getZ());
		// basisb.normalize();
		//        
		// basisa = VectorOps.crossProduct(up,basisb);
		// basisa.normalize();
		//        
		// basisc = VectorOps.crossProduct(basisb,basisa);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @return array de enteros con X en 0 e Y en 1
	 */
	public int[] worldToScreen(double x, double y, double z) {
		// TODO pipeline de transformaciones matriciales
		//
		// Traslacion
		double x2 = x - cx;
		double y2 = y - cy;
		double z2 = z - cz;

		// /* Translate world so that the camera is at the origin */
		// double x2 = x-from.getX();
		// double y2 = y -from.getY();
		// double z2 = z - from.getZ();
		//
		// /* Convert to eye coordinates using basis vectors */
		// x = x2 * basisa.getX() + y2 * basisa.getY() + z2 * basisa.getZ();
		// y = x2 * basisb.getX() + y2 * basisb.getY() + z2 * basisb.getZ();
		// z = x2 * basisc.getX() + y2 * basisc.getY() + z2 * basisc.getZ();

		// Rotaciones
		// Y
		x = (z2 * Math.sin(yaw) + x2 * Math.cos(yaw));
		z = (z2 * Math.cos(yaw) - x2 * Math.sin(yaw));
		y = y2;

		// X
		x2 = x;
		y2 = (y * Math.cos(pitch) - z * Math.sin(pitch));
		z2 = (y * Math.sin(pitch) + z * Math.cos(pitch));

		// Z
		x = (y2 * Math.sin(roll) + x2 * Math.cos(roll));
		y = (y2 * Math.cos(roll) - x2 * Math.sin(roll));
		z = z2;

		// if (z < 0)
		// return new int[2];

		return camToScreen(x, y, z);
	}

	protected int[] normToScreen(Vector3D norm) {
		// projected->h = screen.center.h - screen.size.h * norm.x / 2;
		// projected->v = screen.center.v - screen.size.v * norm.z / 2;
		// int[] p = new int[2];
		// p[0] = (int) (300 - 600 * norm.getX() / 2);
		// p[1] = (int) (300 - 600 * norm.getZ() / 2);

		return camToScreen(norm.getX(), norm.getY(), norm.getZ());
	}

	protected Vector3D camToNorm(double tx, double ty, double tz) {
		double d;

		if (PERSPECTIVE) {
			d = zoom / ty;
			tx = d * tx / tanthetah;
			tz = d * tz / tanthetav;
		} else {
			tx = zoom * tx / tanthetah;
			tz = zoom * tz / tanthetav;
		}

		return new Vector3D(tx, ty, tz);

	}

	protected int[] camToScreen(double tx, double ty, double tz) {
		tx *= zoom;
		tz *= zoom;
		ty *= zoom;
		tx = (eye.getX() + tx) * (eye.getZ() / (tz + eye.getZ()));
		ty = (eye.getY() - ty) * (eye.getZ() / (tz + eye.getZ()));

		return new int[] { (int) tx, (int) ty };
	}

	public Vector3D getEye() {
		return eye;
	}

	public void setEye(Vector3D eye) {
		this.eye = eye;
	}

	public double getPitch() {
		return pitch;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	public double getRoll() {
		return roll;
	}

	public void setRoll(double roll) {
		this.roll = roll;
	}

	public double getYaw() {
		return yaw;
	}

	public void setYaw(double yaw) {
		this.yaw = yaw;
	}

	public void setRotation(double yaw, double pitch, double roll) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
	}

	/**
	 * Punto de vista del observador
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setEye(double x, double y, double z) {
		this.eye = new Vector3D(x, y, z);
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	public void setLocation(double cx, double cy, double cz) {
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
	}
}
