package dynsim.math.analysis.local;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import dynsim.graphics.plot.j2d.layer.Marker;
import dynsim.math.numeric.Computable;
import dynsim.math.numeric.solver.IterativeSolver;
import dynsim.math.numeric.solver.nonlinear.Broyden;
import dynsim.math.numeric.solver.nonlinear.NewtonHomotopy;
import dynsim.simulator.system.DynamicalSystem;

public class LocalStabilityAnalyser implements Computable {

	private DynamicalSystem sys;

	private Collection<CPoint> pts;

	private float itersNum;

	private double dt;

	private boolean useHomotopy;

	public static final int EQUILIBRIUM = 1;

	public static final int FIXED = 2;

	public static final int ALL = EQUILIBRIUM | FIXED;

	private int mode;

	public LocalStabilityAnalyser(DynamicalSystem sys) {
		this.sys = sys;
		this.pts = new ArrayList<CPoint>();
		this.itersNum = 50;
		this.dt = 10 / itersNum;
		// TODO homotopy mode flag
		this.useHomotopy = false;
		this.mode = EQUILIBRIUM;
	}

	// TODO buscar un buen método de raíces múltiples...
	public void compute() {
		double[] params = new double[sys.getIndepVarsNumNoTime()];

		if (useHomotopy && isEnabled(EQUILIBRIUM)) {
			NewtonHomotopy hom = new NewtonHomotopy(sys);

			for (int n = 0; n < params.length; n++) {
				params[n] = 0;
			}

			for (int n = 0; n < params.length; n++) {
				for (float i = 0; i < itersNum; i++) {
					params[n] = i * dt;
					hom.setStartSearch(params.clone());
					hom.solve();
					if (hom.getStatus() == IterativeSolver.OK) {
						doBroyden(hom.getResults());
					}
				}
			}
		} else {
			for (float i = -itersNum; i < itersNum; i++) {
				for (int n = 0; n < params.length; n++) {
					params[n] = i * dt;
				}

				// equilibria
				if (isEnabled(EQUILIBRIUM)) {
					doBroyden(params.clone());
				}

				// fixed
				if (isEnabled(FIXED)) {
					doSeidel(params.clone());
				}
			}
		}
	}

	public void setMode(int mask) {
		mode |= mask;
	}

	public void unsetMode(int mask) {
		mode &= ~mask;
	}

	public void toggleMode(int mask) {
		mode ^= mask;
	}

	public boolean isEnabled(int property) {
		return ((mode & property) == property);
	}

	public boolean isDisabled(int property) {
		return ((mode & property) != property);
	}

	// Get fixed points F(x) = x
	// period 1
	private void doSeidel(double[] params) {
		double[] X = null;
		if (sys.getTimeNature() == DynamicalSystem.CONTINOUS) {
			X = new double[params.length + 1];
			System.arraycopy(params, 0, X, 1, params.length);
		} else {
			X = new double[params.length];
		}

		double[] res = params;
		double[] Xt = X.clone();
		int k = 0;
		double sep = 1;
		double tol = 1E-5;
		int max = 99;

		while (k < max && sep > tol) {
			k++;
			double[] fdX = sys.eval(X);

			for (int i = 0; i < fdX.length; i++) {
				Xt[i] = fdX[i];
				fdX = sys.eval(Xt);
			}

			double sum = 0;
			for (int i = 0; i < Xt.length; i++) {
				sum += Math.abs(Xt[i] - X[i]);
			}

			sep = sum;

			X = Xt;
		}

		if (sep < tol) {
			int idx = 0;

			if (sys.getTimeNature() == DynamicalSystem.CONTINOUS) {
				idx = 1;
			}

			System.arraycopy(X, idx, res, 0, res.length);

			addPoint(res, FIXED);

			for (int n = 0; n < params.length; n++) {
				System.out.println(n + "FOUND " + res[n]);
			}
		}

	}

	// Get equilibrium points F(x)=0
	private void doBroyden(double[] params) {
		double[] res;
		Broyden br = new Broyden(sys);
		br.setStartSearch(params);
		br.solve();
		res = br.getResults();
		if (br.getStatus() == IterativeSolver.OK) {
			addPoint(res, EQUILIBRIUM);
			for (int n = 0; n < res.length; n++) {
				System.out.println(n + "FOUND " + res[n]);
			}
		}
	}

	private void addPoint(double[] pt, int type) {
		if (notIncluded(pt)) {
			CPoint fp = new CPoint(pt);
			fp.setType(type);
			localStability(fp);
			pts.add(fp);
		}
	}

	private void localStability(Marker fp) {
		Matrix jac = sys.getJacobianMatrix(fp.getCoords());
		EigenvalueDecomposition eig = jac.eig();
		double[] imag = eig.getImagEigenvalues();
		double[] real = eig.getRealEigenvalues();
		int st = Stability.isStable(real, imag);
		if (st == 0) {
			if (fp.getType() == FIXED) {
				fp.setColor(Color.pink.darker());
			} else {
				fp.setColor(Color.red.darker());
			}
		} else if (st == 1) {
			fp.setColor(Color.green.darker());
		} else {
			fp.setColor(Color.yellow.darker());
		}
	}

	// TODO hacky
	private boolean notIncluded(double[] pt) {
		if (pts.isEmpty())
			return true;

		Iterator<CPoint> it = pts.iterator();

		while (it.hasNext()) {
			Marker fp = it.next();
			double[] c = fp.getCoords();

			boolean eq = false;

			for (int i = 0; i < c.length; i++) {
				double diff = Math.rint(Math.abs(c[i] - pt[i]));

				if (diff < 1E-5) {
					eq = true;
				} else {
					eq = false;
					break;
				}
			}

			if (eq) {
				return false;
			}
		}

		return true;
	}

	public Collection<CPoint> getFixedPoints() {
		return pts;
	}

	public double getDt() {
		return dt;
	}

	public void setDt(double dt) {
		this.dt = dt;
	}

	public float getItersNum() {
		return itersNum;
	}

	public void setItersNum(float itersNum) {
		this.itersNum = itersNum;
	}

	public void setUseHomotopy(boolean b) {
		useHomotopy = b;
	}

}
