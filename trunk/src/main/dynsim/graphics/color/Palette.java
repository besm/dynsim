package dynsim.graphics.color;

import java.awt.Color;

public class Palette {
	private static int DEFAULT_SIZE = 12000;

	int palSize;

	private int[] palette;

	public Palette(float hmn, float hmx) {
		this(hmn, hmx, DEFAULT_SIZE);
	}

	public Palette(float hmn, float hmx, int siz) {
		palSize = siz;
		palette = new int[palSize];

		makePalette(hmn, hmx, 0.f, 0.9f, 0.15f, 0.9f, palette);

		//
		// for (int i = 0; i < palSize; i++) {
		// hue = hmx - ft * i;
		// sat = 1f - ft * i;
		// val = 0.85f + (ft * 0.25f) * i;
		// if (hue < hmn)
		// hue = hmn;
		// if (sat < 0.15f)
		// sat = 0.15f;
		// if (val > .95f)
		// val = .95f;
		//
		// burnout[i] = Color.HSBtoRGB(hue, sat, val);
		// }
	}

	public Palette() {
		this(0.f, 1.f); // 0.4, 0.5
	}

	public int getColor(double in) {
		// float c = (float) in;
		int c = (int) in;
		if (c >= palSize) {
			// c = palSize - 1;

			if (c >= palSize)
				c = palSize - 1;

			// return burnout[c];
			// fade hacia blanco!!!!

			// return cycle(c);
			// return Color.getHSBColor((0.45f / palSize) * c, 0.95f, 0.95f)
			// .getRGB();
		}
		return palette[c];
	}

	public void makePalette(float hmn, float hmx, float smn, float smx, float vmn, float vmx, int[] pal) {
		float ft = 1.0f / palSize;

		float hue = 0, sat = 0, val = 0;

		// float hmx = 0.5f, hmn = 0.4f; // 0.25 0.15, 27-17

		for (int i = 0; i < palSize; i++) {
			hue = hmn + ft * i;
			sat = smn + ft * i;
			val = vmn + ft * i;
			if (hue > hmx)
				hue = hmx;
			if (sat > smx)
				sat = smx;
			if (val > vmx)
				val = vmx;

			pal[i] = Color.HSBtoRGB(hue, sat, val);
		}
	}

	public void makePalette(float pfue, float pfsv, int[] pal) {
		System.out.println("making palette");

		float fhue = pfue / palSize; // 0.4f 0.3 0.35f hue ok 0.15-2
		float fsv = pfsv / palSize;
		float fsd = 1f - pfsv;

		for (int i = 0; i < palSize; i++) {
			// double pSize = (double) palSize;

			// int r = (int) (fac*i);
			// int g = (int) (0.5*fac*i);
			// int b = (int) (0.25*fac*i);

			// palette[i] = new Color(r,g,b).getRGB();

			pal[i] = Color.getHSBColor((fhue * i), fsd + (fsv * i), fsd + (fsv * i)).getRGB();

			// double intensity = Math.cos(((palSize - i) / pSize) * (0.5)
			// * Math.PI);
			// double red = 4 * 64.0 * intensity;// +
			// // Math.pow(intensity,130)*350*4;
			// double green = 4 * 72.0 * intensity;// +
			// // Math.pow(intensity,130)*350*4;
			// double blue = 4 * 128.0 * intensity;// +
			// // Math.pow(intensity,130)*350*4;
			// if (red > 255.0)
			// red = 255.0;
			// if (green > 255.0)
			// green = 255.0;
			// if (blue > 255.0)
			// blue = 255.0;
			// int r = (int) red;
			// int g = (int) green;
			// int b = (int) blue;
			// palette[i] = (255 << 24) | (r << 16) | (g << 8) | b;
			//
			// System.out.println("red: " + r + " green: " + g + " blue: " +
			// b);
		}

		// palette[palSize-1] = new Color(250,255,250).getRGB();

		// for (int i = palSize-1; i < palSize; i++) {
		// palette[i] = new Color((int) (fac*i),(int) (fac*i),250).getRGB();
		// }
	}

	public int getPalSize() {
		return palSize;
	}

	public void setPalSize(int palSize) {
		this.palSize = palSize;
	}
}
