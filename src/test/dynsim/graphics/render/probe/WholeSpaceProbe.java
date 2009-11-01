package dynsim.graphics.render.probe;

import java.awt.BorderLayout;
import java.awt.Color;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.render.impl.GrainRenderer;
import dynsim.graphics.render.util.DisplayImage;
import dynsim.graphics.render.util.FloatRange;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.helper.BoundsFinder;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.*;
import dynsim.ui.AppFrame;

public class WholeSpaceProbe extends AppFrame {
	private static final long serialVersionUID = 2639541437146777204L;

	Simulator dyn;

	GrainRenderer rend;

	DisplayImage display;

	FloatRange ax, ay, az;

	int px, py, pz;

	OdeSystem sys;

	// IteratedMap sys;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WholeSpaceProbe p = new WholeSpaceProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();

		p.doIt();

	}

	private void init() {
		display = new DisplayImage(400, 400);
		add(display, BorderLayout.CENTER);

		pack();
		center();
		setVisible(true);
	}

	public void doIt() {
		px = 1;
		py = 2;
		pz = 3;

		sys = new Rikitake();

		try {
			bounds(new OdeSimulator(sys));
		} catch (DynSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rend = new GrainRenderer();
		rend.initialize();
		rend.setDetail(0.02f);
		// rend.setAxisRanges(ax, ay, az);
		ax.setMax(ax.getMax() + 0.1f);
		ay.setMax(ay.getMax() + 0.1f);
		ax.setMin(ax.getMin() - 0.1f);
		ay.setMin(ay.getMin() - 0.1f);
		az.setMin(az.getMax() + 0.1f);
		az.setMin(az.getMin() - 0.1f);
		rend.setAxisRanges(ax, ay, az);
		rend.setVarpos(new int[] { px, py, pz });
		// rend.setMode(Render._2D);
		// rend.setEye(750, 750, 1500);
		// rend.setRotation(0.15, 0.15, .50);
		// rend.setRotation(Math.PI/3,Math.PI/4,Math.PI/6);

		dyn = SimulatorFactory.createSimulator(sys, rend);

		// TODO hacky damping!
		dyn.setSkip(0);
		dyn.setItersMax(120);

		WSIterator.iterate(dyn, ax, ay, az, 0.015, display);
	}

	/*
	 * blobby chua dyn.setItersMax(50); // grado estructura
	 * 
	 * double inc = 0.0025; // parrilla
	 * 
	 * for (double x = -2; x < 2; x += inc) { for (double y = -0.4; y < 0.4; y
	 * += inc) {
	 */
	public void test() throws DynSimException {
		dyn.setSkip(0);
		// dyn.setItersMax(100); // grado estructura
		//
		// double inc = 0.15; // parrilla
		//
		// for (double x = -6; x < 6; x += inc) {
		// for (double y = -3; y < 3; y += inc) {
		// for (double z = 5; z < 8.4; z += inc) {

		dyn.setItersMax(75); // grado estructura

		double inc = 0.075; // parrilla 0.075

		for (double x = -18; x < 18; x += inc) {
			for (double y = -24; y < 24; y += inc) {
				// se pone z fijo para cortes en lor ej 23, 10 black, 40..
				// for (double z = 0; z < 45; z += inc) {
				double[] initc = new double[] { 0, x, y, 10 };

				sys.setInitialConditions(initc);

				dyn.compute();

				dyn.reset();

				// }
			}

			display.setImage(rend.getImage());

			display.repaint(20000);
		}

		rend.flush();

		System.out.println("End Of Computation. Repainting...");

		display.repaint();
	}

	public void bounds(Simulator sim) throws DynSimException {
		FloatRange[] frs = BoundsFinder.getBounds(sim);
		ax = frs[px];
		ay = frs[py];
		if (frs.length > pz) {
			az = frs[pz];
		}
	}
}
