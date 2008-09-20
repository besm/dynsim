package dynsim.graphics.plot.config;

public class GrapherConfig3D extends GrapherConfig {
	public static final int DRAW_GRID = 1;

	public static final int DRAW_BORDER = 2;

	public static final int DRAW_VECTFIELD = 4;

	public static final int DRAW_AXIS_Y = 8;

	public static final int DRAW_AXIS_X = 16;

	public static final int DRAW_AXIS_Z = 32;

	public static final int DRAW_AXIS_TEXT_X = 64;

	public static final int DRAW_AXIS_TEXT_Y = 128;

	public static final int DRAW_AXIS_TEXT_Z = 256;

	public static final int DRAW_AXES_TEXT = DRAW_AXIS_TEXT_X | DRAW_AXIS_TEXT_Y | DRAW_AXIS_TEXT_Z;

	public static final int DRAW_AXES = DRAW_AXIS_Y | DRAW_AXIS_X | DRAW_AXIS_Z | DRAW_AXES_TEXT;
}
