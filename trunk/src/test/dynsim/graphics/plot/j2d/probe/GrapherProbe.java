package dynsim.graphics.plot.j2d.probe;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.simulator.color.MultirampColors;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.*;
import dynsim.ui.AppFrame;

public class GrapherProbe extends AppFrame {

	private static final long serialVersionUID = -4871969491316656883L;

	private OdeSystem sys;

	private OdeSimulator dyn;

	private Grapher2D graph;

	private Storage data;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GrapherProbe p = new GrapherProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		GrapherConfig conf = new GrapherConfig2D();
		conf.setDrawConfig(GrapherConfig2D.DRAW_AXES);
		graph = new Grapher2D(500, 500, conf);

		add(graph, BorderLayout.CENTER);
		add(new Button("WEST"), BorderLayout.WEST);
		add(new Button("EAST"), BorderLayout.EAST);

		sim();

		pack();
		setVisible(true);
	}

	private void sim() {
		// sys = new SphericalRep();
		sys = new Rossler();
		dyn = new OdeSimulator(sys);
		dyn.setSkip(2000);
		dyn.setItersMax(15000);
		try {
			dyn.compute();
		} catch (DynSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data = dyn.getStateSeries();

		graph.addData(data, new MultirampColors());

		// sys.setParams(new double[] { 0.15, 0.2, 9.0 });
		// dyn.reset();
		// dyn.setSkip(1000);
		// dyn.compute();
		// data = dyn.getResults();
		// graph.addData(data);
		//

		graph.setTickStep(2, 2);
		GrapherConfig conf = graph.getGrapherConfig();

		// conf.setPlotVarX("x");
		// conf.setPlotVarY("z");

		// conf.setPlotStyle(GrapherConfig.CURVES);
		// conf.setColors(new Color[] { Color.red, Color.yellow, Color.cyan });
		// conf.setCurveControlPointsNum(50);
		// conf.setSplineParts(40);

		conf.unsetDrawConfig(GrapherConfig2D.DRAW_AXES_TEXT);
		conf.setDrawConfig(GrapherConfig2D.DRAW_GRID);
	}

	public void dispose() {
		data.remove();
		super.dispose();
	}

}
