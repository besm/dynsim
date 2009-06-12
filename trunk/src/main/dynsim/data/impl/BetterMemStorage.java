package dynsim.data.impl;

import dynsim.data.AbstractStorage;
import array.InvalidArrayAxisException;
import array.InvalidArrayShapeException;
import array.doubleArray2D;

public class BetterMemStorage extends AbstractStorage {
	private doubleArray2D datum;

	public BetterMemStorage(final double[][] results, final String[] names) {
		super(names);
		this.rowsNum = results[0].length;

		put(results);
	}

	private void put(final double[][] results) {
		try {
			datum = new doubleArray2D(results);
			for (int i = 0; i < results.length; i++) {
				for (int j = 0; j < rowsNum; j++) {
					notifyValue(i, datum.get(i, j));
				}
			}
		} catch (InvalidArrayShapeException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	public BetterMemStorage(final int colsNum, final int rowsNum, String[] names) {
		super(names);
		this.rowsNum = rowsNum;

		try {
			datum = new doubleArray2D(colsNum, rowsNum);
		} catch (InvalidArrayShapeException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.simulator.ResultsData#get(int)
	 */
	public double[] getAll(final int i) {
		return (datum.toJava())[i];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.simulator.ResultsData#add(double[])
	 */
	public void add(double[] holder) {
		for (int n = 0; n < colsNum; n++) {
			datum.set(n, currentRowIndex, holder[n]);
			notifyValue(n, holder[n]);
		}

		currentRowIndex++;
	}

	public doubleArray2D getData() {
		return datum;
	}

	public int getAvailableRows() {
		if (datum == null)
			return 0;

		try {
			return datum.size(0);
		} catch (InvalidArrayAxisException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public double get(final int c, final int r) {
		return datum.get(c, r);
	}

	public void put(final int c, final int r, final double v) {
		datum.set(c, r, v);
		notifyValue(c, v);
	}

	public double[][] getRawData() {
		return datum.toJava();
	}
}
