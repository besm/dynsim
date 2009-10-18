package dynsim.graphics.render.shaders;

import dynsim.graphics.render.light.Light;
import dynsim.graphics.render.light.Material;
import dynsim.math.vector.Vector3D;

public class PlasticShader extends BaseShader {

	public float[] shade(final Light lit, final Vector3D P, final Vector3D N, final Vector3D V) {
		return plastic(lit, P, N, V);
	}

	protected float[] plastic(final Light lit, final Vector3D P, final Vector3D N, final Vector3D V) {
		final Material mat = lit.getMaterial();

		final Vector3D L = lit.getPosition(P);

		final float[] d = mixDiff(mat, lambertDiffuse(L, N));

		final float[] s;

		if (lit.isDisabled(Light.NO_SPEC)) {
			s = mixSpec(mat, phongNHSpecular(L, N, getViewPosition(P, V), 1f / mat.roughness));
		} else {
			s = new float[] { 0, 0, 0 };
		}

		final float[] c = lit.getIntensity();

		final float att = lit.getAttenuation(P);

		return mix(d, s, c, att);
	}

	protected float[] plasticVariant(final Light lit, final Vector3D P, final Vector3D N, final Vector3D V) {
		final Material mat = lit.getMaterial();
		final Vector3D L = lit.getPosition(P);

		final float[] d = mixDiff(mat, lambertDiffuse(L, N));

		final float[] s;

		if (lit.isDisabled(Light.NO_SPEC)) {
			s = mixSpec(mat, phongSpecular(L, N, getViewPosition(P, V), 1f / mat.roughness));
		} else {
			s = new float[] { 0, 0, 0 };
		}

		final float[] c = lit.getIntensity();

		final float att = lit.getAttenuation(P);

		return mix(d, s, c, att);
	}
}
