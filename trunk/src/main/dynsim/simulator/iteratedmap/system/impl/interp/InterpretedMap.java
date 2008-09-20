/**
 * 
 */
package dynsim.simulator.iteratedmap.system.impl.interp;

import java.util.Iterator;

import dynsim.simulator.iteratedmap.system.AbstractIteratedMap;
import dynsim.simulator.system.interp.InterpretedSystem;
import dynsim.simulator.system.interp.JcmParserWrapper;
import dynsim.simulator.system.interp.ParserWrapper;
import edu.hws.jcm.data.ExpressionProgram;
import edu.hws.jcm.data.Variable;

/**
 * @author maf83
 * 
 */
public class InterpretedMap extends AbstractIteratedMap implements InterpretedSystem {
	private ParserWrapper wrapper;

	public InterpretedMap() {
		wrapper = new JcmParserWrapper();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.simulator.iteratedmap.system.IteratedMapSystem#eval(double[])
	 */
	public double[] eval(double[] x) {
		double[] r = new double[x.length];
		Iterator<ExpressionProgram> itor = wrapper.iterator();
		int c = 0;

		for (int i = 0; i < r.length; i++) {
			Variable v = wrapper.getVariable("x" + i);
			v.setVal(x[i]);
		}

		while (itor.hasNext()) {
			r[c] = itor.next().getVal();
			c++;
		}

		return r;
	}

	public ParserWrapper getWrapper() {
		return wrapper;
	}

	public void setWrapper(ParserWrapper wrapper) {
		this.wrapper = wrapper;
	}

}
