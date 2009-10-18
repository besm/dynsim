package dynsim.math.analysis.rqa;

import dynsim.data.Storage;
import dynsim.math.numeric.Computable;
import dynsim.math.vector.VectorN;

public class RecurrencePlot implements Computable {

	public static final int PLOT = 1;

	public static final int QANALYSIS = 2;

	public static final int FULL = PLOT | QANALYSIS;

	private int mode = FULL;

	private Storage inStor;

	protected int rowsNum;

	protected double threshold;

	private String[] names;

	private double maxDist;

	private RecurrencePlotDelegate plotDelegate;

	private int allpoints;

	double[] CRds;

	private double[] CLds;

	private int maxDiag;

	private int minLineLength;

	private boolean alreadyComputed;

	private int dim;

	private RQA rqa;

	public RecurrencePlot(Storage state, String[] names) {
		this(state, names, null);
		unsetMode(PLOT);
	}

	public RecurrencePlot(Storage state, String[] names, RecurrencePlotDelegate delegate) {
		this.inStor = state;
		this.rowsNum = state.getRowsNum();
		this.threshold = 0.5;
		this.names = names;
		this.maxDist = 0;
		this.plotDelegate = delegate;
		this.allpoints = 0;
		this.alreadyComputed = false;
		dim = names.length; // dimensions
		CRds = new double[dim];
		CLds = new double[250];
		this.minLineLength = 2;
	}

	public void compute() {
		recurrence();
	}

	private void recurrence() {
		for (int i = 0; i < rowsNum; i++) {
			for (int j = 0; j < rowsNum; j++) {

				double[] tmpi = new double[dim];
				double[] tmpj = new double[dim];

				if (i != j && needComputation(dim, i, j) && isEnabled(QANALYSIS)) {
					int maxL = checkRange(CLds.length, i, j);

					int dl = minLineLength - 1;
					for (int l = 0; l < maxL; l++) {

						load(i + l, j + l, tmpi, tmpj);
						double d = distance(tmpi, tmpj);
						int p2 = heaviside(threshold - d);

						// reached min line?
						if (l >= dl) {
							CLds[l - dl] += p2;

							if (CLds[l - dl] > 0) {
								if (l > maxDiag)
									maxDiag = l;
							}
						}

						if (p2 == 0)
							break;
					}

				}

				load(i, j, tmpi, tmpj);

				double d = distance(tmpi, tmpj);

				int p = heaviside(threshold - d);

				if (isEnabled(QANALYSIS) && !alreadyComputed) {
					if (d > maxDist) {
						maxDist = d;
					}

					if (i != j) {
						CRds[0] += p;
					}
				}

				if (isEnabled(PLOT)) {
					if (plotDelegate.isColored()) {
						// Global rplot
						plotDelegate.put(i, j, d);
					} else {
						if (p == 1) {
							// Local
							plotDelegate.put(i, j, p);
						}
					}
				}

				allpoints++;
			}
		}

		if (isEnabled(QANALYSIS) && !alreadyComputed) {
			System.out.println("Max dist: " + maxDist);

			rqa = new RQA(allpoints, CRds, CLds, maxDiag, minLineLength);
			System.out.println(rqa);
		}

		alreadyComputed = true;

		if (isEnabled(PLOT)) {
			plotDelegate.end();
		}
	}

	private void load(int i, int j, double[] tmpi, double[] tmpj) {
		for (int k = 0; k < dim; k++) {
			tmpi[k] = inStor.get(names[k], i);
			tmpj[k] = inStor.get(names[k], j);
		}
	}

	private int checkRange(int length, int i, int j) {
		if (i < rowsNum - length && j < rowsNum - length) {
			return length;
		} else {
			length = Math.min(rowsNum - i, rowsNum - j);
			return length;
		}
	}

	private boolean needComputation(int m, int i, int j) {
		return !alreadyComputed && i >= 1 && j >= 1;
	}

	private double distance(double[] tmpi, double[] tmpj) {
		VectorN vi = new VectorN(tmpi);
		VectorN vj = new VectorN(tmpj);

		// TODO switch norm type... default euclid
		return vi.norm(vj);
	}

	private int heaviside(double v) {
		if (0 <= v) {
			return 1;
		}
		return 0;
	}

	public void setThreshold(double eps) {
		this.threshold = eps;
	}

	public RQA getRQA() {
		return rqa;
	}

	public double getThreshold() {
		return threshold;
	}

	public double getMaxDist() {
		return maxDist;
	}

	public void setMode(int mask) {
		mode |= mask;
	}

	public void unsetMode(int mask) {
		mode &= ~mask;
	}

	public boolean isEnabled(int property) {
		return ((mode & property) == property);
	}

	public boolean isDisabled(int property) {
		return ((mode & property) != property);
	}

	public int getMinLineLength() {
		return minLineLength;
	}

	public void setMinLineLength(int minLineLength) {
		this.minLineLength = minLineLength;
	}

	public int getMode() {
		return mode;
	}
}
