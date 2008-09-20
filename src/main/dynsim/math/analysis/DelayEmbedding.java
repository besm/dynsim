package dynsim.math.analysis;

import dynsim.data.Storage;
import dynsim.data.impl.BetterMemStorage;
import dynsim.math.vector.VectorN;

public class DelayEmbedding {
	public static Storage embed(double[] ts, int dimension, int delay) {
		// dimension > 2D + 1 where D = dimension of the system

		String[] names = new String[dimension];

		for (int i = 0; i < dimension; i++) {
			names[i] = "x" + i;
		}

		return embed(ts, names, delay);
	}

	public static Storage embed(double[] ts, String[] names, int delay) {
		int M = names.length;
		double[] holder = new double[M];
		Storage out = new BetterMemStorage(M, ts.length, names);
		for (int t = 0; t < ts.length - ((M - 1) * delay); t++) {
			for (int n = 0; n < M; n++) {
				holder[n] = ts[t + (n * delay)];
			}
			out.add(holder);
		}
		return out;
	}

	// TODO Estimate time delay

	public static int estimateDimension(double[] ts, int delay) {
		int d = 2;

		boolean found = false;

		VectorN y0, y1;

		int falseNN = 0;

		int maxDim = 100;

		while (!found && d < maxDim) {
			y0 = new VectorN(d);
			y1 = new VectorN(d);

			double x0 = 0, x1 = 0;

			for (int k = 0; k < ts.length - (delay + d); k++) {

				for (int dn = 0; dn < d; dn++) {
					x0 = ts[k];
					y0.set(dn, x0);

					x1 = ts[k + d]; // nearest neighbourgh
					y1.set(dn, x1);
				}
				x0 = ts[k + delay];
				x1 = ts[k + delay + d];

				double dx = Math.abs(x0 - x1);
				double dy = y0.norm(y1);

				double isf = (dx / dy);
				if (isf > 10) {
					falseNN++;
				}
			}

			double rat = 0;

			if (falseNN != 0) {
				rat = falseNN / (ts.length - (1d + d));
			}

			falseNN = 0;

			if (rat < 0.01) {
				found = true;
			} else {
				d++;
			}

			// if(!found) {
			// //throws expect...
			// }

		}
		return d;
	}
}
