package dynsim.math.analysis.nonlinear.probe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Iterator;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.graphics.AppFrame;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.math.analysis.local.CPoint;
import dynsim.math.analysis.local.LocalStabilityAnalyser;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.color.MultirampColors;
import dynsim.simulator.ode.system.impl.*;
import dynsim.simulator.system.DynamicalSystem;

public class LocalStabilityProbe extends AppFrame {

	private static final long serialVersionUID = -4871969491316656883L;

	private DynamicalSystem sys;

	private Simulator sim;

	private Grapher2D graph;

	private Storage data;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LocalStabilityProbe p = new LocalStabilityProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		graph = new Grapher2D(400, 400);

		// graph.setAxisX(new Axis(-2, 0));
		// graph.setAxisY(new Axis(-1, 1));
		// Axis axis = new Axis();
		// axis.setLog(true);
		// graph.setAxisY(axis);
		// graph.setTickStep(0.1f);
		//
		// graph.getGrapherConfig().setAutoAdjust(false);

		add(graph, BorderLayout.CENTER);

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

		sim = SimulatorFactory.createSimulator(sys);
		// sim.setDt(0.0015);
		sim.setSkip(2000);
		sim.setItersMax(6000);
		sim.compute();
		data = sim.getStateSeries();

		LocalStabilityAnalyser lsa = new LocalStabilityAnalyser(sys);
		lsa.setUseHomotopy(false);
		// lsa.setMode(LocalStabilityAnalyser.FIXED);
		lsa.compute();

		Iterator<CPoint> itor = lsa.getFixedPoints().iterator();

		while (itor.hasNext()) {
			CPoint fp = itor.next();
			graph.addMarker(fp);
		}

		graph.addData(data, new MultirampColors());

		GrapherConfig conf = graph.getGrapherConfig();
		graph.setTickStep(5, 5);
		graph.setForeground(Color.white);

		conf.setPlotStyle(GrapherConfig.CURVES);
		// conf.setLineWeight(1f);
		conf.setPlotVarX("y");
		conf.setPlotVarY("z");
		conf.setDrawConfig(GrapherConfig2D.DRAW_AXES);
		// conf.setColor(Color.BLUE, 0);

		// //////////////
		// ColoringStrategy coloring = new MultirampColors();
		// graph.setMainColoring(coloring.getColors(data));
	}
}
