package dynsim.simulator.ode.probe;

import java.awt.BorderLayout;
import java.awt.Color;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.graphics.AppFrame;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.simulator.color.MultirampColors;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.*;

public class OdeProbe extends AppFrame {

	private static final long serialVersionUID = -4871969491316656883L;

	private OdeSystem sys;

	private OdeSimulator sim;

	private Grapher2D graph;

	private Storage data;

	private Grapher2D graph2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OdeProbe p = new OdeProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		graph = new Grapher2D(400, 400);
		graph2 = new Grapher2D(400, 400);

		add(graph, BorderLayout.CENTER);
		add(graph2, BorderLayout.WEST);

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
		sys = new ChenUeta();
		sim = new OdeSimulator(sys);
		sim.setDt(0.0015);
		sim.setSkip(1000);
		sim.setItersMax(10000);
		sim.compute();
		data = sim.getStateSeries();

		graph.addData(data, new MultirampColors());
		GrapherConfig conf = graph.getGrapherConfig();
		conf.setCurveControlPointsNum(50);
		conf.setSplineParts(100);
		conf.setPlotStyle(GrapherConfig.CURVES);
		conf.setPlotVarX("y");
		conf.setPlotVarY("z");
		conf.setColor(Color.BLUE, 0);

		graph2.addData(data, new MultirampColors());
		GrapherConfig conf2 = graph2.getGrapherConfig();
		conf2.setPlotStyle(GrapherConfig.LINES);
		conf2.setPlotVarX("x");
		conf2.setPlotVarY("z");
	}
}
