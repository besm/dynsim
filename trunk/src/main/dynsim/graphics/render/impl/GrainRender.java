/**
 * 
 */
package dynsim.graphics.render.impl;

import java.awt.Color;

/**
 * @author maf83
 * 
 */
public class GrainRender extends DirectRenderer {

	private float grain;

	public GrainRender() {
		super();
		grain = 0.002f;
	}

	protected void procPixel(int x, int y, double dx, double dy, double dz) {
		if (outOfBounds(x, y))
			return;

		try {
			int px = img.getRGB(x, y);

			float[] rgb1 = new float[4];
			new Color(px).getColorComponents(rgb1);
			float r = rgb1[0];
			float g = rgb1[1];
			float b = rgb1[2];

			// int rgb = img.getRGB(x, y);
			//
			// int cr = (rgb >> 16) & 0xFF;
			// float dr = cr / 255f;
			// int cg = (rgb >> 8) & 0xFF;
			// float dg = cg / 255f;
			// int cb = rgb & 0xFF;
			// float db = cb / 255f;

			// Crypton
			// g += grain;
			//
			// if (g > 0.9f) {
			// g -= grain;
			// r += grain;
			//
			// if (r > 0.9) {
			// r -= grain;
			// b += grain;
			// }
			//
			// if (b > 0.7) {
			// b -= grain * 3;
			// r += grain * 3;
			// if (r > 1) {
			// r = 0.9f;
			// }
			// g -= grain;
			// }
			//
			// }

			// FIre
			r += grain;

			if (r > 0.9f) {
				r -= grain;
				g += grain;

				if (g > 0.9) {
					g -= grain;
					if (r > 0.5)
						r -= grain;

					if (b < 1) {
						b += grain;
						if (g > 0.5)
							g -= grain;
					}

					if (b > 1)
						b = 1;
				}
			}

			//

			px = new Color(r, g, b).getRGB();

			img.setRGB(x, y, px);
		} catch (Exception e) {
			System.out.println("x" + x + "y" + y);
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public float getGrain() {
		return grain;
	}

	public void setGrain(float grain) {
		this.grain = grain;
	}
}
