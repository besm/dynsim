package dynsim.math.analysis.nonlinear.probe;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.graphics.AppFrame;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.axis.Axis;
import dynsim.math.analysis.global.LyapunovPlot;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.ode.system.impl.Lorentz;
import dynsim.simulator.system.DynamicalSystem;

public class LyapunovOdePlotProbe extends AppFrame {
	private static final long serialVersionUID = -4871969491316656883L;

	private DynamicalSystem sys;

	private Grapher2D graph;

	private Grapher2D graph2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LyapunovOdePlotProbe p = new LyapunovOdePlotProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		graph = new Grapher2D(400, 400);
		graph2 = new Grapher2D(400, 400);
		GrapherConfig conf = graph.getGrapherConfig();
		conf.setDrawConfig(GrapherConfig2D.DRAW_AXES);

		GrapherConfig conf2 = graph2.getGrapherConfig();
		conf2.setDrawConfig(GrapherConfig2D.DRAW_AXES);

		add(graph, BorderLayout.CENTER);
		add(graph2, BorderLayout.WEST);
		add(new Button("EAST"), BorderLayout.EAST);

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

	private void sim() throws DynSimException {
		sys = new Lorentz();

		LyapunovPlot lp = new LyapunovPlot();
		lp.setSystem(sys);
		lp.setParameterName("b");
		lp.setParameterRange(1, 28);
		lp.compute();

		graph.setAxisX(new Axis(0, 30));
		graph.setTickStep(10, 1);
		graph2.setTickStep(10, 20);
		graph.setAxisY(new Axis(-10, 10));

		String names[] = { "x", "y" };

		Storage data = lp.getData();
		names = data.getColumnNames();
		names[0] = "x";
		names[1] = "y";
		data.setColumnNames(names);
		graph.addData(data);

		graph.getGrapherConfig().setPlotStyle(GrapherConfig2D.LINES);

		Simulator sim = SimulatorFactory.createSimulator(sys);
		sim.compute();
		graph2.addData(sim.getStateSeries());

		GrapherConfig conf = graph.getGrapherConfig();
		conf.setAutoAdjust(false);
	}
}
