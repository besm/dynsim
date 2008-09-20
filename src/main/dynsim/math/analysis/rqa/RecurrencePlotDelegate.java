package dynsim.math.analysis.rqa;

public interface RecurrencePlotDelegate {

	public static final int MONOCHROME = 0;

	public static final int COLOR = 1;

	void put(int i, int j, double d);

	void end();

	boolean isColored();

	public void setType(int type);

	public int getType();

}
