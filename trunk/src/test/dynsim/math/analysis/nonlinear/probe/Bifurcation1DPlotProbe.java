package dynsim.math.analysis.nonlinear.probe;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;

import dynsim.graphics.AppFrame;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.axis.Axis;
import dynsim.math.analysis.global.BifurcationDiagram1D;
import dynsim.math.analysis.global.BifurcationPlot;
import dynsim.simulator.iteratedmap.system.impl.*;
import dynsim.simulator.system.DynamicalSystem;

public class Bifurcation1DPlotProbe extends AppFrame {
	private static final long serialVersionUID = -4871969491316656883L;

	private DynamicalSystem sys;

	private BifurcationPlot bifurcationGraph;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Bifurcation1DPlotProbe p = new Bifurcation1DPlotProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();
	}

	private void init() {
		bifurcationGraph = new BifurcationPlot(400, 400);
		GrapherConfig conf = bifurcationGraph.getGrapherConfig();
		conf.setDrawConfig(GrapherConfig2D.DRAW_AXES);

		add(bifurcationGraph, BorderLayout.CENTER);
		add(new Button("WEST"), BorderLayout.WEST);
		add(new Button("EAST"), BorderLayout.EAST);

		sim();

		pack();

		setVisible(true);
	}

	private void sim() {
		sys = new LogisticMap();

		BifurcationDiagram1D orbit = bifurcationGraph.createBifurcationDiagram();
		orbit.setSystem(sys);
		orbit.setItersNum(100);
		// orbit.setParamItersNum(20000);
		orbit.setParamItersNum(2000);
		orbit.setVarName("x");
		// orbit.setVarRange(-2, 2);
		// orbit.setParameterName("b");
		// orbit.setParameterRange(-1, 1);
		// orbit.setResolution(10);
		// orbit.setWarm(100);
		// orbit.setMaxPeriod(4);
		bifurcationGraph.setDotWeight(2);

		// Test
		// orbit.setVarRange(-1.25, 1.25);
		// orbit.setParameterName("a");
		// orbit.setParameterRange(-5, 0);
		// // orbit.setWarm(1500);
		// // con warm resolución más baja 1500warm=>100
		// orbit.setResolution(500);
		// // orbit.setMinPeriod(2);
		// orbit.setMaxPeriod(8);
		// // bifurcationGraph.setDotWeight(2);

		// insect
		// orbit.setVarRange(0, 1.5);
		// orbit.setParameterName("b");
		// orbit.setParameterRange(0, 2);
		// orbit.setWarm(100);
		// orbit.setResolution(100);
		// orbit.setMaxPeriod(6);

		// Log
		orbit.setVarRange(-2, 2);
		orbit.setParameterName("a");
		orbit.setParameterRange(-2, 0.5);
		orbit.setWarm(300);
		// TODO la resol va relacionada con num iters
		// 400=>1500, 1000=> mayor de 2500...
		// igual converge a 400 max (1500 resol)
		// para mayor precision aumentar iters de integra?
		orbit.setResolution(200);
		// orbit.setMinPeriod(0);
		orbit.setMaxPeriod(7);
		// Hen
		// orbit.setVarRange(-1.5, 1.5);
		// orbit.setParameterName("a");
		// orbit.setParameterRange(0, 1.5);
		// orbit.setMaxPeriod(12);
		// orbit.setResolution(100);
		// orbit.setWarm(400);

		bifurcationGraph.addData(orbit.getData());

		GrapherConfig conf = bifurcationGraph.getGrapherConfig();
		conf.setPlotVarX(orbit.getParameterName());
		conf.setPlotVarY(orbit.getVariableName());

		// Test
		// bifurcationGraph.setAxisX(new Axis(-5, 0));
		// bifurcationGraph.setAxisY(new Axis(-1.25, 1.25));
		// insect
		// bifurcationGraph.setAxisX(new Axis(0, 2));
		// bifurcationGraph.setAxisY(new Axis(0, 1.5));
		// Hen
		// bifurcationGraph.setAxisX(new Axis(0, 1.5));
		// bifurcationGraph.setAxisY(new Axis(-1.5, 1.5));
		// Log
		bifurcationGraph.setAxisX(new Axis(-2, 0.5));
		bifurcationGraph.setAxisY(new Axis(-2, 2));

		conf.setAutoAdjust(false);
	}
}
