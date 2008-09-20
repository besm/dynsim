package dynsim.simulator.system.interp.test;

import java.util.Iterator;

import dynsim.simulator.system.interp.JcmParserWrapper;
import dynsim.simulator.system.interp.ParserWrapper;
import edu.hws.jcm.data.ExpressionProgram;
import edu.hws.jcm.data.Variable;
import junit.framework.TestCase;

public class JcmParserWrapperTest extends TestCase {

	/*
	 * Test method for
	 * 'dynsim.simulator.system.interp.JcmParserWrapper.JcmParserWrapper()'
	 */
	public void testJcmParserWrapper() {
		ParserWrapper parser = new JcmParserWrapper();
		// variables para los argumentos de la func
		Variable f0 = new Variable("f0");
		Variable f1 = new Variable("f1");
		parser.add(f0);
		parser.add(f1);
		parser.addFunction("lambda", new Variable[] { f0, f1 }, "(f0^2+f1^2)");
		parser.addExpression("lambda(2,2)");
		Iterator<ExpressionProgram> itor = parser.iterator();
		double res = itor.next().getVal();
		assertEquals(8., res);
	}

}
