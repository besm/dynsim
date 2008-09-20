package dynsim.math.analysis.nonlinear.probe;

import java.awt.BorderLayout;
import java.awt.Color;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.graphics.AppFrame;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.math.analysis.OdeReturnMap;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.*;
import dynsim.simulator.system.DynamicalSystem;

public class ReturnMapProbe extends AppFrame {
	private static final long serialVersionUID = -4871969491316656883L;

	private DynamicalSystem sys;

	private Grapher2D graph, graph2;

	private Storage data;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ReturnMapProbe p = new ReturnMapProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		graph = new Grapher2D(400, 400);
		GrapherConfig conf = graph.getGrapherConfig();
		conf.setDrawConfig(GrapherConfig2D.DRAW_AXES);
		graph.setTickStep(2);

		graph2 = new Grapher2D(400, 400);
		GrapherConfig conf2 = graph2.getGrapherConfig();
		conf2.setDrawConfig(GrapherConfig2D.DRAW_AXES);
		graph2.setTickStep(2);

		add(graph);
		add(graph2, BorderLayout.EAST);

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

		// TODO sólo para rossler? Hacer poincaré section con condición o
		// bien temporal o bien por corte!
		// //////////////////////////////////////
		// Ueda
		// PS = if (Math.sin(t) < 0.01 && Math.sin(t) > -0.01
		// && Math.cos(t) > 0.999 && Math.cos(t) < 1.01) {
		sys = new Rossler();
		Simulator sim = SimulatorFactory.createSimulator(sys);

		sim.setItersMax(15000);
		sim.setSkip(1000);
		OdeReturnMap rm = new OdeReturnMap();
		rm.setSystem((OdeSystem) sys);
		data = rm.getReturnMap();

		sim.compute();
		Storage res = sim.getStateSeries();
		// // data = PoincareSection.getPoincareSection(res);
		// data = PoincareSection.getSect(res, "y");
		//
		// data = PoincareSection.getReturnMap(data, "t");
		//		
		graph.addData(data);
		// // graph.setAxisX(new Axis(-5,1));
		// // graph.setAxisY(new Axis(-10,0));
		GrapherConfig conf = graph.getGrapherConfig();
		conf.setPlotVarX(data.getColumnName(0));
		conf.setPlotVarY(data.getColumnName(1));
		// conf.setAutoAdjust(false);

		graph2.addData(res);
	}
}
