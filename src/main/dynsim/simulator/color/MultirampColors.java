package dynsim.simulator.color;

import dynsim.data.Storage;
import dynsim.graphics.color.scheme.ColorScheme;
import dynsim.graphics.color.scheme.impl.Rainbow;
import dynsim.math.util.MathUtils;
import dynsim.math.vector.Vector2D;
import dynsim.math.vector.Vector3D;

public class MultirampColors implements ColoringStrategy {
	protected int[] colors;

	private int[][] pals;

	private boolean loaded;

	public MultirampColors() {
		loaded = false;
	}

	public void initPals(ColorScheme sch) {
		pals = sch.loadPals();
		loaded = true;
	}

	public int[] getColors(Storage data) {
		if (!loaded) {
			initPals(new Rainbow());
		}

		int siz = data.getRowsNum();
		colors = new int[siz];

		double min = 1;
		double max = 0;

		double[] value = new double[siz];

		int[] colsIds = checkForTemporalColumn(data);

		for (int i = 1; i < siz; i++) {
			double[] diffs = new double[colsIds.length];
			for (int c = 0; c < diffs.length; c++) {
				diffs[c] = data.get(colsIds[c], i) - data.get(colsIds[c], i - 1);
			}

			value[i] = getValue(diffs);

			if (max < value[i])
				max = value[i];
			if (min > value[i])
				min = value[i];
		}

		for (int i = 1; i < siz; i++) {
			setColor(value[i], i, siz, min, max);
		}

		return colors;
	}

	private int[] checkForTemporalColumn(Storage data) {
		int[] colsIds;
		if (data.exists("t")) {
			int cn = data.getColumnsNum() - 1;
			colsIds = new int[cn];
			for (int i = 0; i < cn; i++) {
				colsIds[i] = i + 1;
			}
		} else {
			int cn = data.getColumnsNum();
			colsIds = new int[cn];
			for (int i = 0; i < cn; i++) {
				colsIds[i] = i;
			}
		}

		return colsIds;
	}

	private void setColor(double mag, int i, int siz, double min, double max) {
		int p = 0;
		int mm = pals[p].length - 1;
		double v = Math.abs(mm / (max - min) * (mag - max) + mm);
		int c = (int) v;

		colors[i] = pals[p][(int) c];
	}

	private double getValue(double[] diffs) {
		double L = 0;
		if (diffs.length == 2) {
			Vector2D v;
			v = new Vector2D(diffs[0], diffs[1]);
			L = v.norm();
		} else {
			Vector3D v;
			v = new Vector3D(diffs[0], diffs[1], diffs[2]);
			L = v.norm();
		}

		double value = Math.abs(L);

		return value;
	}

	protected void setColor(double v, int i, double div, int n) {
		double value = Math.abs((255 / div) * v);
		int c = (int) MathUtils.trunc(value, 0);

		int[] pal = pals[n];
		colors[i] = pal[c];
	}
}
