package dynsim.math.numeric.solver;

public abstract class AbstractSolver implements IterativeSolver {
	protected int status;

	protected double[] results;

	protected int itersNum;

	protected double etol; // tolerancia

	public int getStatus() {
		return status;
	}

	public double[] getResults() {
		return (double[]) results.clone();
	}

	public void setItersNum(int itersNum) {
		this.itersNum = itersNum;
	}

	public void setErrorTolerance(double etol) {
		this.etol = etol;
	}

	public int getItersNum() {
		return itersNum;
	}

	public double getErrorTolerance() {
		return etol;
	}
}
