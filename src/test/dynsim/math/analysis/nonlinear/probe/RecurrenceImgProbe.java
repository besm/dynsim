package dynsim.math.analysis.nonlinear.probe;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.math.analysis.DelayEmbedding;
import dynsim.math.analysis.rqa.RecurrencePlotToImg;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.*;
import dynsim.simulator.system.DynamicalSystem;

public class RecurrenceImgProbe {
	RecurrencePlotToImg rp;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RecurrenceImgProbe p = new RecurrenceImgProbe();
		try {
			p.sim();
		} catch (DynSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sim() throws DynSimException {
		DynamicalSystem sys = new Ueda();
		OdeSimulator sim = new OdeSimulator((OdeSystem) sys);
		sim.setItersMax(2500);
		sim.setSkip(1000);

		sim.compute();

		String[] names = new String[] { "x", "y", "z" };

		Storage data = sim.getStateSeries();
		// data = DelayEmbedding.embed(data.getAll("x"), names, 1);

		rp = new RecurrencePlotToImg(data, names);
		rp.setSharpen(1.5f);
		rp.compute();
	}
}
