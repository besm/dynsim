package dynsim.math.numeric.solver.nonlinear;

import dynsim.math.numeric.solver.AbstractRootFinder;
import dynsim.math.vector.VectorN;
import dynsim.math.vector.VectorOps;
import dynsim.simulator.system.DynamicalSystem;

public class GradientDescent extends AbstractRootFinder {
	private int varsNum;

	private double[] funcVal;

	private double[] varsVal;

	private DynamicalSystem sys;

	private double g1;

	private double h1;

	private double h3;

	private double g3;

	private double g2;

	private double alfa0;

	private double g0;

	private double alfa2;

	private double alfa3;

	private double alfa1;

	public GradientDescent(DynamicalSystem sys) {
		setSystem(sys);
		g0 = -1;
		g1 = -1;
		g2 = -1;
		g3 = -1;
		h1 = -1;
		h3 = -1;
	}

	public void setSystem(DynamicalSystem sys) {
		varsNum = sys.getIndepVarsNumNoTime();
		this.sys = sys;
		init();
	}

	public void solve() {
		etol = 1E-5;
		alfa3 = 1;
		alfa2 = 0.5;
		alfa1 = 0;

		newPoint(start);

		int aprox = 2;
		for (int i = 0; i < aprox; i++) {
			// System.out.println("p"+i+" "+results[0]+","+results[1]);
			newPoint(results);
		}
	}

	private void newPoint(double[] begin) {
		setInitialValues(begin);

		VectorN z1 = new VectorN(varsNum);
		g1 = fg(z1);

		VectorN p0 = new VectorN(begin);
		p0.substract(VectorOps.multiply(z1, alfa1));
		varsVal = p0.getArray();
		g1 = fg(z1);

		p0 = new VectorN(begin);
		p0.substract(VectorOps.multiply(z1, alfa3));
		varsVal = p0.getArray();
		VectorN z2 = new VectorN(varsNum);
		g3 = fg(z2);

		if (g3 < g1) {
			p0 = new VectorN(begin);
			p0.substract(VectorOps.multiply(z2, alfa2));
			varsVal = p0.getArray();

			VectorN z3 = new VectorN(varsNum);
			g2 = fg(z3);

			h1 = (g2 - g1) / (alfa2 - 0);
			double h2 = (g3 - g2) / (alfa3 - alfa2);
			h3 = (h2 - h1) / (alfa3);

			alfa0 = secant(150000, etol, 0, 0.00015);

			p0 = new VectorN(begin);
			p0.substract(VectorOps.multiply(z3, alfa0));
			varsVal = p0.getArray();

			g0 = fg(null);

			if (g0 < g1 && g0 < g3) {
				// next point!
				results = p0.getArrayCopy();
				status = OK;
				return;
			}

			// results = p0.getArrayCopy();
		}

		// while (curIter < itersNum) {
		//
		// curIter++;
		// } // end while

		status = NO_CONVERGE;
	}

	private double f(double alfa) {
		return g1 + h1 * alfa + h3 * alfa * (alfa - alfa2);
	}

	// TODO mejorar y sacar de aquí
	// se puede emplear para localizar puntos de eq.
	// en sistemas discretos
	private double secant(int n, double del, double x, double dx) {
		int k = 0;
		double x1 = x + dx;
		while ((Math.abs(dx) > del) && (k < n)) {
			double d = f(x1) - f(x);

			if (d == 0)
				return 0;

			double x2 = x1 - f(x1) * (x1 - x) / d;
			x = x1;
			x1 = x2;
			dx = x1 - x;
			k++;
		}
		if (k == n) {
			// System.out.println("Convergence not" + " found after " + n
			// + " iterations");
		}

		return x1;
	}

	private double fg(VectorN z) {
		funcVal = eval();
		VectorN fx = new VectorN(funcVal);
		VectorN vv = (VectorN) VectorOps.mul(fx, fx);
		double gp = sum(vv);

		VectorN grad = new VectorN(sys.getGradient(varsVal));
		if (grad.isZero()) {
			return 0;
		}
		grad.multiply(2);
		grad = (VectorN) VectorOps.mul(grad, fx);

		double z0 = grad.norm();

		if (z != null && z0 != 0)
			z.set(VectorOps.multiply(grad, 1 / z0).getArray());

		return gp;
	}

	private double sum(VectorN vv) {
		double sum = 0;
		for (int i = 0; i < vv.componentsNum(); i++) {
			sum += vv.get(i);
		}
		return sum;
	}

	private void init() {
		initMats();
		itersNum = 150;
		etol = 0.00001;
		status = NO_CONVERGE;
	}

	private void initMats() {
		funcVal = new double[varsNum];
		varsVal = new double[varsNum];
		funcVal = new double[varsNum];
		results = new double[varsNum];
	}

	private void setInitialValues(double[] vars) {
		varsVal = vars.clone();
	}

	private double[] eval() {
		if (sys.getIndependentVarsNum() > varsNum) {
			double[] vtemp = new double[sys.getIndependentVarsNum()];
			System.arraycopy(varsVal, 0, vtemp, 1, varsVal.length);
			vtemp = sys.eval(vtemp);
			double[] rtemp = new double[varsNum];
			System.arraycopy(vtemp, 1, rtemp, 0, rtemp.length);
			return rtemp;
		} else {
			return sys.eval(varsVal);
		}
	}

	/*
	 * private boolean procFound(double err) { // Actualizamos siempre a la
	 * aprox. actual System.arraycopy(varsVal, 0, results, 0, varsVal.length);
	 * 
	 * if (err <= etol) { status = OK; return true; } return false; }
	 */
}
