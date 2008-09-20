/**
 * 
 */
package dynsim.graphics.render.probe;

import java.awt.BorderLayout;
import java.awt.Color;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.AppFrame;
import dynsim.graphics.render.RenderConfig;
import dynsim.graphics.render.impl.DensityRenderer;
import dynsim.graphics.render.util.DisplayImage;
import dynsim.graphics.render.util.FloatRange;
import dynsim.simulator.Simulator;
import dynsim.simulator.helper.BoundsFinder;
import dynsim.simulator.iteratedmap.IteratedMapSimulator;
import dynsim.simulator.iteratedmap.system.IteratedMap;
import dynsim.simulator.iteratedmap.system.impl.*;

/**
 * @author maf83
 * 
 */
public class MapsProbe extends AppFrame {
	private static final long serialVersionUID = -4728399338932470546L;

	Simulator dyn;

	DensityRenderer rend;

	FloatRange ax, ay, az;

	int px, py, pz;

	private DisplayImage display;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MapsProbe p = new MapsProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();

		try {
			p.doIt();
		} catch (DynSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init() {
		display = new DisplayImage(400, 400);
		add(display, BorderLayout.CENTER);

		pack();
		center();
		setVisible(true);
	}

	public void doIt() throws DynSimException {
		IteratedMap sys;
		px = 0;
		py = 1;
		pz = 2;

		sys = new MountainsOfMadness();

		bounds(new IteratedMapSimulator(sys));

		rend = new DensityRenderer();
		RenderConfig cfg = new RenderConfig(RenderConfig.MODE_2D);
		rend.setConfig(cfg);
		rend.setAxisRanges(ax, ay, az);
		rend.setVarpos(new int[] { px, py, pz });
		// rend.setExposure(1f); delte!
		// rend.setGamma(1.75f);
		// rend.setScale(1.5f);
		// rend.setDetail(0.025f);
		// rend.setBackgroundColor(Color.RED);

		// rend.setRotation(Math.PI/3,Math.PI/4,0);

		dyn = new IteratedMapSimulator(sys, rend);

		test();
		rend.flush();
	}

	public void test() throws DynSimException {
		// dyn.setSkip(100000);
		// dyn.setItersMax(2000000);
		dyn.setSkip(0);
		dyn.setItersMax(20000000);

		dyn.compute();

		display.setImage(rend.getImage());
		display.repaint();

		System.out.println(MapsProbe.class.getName() + " end");
	}

	public void bounds(Simulator sim) {
		FloatRange[] frs;
		try {
			frs = BoundsFinder.getBounds(sim);
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
