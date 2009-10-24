package dynsim.math.analysis.nonlinear.probe;

import java.awt.BorderLayout;
import java.awt.Color;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.axis.Axis;
import dynsim.math.analysis.rqa.ColorRecurrencePlot;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.*;
import dynsim.ui.AppFrame;

public class RecurrencePlotProbe2 extends AppFrame {
	private static final long serialVersionUID = -8339107382776469057L;

	ColorRecurrencePlot graph;

	// private Grapher graph2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RecurrencePlotProbe2 p = new RecurrencePlotProbe2();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		graph = new ColorRecurrencePlot(500, 500);

		// graph2 = new Grapher(400, 400);
		add(graph, BorderLayout.CENTER);
		// add(graph2, BorderLayout.WEST);

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

	public void sim() throws DynSimException {
		OdeSystem sys = new Chua();
		OdeSimulator sim = new OdeSimulator(sys);

		sys.setParameter("a", 15.8);
		
		sim.setSkip(2000);
		sim.setItersMax(4000);
		sim.compute();

		String[] names = { "x", "y", "z" };
		// int numi = 1500;
		// Storage noise = new MemStorage(new double[3][numi], names);
		// Random rand = new Random();
		// double[] h = new double[3];
		// for (int i = 0; i < numi; i++) {
		// h[0] = rand.nextGaussian();
		// h[1] = rand.nextGaussian();
		// h[2] = rand.nextGaussian();
		// noise.add(h);
		// }

		/* RecurrencePlot rp = */
		graph.createRecurrencePlot(sim.getStateSeries(), names);
		// graph.createRecurrencePlot(noise, names);
		// rp.setThreshold(4.5);
		// ikeda 0.1
		// rp.setThreshold(1.5);

		GrapherConfig conf = graph.getGrapherConfig();
		conf.setDrawConfig(GrapherConfig2D.DRAW_BORDER | GrapherConfig2D.DRAW_AXES);

		graph.setForeground(Color.white);
		graph.setTickStep(1);
		graph.setAxisX(new Axis(0, 10));
		graph.setAxisY(new Axis(0, 10));

		// graph2.addData(sim.getStateSeries());
		// graph2.getGrapherConfig().setPlotVarY("z");
	}
	// private void test() {
	// // IteratedMapSystem sys = new Rare1();
	// // IteratedMapSimulator sim = new IteratedMapSimulator(sys);
	//
	// OdeSystem sys = new Lorentz();
	// OdeSimulator sim = new OdeSimulator(sys);
	//
	// // sim.setSkip(3000);
	// sim.setItersMax(500);
	// sim.compute();
	//
	// String[] names = { "x", "y" };
	// Storage b = new MemStorage(new double[2][500], names);
	// Random rand = new Random();
	// double[] h = new double[2];
	// for (int i = 0; i < 500; i++) {
	// h[0] = rand.nextDouble();
	// h[1] = rand.nextDouble();
	// // h[0] = rand.nextGaussian();
	// // h[1] = rand.nextGaussian();
	// b.add(h);
	// }
	// // RecurrencePlotToImg rp = new RecurrencePlotToImg(b,
	// // names);
	// RecurrencePlotToImg rp = new
	// RecurrencePlotToImg(sim.getStateSeries(),
	// names);
	// // rp.setThreshold(4.5);
	// // ikeda 0.1
	// rp.setThreshold(1.5);
	// rp.compute();
	// RQA rqa = rp.getRQA();
	// rqa.computeDiagonals();
	//
	// System.out.println(rqa);
	// }
}
