package dynsim.data;

public interface Storage {
	public abstract double[] getAll(String name);

	public abstract double[] getAll(int i);

	public abstract void add(double[] holder);

	public abstract void setColumnNames(String[] names);

	public abstract String[] getColumnNames();

	public abstract int getColumnsNum();

	public abstract int getRowsNum();

	public abstract String getColumnName(int i);

	public abstract double get(String name, int r);

	public abstract double get(int c, int r);

	public abstract void remove();

	public abstract void put(int c, int r, double v);

	public abstract void setColumnsNum(int n);

	public abstract void setCurrentRowIndex(int n);

	public abstract double[][] getRawData();

	public abstract double getMax(int i);

	public abstract double getMin(int i);

	public abstract double getMax(String name);

	public abstract double getMin(String name);

	public abstract double getMax();

	public abstract double getMin();

	public abstract boolean exists(String name);

	public abstract boolean hasMaxMin();

	public abstract double[] getAllForRow(int r);
}