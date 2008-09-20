package dynsim.simulator.iteratedmap.probe;

import java.awt.BorderLayout;
import java.awt.Button;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.AppFrame;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.simulator.color.*;
import dynsim.simulator.iteratedmap.IteratedMapSimulator;
import dynsim.simulator.iteratedmap.system.IteratedMap;
import dynsim.simulator.iteratedmap.system.impl.*;

public class IteratedMapProbe extends AppFrame {
	private static final long serialVersionUID = -8339107382776469057L;

	Grapher2D graph;

	private IteratedMap sys;

	private IteratedMapSimulator dyn;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IteratedMapProbe p = new IteratedMapProbe();
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
		sys = new Ikeda();
		dyn = new IteratedMapSimulator(sys);
		dyn.setItersMax(25000);
		// dyn.setSkip(100);
		dyn.compute();
		ColoringStrategy col = new MultirampColors();
		graph.addData(dyn.getStateSeries(), col);
	}
}
