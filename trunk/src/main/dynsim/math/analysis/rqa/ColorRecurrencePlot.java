package dynsim.math.analysis.rqa;

import dynsim.graphics.color.Palette;

public class ColorRecurrencePlot extends AbstractRecurrencePlot {
	private static final long serialVersionUID = 5164273098429245188L;

	private int numColors;

	private Palette palette;

	private float sharpen;

	public ColorRecurrencePlot(int w, int h) {
		super(w, h);
		setNumColors(1024);
		setType(RecurrencePlotDelegate.COLOR);
		sharpen = 1f;
	}

	public void updateColors() {
		setPalette(new Palette(0, 1f, numColors));
	}

	public void put(int i, int j, double v) {
		int c = 0;

		if (v != 0) {
			if (sharpen > 0) {
				double f = (sharpen * numColors);
				c = palette.getColor(f / v);
			} else {
				c = palette.getColor(numColors / v);
			}
		}

		putPx(i, j, c);
	}

	public int getNumColors() {
		return numColors;
	}

	public void setNumColors(int numColors) {
		this.numColors = numColors;
		updateColors();
	}

	public Palette getPalette() {
		return palette;
	}

	public void setPalette(Palette palette) {
		this.palette = palette;
	}

	public float getSharpen() {
		return sharpen;
	}

	public void setSharpen(float sharpen) {
		this.sharpen = sharpen;
	}

}
