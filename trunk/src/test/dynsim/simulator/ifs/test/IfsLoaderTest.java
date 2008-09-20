package dynsim.simulator.ifs.test;

import junit.framework.TestCase;

import dynsim.exceptions.DynSimException;
import dynsim.simulator.ifs.IfsSimulator;
import dynsim.simulator.ifs.system.IteratedFunctionSystem;
import dynsim.simulator.ifs.system.impl.StochasticIfs;
import dynsim.simulator.ifs.system.loader.IfsDatum;
import dynsim.simulator.ifs.system.loader.IfsLoader;

public class IfsLoaderTest extends TestCase {
	IfsLoader il;

	IfsDatum data;

	public void setUp() {
		il = new IfsLoader();
		il.loadFromFile("data/test/ifs/ifs.xml");
		data = il.getIfsById("dragon");
	}

	public void test() {
		double[][] coeff = data.getCoefs();
		double[] prob = data.getProbs();

		assertTrue(prob[0] == .0);
		assertTrue(coeff[0][2] == -.212346);
	}

	public void testProcessing() throws DynSimException {
		IteratedFunctionSystem ifs = new StochasticIfs(data);
		IfsSimulator sim = new IfsSimulator(ifs);
		sim.setItersMax(200);
		sim.compute();
		double[][] results = sim.getRawResults();
		assertTrue(results[0][79] != .0);
	}

}
