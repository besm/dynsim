package dynsim.graphics.render.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dynsim.exceptions.DynSimException;
import dynsim.exceptions.UnsupportedModeException;
import dynsim.graphics.render.AbstractRenderer;
import dynsim.graphics.render.RenderConfig;
import dynsim.graphics.render.bones3D.Camera;
import dynsim.graphics.render.light.Light;
import dynsim.graphics.render.light.Material;
import dynsim.graphics.render.shaders.BaseShader;
import dynsim.graphics.render.shaders.Shader;
import dynsim.graphics.render.util.FloatRange;
import dynsim.math.vector.Vector3D;

public class CasterRenderer extends AbstractRenderer {

	private static final int B = 2;

	private static final int G = 1;

	private static final int R = 0;

	Sample[][] points;

	float maxz;

	private double ox, oy, oz, ox2, oy2, oz2, ox3, oy3, oz3, ox4, oy4, oz4;

	// Vector3D L = new Vector3D(-1, -1, -1);
	// VectorN V = new VectorN(new double[] { 1, 1, 0 });

	// Vector3D L = new Vector3D(-1, -1, -1);
	//
	// VectorN V = new VectorN(new double[] { 1, -1, 0 });
	//
	// Vector3D L2 = new Vector3D(1, 0, 1);

	private float[] ambient;

	private float ka;

	private List<Light> lights;

	private Vector3D V;

	private Shader shader;

	public CasterRenderer() {
		setDimensions(400, 400, 400);

		conf = new RenderConfig(RenderConfig.MODE_3D);

		lights = new ArrayList<Light>();

		points = new Sample[w][h];

		cam = new Camera();
		// cam.setEye(w, h - 50, d * d); //ravi..
		cam.setEye(w / 2, h / 2, d * d);
		cam.setZoom(0.9f);
		// cam.setRotation(1, 0.25, 0.5);

		setAllAxisRanges(new FloatRange(-50, 50));

		setDefaultCorrection();
		setDetail(0.015f);

		// V = new Vector3D(0, -10, 0);
		// V = new Vector3D(0, 0, -15);
		// V = new Vector3D(0, 0, -1);
		V = new Vector3D(0, -5, -5);

		defaultLights();

		shader = new BaseShader();
	}

	private void defaultLights() {
		// ka = 0.05f;
		ka = 0.0f;
		// 0.1f, 0.21f, 0.5f
		// ambient = new float[] { 0.32f, 0.44f, 0.32f };
		ambient = new float[] { 0.833f, 0.64f, 0.0f };
		// ambient = new float[] { 0.35f, 0.15f, 0.f };

		// Light key = new Light(-5, -25, 35);
		Light key = new Light(-5, 25, -35);
		// key.setConfig(Light.OMNI);

		Light fill = new Light(10, -25, -45);
		// Light fill = new Light(10, -25, -45);
		fill.setConfig(Light.NO_SPEC);

		Light rim = new Light(0, 25, 0);
		rim.setConfig(Light.RIM);

		// Material material = new Material();
		// material.setDiffuseColor(0.833f, 0.64f, 0.0f);
		// material.setSpecularColor(1f, 0.88f, 0.25f);
		// thin plastic
		// material.setShiney(1f / 0.1f);
		// material.ks = 0.5f;
		// material.kd = 0.8f;
		// material.kr = 0.15f;

		Material material = new Material();
		// 0.23f, 0.37f, 0.8f
		// material.setDiffuseColor(0.42f, 0.54f, 0.42f);
		// material.setDiffuseColor(0.84f, 0.94f, 0.84f);
		// 139,123,139
		material.setDiffuseColor(0.54f, 0.35f, 0.f);
		material.setSpecularColor(1f, 1f, 0.95f);
		material.setRoughness(0.1f);
		material.ks = 0.9f;
		material.kd = 0.05f;
		material.kr = 1f;
		// material.setRoughness(0.75f);
		// material.ks = 0.8f;
		// material.kd = 0.05f;

		key.setIntensity(0.8f, 0.8f, 0.8f);
		key.setMaterial(material);

		fill.setIntensity(0.25f, 0.25f, 0.25f);
		fill.setMaterial(material);

		Material m = new Material();
		m.setDiffuseColor(0.0f, 0.35f, 0.54f);
		m.setSpecularColor(1f, 1f, 1f);
		m.setRoughness(0.1f);
		rim.setIntensity(0.25f, 0.25f, 0.25f);
		rim.setMaterial(m);

		addLight(key);
		addLight(fill);
		addLight(rim);

		setBackgroundColor(Color.BLACK);
	}

