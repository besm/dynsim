package dynsim.graphics.plot;

import dynsim.data.Storage;
import dynsim.graphics.plot.config.GrapherConfig;

public class GrapherUtils {

	public static void loadPart3Array(final int pnum, final double[] x, final double[] y, final int b, final double[] ct) {
		for (int j = 0, k = 0; j < ct.length; j += 3, k++) {
			if (pnum > 0 && k == 0) {
				ct[j] = x[b - 1];
				ct[j + 1] = y[b - 1];
				ct[j + 2] = 0;
				j += 3;
				k++;
				ct[j] = x[b];
				ct[j + 1] = y[b];
				ct[j + 2] = 0;
			} else {
				ct[j] = x[b + k];
				ct[j + 1] = y[b + k];
				ct[j + 2] = 0;
			}
		}
	}

	public static int loadPart(final Grapher grapher, final int parts, final int b, final double[] tmpx,
			final double[] tmpy, int i) {

		final Storage data = grapher.getCurrentData();
		final GrapherConfig config = grapher.getGrapherConfig();

		if (parts > 0 && i == 0) {
			tmpx[i] = data.get(config.getPlotVarX(), b - 1);
			tmpy[i] = data.get(config.getPlotVarY(), b - 1);
			i++;
			tmpx[i] = data.get(config.getPlotVarX(), b);
			tmpy[i] = data.get(config.getPlotVarY(), b);
		} else {
			tmpx[i] = data.get(config.getPlotVarX(), b + i);
			tmpy[i] = data.get(config.getPlotVarY(), b + i);
		}
		return i;
	}

}
