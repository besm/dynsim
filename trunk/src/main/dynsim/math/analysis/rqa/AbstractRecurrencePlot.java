package dynsim.math.analysis.rqa;

import dynsim.data.Storage;
import dynsim.graphics.plot.j2d.RasterGrapher2D;

public abstract class AbstractRecurrencePlot extends RasterGrapher2D implements RecurrencePlotDelegate {
	private static final long serialVersionUID = 2458236850897074084L;

	RecurrencePlot rp;

	private int type;

	public AbstractRecurrencePlot(int w, int h) {
		super(w, h);
	}

	public RecurrencePlot createRecurrencePlot(Storage serie, String[] names) {
		rp = new RecurrencePlot(serie, names, this);
		return rp;
	}

	public RecurrencePlot createRecurrencePlot(Storage serie) {
		return createRecurrencePlot(serie, serie.getColumnNames());
	}

	public void compute() {
		rp.compute();
	}

	protected void putPx(int i, int j, int c) {
		float ni = rp.rowsNum / (getWidth() - getMarginW());
		i /= ni;

		float nj = rp.rowsNum / (getHeight() - getMarginH());
		j /= nj;

		if (j >= getHeight() - getMarginH())
			return;
		if (i >= getWidth() - getMarginW())
			return;

		this.offscr.setRGB(i + getHalfMW(), (getHeight() - getHalfMH() - j), c);
	}

	public void end() {
		// Override if needed
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isColored() {
		return type == RecurrencePlotDelegate.COLOR;
	}
}
