/**
 * 
 */
package dynsim.graphics.color.scheme.impl;

import java.awt.Color;

import dynsim.graphics.color.ColorRamp;
import dynsim.graphics.color.scheme.AbstractColorScheme;

/**
 * @author maf83
 * 
 */
public class Rainbow extends AbstractColorScheme {

	/**
	 * @param fader
	 */
	public Rainbow(final ColorRamp fader) {
		super(fader);
	}

	public Rainbow() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enectic.dynamics.graphics.color.scheme.ColorScheme#loadPals(int[][])
	 */
	public int[][] loadPals() {
		final int[][] pals = new int[1][200];
		// pals[6] = fader.getColors(new Color(255, 204, 204), new Color(204,
		// 204, 255));
		// pals[5] = fader.getColors(new Color(255, 153, 51), new Color(255,
		// 204, 204));
		// pals[4] = fader.getColors(new Color(204, 255, 102), new Color(255,
		// 153, 51));
		// pals[3] = fader.getColors(new Color(153, 204, 153), new Color(204,
		// 255, 102));
		// pals[2] = fader.getColors(new Color(178, 34, 34), new Color(153, 204,
		// 102));
		// pals[1] = fader.getColors(new Color(102, 153, 51), new Color(178, 34,
		// 34));
		// pals[0] = fader.getColors(new Color(92, 0, 0), new Color(102, 153,
		// 51));

		// pals[0] = fader.getColors(Color.CYAN.darker().darker().darker(),
		// Color.YELLOW.brighter().brighter().brighter());
		// pals[1] = fader.getColors(new Color(102, 153, 51), new Color(178, 34,
		// 34));
		final int[] tmp = fader.getColors(50, Color.ORANGE, Color.RED);
		final int[] tmp2 = fader.getColors(50, Color.YELLOW, Color.ORANGE);
		final int[] tmp3 = fader.getColors(50, Color.CYAN, Color.YELLOW);
		final int[] tmp4 = fader.getColors(50, Color.MAGENTA, Color.CYAN);
		System.arraycopy(tmp, 0, pals[0], 0, tmp2.length);
		System.arraycopy(tmp2, 0, pals[0], 50, tmp.length);
		System.arraycopy(tmp3, 0, pals[0], 100, tmp3.length);
		System.arraycopy(tmp4, 0, pals[0], 150, tmp4.length);

		return (int[][]) pals.clone();
	}
}