	@Override
	protected void procPixel(int x, int y, double dx, double dy, double dz) throws DynSimException {
		// if (dz > 2.75)
		// return; // z=0 section

		if (conf.isDisabled(RenderConfig.MODE_3D)) {
			throw new UnsupportedModeException("Only 3D Mode supported.");
		}

		putZB(x, y, dx, dy, dz);
	}

	private void illumination(int x, int y, double dx, double dy, double dz, Vector3D N) {

		float alpha = detail * ((float) (1f - N.getZ()));

		Iterator<Light> il = lights.iterator();
		float[] I = new float[3];

		Vector3D P = new Vector3D(dx, dy, dz);

		while (il.hasNext()) {
			Light lit = il.next();
			float[] c = null;
			if (lit.isEnabled(Light.RIM)) {
				// TODO do well
				c = ((BaseShader) shader).rim(lit, P, N, V);
			} else {
				c = shader.shade(lit, P, N, V);
			}

			I[0] += c[0];
			I[1] += c[1];
			I[2] += c[2];
		}

		for (int i = 0; i < 3; i++) {
			if (conf.isEnabled(RenderConfig.OPAQUE)) {
				points[x][y].intensity[i] = I[i];
			} else if (conf.isDisabled(RenderConfig.NO_DARK_TRICK)) {
				if (points[x][y].intensity[i] < I[i]) {
					points[x][y].intensity[i] = blend(points[x][y].intensity[i], I[i], alpha);
				}
			} else {
				points[x][y].intensity[i] = blend(points[x][y].intensity[i], I[i], alpha);
			}
		}
	}

	private void putZB(int x, int y, double dx, double dy, double dz) {

		float fz = movePositive((float) dz);

		Vector3D N = fwDiff(dx, dy, dz); // updates ox,oy..

		if (points[x][y] == null) {
			points[x][y] = new Sample(fz);
			if (conf.isEnabled(RenderConfig.OPAQUE)) {
				N.normalize();
				illumination(x, y, dx, dy, dz, N);
			}
		}

		if (points[x][y].z <= fz) {
			points[x][y].z = fz;
			if (conf.isEnabled(RenderConfig.OPAQUE)) {
				N.normalize();
				illumination(x, y, dx, dy, dz, N);
			}
		}

		if (points[x][y].z > maxz)
			maxz = points[x][y].z;

		if (conf.isDisabled(RenderConfig.OPAQUE)) {
			N.normalize();
			illumination(x, y, dx, dy, dz, N);
		}

	}

	private float movePositive(float dz) {
		switch (getPosZ()) {
		case 3:
			dz = (az.getMax() - az.getMin()) + dz;
			break;
		case 2:
			dz = (ay.getMax() - ay.getMin()) + dz;
			break;
		case 1:
			dz = (ax.getMax() - ax.getMin()) + dz;
			break;
		}
		return dz;
	}

	private float blend(float src, float dest, float alpha) {
		return (float) (((1 - alpha) * src) + (alpha * dest));
	}

	private Vector3D fwDiff(double dx, double dy, double dz) {
		double x, y, z;

		x = dx - (2 * ox) - (2 * ox2) - (2 * ox3) + ox4;
		y = dy - (2 * oy) - (2 * oy2) - (2 * oy3) + oy4;
		z = dz - (2 * oz) - (2 * oz2) - (2 * oz3) + oz4;

		ox4 = ox3;
		oy4 = oy3;
		oz4 = oz3;

		ox3 = ox2;
		oy3 = oy2;
		oz3 = oz2;

		ox2 = ox;
		oy2 = oy;
		oz2 = oz;

		ox = dx;
		oy = dy;
		oz = dz;

		return new Vector3D(x, y, z);
	}

