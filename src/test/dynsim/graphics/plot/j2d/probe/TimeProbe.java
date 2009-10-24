/**
 * 
 */
package dynsim.graphics.plot.j2d.probe;

import java.awt.BorderLayout;
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

/**
 * @author maf83
 * 
 */
public class TimeProbe extends AppFrame {

	private static final long serialVersionUID = -4871969491316656883L;

	private OdeSystem sys;

	private OdeSimulator dyn;

	private Grapher2D graph;

	private Storage data;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TimeProbe p = new TimeProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		GrapherConfig conf = new GrapherConfig2D();
		conf.setDrawConfig(GrapherConfig2D.DRAW_AXES | GrapherConfig2D.DRAW_GRID);
		graph = new Grapher2D(500, 200, conf);

		add(graph, BorderLayout.CENTER);

		sim();

		pack();
		center();
		setVisible(true);
	}

	private void sim() {
		sys = new Ueda();
		dyn = new OdeSimulator(sys);
		// dyn.setSkip(1000);
		// dyn.setItersMax(415000);
		try {
			dyn.compute();
		} catch (DynSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data = dyn.getStateSeries();
		graph.addData(data, new MultirampColors());

		graph.setTickStep(5.f);
		GrapherConfig conf = graph.getGrapherConfig();
		conf.unsetDrawConfig(GrapherConfig2D.DRAW_AXIS_TEXT_X);
		conf.setPlotVarX("t");
		conf.setPlotVarY("x");

		conf.setPlotStyle(GrapherConfig.LINES);
	}

}
