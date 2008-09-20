package dynsim.simulator.system.interp;

import java.util.Iterator;

import dynsim.math.numeric.function.RealFunction;

import edu.hws.jcm.data.ExpressionProgram;
import edu.hws.jcm.data.MathObject;
import edu.hws.jcm.data.Variable;

public interface ParserWrapper {

	// TODO Si hiciese falta utilizar otro tipo de parser
	// se generalizarán los objetos Variable, MathObj etc...
	// que ahora dependen de jcm

	public abstract void add(MathObject arg0);

	public abstract void addOptions(int arg0);

	public abstract void addExpression(String expr);

	// Ej. "lambda(x,y) (x^2+y^2)"
	public abstract void addFunction(String name, Variable[] vars, String expr);

	public abstract Iterator<ExpressionProgram> iterator();

	public abstract Variable getVariable(String varname);

	public abstract RealFunction getRealFunction(int indx);

	public abstract int size();
}