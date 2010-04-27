package dynsim.graphics.color;

public class Palette {
	private static int DEFAULT_SIZE = 12000;

	private final int[] palette;

	public Palette(final float hmn, final float hmx) {
		this(DEFAULT_SIZE, hmn, hmx);
	}

	public Palette(final int size, final float hmn, final float hmx) {
		palette = ColorLib.getHotPalette(size);
	}

	public Palette() {
		this(0.f, 1.f); // 0.4, 0.5
	}

	public int getColor(double in) {
		int c = (int) in;
		if (c >= palette.length) {
			c = palette.length - 1;
		}
		return palette[c];
	}
}
