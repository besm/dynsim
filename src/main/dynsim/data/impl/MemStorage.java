package dynsim.data.impl;

import dynsim.data.AbstractStorage;

public class MemStorage extends AbstractStorage {
	private static final int GROW_SIZE = 10;

	private double[][] datum;

	public MemStorage(double[][] results, String[] names) {
		super(names);
		datum = results;
		currentRowIndex = 0;
		rowsNum = datum[0].length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.simulator.ResultsData#get(int)
	 */
	public double[] getAll(int i) {
		return datum[i];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.simulator.ResultsData#add(double[])
	 */
	public void add(double[] holder) {
		if (currentRowIndex > getAvailableRows()) {
			growArray();
		}

		for (int n = 0; n < colsNum; n++) {
			datum[n][currentRowIndex] = holder[n];
			notifyValue(n, holder[n]);
		}

		currentRowIndex++;
	}

	public void setData(double[][] data) {
		this.datum = data;
	}

	public double[][] getData() {
		return datum;
	}

	public int getAvailableRows() {
		if (datum == null)
			return 0;

		return getRowsNum();
	}

	private void growArray() {
		double[][] resL = new double[currentRowIndex][datum[0].length + GROW_SIZE];
		for (int i = 0; i < currentRowIndex; i++)
			System.arraycopy(datum[i], 0, resL[i], 0, datum[0].length);
		datum = resL;
	}

	public double get(int c, int r) {
		return datum[c][r];
	}

	public void put(int c, int r, double v) {
		datum[c][r] = v;
		notifyValue(c, v);
	}

	public double[][] getRawData() {
		return datum;
	}

	// public DataInputStream getDataInputStream(int n) {
	// double[] dat = getAll(n);
	// byte[] doub = new byte[8];
	//
	// // In mem -> loadAll
	// byte[] buff = new byte[dat.length * 8];
	// for (int i = 0; i < dat.length; i++) {
	// doub = longToByteArray(Double.doubleToLongBits(dat[i]));
	// System.arraycopy(doub, 0, buff, i * 8, doub.length);
	// }
	// return new DataInputStream(new ByteArrayInputStream(buff));
	// }

	// private byte[] longToByteArray(long l) {
	// return BigInteger.valueOf(l).toByteArray();
	// }
}
