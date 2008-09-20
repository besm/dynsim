/**
 * 
 */
package dynsim.graphics.plot.j2d.probe;

import java.awt.Color;
import java.awt.FlowLayout;

import dynsim.data.Storage;
import dynsim.graphics.AppFrame;
import dynsim.graphics.color.scheme.impl.Warm;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.simulator.color.MultirampColors;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.Rossler;

/**
 * @author maf83
 * 
 */
public class MultiSlotGrapherProbe extends AppFrame {

	private static final long serialVersionUID = -4871969491316656883L;

	private OdeSystem sys;

	private OdeSimulator dyn;

	private Grapher2D graph, graph2;

	private Storage data;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MultiSlotGrapherProbe p = new MultiSlotGrapherProbe();
		p.setLayout(new FlowLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		graph = new Grapher2D(400, 400);
		graph2 = new Grapher2D(400, 400);

		add(graph);
		add(graph2);

		sim();

		pack();
		center();
		setVisible(true);
	}

	private void sim() {
		try {
			sys = new Rossler();
			dyn = new OdeSimulator(sys);
			dyn.setSkip(1000);
			dyn.compute();

			graph.getGrapherConfig().setPlotStyle(GrapherConfig2D.LINES);

			data = dyn.getStateSeries();

			MultirampColors mc = new MultirampColors();

			graph.addData(data);
			graph2.addData(data, mc);

			sys.setParameter("a", 0.2);
			sys.setParameter("b", 0.2);
			sys.setParameter("c", 6.7);
			dyn.setSkip(1000);
			dyn.reset();
			dyn.compute();
			data = dyn.getStateSeries();
			graph.addData(data);
			graph2.addData(data, mc);

			data = dyn.getStateSeries();

			mc.initPals(new Warm());

			sys.setParameter("a", 0.2);
			sys.setParameter("b", 0.67);
			sys.setParameter("c", 9.7);
			dyn.setSkip(1000);
			dyn.reset();
			dyn.compute();
			data = dyn.getStateSeries();
			graph.addData(data);
			graph2.addData(data, mc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
