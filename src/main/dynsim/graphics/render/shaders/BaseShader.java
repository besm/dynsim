package dynsim.graphics.render.shaders;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import dynsim.graphics.render.light.Light;
import dynsim.graphics.render.light.Material;
import dynsim.math.vector.Vector3D;
import dynsim.math.vector.VectorN;
import dynsim.math.vector.VectorOps;

public class BaseShader implements Shader {

	private BufferedImage txt;

	public BaseShader() {
		txt = null;
		try {
			txt = ImageIO.read(new File("data/images/text11.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public float[] shade(Light lit, Vector3D P, Vector3D N, Vector3D V) {
		return shiny(lit, P, N, V);
	}

	protected float[] plastic2(Light lit, Vector3D P, Vector3D N, Vector3D V) {
		Material mat = lit.getMaterial();
		Vector3D L = lit.getPosition(P);

		float[] d = mixDiff(mat, lambertDiffuse(L, N));

		float[] s = new float[] { 0, 0, 0 };

		if (lit.isDisabled(Light.NO_SPEC)) {
			s = mixSpec(mat, phongSpecular(L, N, getViewPosition(P, V), 1f / mat.roughness));
		}

		float[] c = lit.getIntensity();

		float att = lit.getAttenuation(P);

		return mix(d, s, c, att);
	}

	public float[] rim(Light lit, Vector3D P, Vector3D N, Vector3D V) {
		Material mat = lit.getMaterial();
		Vector3D L = lit.getPosition(P);

		float[] d = mixDiff(mat, lambertDiffuse(L, N));

		float[] s = grazingSpecular(lit, P, N, getViewPosition(P, V), 0.15f);

		float[] c = lit.getIntensity();

		float att = lit.getAttenuation(P);

		return mix(d, s, c, att);
	}

	protected float[] grazingSpecular(Light lit, Vector3D P, Vector3D N, Vector3D V, float backscatter) {

		float blendval;
		float ldotn;
		float idotn;
		Vector3D In = new Vector3D(V);
		In.reverse();

		/* roughfactor controls brightness of rimlight */
		float roughfactor;
		/* scatter controls how big an area the rimlight covers */
		float scatter;

		/*
		 * these values were found empirically. can adjust to get get different
		 * results.
		 */
		roughfactor = 0.5f - 0.5f * backscatter;
		scatter = (1 - backscatter) * 0.1f;

		/* idotn tells us angle between normal and view direction */
		idotn = (float) VectorOps.dot(In, N);
		/* ldotn tells us angle between normal and light direction */
		ldotn = (float) VectorOps.dot(lit.getPosition(P), N);
		/* put both dot products in range [0,1] and multiply. */
		blendval = ((ldotn + 1) / 2) * ((idotn + 1) / 2);
		/*
		 * only want to use values in [0, 0.5] to cause rim lighting. changing
		 * the range will alter the effect
		 */
		blendval = smoothstep(scatter, roughfactor, blendval);

		Material mat = lit.getMaterial();
		float[] spec = new float[3];
		spec[0] = blend(0f, mat.Is[0], blendval);
		spec[1] = blend(0f, mat.Is[1], blendval);
		spec[2] = blend(0f, mat.Is[2], blendval);

		return spec;
	}

	protected float[] matte(Light lit, Vector3D P, Vector3D N, Vector3D V) {
		Material mat = lit.getMaterial();
		Vector3D L = lit.getPosition(P);

		float[] d = mixDiff(mat, lambertDiffuse(L, N));

		float[] s = new float[] { 0, 0, 0 };
		float fkr = fresnel(L, N, getViewPosition(P, new Vector3D(V.getNeg())), 0.5f);

		s = mixSpec(mat, fkr * phongNHSpecular(L, N, getViewPosition(P, V), 1f / mat.roughness));

		float[] c = lit.getIntensity();

		float att = lit.getAttenuation(P);

		float fkt = 1 - fkr;
		return mix(d, s, c, fkt * att);
	}

	protected float[] shiny(Light lit, Vector3D P, Vector3D N, Vector3D V) {
		Material mat = lit.getMaterial();
		Vector3D L = lit.getPosition(P);

		float diff = lambertDiffuse(L, N);
		float spec = phongNHSpecular(L, N, getViewPosition(P, V), 1f / mat.roughness);
		// float spec = beckmannSpec(L, N, getViewPosition(P, V),
		// mat.roughness);

		float fresnelTerm = (float) Math.pow(VectorOps.dot(N, getViewPosition(P, V)) + 1, mat.kr);

		// float fresnelTerm = fresnel(L, N, getViewPosition(P, V), mat.kr);

		// TODO if clamp inten...
		fresnelTerm = Math.min(1f, fresnelTerm);

		// A Cube-Map is normally used to do environment mapping
		int x = (int) (N.getX() * 200) + 199;
		int y = (int) (N.getY() * 200) + 199;
		// U = N.x * (width / 2) + ((width / 2) - 1)
		// V = N.y * (height / 2) + ((height / 2) - 1)

		Color txtc = new Color(txt.getRGB(x, y));
		float[] rf = new float[] { txtc.getRed() / 255f, txtc.getGreen() / 255f, txtc.getBlue() / 255f };
		// float[] rf = new float[] { 0.35f, 0.35f, 0.35f };

		// highlight color
		float[] lc = new float[] { 0.85f, 0.45f, 0.15f };

		Material m = new Material(mat);
		for (int i = 0; i < lc.length; i++) {
			m.Id[i] *= lc[i];
		}
		float[] d = mixDiff(mat, diff);

		float[] s = mixSpec(mat, spec);

		float[] c = lit.getIntensity();

		float[] res = mix(d, s, c, (1 - fresnelTerm));

		for (int i = 0; i < res.length; i++) {
			res[i] += rf[i] * fresnelTerm;
		}

		return res;
	}

	protected float[] thinPlastic(Light lit, Vector3D P, Vector3D N, Vector3D V) {
		Material mat = lit.getMaterial();
		Vector3D L = lit.getPosition(P);
		float diffuse = lambertDiffuse(L, N);

		float kt = 0.5f;
		diffuse += (float) kt * VectorOps.dot(new Vector3D(N).reverse(), L);

		float[] d = mixDiff(mat, diffuse);

		float[] s = new float[] { 0, 0, 0 };

		if (lit.isDisabled(Light.NO_SPEC)) {
			s = mixSpec(mat, phongNHSpecular(L, N, getViewPosition(P, V), 1f / mat.roughness));
		}

		float[] c = lit.getIntensity();

		float att = lit.getAttenuation(P);

		return mix(d, s, c, att);
	}

	protected float[] glossy(Light lit, Vector3D P, Vector3D N, Vector3D V) {
		Material mat = lit.getMaterial();

		Vector3D L = lit.getPosition(P);

		float[] d = mixDiff(mat, lambertDiffuse(N, L));

		float[] s = new float[] { 0, 0, 0 };

		if (lit.isDisabled(Light.NO_SPEC)) {
			s = mixSpec(mat, glossySpec(L, N, getViewPosition(P, V), (1f / mat.roughness) / 10f, 0.15f));
		}

		float[] c = lit.getIntensity();

		float att = lit.getAttenuation(P);

		return mix(d, s, c, att);
	}

	protected float[] trooper(Light lit, Vector3D P, Vector3D N, Vector3D V) {
		Material mat = lit.getMaterial();

		Vector3D L = lit.getPosition(P);

		float[] d = mixDiff(mat, lambertDiffuse(L, N));

		float[] s = new float[] { 0, 0, 0 };

		if (lit.isDisabled(Light.NO_SPEC)) {
			float dist = 0;

			dist = beckmannSpec(L, N, getViewPosition(P, V), mat.roughness);

			// dist += 0.33 * beckmannSpec(L, N, V, mat.roughness);
			// dist += 0.33 * beckmannSpec(L, N, V, mat.roughness -
			// (0.05f));
			// dist += 0.33 * beckmannSpec(L, N, V, mat.roughness +
			// (0.05f));

			// float[] s = mixSpec(mat, gaussianSpec(L, N, V,
			// mat.roughness));

			s = mixSpec(mat, dist);
		}

		float[] c = lit.getIntensity();

		float att = lit.getAttenuation(P);

		return mix(d, s, c, att);
	}

	protected float[] plastic(Light lit, Vector3D P, Vector3D N, Vector3D V) {
		Material mat = lit.getMaterial();

		Vector3D L = lit.getPosition(P);

		float[] d = mixDiff(mat, lambertDiffuse(L, N));

		float[] s = new float[] { 0, 0, 0 };

		if (lit.isDisabled(Light.NO_SPEC)) {
			s = mixSpec(mat, phongNHSpecular(L, N, getViewPosition(P, V), 1f / mat.roughness));
		}

		float[] c = lit.getIntensity();

		float att = lit.getAttenuation(P);

		return mix(d, s, c, att);
	}

	protected float lambertDiffuse(Vector3D L, Vector3D N) {
		return (float) VectorOps.dot(N, L);
	}

	protected float phongNHSpecular(Vector3D L, Vector3D N, Vector3D V, float roughness) {
		VectorN H = (VectorN) VectorOps.add(L, V);
		H.normalize();

		float NH = (float) VectorOps.dot(N, H);
		return (float) Math.pow(Math.max(0, NH), roughness);
	}

	private float phongSpecular(Vector3D L, Vector3D N, Vector3D V, float roughness) {
		Vector3D rRay = new Vector3D(N);
		rRay.substract(L);
		rRay.multiply(2 * VectorOps.dot(N, L));

		return (float) Math.pow(Math.max(0, VectorOps.dot(rRay, V)), roughness);
	}

	private float fresnel(Vector3D L, Vector3D N, Vector3D V, float kr) {
		Vector3D rRay = new Vector3D(N);
		rRay.substract(L);
		rRay.multiply(2 * VectorOps.dot(N, L));

		// float fresnelTerm = (float) Math.pow(VectorOps.dot(N,
		// V) + 1, kr);

		float fkr;

		float CI = (float) VectorOps.dot(rRay.reverse(), V);

		float CT = (float) Math.sqrt(1 - (kr * kr) * (1 - (CI * CI)));

		// Vector3D T;
		// T = (Vector3D) N.getNewCopy();
		//	
		// T.multiply((ior*CI)-CT);
		// T.substract(L.getNewCopy().multiply(ior));
		float c0 = (float) Math.sin(CI - CT);
		float c1 = (float) Math.sin(CI + CT);
		float c2 = (float) Math.cos(CI - CT);
		float c3 = (float) Math.cos(CI + CT);

		c0 *= c0;
		c1 *= c1;
		c2 *= c2;
		c3 *= c3;

		fkr = 0.5f * (c0 / c1) * (1 + (c3 / c2));

		if (CI < 1) {
			fkr = 1;
		}

		return fkr;
	}

	protected float glossySpec(Vector3D L, Vector3D N, Vector3D V, float roughness, float sharpness) {
		VectorN H = (VectorN) VectorOps.add(L, V);
		H.normalize();

		float NH = (float) VectorOps.dot(N, H);

		float w = .18f * (1 - sharpness);
		return smoothstep(0.72f - w, 0.72f + w, (float) Math.pow(Math.max(0, NH), roughness));
	}

	protected float gaussianSpec(Vector3D L, Vector3D N, Vector3D V, float m) {
		VectorN H = (VectorN) VectorOps.add(L, V);
		H.normalize();

		float NH = (float) VectorOps.dot(N, H);
		float mangl = (float) (Math.acos(NH) / m);
		mangl *= mangl;
		mangl = (float) Math.exp(-mangl);
		return (float) mangl;
	}

	protected float beckmannSpec(Vector3D L, Vector3D N, Vector3D V, float m) {
		VectorN H = (VectorN) VectorOps.add(L, V);
		H.normalize();

		float NH = (float) VectorOps.dot(N, H);

		float c = (float) (1f / (4 * (m * m) * Math.pow(NH, 4)));

		float mtan = (float) Math.tan(Math.acos(NH)) / m;
		mtan *= mtan;
		mtan = (float) Math.exp(-mtan);

		return c * mtan;
	}

	protected float[] mix(float[] d, float[] s, float[] c, float att) {
		return new float[] { att * (c[0] * (d[0] + s[0])), att * (c[1] * (d[1] + s[1])), att * (c[2] * (d[2] + s[2])) };
	}

	protected float[] mixSpec(Material mat, float spec) {
		return new float[] { mat.ks * (mat.Is[0] * spec), mat.ks * (mat.Is[1] * spec), mat.ks * (mat.Is[2] * spec) };
	}

	protected float[] mixDiff(Material mat, float d) {
		return new float[] { mat.kd * (mat.Id[0] * d), mat.kd * (mat.Id[1] * d), mat.kd * (mat.Id[2] * d) };
	}

	protected float smoothstep(float e1, float e2, float x) {
		float interval, x2, y;

		if (x < e1)
			return 0.0f;
		if (x > e2)
			return 1.0f;
		interval = Math.abs(e2 - e1);
		x2 = (x - e1) / interval;
		y = (x2 * x2) * (3 - (2 * x2));
		return y;
	}

	private float blend(float src, float dest, float alpha) {
		return (float) (((1 - alpha) * src) + (alpha * dest));
	}

	private Vector3D getViewPosition(Vector3D P, Vector3D V) {
		Vector3D VP = new Vector3D(V);
		VP.substract(P);
		VP.normalize();
		return VP;
	}
}
