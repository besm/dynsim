package dynsim.graphics.render.light;

import dynsim.general.Configuration;
import dynsim.math.vector.Vector3D;
import dynsim.math.vector.VectorOps;

public class Light extends Configuration {
	public static final int DIRECTIONAL = 0;

	public static final int OMNI = 1;

	public static final int RIM = 2;

	public static final int NO_SPEC = 3;

	private Vector3D position;

	private Vector3D pnorm;

	private float[] intensity;

	private Material currentMat;

	private float c1, c2, c3;

	public Light() {
		intensity = new float[] { 1, 1, 1 };
		setConfig(DIRECTIONAL);
		c1 = 0.001f;
		c2 = 0.5f;
		c3 = 0.25f;
	}

	public Light(double x, double y, double z) {
		this();
		setPosition(x, y, z);

	}

	public Light(Light l) {
		this();
		setIntensity(l.intensity[0], l.intensity[1], l.intensity[2]);
		setPosition(new Vector3D(l.getPosition()));
	}

	public Vector3D getPosition() {
		// Directional light
		return pnorm;
	}

	public Vector3D getPosition(Vector3D P) {
		if (isEnabled(DIRECTIONAL)) {
			return getPosition();
		} else if (isEnabled(OMNI)) {
			Vector3D tmp = new Vector3D(VectorOps.sub(P, position));
			tmp.normalize();
			return tmp;
		} else {
			return null;
		}
	}

	public float getAttenuation(Vector3D P) {
		float d = 1;

		Vector3D t = new Vector3D(P);
		t.normalize();

		d = (float) t.norm(pnorm);
		d = Math.min(1, 1f / (c1 + (c2 * d) + (c3 * d * d)));
		return d;
	}

	public void setPosition(double x, double y, double z) {
		setPosition(new Vector3D(x, y, z));
	}

	public void setPosition(Vector3D position) {
		this.position = position;
		this.pnorm = new Vector3D(this.position);
		this.pnorm.normalize();
	}

	public void setIntensity(float r, float g, float b) {
		intensity[0] = r;
		intensity[1] = g;
		intensity[2] = b;
	}

	public void setMaterial(Material material) {
		this.currentMat = material;
	}

	public Material getMaterial() {
		return currentMat;
	}

	public float[] getIntensity() {
		return intensity;
	}

	public void setAttenuationWeights(float c1, float c2, float c3) {
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
	}

	public Vector3D getOrigPosition() {
		return position;
	}
}
