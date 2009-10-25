package dynsim.ui;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class JNumericSpinner extends JSpinner {

	private static final long serialVersionUID = 7811498093820444001L;

	public JNumericSpinner(Number value, Comparable<Number> min, Comparable<Number> max, Number step) {
		super(new SpinnerNumberModel(value, min, max, step));
	}

	public JNumericSpinner(int value, int min, int max, int step) {
		super(new SpinnerNumberModel(value, min, max, step));
	}

	public JNumericSpinner(double value, double min, double max, double step) {
		super(new SpinnerNumberModel(value, min, max, step));
	}

	public float getFloat() {
		return new Float((Double) super.getValue());
	}

	public double getDouble() {
		return (Double) super.getValue();
	}

	public int getInt() {
		return (Integer) super.getValue();
	}

}
