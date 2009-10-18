package dynsim.simulator.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import Jama.Matrix;
import dynsim.math.numeric.differentiation.Ridders;
import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.Parameters;

public abstract class AbstractDynamicalSystem implements DynamicalSystem {
	protected final Parameters params;

	protected final Parameters initConditions;

	protected final Collection<RealFunction> funcs;

	private int type;

	public AbstractDynamicalSystem() {
		params = new Parameters();
		initConditions = new Parameters();
		funcs = new ArrayList<RealFunction>();
		type = DynamicalSystem.UNKNOWN_T;
	}

	public void addFunction(RealFunction f) {
		funcs.add(f);
	}

	public Collection<RealFunction> getFunctions() {
		return funcs;
	}

	public double[] getGradient(final double[] point) {
		final Iterator<RealFunction> itor = funcs.iterator();
		final int nvars = getIndepVarsNumNoTime();
		final double[] g = new double[nvars];
		final Ridders diffmethod = new Ridders();
		int i = 0;
		while (itor.hasNext()) {
			final RealFunction f = itor.next();
			g[i] = diffmethod.compute(f, point, i, 0.15, 10);
			i++;
		}

		return g;
	}

	public String[] getIndependentVarNames() {
		final Collection<String> tmp = initConditions.getLabels();
		final String[] res = new String[tmp.size()];
		return tmp.toArray(res);
	}

	public int getIndependentVarsNum() {
		return initConditions.size();
	}

	public int getIndepVarsNumNoTime() {
		return getIndependentVarsNum();
	}

	public Parameters getInitialConditions() {
		return initConditions;
	}

	public double[] getInitialValues() {
		return initConditions.getValuesAsArray();
	}

	public Matrix getJacobianMatrix(final double[] point) {
		final Iterator<RealFunction> itor = funcs.iterator();
		final int nvars = getIndepVarsNumNoTime();
		final Ridders diffmethod = new Ridders();

		final double[][] jacobian = new double[nvars][funcs.size()];
		int fi = 0;

		while (itor.hasNext()) {
			final RealFunction f = itor.next();
			for (int i = 0; i < nvars; i++) {
				jacobian[i][fi] = diffmethod.compute(f, point, i, 0.15, 10);
			}
			fi++;
		}

		return new Matrix(jacobian);
	}

	public double getParameter(final String name) {
		return params.getValue(name);
	}

	public Parameters getParameters() {
		return params;
	}

	public int getType() {
		return type;
	}

	public int getVarPosition(String name) {
		final Iterator<String> tmp = initConditions.getLabels().iterator();

		int p = 0;
		while (tmp.hasNext()) {
			if (tmp.next().equals(name)) {
				return p;
			}
			p++;
		}

		return -1;
	}

	@Override
	public void resetInitialConditions() {
		initConditions.clear();
	}

	@Override
	public void resetParameters() {
		params.clear();
	}

	public void setInitialCondition(final String label, final double value) {
		initConditions.put(label, value);
	}

	public void setInitialConditions(final double[] conds) {
		initConditions.putAll(conds);
	}

	public void setParameter(String label, double value) {
		params.put(label, value);
	}

	public void setParameters(Parameters params) {
		this.params.putAll(params);
	}

	public void setType(int type) {
		this.type = type;
	}
}
