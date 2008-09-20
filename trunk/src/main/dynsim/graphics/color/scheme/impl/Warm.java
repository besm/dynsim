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
public class Warm extends AbstractColorScheme {

	/**
	 * @param fader
	 */
	public Warm(ColorRamp fader) {
		super(fader);
	}

	public Warm() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enectic.dynamics.graphics.color.scheme.ColorScheme#loadPals(int[][])
	 */
	public int[][] loadPals() {
		int[][] pals = new int[1][200];
		int[] tmp = fader.getColors(50, new Color(164, 134, 0), new Color(210, 200, 0));
		int[] tmp2 = fader.getColors(50, new Color(155, 50, 0), new Color(164, 134, 0));
		int[] tmp3 = fader.getColors(50, new Color(100, 150, 0), new Color(155, 50, 0));
		int[] tmp4 = fader.getColors(50, new Color(50, 50, 200), new Color(100, 150, 0));
		System.arraycopy(tmp, 0, pals[0], 0, tmp2.length);
		System.arraycopy(tmp2, 0, pals[0], 50, tmp.length);
		System.arraycopy(tmp3, 0, pals[0], 100, tmp3.length);
		System.arraycopy(tmp4, 0, pals[0], 150, tmp4.length);

		return (int[][]) pals.clone();
	}
}
