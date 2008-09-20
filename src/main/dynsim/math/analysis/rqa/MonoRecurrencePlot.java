package dynsim.math.analysis.rqa;

public class MonoRecurrencePlot extends AbstractRecurrencePlot {

	private static final long serialVersionUID = 5164273098429245188L;

	public MonoRecurrencePlot(int w, int h) {
		super(w, h);
		setType(RecurrencePlotDelegate.MONOCHROME);
	}

	public void put(int i, int j, double v) {
		if (v > 0) {
			putPx(i, j, super.getGrapherConfig().getMainColor().getRGB());
		}
	}
}
