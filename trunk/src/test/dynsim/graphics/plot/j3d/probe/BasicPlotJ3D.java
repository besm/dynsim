package dynsim.graphics.plot.j3d.probe;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.io.IOException;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.graphics.AppFrame;
import dynsim.graphics.color.scheme.impl.Warm;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig3D;
import dynsim.graphics.plot.j3d.Grapher3D;
import dynsim.simulator.color.MultirampColors;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.*;

public class BasicPlotJ3D extends AppFrame {
	private static final long serialVersionUID = 6320505559597728945L;

	public static void main(String[] argv) throws IOException {
		BasicPlotJ3D p = new BasicPlotJ3D();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private Grapher3D graph;

	private void init() {
		graph = new Grapher3D();
		GrapherConfig conf = graph.getGrapherConfig();
		conf.setDrawConfig(GrapherConfig3D.DRAW_AXES);

		add(graph, BorderLayout.CENTER);
		add(new Button("WEST"), BorderLayout.WEST);
		add(new Button("EAST"), BorderLayout.EAST);

		sim();

		pack();
		setVisible(true);
	}

	private void sim() {
		OdeSystem sys = new Rikitake();
		OdeSimulator dyn = new OdeSimulator(sys);
		dyn.setSkip(1000);
		dyn.setItersMax(15000);
		try {
			dyn.compute();
		} catch (DynSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Storage data = dyn.getStateSeries();
		graph.setData(data);
		MultirampColors mc = new MultirampColors();
		mc.initPals(new Warm());
		graph.setMainColoring(mc.getColors(data));
		// GrapherConfig conf = graph.getGrapherConfig();
		// conf.setPlotVarZ("t");
	}

}
