package dynsim.math.analysis.nonlinear.test;

import dynsim.math.analysis.Correlation;
import dynsim.simulator.iteratedmap.IteratedMapSimulator;
import dynsim.simulator.iteratedmap.system.IteratedMap;
import dynsim.simulator.iteratedmap.system.impl.*;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.*;
import junit.framework.TestCase;

public class CorrelationTest extends TestCase {
	public void test() {
		try {
			OdeSystem sys = new Rossler();
			OdeSimulator sim = new OdeSimulator(sys);
			sim.setSkip(1000);
			sim.setItersMax(7000);

			IteratedMap esys = new Henon();
			IteratedMapSimulator ims = new IteratedMapSimulator(esys);
			ims.setSkip(1000);
			ims.setItersMax(3000);
			ims.compute();

			sim.compute();
			double[][] r = sim.getRawResults();
			r = delTime(r);
			Correlation cor = new Correlation(r);
			cor.compute();
			System.out.println("Flow D2: " + cor.D2);

			r = ims.getRawResults();
			cor = new Correlation(r);
			cor.compute();
			System.out.println(cor.D2);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private double[][] delTime(double[][] vec) {
		double[][] tmp = new double[vec.length - 1][vec[0].length];
		System.arraycopy(vec, 1, tmp, 0, tmp.length);
		return tmp;
	}
}
