package dynsim.graphics;

/**
 * @author maf83
 * 
 */
public class ScreenMap {
	// World2ScreenCoords
	// (u,v) screen
	// (x,y) world
	// P = (x-xmin) * ((umax-umin)/(xmax-xmin)+umin)
	public static float toScreenFloat(final double x, final int scrmax, final double min, final double max) {
		return (float) toScreenDouble(x, scrmax, min, max);
		// return (float) (iMax / (max - min) * (x - max) + iMax);
	}

	public static int toScreenInt(final double x, final int scrmax, final double min, final double max) {
		return new Double(toScreenDouble(x, scrmax, max, min)).intValue();
	}

	/**
	 * @param x
	 *            world coordinate to transform
	 * @param iMax
	 *            screen maximum for x
	 * @param min
	 *            world minimum for x
	 * @param max
	 *            world maximum for x
	 * @return world coordinate transformed to screen
	 */
	public static double toScreenDouble(final double x, final int scrmax, final double min, final double max) {
		return (x - min) * (scrmax / (max - min));
	}
}
