package dynsim.math.analysis.nonlinear.probe;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;

import dynsim.data.Storage;
import dynsim.data.impl.BetterMemStorage;
import dynsim.graphics.AppFrame;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.axis.Axis;
import dynsim.math.analysis.global.LyapunovPlot;
import dynsim.simulator.iteratedmap.system.impl.*;
import dynsim.simulator.system.DynamicalSystem;

public class LyapunovPlotProbe extends AppFrame {
	private static final long serialVersionUID = -4871969491316656883L;

	private DynamicalSystem sys;

	private Grapher2D graph;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LyapunovPlotProbe p = new LyapunovPlotProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		graph = new Grapher2D(500, 500);
		GrapherConfig conf = graph.getGrapherConfig();
		conf.setDrawConfig(GrapherConfig2D.DRAW_AXES);

		add(graph, BorderLayout.CENTER);
		add(new Button("WEST"), BorderLayout.WEST);
		add(new Button("EAST"), BorderLayout.EAST);

		sim();

		pack();

		setVisible(true);
	}

	private void sim() {
		sys = new Insect();

		LyapunovPlot lp = new LyapunovPlot();
		lp.setSystem(sys);
		lp.setItersNum(500);
		lp.setParameterName("b");
		lp.setParameterRange(0, 1.);
		// lp.setParameterName("a");
		// lp.setParameterRange(0, 1.42);
		// lp.setLyaItersNum(10000);
		// lp.setParameterRange(2.4, 4, 0.0005);
		lp.compute();

		graph.setTickStep(0.1f, 0.5f);
		graph.setAxisX(new Axis(0, 1.));
		graph.setAxisY(new Axis(-2.5, 2.5));

		String names[] = { "x", "y" };

		Storage data = lp.getData();
		// names = data.getColumnNames();
		double[] t = data.getAll(0);
		double[] x = data.getAll(1);
		double[] y = data.getAll(2);
		// names[0] = "x";
		// names[1] = "y";
		// data.setColumnNames(names);
		graph.addData(new BetterMemStorage(new double[][] { t, x }, names));
		graph.addData(new BetterMemStorage(new double[][] { t, y }, names));

		GrapherConfig conf = graph.getGrapherConfig();
		conf.setAutoAdjust(false);
		conf.setPlotStyle(GrapherConfig2D.LINES);
	}
}
