package dynsim.graphics.color;

import java.awt.Color;

public interface ColorRamp {
	public int[] getColors(int numColors, Color from, Color to);
}
