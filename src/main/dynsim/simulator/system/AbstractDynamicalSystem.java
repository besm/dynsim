package dynsim.simulator.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import Jama.Matrix;
import dynsim.math.numeric.differentiation.Ridders;
import dynsim.math.numeric.function.RealFunction;
import dynsim.simulator.Parameters;

public abstract class AbstractDynamicalSystem implements DynamicalSystem {
	protected Parameters params;

	protected Parameters initConditions;

	protected Collection<RealFunction> funcs;

	private int type;

	public AbstractDynamicalSystem() {
		params = new Parameters();
		initConditions = new Parameters();
		funcs = new ArrayList<RealFunction>();
		type = DynamicalSystem.UNKNOWN_T;
	}

	public String[] getIndependentVarNames() {
		Collection<String> tmp = initConditions.getLabels();
		String[] res = new String[tmp.size()];
		return tmp.toArray(res);
	}

	public int getIndependentVarsNum() {
		return initConditions.size();
	}

	public int getIndepVarsNumNoTime() {
		return getIndependentVarsNum();
	}

	public double[] getInitialValues() {
		return initConditions.getValuesAsArray();
	}

	public Parameters getInitialConditions() {
		return initConditions;
	}

	public double getParameter(String name) {
		return params.getValue(name);
	}

	public Parameters getParameters() {
		return params;
	}

	public void setParameters(Parameters params) {
		this.params = params;
	}

	public void setInitialCondition(String label, double value) {
		initConditions.put(label, value);
	}

	public void setParameter(String label, double value) {
		params.put(label, value);
	}

	public void addFunction(RealFunction f) {
		funcs.add(f);
	}

	public int getVarPosition(String name) {
		Iterator<String> tmp = initConditions.getLabels().iterator();

		int p = 0;
		while (tmp.hasNext()) {
			if (tmp.next().equals(name)) {
				return p;
			}
			p++;
		}

		return -1;
	}

	public Collection<RealFunction> getFunctions() {
		return funcs;
	}

	public double[] getGradient(double[] point) {
		Iterator<RealFunction> itor = funcs.iterator();
		int nvars = getIndepVarsNumNoTime();
		double[] g = new double[nvars];
		Ridders diffmethod = new Ridders();
		int i = 0;
		while (itor.hasNext()) {
			RealFunction f = itor.next();
			g[i] = diffmethod.compute(f, point, i, 0.15, 10);
			i++;
		}

		return g;
	}

	public Matrix getJacobianMatrix(double[] point) {
		Iterator<RealFunction> itor = funcs.iterator();
		int nvars = getIndepVarsNumNoTime();
		Ridders diffmethod = new Ridders();

		double[][] jacobian = new double[nvars][funcs.size()];
		int fi = 0;

		while (itor.hasNext()) {
			RealFunction f = itor.next();
			for (int i = 0; i < nvars; i++) {
				jacobian[i][fi] = diffmethod.compute(f, point, i, 0.15, 10);
			}
			fi++;
		}

		return new Matrix(jacobian);
	}

	public void setInitialConditions(double[] conds) {
		initConditions.putAll(conds);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
