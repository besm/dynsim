package dynsim.graphics.render.shaders;

import dynsim.graphics.render.light.Light;
import dynsim.math.vector.Vector3D;

public class ShaderContext {
	private final Light light;
	private final Vector3D point;
	private final Vector3D normal;
	private final Vector3D viewer;

	public ShaderContext(final Light lit, final Vector3D P, final Vector3D N, final Vector3D V) {
		this.light = lit;
		this.point = P;
		this.normal = N;
		this.viewer = V;
	}

	public Light getLight() {
		return light;
	}

	public Vector3D getPoint() {
		return point;
	}

	public Vector3D getNormal() {
		return normal;
	}

	public Vector3D getViewer() {
		return viewer;
	}

}
