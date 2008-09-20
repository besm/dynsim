package dynsim.math.analysis.local;

import dynsim.data.Storage;

public class LKOrbits {

	double epsilon;

	private Storage data;

	public LKOrbits(Storage data) {
		this.data = data;
		epsilon = 0.001;
	}

	public void compute() {
		// computeHistogram();
		findCycles();
	}

	// Búsqueda de órbitas estables por el método
	// de establecimiento
	// TODO criterio de Bendixon
	private void findCycles() {
		// Tomamos el primer punto que debe haber
		// pasado la transición inestable
		double[] x0 = data.getAllForRow(0);

		// i = min t
		for (int i = 1; i < data.getRowsNum(); i++) {
			double[] xt = data.getAllForRow(i);

			if (isNear(x0, xt)) {
				System.out.println(i + " time distance for closure at" + " x1: " + x0[1] + " y1: " + x0[2] + " x2: "
						+ xt[1] + " y2: " + xt[2]);
				break;
			}
		}

	}

	private boolean isNear(double[] x0, double[] xt) {
		double sum = 0, term;
		if (data.getColumnNames()[0].equals("t")) {
			for (int i = 1; i < x0.length; i++) {
				term = xt[i] - x0[i];
				term *= term;
				sum += term;
			}
		}

		return (Math.sqrt(sum) < epsilon);
	}

}
