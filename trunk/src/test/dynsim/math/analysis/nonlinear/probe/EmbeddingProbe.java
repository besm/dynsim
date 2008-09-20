package dynsim.math.analysis.nonlinear.probe;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.graphics.AppFrame;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.math.analysis.DelayEmbedding;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.*;

public class EmbeddingProbe extends AppFrame {
	private static final long serialVersionUID = -8339107382776469057L;

	Grapher2D graph;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EmbeddingProbe p = new EmbeddingProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		graph = new Grapher2D(400, 400);

		add(graph, BorderLayout.CENTER);
		add(new Button("WEST"), BorderLayout.WEST);
		add(new Button("EAST"), BorderLayout.EAST);

		sim();

		pack();
		setVisible(true);
	}

	public void sim() {
		// IteratedMapSystem sys = new Henon();
		// IteratedMapSimulator sim = new IteratedMapSimulator(sys);
		// sim.setSkip(1000);
		// sim.setItersMax(2000);

		OdeSystem sys = new RabinovichFabrikant();
		OdeSimulator sim = new OdeSimulator(sys);
		sim.setItersMax(10000);
		sim.setSkip(5000);

		try {
			sim.compute();
		} catch (DynSimException e) {
			e.printStackTrace();
		}
		// double[][] r = sim.getRawResults();
		// r = delTime(r);
		// String[] names = { "x", "y", "z" };
		// univariate time series
		double[] ts = sim.getStateSeries().getAll("x");
		// (values,dim[2,3,4..n],roll[4,10,15,25..150])
		Storage res = DelayEmbedding.embed(ts, 2, 50);

		// TODO no funciona la estimación
		System.out.println(DelayEmbedding.estimateDimension(ts, 25));

		GrapherConfig conf = graph.getGrapherConfig();
		conf.setMainColor(Color.GREEN);
		conf.setPlotVarX("x0");
		conf.setPlotVarY("x1");
		graph.addData(res);
	}
}
