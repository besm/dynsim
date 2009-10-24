/**
 * 
 */
package dynsim.graphics.plot.j2d.probe;

import java.awt.Color;
import java.awt.Font;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.graphics.color.scheme.impl.Warm;
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
public class AxisProbe extends AppFrame {

	private static final long serialVersionUID = -4871969491316656883L;

	private OdeSystem sys;

	private OdeSimulator dyn;

	private Grapher2D graph;

	private Storage data;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AxisProbe p = new AxisProbe();
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		GrapherConfig conf = new GrapherConfig2D();
		conf.setDrawConfig(GrapherConfig2D.DRAW_AXES | GrapherConfig2D.DRAW_GRID);

		graph = new Grapher2D(400, 400, conf);

		// TODO esta forma de configurar con notifier..
		// graph = new Grapher(800, 800);
		//
		// GrapherConfig conf = graph.getGrapherConfig();
		// conf.setDrawConfig(GrapherConfig2D.DRAW_AXES
		// | GrapherConfig2D.DRAW_GRID);

		graph.setTickStep(0.2f, 1.5f);
		graph.setFont(new Font("Arial", Font.PLAIN, 12));
		// graph.setBackground(Color.BLACK);
		// graph.setForeground(Color.YELLOW);

		add(graph);

		sim();

		pack();
		center();
		setVisible(true);
	}

	private void sim() {
		sys = new Chua();
		dyn = new OdeSimulator(sys);
		dyn.setSkip(1000);
		dyn.setItersMax(50000);

		try {
			dyn.compute();
		} catch (DynSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data = dyn.getStateSeries();

		MultirampColors mc = new MultirampColors();
		mc.initPals(new Warm());

		graph.addData(data, mc);

		GrapherConfig conf = graph.getGrapherConfig();
		// graph.setAxisX(new Axis(-1,1));
		// graph.setAxisY(new Axis(-1,1));
		// conf.setAutoAdjust(false);

		// // graph.setTickStep(2.f);
		conf.setPlotVarX("y");
		conf.setPlotVarY("z");
	}

}
