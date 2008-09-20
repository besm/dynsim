package dynsim.graphics.render.impl;

import java.io.IOException;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.render.AbstractRenderer;
import dynsim.graphics.render.RenderConfig;
import dynsim.graphics.render.bones3D.Camera;
import dynsim.graphics.render.util.FloatRange;

public class ReflectorRenderer extends AbstractRenderer {

	Sample[][] points;

	float max;

	private float grain;

	private float kr;

	public ReflectorRenderer() {
		setDimensions(400, 400, 400);

		varpos = new int[] { 1, 2, 3 };

		conf = new RenderConfig(RenderConfig.MODE_3D);

		points = new Sample[w][h];

		// Chua
		// grain = 0.0025f;
		// kr = 0.05f;

		grain = 0.0025f;
		kr = 0.55f;

		cam = new Camera();
		cam.setEye(w / 2, h / 2, d * d);
		cam.setZoom(0.8f);

		setAllAxisRanges(new FloatRange(-50, 50));
	}

	@Override
	protected void procPixel(int x, int y, double dx, double dy, double dz) {
		float near = az.getMin();
		float far = az.getMax();

		float zp = (float) ((far + near / far - near) + (1f / dz) * ((-2 * far * near) / far - near));

		if (points[x][y] == null) {
			points[x][y] = new Sample(Math.abs(grain * zp));
		} else {
			points[x][y].w += Math.abs(grain * zp);
		}

		if (points[x][y].w > max) {
			max = points[x][y].w;
		}
	}

	public void rasterize() {
		initImage();

		float r = 0, g = 0, b = 0;

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {

				Sample cs = points[x][y];

				if (cs == null)
					continue;

				float acc = logTransfer(points[x][y].w, 1);

				//
				// r = 0.1f + (0.40f * I);
				// g = 0.1f + (0.35f * I);
				// b = 0.1f + (0.25f * I);

				float I = kr * acc;

				// r = g = b = I;
				// transform color kernel?
				r = 0.05f + (0.45f * I);
				g = 0.05f + (0.40f * I);
				b = 0.05f + (0.30f * I);

				putPx(x, y, r, g, b);
			}
		}
	}

	public void flush() throws DynSimException {
		try {
			writeToDisk("reflerend", "png", "data/images");
		} catch (IOException e) {
			throw new DynSimException("Error flushing to disk.", e);
		}
	}

	protected class Sample {
		public float w;

		public Sample(float w) {
			this.w = w;
		}
	}

}
