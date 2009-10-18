package dynsim.math.analysis.nonlinear.test;

import junit.framework.TestCase;
import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.math.analysis.rqa.RQA;
import dynsim.math.analysis.rqa.RecurrencePlot;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.Ueda;
import dynsim.simulator.system.DynamicalSystem;

public class RecurrenceTest extends TestCase {
	public void testRecurrence() throws DynSimException {
		 DynamicalSystem sys = new Ueda();
		 sys.setParameter("a", 1);
		 
		 OdeSimulator sim = new OdeSimulator((OdeSystem) sys);
		 sim.setItersMax(4000);
		 sim.setSkip(2000);

		sim.compute();
		Storage serie = sim.getStateSeries();
		RecurrencePlot rp = new RecurrencePlot(serie, serie.getColumnNames());
		rp.compute();
		RQA rqa = rp.getRQA();
		rqa.getEntropy();
	}
}
