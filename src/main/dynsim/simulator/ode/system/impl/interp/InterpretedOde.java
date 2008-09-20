package dynsim.simulator.ode.system.impl.interp;

import java.util.Iterator;

import Jama.Matrix;

import dynsim.simulator.ode.system.AbstractOdeSystem;
import dynsim.simulator.system.interp.InterpretedSystem;
import dynsim.simulator.system.interp.JcmParserWrapper;
import dynsim.simulator.system.interp.ParserWrapper;
import edu.hws.jcm.data.ExpressionProgram;
import edu.hws.jcm.data.Variable;

public class InterpretedOde extends AbstractOdeSystem implements InterpretedSystem {

	private ParserWrapper wrapper;

	public InterpretedOde() {
		wrapper = new JcmParserWrapper();
	}

	public double[] eval(double[] x, double t) {
		int dim = getIndependentVarsNum();
		double[] rhs = new double[dim];

		Iterator<ExpressionProgram> itor = wrapper.iterator();
		int c = 1;

		rhs[0] = 1.; // tiempo

		for (int i = 0; i < rhs.length - 1; i++) {
			Variable v = wrapper.getVariable("x" + i);
			v.setVal(x[i + 1]);
		}

		while (itor.hasNext()) {
			rhs[c] = itor.next().getVal();
			c++;
		}

		return rhs;
	}

	public ParserWrapper getWrapper() {
		return wrapper;
	}

	public void setWrapper(ParserWrapper wrapper) {
		this.wrapper = wrapper;
	}

	@Override
	public Matrix getJacobianMatrix(double[] point) {
		setFunctions();
		return super.getJacobianMatrix(point);
	}

	private void setFunctions() {
		if (funcs.isEmpty()) {
			int siz = wrapper.size();
			for (int i = 0; i < siz; i++) {
				addFunction(wrapper.getRealFunction(i));
			}
		}
	}
}
