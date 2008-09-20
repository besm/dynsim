package dynsim.graphics.plot.j2d.axis;

public class Axis {
	public static final double LOG2 = 0.301;

	private double start, end, tickStep;

	// TODO scaleType
	private boolean log;

	public Axis(double start, double end, double tickStep) {
		this.start = start;
		this.end = end;
		this.tickStep = tickStep;
		this.log = false;
	}

	public Axis() {
		this(0, 0, 0.5);
	}

	public Axis(double start, double end) {
		this(start, end, 1);
	}

	public Axis(double tickStep) {
		this(0, 0, tickStep);
	}

	public double getEnd() {
		return end;
	}

	public void setEnd(double end) {
		this.end = end;
	}

	public double getStart() {
		return start;
	}

	public void setStart(double start) {
		this.start = start;
	}

	public double getTickStep() {
		return tickStep;
	}

	public void setTickStep(double tickStep) {
		this.tickStep = tickStep;
	}

	public boolean isLog() {
		return log;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	public boolean inAxisRange(double v) {
		return (v >= getStart() && v <= getEnd());
	}
}
