package dynsim.simulator.system.interp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dynsim.math.numeric.function.RealFunction;

import edu.hws.jcm.data.Expression;
import edu.hws.jcm.data.ExpressionProgram;
import edu.hws.jcm.data.MathObject;
import edu.hws.jcm.data.Parser;
import edu.hws.jcm.data.Variable;
import edu.hws.jcm.functions.ExpressionFunction;

public class JcmParserWrapper implements ParserWrapper {
	protected Parser parser;

	protected List<ExpressionProgram> exprs;

	public JcmParserWrapper() {
		parser = new Parser();
		exprs = new ArrayList<ExpressionProgram>();
	}

	public void add(MathObject arg0) {
		parser.add(arg0);
	}

	public void addOptions(int arg0) {
		parser.addOptions(arg0);
	}

	public void addExpression(String expr) {
		ExpressionProgram exp = parser.parse(expr);
		exprs.add(exp);
	}

	public void addFunction(String name, Variable[] vars, String exprstr) {
		Expression expr = parser.parse(exprstr);
		ExpressionFunction ef = new ExpressionFunction(name, vars, expr);
		parser.add(ef);
	}

	public Iterator<ExpressionProgram> iterator() {
		return exprs.iterator();
	}

	public Variable getVariable(String varname) {
		return (Variable) parser.get(varname);
	}

	public RealFunction getRealFunction(int indx) {
		return new fexp(exprs.get(indx));
	}

	public int size() {
		return exprs.size();
	}

	class fexp implements RealFunction {
		Expression exp;

		public fexp(ExpressionProgram exp) {
			this.exp = exp;
		}

		public double eval(double[] vars) {
			for (int i = 0; i < vars.length; i++) {
				Variable v = getVariable("x" + i);
				v.setVal(vars[i]);
			}
			return exp.getVal();
		}
	}
}
