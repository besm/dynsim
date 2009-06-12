package dynsim.data;

public abstract class AbstractStorage implements Storage {

	protected String[] columnNames;

	protected int currentRowIndex;

	protected int colsNum;

	protected int rowsNum;

	private double[] max;

	private double[] min;

	private boolean minmax;

	public AbstractStorage(final String[] names) {
		columnNames = names;
		colsNum = names.length;
		currentRowIndex = 0;
		rowsNum = 0;
		// TODO Double.MAX
		max = new double[colsNum];
		min = new double[colsNum];
		minmax = false;
	}

	protected int resolveName(final String name) {
		for (int i = 0; i < columnNames.length; i++) {
			if (columnNames[i].equalsIgnoreCase(name)) {
				return i;
			}
		}
		return 0;
	}

	protected void notifyValue(final int c, final double v) {
		minmax = true;

		if (max[c] < v) {
			max[c] = v;
		}
		if (min[c] > v) {
			min[c] = v;
		}
	}

	public void setColumnNames(final String[] names) {
		this.columnNames = names;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public String getColumnName(int i) {
		return columnNames[i];
	}

	public int getColumnsNum() {
		return colsNum;
	}

	public int getRowsNum() {
		return rowsNum;
	}

	public double[] getAll(final String name) {
		return getAll(resolveName(name));
	}

	public double[] getAllForRow(final int r) {
		double[] ret = new double[getColumnsNum()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = get(i, r);
		}
		return ret;
	}

	public double get(String name, int r) {
		return get(resolveName(name), r);
	}

	public void setColumnsNum(int n) {
		this.colsNum = n;
	}

	public void setCurrentRowIndex(int n) {
		this.currentRowIndex = n;
	}

	public void remove() {

	}

	public double[][] getRawData() {
		return null;
	}

	public double getMax(String name) {
		return getMax(resolveName(name));
	}

	public double getMax(int i) {
		return max[i];
	}

	public double getMin(String name) {
		return getMin(resolveName(name));
	}

	public double getMin(int i) {
		return min[i];
	}

	public double getMax() {
		double rm = max[0];
		for (int i = 1; i < max.length; i++) {
			if (rm < max[i]) {
				rm = max[i];
			}
		}
		return rm;
	}

	public double getMin() {
		double rm = min[0];
		for (int i = 1; i < min.length; i++) {
			if (rm > min[i]) {
				rm = min[i];
			}
		}
		return rm;
	}

	public boolean exists(String name) {
		for (int i = 0; i < columnNames.length; i++) {
			if (columnNames[i].equals(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasMaxMin() {
		return minmax;
	}
}
