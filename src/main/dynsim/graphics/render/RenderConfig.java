package dynsim.graphics.render;

import dynsim.general.Configuration;

public class RenderConfig extends Configuration {
	public static final int MODE_2D = 0;

	public static final int MODE_3D = 1;

	public static final int Z_BUFFER = 2;

	public static final int OPAQUE = 3;

	public static final int NO_DARK_TRICK = 4;

	public static final int DRAW_REFS = 5;

	public RenderConfig(int mask) {
		setConfig(mask);
	}
}
