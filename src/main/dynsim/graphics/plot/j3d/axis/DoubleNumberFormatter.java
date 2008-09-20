package dynsim.graphics.plot.j3d.axis;

import java.text.NumberFormat;

/**
 * Format a double number.
 * 
 * @author Joy Kyriakopulos (joyk@fnal.gov)
 * @version $Id: DoubleNumberFormatter.java,v 1.1 2001/05/19 00:11:53 tonyj Exp
 *          $
 */
final class DoubleNumberFormatter {
	DoubleNumberFormatter(int power) {
		if (formatter == null)
			formatter = NumberFormat.getInstance();
		this.power = power;
	}

	void setFractionDigits(int fractDigits) {
		formatter.setMinimumFractionDigits(fractDigits);
		formatter.setMaximumFractionDigits(fractDigits);
	}

	String format(final double d) {
		return formatter.format(power != 0 ? d / Math.pow(10.0, power) : d);
	}

	private static NumberFormat formatter = null;
	private int power;
}
