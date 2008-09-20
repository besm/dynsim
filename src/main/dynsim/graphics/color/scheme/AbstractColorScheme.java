/**
 * 
 */
package dynsim.graphics.color.scheme;

import dynsim.graphics.color.ColorRamp;
import dynsim.graphics.color.LinearRamp;

/**
 * @author maf83
 * 
 */
public abstract class AbstractColorScheme implements ColorScheme {
	protected ColorRamp fader;

	public AbstractColorScheme(ColorRamp fader) {
		this.fader = fader;
	}

	public AbstractColorScheme() {
		this(new LinearRamp());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enectic.dynamics.graphics.Drawable#draw()
	 */
	// public void draw(Plotter p) {
	// int[][] pals = loadPals();
	// for (int n = 0; n < pals.length; n++) {
	// for (int i = 0; i < pals[n].length; i++) {
	// int c = pals[n][i];
	// p.plotLine(i, n, i, n + 1, new Color(c));
	// }
	// }
	// }
}
