package dynsim.graphics.color;

import java.awt.Color;

public class LinearRamp implements ColorRamp {

	public int[] getColors(int numColors, Color c1, Color c2) {
		int[] colors = new int[numColors];

		for (int n = 0; n < numColors; n++) {
			float ratio = 1.0f * n / numColors;
			int r = (int) (ratio * c1.getRed() + (1 - ratio) * c2.getRed());
			int g = (int) (ratio * c1.getGreen() + (1 - ratio) * c2.getGreen());
			int b = (int) (ratio * c1.getBlue() + (1 - ratio) * c2.getBlue());

			colors[n] = new Color(r, g, b).getRGB();
		}

		return (int[]) colors.clone();
	}
}