	public void rasterize() {
		initImage();

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				Sample cs = points[x][y];

				if (cs == null || Float.isNaN(cs.z)) {
					continue;
				}

				convolve(y, x, 6);

				float Ir = (ka * ambient[R]) + points[x][y].intensity[R];
				// Ir = blend(0, points[x][y].z / maxz, Ir);

				float Ig = (ka * ambient[G]) + points[x][y].intensity[G];
				// Ig = blend(0, points[x][y].z / maxz, Ig);

				float Ib = (ka * ambient[B]) + points[x][y].intensity[B];
				// Ib = blend(0, points[x][y].z / maxz, Ib);

				putPx(x, y, Ir, Ig, Ib);
			}
		}

		onEndRaster();
	}

	protected void onEndRaster() {
		if (conf.isEnabled(RenderConfig.DRAW_REFS)) {
			Iterator<Light> lits = lights.iterator();
			while (lits.hasNext()) {
				Light l = lits.next();
				Vector3D p = l.getOrigPosition();
				Material m = l.getMaterial();
				Color c = Color.GREEN;
				if (l.isEnabled(Light.NO_SPEC)) {
					c = new Color(m.Id[0], m.Id[1], m.Id[2]);
				} else {
					c = new Color(m.Is[0], m.Is[1], m.Is[2]);
				}
				drawPoint(p.getX(), p.getY(), p.getZ(), c);
			}

			drawPoint(V.getX(), V.getY(), V.getZ(), Color.RED);
			drawAxes();
		}

		// TODO postedicion
		// /////////////////
		// if conf postpro
		// LensBlurFilter f = new LensBlurFilter();
		// // f.setRadius(10);
		// // f.setBloom(2);
		// // f.setSides(10);
		// // f.setBloomThreshold(115);
		// BufferedImage bloom = f.filter(img, null);
		//
		// Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
		// 0.25f);
		// Graphics2D g2 = (Graphics2D) img.getGraphics();
		// g2.setComposite(comp);
		// g2.drawImage(bloom, 0, 0, null);
		// g2.dispose();
	}

	private void convolve(int y, int x, int siz) {
		// TODO size neighbours
		if (x + 1 < w - 1 && y + 1 < h - 1 && x > 0 && y > 0) {

			fixPoint(x + 1, y + 1);
			fixPoint(x - 1, y - 1);
			fixPoint(x - 1, y);
			fixPoint(x + 1, y);

			// fixPoint(x, y - 1);
			// fixPoint(x, y + 1);

			fixPoint(x + 1, y - 1);
			fixPoint(x - 1, y + 1);

			for (int i = 0; i < 3; i++) {
				points[x][y].intensity[i] += points[x + 1][y + 1].intensity[i];
				points[x][y].intensity[i] += points[x - 1][y - 1].intensity[i];
				points[x][y].intensity[i] += points[x - 1][y].intensity[i];
				points[x][y].intensity[i] += points[x + 1][y].intensity[i];

				// points[x][y].intensity[i] += points[x][y + 1].intensity[i];
				// points[x][y].intensity[i] += points[x][y - 1].intensity[i];

				points[x][y].intensity[i] += points[x - 1][y + 1].intensity[i];
				points[x][y].intensity[i] += points[x + 1][y - 1].intensity[i];

				points[x][y].intensity[i] /= 6;
			}
		}
	}

	private void fixPoint(int x, int y) {
		if (points[x][y] == null) {
			points[x][y] = new Sample(Float.NaN);
			points[x][y].intensity[0] = getBackgroundColor().getRed() / 255f;
			points[x][y].intensity[1] = getBackgroundColor().getGreen() / 255f;
			points[x][y].intensity[2] = getBackgroundColor().getBlue() / 255f;
		}
	}

	public void flush() throws DynSimException {
		try {
			writeToDisk("casterend", "png", "data/images");
		} catch (IOException e) {
			throw new DynSimException("Error flushing to disk", e);
		}
	}

	protected class Sample {
		public float z;

		public float[] intensity;

		public Sample(float z) {
			this.z = z;
			intensity = new float[3];
		}
	}

	protected void drawPoint(double x, double y, double z, Color c) {
		Graphics g = img.getGraphics();
		int x1, y1;

		double max = 0, min = 0;

		if (x > ax.getMax()) {
			max = x;
		} else {
			max = ax.getMax();
		}
		if (x < ax.getMin()) {
			min = x;
		} else {
			min = ax.getMin();
		}

		x *= (w / (max - min));

		if (y > ay.getMax()) {
			max = y;
		} else {
			max = ay.getMax();
		}
		if (y < ay.getMin()) {
			min = y;
		} else {
			min = ay.getMin();
		}

		y *= (h / (max - min));

		if (z > az.getMax()) {
			max = z;
		} else {
			max = az.getMax();
		}
		if (z < az.getMin()) {
			min = z;
		} else {
			min = az.getMin();
		}

		z *= (d / (max - min));

		int[] xy = cam.worldToScreen(x, y, z);
		x1 = xy[0];
		y1 = xy[1];

		g.setColor(c);
		g.fillRect(x1, y1, 5, 5);
	}

	public void setView(Vector3D view) {
		this.V = view;
	}

	public void addLight(Light l) {
		lights.add(l);
	}

	public void setLights(List<Light> l) {
		lights = l;
	}

	public List<Light> getLights() {
		return lights;
	}

	public void setAmbient(float ka, float[] rgb) {
		this.ka = ka;
		this.ambient = rgb;
	}
}
