package dynsim.graphics.render.shaders;

import dynsim.graphics.render.light.Light;
import dynsim.math.vector.Vector3D;

public interface Shader {

	public abstract float[] shade(Light lit, Vector3D P, Vector3D N, Vector3D V);

	public abstract float[] rim(Light lit, Vector3D P, Vector3D N, Vector3D V);

	public abstract float[] shade(ShaderContext context);

	public abstract float[] rim(ShaderContext context);
}