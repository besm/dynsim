/**
 * 
 */
package dynsim.simulator.iteratedmap.probe;

import java.awt.BorderLayout;
import java.awt.Button;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.iteratedmap.system.impl.interp.InterpretedMap;
import dynsim.simulator.system.interp.ParserWrapper;
import dynsim.ui.AppFrame;
import edu.hws.jcm.data.Variable;

/**
 * @author maf83
 * 
 */
public class InterpretedMapProbe extends AppFrame {
	private static final long serialVersionUID = 3603502289244997821L;

	Grapher2D graph;

	private InterpretedMap sys;

	private Simulator dyn;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InterpretedMapProbe p = new InterpretedMapProbe();
		p.init();
	}

	private void init() {
		graph = new Grapher2D(400, 400);

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
		sys = new InterpretedMap();
		setUpExpressions();

		sys.setInitialCondition("x0", .1);
		sys.setInitialCondition("x1", .1);
		// sys.setInitialConditions(new double[] { .1, .1 });

		dyn = SimulatorFactory.createSimulator(sys);
		// dyn.setItersMax(5000);
		// dyn.setSkip(100);
		dyn.compute();
		GrapherConfig conf = graph.getGrapherConfig();
		conf.setPlotVarX("x0");
		conf.setPlotVarY("x1");
		graph.addData(dyn.getStateSeries());
	}

	private void setUpExpressions() {
		ParserWrapper parser = sys.getWrapper();
		parser.add(new Variable("x0"));
		parser.add(new Variable("x1"));

		parser.add(new Variable("a", 0.999776));
		parser.add(new Variable("b", 0.1));

		parser.addExpression("-a * x1");
		parser.addExpression("x0 + (b * x1)");
	}
}
