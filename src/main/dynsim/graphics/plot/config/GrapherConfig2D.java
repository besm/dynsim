package dynsim.graphics.plot.config;

public class GrapherConfig2D extends GrapherConfig {
	public static final int DRAW_GRID = 1;

	public static final int DRAW_BORDER = 2;

	public static final int DRAW_VECTFIELD = 4;

	public static final int DRAW_AXIS_Y = 8;

	public static final int DRAW_AXIS_X = 16;

	public static final int DRAW_AXIS_TEXT_X = 32;

	public static final int DRAW_AXIS_TEXT_Y = 64;

	public static final int DRAW_AXES_TEXT = DRAW_AXIS_TEXT_X | DRAW_AXIS_TEXT_Y;

	public static final int DRAW_AXES = DRAW_BORDER | DRAW_AXIS_Y | DRAW_AXIS_X | DRAW_AXES_TEXT;

}
