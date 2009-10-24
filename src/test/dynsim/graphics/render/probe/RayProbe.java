package dynsim.graphics.render.probe;

import java.awt.BorderLayout;
import java.awt.Color;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.render.util.DisplayImage;
import dynsim.graphics.render.util.FloatRange;
import dynsim.simulator.Simulator;
import dynsim.simulator.helper.BoundsFinder;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.*;
import dynsim.ui.AppFrame;

public class RayProbe extends AppFrame {
	private static final long serialVersionUID = 2639541437146777204L;

	Simulator dyn;

	DirtyHax rend;

	DisplayImage display;

	FloatRange ax, ay, az;

	int px, py, pz;

	OdeSystem sys;

	// IteratedMap sys;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RayProbe p = new RayProbe();
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

		sys = new Chua();

		bounds(new OdeSimulator(sys));

		rend = new DirtyHax(sys);

		ax.setMax(ax.getMax() + 0.1f);
		ay.setMax(ay.getMax() + 0.1f);
		ax.setMin(ax.getMin() - 0.1f);
		ay.setMin(ay.getMin() - 0.1f);
		rend.setAxisRanges(ax, ay);
		rend.setVarpos(new int[] { px, py, pz });

		dyn = new OdeSimulator(sys, rend);

		try {
			test();
		} catch (DynSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void test() throws DynSimException {
		dyn.setSkip(50000);
		dyn.setItersMax(5000000);

		// for (double x = 0; x < 2; x += 2/150f) {
		// for (double y = 0; y < 3; y += 3/150f) {
		// for (double z = 5; z < 8.4; z += 4/50f) {

		// for (double x = -30; x < 30; x += 60/100f) {
		// for (double y = -30; y < 30; y += 60/100f) {
		// for (double z = 0; z < 60; z += 60/100f) {

		// double[] initc = new double[] { 0, x, y, 0 };
		//
		// sys.setInitialConditions(initc);

		dyn.compute();

		// dyn.reset();

		// }
		// }
		rend.rasterize();

		display.setImage(rend.getImage());

		display.repaint(20000);
		// }

		// rend.rasterize();

		// display.setImage(rend.getImage());
		//		
		// display.repaint(20000);

		rend.flush();

		System.out.println("End Of Computation. Repainting...");

		// display.repaint();
	}

	public void bounds(Simulator sim) {
		try {
			FloatRange[] frs = BoundsFinder.getBounds(sim);
			ax = frs[px];
			ay = frs[py];
			if (frs.length > pz) {
				az = frs[pz];
			}
		} catch (DynSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
