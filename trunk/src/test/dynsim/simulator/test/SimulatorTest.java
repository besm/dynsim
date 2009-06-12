package dynsim.simulator.test;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.ode.system.impl.Crispy;
import dynsim.simulator.system.DynamicalSystem;
import junit.framework.TestCase;

public class SimulatorTest extends TestCase {
	public void testSimple() throws DynSimException {
		DynamicalSystem sys;
		sys = new Crispy();
		Simulator dyn = SimulatorFactory.createSimulator(sys);
		dyn.setSkip(1000);
		dyn.setItersMax(100000);
		dyn.compute();
		Storage stateSeries = dyn.getStateSeries();
		double max = stateSeries.getMax();
		double testValue = stateSeries.get(1, 1000);
		assertTrue(1499 < max && 1510 > max);
		assertTrue(1.7 < testValue && 1.8 > testValue);
	}

	public void testMulti() throws DynSimException {
		DynamicalSystem sys;
		sys = new Crispy();
		Simulator dyn = SimulatorFactory.createSimulator(sys);
		dyn.setSkip(1000);
		dyn.setItersMax(100000);
		dyn.compute();
		Storage stateSeries = dyn.getStateSeries();
		double max = stateSeries.getMax();
		double testValue = stateSeries.get(1, 1000);
		assertTrue(1499 < max && 1510 > max);
		assertTrue(1.7 < testValue && 1.8 > testValue);
		
		dyn.reset();
		dyn.compute();
		stateSeries = dyn.getStateSeries();
		max = stateSeries.getMax();
		testValue = stateSeries.get(1, 1000);
		assertTrue(1499 < max && 1510 > max);
		assertTrue(1.7 < testValue && 1.8 > testValue);
	}
}
