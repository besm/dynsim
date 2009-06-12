package dynsim.graphics.color;

import java.awt.Color;

public class LinearRamp implements ColorRamp {

	public int[] getColors(final int numColors, final Color c1, final Color c2) {
		final int[] colors = new int[numColors];

		for (int n = 0; n < numColors; n++) {
			final float ratio = 1.0f * n / numColors;
			final int r = (int) (ratio * c1.getRed() + (1 - ratio) * c2.getRed());
			final int g = (int) (ratio * c1.getGreen() + (1 - ratio) * c2.getGreen());
			final int b = (int) (ratio * c1.getBlue() + (1 - ratio) * c2.getBlue());

			colors[n] = new Color(r, g, b).getRGB();
		}

		return (int[]) colors.clone();
	}
}
