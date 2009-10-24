package dynsim.math.analysis.nonlinear.probe;

import java.awt.BorderLayout;
import java.awt.Color;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.math.analysis.DelayEmbedding;
import dynsim.math.analysis.local.LKOrbits;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.ode.system.impl.*;
import dynsim.simulator.system.DynamicalSystem;
import dynsim.ui.AppFrame;

public class LKProbe extends AppFrame {
	private static final long serialVersionUID = 4767016572007481541L;

	public static void main(String[] args) {
		LKProbe p = new LKProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private Grapher2D graph;

	private void init() {
		graph = new Grapher2D(400, 400);
		graph.getGrapherConfig().setDrawConfig(GrapherConfig2D.DRAW_AXES);

		// graph.getAxisX().setTickStep(100);
		// graph.getAxisY().setTickStep(1000);

		add(graph, BorderLayout.CENTER);

		sim();

		pack();
		center();
		setVisible(true);
	}

	public void sim() {
		DynamicalSystem sys = new StableCycle();
		// sys.setParameter("c", 12);
		Simulator sim = SimulatorFactory.createSimulator(sys);
		sim.setItersMax(25000);
		sim.setSkip(20000);
		try {
			sim.compute();
		} catch (DynSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		double[] ts = sim.getStateSeries().getAll("x");

		LKOrbits lko = new LKOrbits(sim.getStateSeries());
		lko.compute();
		// String[] names = new String[] { "x", "y", "z" };
		// graph.addData(DelayEmbedding.embed(ts, names, 50));

		graph.addData(sim.getStateSeries());
		// graph.addData(lko.getHisto());
		// graph.addData(lko.getOrbit(10));
	}
}
