package dynsim.graphics.plot.j2d.probe;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.GradientPaint;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.graphics.AppFrame;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.axis.Axis;
import dynsim.graphics.plot.j2d.layer.field.VectField;
import dynsim.graphics.plot.j2d.layer.field.impl.OdeVectField;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.*;

public class VectFieldProbe extends AppFrame {

	private static final long serialVersionUID = -4871969491316656883L;

	private OdeSystem sys;

	private OdeSimulator dyn;

	private Grapher2D graph;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		VectFieldProbe p = new VectFieldProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		// Axis ax = new Axis(-3,3);
		// graph = new Grapher(400, 400, ax, ax);
		GrapherConfig conf = new GrapherConfig2D();
		conf.setDrawConfig(GrapherConfig2D.DRAW_AXES | GrapherConfig2D.DRAW_GRID);
		conf.setAutoAdjust(false);

		graph = new Grapher2D(400, 400, conf);

		// conf.unsetDrawConfig(GrapherConfig2D.DRAW_AXES_TEXT);

		graph.setForeground(new Color(255, 255, 0, 128));
		graph.setAxisX(new Axis(-5, 5));
		graph.setAxisY(new Axis(-5, 5));

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

	private void sim() throws DynSimException {
		sys = new SimpleTest();
		dyn = new OdeSimulator(sys);
		// dyn.setSkip(1000);
		// dyn.setItersMax(415000);
		dyn.compute();
		Storage data = dyn.getStateSeries();
		graph.addData(data);

		// graph.setPlotVarX("x");
		// graph.setPlotVarY("z");
		OdeVectField vf = new OdeVectField(sys);
		// GradientVectField vf = new GradientVectField(sys);
		vf.setGradient(new GradientPaint(0, 0, new Color(0, 255, 0, 0), 255, 0, new Color(0, 255, 0, 255), false));
		vf.setStyle(VectField.GRAD_LINE);
		graph.setVectField(vf);
		GrapherConfig conf = graph.getGrapherConfig();
		conf.setDrawConfig(GrapherConfig2D.DRAW_VECTFIELD);
	}

}
