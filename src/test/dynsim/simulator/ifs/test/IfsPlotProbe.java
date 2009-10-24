package dynsim.simulator.ifs.test;

import java.awt.BorderLayout;
import java.awt.Button;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.ifs.system.FilteredIfs;
import dynsim.simulator.ifs.system.filter.Swirl;
import dynsim.simulator.ifs.system.impl.StochasticFilteredIfs;
import dynsim.simulator.ifs.system.loader.IfsDatum;
import dynsim.simulator.ifs.system.loader.IfsLoader;
import dynsim.ui.AppFrame;

public class IfsPlotProbe extends AppFrame {
	private static final long serialVersionUID = -8339107382776469057L;

	Grapher2D graph;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IfsPlotProbe p = new IfsPlotProbe();
		p.init();
	}

	private void init() {
		graph = new Grapher2D(400, 400);
		// graph.setDrawAxis(true);
		// graph.setDrawGrid(true);

		add(graph, BorderLayout.CENTER);
		add(new Button("WEST"), BorderLayout.WEST);
		add(new Button("EAST"), BorderLayout.EAST);

		try {
			sim();
		} catch (DynSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pack();
		setVisible(true);
	}

	public void sim() throws DynSimException {
		String fname = "data/test/ifs/ifs.xml";

		IfsLoader il = new IfsLoader();
		il.loadFromFile(fname);
		String sn = "dragon";
		// sn = "farn";
		// sn = "swirl";
		// sn="fuckup";
		// sn="Schere";
		// sn="Koch";
		// sn="Seltsam";
		// sn = "coral";
		IfsDatum data = il.getIfsById(sn);
		// IteratedFunctionSystem ifs = new StochasticIfs(data);
		FilteredIfs ifs = new StochasticFilteredIfs(data);
		// ifs.add(new Sinusoidal());
		ifs.add(new Swirl());
		// ifs.add(new Spherical());
		// ifs.add(new Horseshoe());

		// IfsSimulator sim = new IfsSimulator(ifs);
		Simulator sim = SimulatorFactory.createSimulator(ifs);
		sim.setItersMax(25000);
		sim.compute();

		graph.addData(sim.getStateSeries());
	}

}
