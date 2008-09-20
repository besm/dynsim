/**
 * 
 */
package dynsim.graphics.render.impl;

import java.io.IOException;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.render.AbstractRenderer;
import dynsim.graphics.render.RenderConfig;
import dynsim.graphics.render.bones3D.Camera;
import dynsim.graphics.render.util.FloatRange;

/**
 * @author maf83
 * 
 */
public class DensityRenderer extends AbstractRenderer {

	protected float[][] bins, zbuff;

	protected float maxz;

	protected float exposure;

	public DensityRenderer() {
		setDimensions(400, 400, 400);

		cam = new Camera();
		cam.setEye(w / 2, h / 2, d * d);
		cam.setZoom(0.85f);

		setAllAxisRanges(new FloatRange(-50, 50));
		setDetail(0.0015f);
		setDefaultCorrection();

		exposure = 1f;

		varpos = new int[] { 1, 2, 3 };

		conf = new RenderConfig(RenderConfig.Z_BUFFER | RenderConfig.MODE_3D);

		bins = new float[w][h];
	}

	/**
	 * @param x
	 * @param y
	 */
	protected void procPixel(int x, int y, double dx, double dy, double dz) {
		if (outOfBounds(x, y))
			return;

		bins[x][y] += detail;

		if (conf.isEnabled(RenderConfig.Z_BUFFER)) {
			putZB(x, y, (float) dz);
		}

	}

	private void putZB(int x, int y, float dz) {
		if (zbuff == null) {
			zbuff = new float[w][h];
		}

		dz = -az.getMin() + dz;

		if (zbuff[x][y] <= dz) {
			zbuff[x][y] = dz;
		}

		if (zbuff[x][y] > maxz)
			maxz = zbuff[x][y];
	}

	protected void scale(float overexp, float max, float[][] v) {
		float ratio = overexp / max;
		float tmp;

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				tmp = v[x][y];
				tmp *= ratio;
				v[x][y] = tmp;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.render.Render#flush()
	 */
	public void flush() throws DynSimException {
		try {
			writeToDisk("densrend", "png", "data/images");
		} catch (IOException e) {
			throw new DynSimException("Error flushing to disk", e);
		}
	}

	// TODO buffer de color por bin según velocidad
	public void rasterize() {
		initImage();

		float r = 0, g = 0, b = 0;

		if (conf.isEnabled(RenderConfig.Z_BUFFER)) {
			scale(exposure, maxz, zbuff);
		}

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {

				int rgb = img.getRGB(x, y);

				int cr = (rgb >> 16) & 0xFF;
				float dr = cr / 255f;
				int cg = (rgb >> 8) & 0xFF;
				float dg = cg / 255f;
				int cb = rgb & 0xFF;
				float db = cb / 255f;

				if (conf.isEnabled(RenderConfig.Z_BUFFER)) {
					float zi = zbuff[x][y];
					float al = logTransfer(bins[x][y], exposure);

					r = ((1 - al) * dr) + (al * zi);
					g = ((1 - al) * dg) + (al * zi);
					b = ((1 - al) * db) + (al * zi);
				} else {
					float al = logTransfer(bins[x][y], exposure);
					r = ((1 - al) * dr) + (al);
					g = ((1 - al) * dg) + (al);
					b = ((1 - al) * db) + (al);
				}

				putPx(x, y, r, g, b);
			}
		}
	}

	public float getExposure() {
		return exposure;
	}

	public void setExposure(float exposure) {
		this.exposure = exposure;
	}
}
