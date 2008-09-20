package dynsim.math.analysis.signal.probe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Random;

import dynsim.data.Storage;
import dynsim.data.impl.BetterMemStorage;
import dynsim.exceptions.DynSimException;
import dynsim.graphics.AppFrame;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.axis.Axis;
import dynsim.math.analysis.signal.Fft;
import dynsim.math.analysis.signal.PowerSpectrum;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.iteratedmap.system.impl.*;
import dynsim.simulator.ode.system.impl.*;
import dynsim.simulator.system.DynamicalSystem;

public class FrequencyBasicsProbe extends AppFrame {

	private static final long serialVersionUID = -4871969491316656883L;

	private DynamicalSystem sys;

	private Simulator sim;

	private Grapher2D graph;

	private Storage data;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FrequencyBasicsProbe p = new FrequencyBasicsProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		graph = new Grapher2D(400, 400);

		// graph.setAxisX(new Axis(0, 500));
		// graph.setAxisY(new Axis(-10, 1));
		// // //
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
		sys = new ChenUeta();
		sim = SimulatorFactory.createSimulator(sys);
		// sim.setDt(0.0015);
		sim.setSkip(1000);
		sim.setItersMax(3048);
		sim.compute();
		data = sim.getStateSeries();

		graph.getAxisY().setLog(true);

		try {
			Fft fft = new Fft(1000, 2048, 10);
			// // PowerSpectrum ps = new PowerSpectrum(data.getAll(1));
			// // ps.compute();
			// // double[] noise = new double[2048];
			// // Random rand = new Random();
			// // for(int i = 0; i < noise.length; i++) {
			// // noise[i] = 10*rand.nextGaussian();
			// // }
			// fft.calculate(data.getAll(1));
			// // fft.calculate(noise);
			// double[] spec = fft.getSpectra();
			//
			// // System.out.println(fft);
			// // double[] spec = fft.getSpectra();
			// double[][] st = new double[2][spec.length];
			// st[1] = spec;
			// st[0] = fft.getFreq();
			//
			// // for (int n = 0; n < spec.length; n++) {
			// // st[0][n] = n;
			// // }
			//
			// graph.addData(new BetterMemStorage(st, new String[] { "x", "y"
			// }));

			spectra(fft, 1);
			spectra(fft, 2);
			spectra(fft, 3);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		GrapherConfig conf = graph.getGrapherConfig();
		graph.setTickStep(50, 1f);
		graph.setForeground(Color.white);

		conf.setPlotStyle(GrapherConfig.LINES);
		conf.setPlotVarX("x");
		conf.setPlotVarY("y");
		conf.setDrawConfig(GrapherConfig2D.DRAW_AXES | GrapherConfig2D.DRAW_GRID);

		// // conf.setColor(Color.BLUE, 0);
		// ColoringStrategy coloring = new MultirampColors();
		// graph.setMainColoring(coloring.getColors(data));
	}

	private void spectra(Fft fft, int idx) {
		double[] spec;
		double[][] st;
		fft.calculate(data.getAll(idx));
		// fft.calculate(noise);
		spec = fft.getSpectra();

		// System.out.println(fft);
		// double[] spec = fft.getSpectra();
		st = new double[2][spec.length];
		st[1] = spec;
		st[0] = fft.getFreq();

		// for (int n = 0; n < spec.length; n++) {
		// st[0][n] = n;
		// }
		Storage stor = new BetterMemStorage(st, new String[] { "x", "y" });
		graph.addData(stor);
	}
}
