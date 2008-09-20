package dynsim.math.numeric.solver;

public interface IterativeSolver {
	public final static int OK = 0;

	public final static int ERROR = 1;

	public double[] getResults();

	public int getStatus();

	public void solve();

	public void setItersNum(int itersNum);

	public int getItersNum();

	public void setErrorTolerance(double etol);

	public double getErrorTolerance();
}
