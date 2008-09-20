package dynsim.math.numeric.solver;

public abstract class AbstractRootFinder extends AbstractSolver {
	public static final int NO_CONVERGE = 1;

	public static final int NOT_FOUND = 2;

	public static final int JACOBIAN_NO_INVERSE = 3;

	protected double[] start;

	public void setStartSearch(double[] start) {
		this.start = start;
	}
}
