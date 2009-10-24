package dynsim.simulator.ode.probe;

import java.awt.BorderLayout;
import java.awt.Button;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.color.MultirampColors;
import dynsim.simulator.ode.system.impl.interp.InterpretedOde;
import dynsim.simulator.system.interp.ParserWrapper;
import dynsim.ui.AppFrame;
import edu.hws.jcm.data.Variable;

public class InterpretedOdeProbe extends AppFrame {
	private static final long serialVersionUID = 3603502289244997821L;

	Grapher2D graph;

	private InterpretedOde sys;

	private Simulator dyn;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InterpretedOdeProbe p = new InterpretedOdeProbe();
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
		sys = new InterpretedOde();
		setUpExpressions();

		sys.setInitialCondition("x0", .1);
		sys.setInitialCondition("x1", .1);
		sys.setInitialCondition("x2", .1);

		dyn = SimulatorFactory.createSimulator(sys);
		dyn.setSkip(1000);
		dyn.setItersMax(5000);
		dyn.compute();
		GrapherConfig conf = graph.getGrapherConfig();
		conf.setPlotVarX("x1");
		conf.setPlotVarY("x2");
		conf.setPlotStyle(GrapherConfig.CURVES);
		Storage data = dyn.getStateSeries();
		graph.addData(data, new MultirampColors());
	}

	private void setUpExpressions() {
		ParserWrapper parser = sys.getWrapper();

		// addVariable(numofvars)
		parser.add(new Variable("x0"));
		parser.add(new Variable("x1"));
		parser.add(new Variable("x2"));

		// a = 35, b = 3, c = 28;

		parser.add(new Variable("a", 35));
		parser.add(new Variable("b", 3));
		parser.add(new Variable("c", 28));

		// a*(y-x)
		// (c-a)*x-x*z+c*y
		// x*y-b*z

		parser.addExpression("a * (x1 - x0)");
		parser.addExpression("(c-a) * x0 - x0 * x2 + c * x1");
		parser.addExpression("x0 * x1 - b * x2");
	}
}
