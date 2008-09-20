package dynsim.math.analysis.nonlinear.probe;

import java.awt.BorderLayout;
import java.awt.Color;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.graphics.AppFrame;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.math.analysis.DelayEmbedding;
import dynsim.math.analysis.rqa.*;
import dynsim.simulator.iteratedmap.IteratedMapSimulator;
import dynsim.simulator.iteratedmap.system.IteratedMap;
import dynsim.simulator.iteratedmap.system.impl.*;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.*;
import dynsim.simulator.system.DynamicalSystem;

public class RecurrencePlotProbe extends AppFrame {
	private static final long serialVersionUID = -8339107382776469057L;

	ColorRecurrencePlot colorRP;

	AbstractRecurrencePlot monoRP;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RecurrencePlotProbe p = new RecurrencePlotProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		colorRP = new ColorRecurrencePlot(400, 400);
		// monoRP = new MonoRecurrencePlot(400, 400);

		add(colorRP, BorderLayout.CENTER);
		// add(monoRP, BorderLayout.WEST);

		try {
			sim();
		} catch (DynSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pack();
		center();
		setVisible(true);
	}

	public void sim() throws DynSimException {
		IteratedMap sys = new MikeCell();
		IteratedMapSimulator sim = new IteratedMapSimulator(sys);
		// posiciones de la historia...t vs t
		// TODO ver como se relaciona con el tamaño del plot
		sim.setSkip(7000);
		sim.setItersMax(9000);

		// DynamicalSystem sys = new Rossler();
		// OdeSimulator sim = new OdeSimulator((OdeSystem) sys);
		// sim.setItersMax(4000);
		// sim.setSkip(3000);

		sim.compute();

		// String[] names = sys.getIndependentVarNames();
		// if (sys.getTimeNature() == DynamicalSystem.CONTINOUS) {
		// String[] newnames = new String[names.length - 1];
		// System.arraycopy(names, 1, newnames, 0, newnames.length);
		// names = newnames;
		// }

		String[] names = new String[] { "x", "y", /* "z" */};

		Storage data = sim.getStateSeries();
		// data = DelayEmbedding.embed(data.getAll("x"), names, 1);

		RecurrencePlot rp = colorRP.createRecurrencePlot(data, names);
		rp.unsetMode(RecurrencePlot.QANALYSIS);
		colorRP.setSharpen(1f);
		// colorRP.setNumColors(1024);

		// RecurrencePlot rp = monoRP.createRP(sim.getStateSeries(), names);
		// rp.setThreshold(20);
		// rp.setThreshold(0.052);

		GrapherConfig conf = colorRP.getGrapherConfig();
		conf.setMainColor(Color.pink);
		conf.setDrawConfig(GrapherConfig2D.DRAW_AXES);
		colorRP.setForeground(Color.white);
		// colorRP.setTickStep(1);

		// monoRP.setGrapherConfig(conf);
		// monoRP.setForeground(Color.white);
		// monoRP.setTickStep(100);
	}
}