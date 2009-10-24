/**
 * 
 */
package dynsim.graphics.render.probe;

import java.awt.BorderLayout;
import java.awt.Color;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.render.Renderer;
import dynsim.graphics.render.impl.GrainRender;
import dynsim.graphics.render.util.DisplayImage;
import dynsim.graphics.render.util.FloatRange;
import dynsim.simulator.Simulator;
import dynsim.simulator.helper.BoundsFinder;
import dynsim.simulator.iteratedmap.IteratedMapSimulator;
import dynsim.simulator.iteratedmap.system.IteratedMap;
import dynsim.simulator.iteratedmap.system.impl.*;
import dynsim.ui.AppFrame;

/**
 * @author maf83
 * 
 */
public class RenderProbe extends AppFrame {

	private static final long serialVersionUID = 7777115633041049554L;

	Simulator dyn;

	Renderer rend;

	DisplayImage display;

	FloatRange ax, ay, az;

	int px, py, pz;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RenderProbe p = new RenderProbe();
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
		// OdeSystem sys;
		IteratedMap sys;
		px = 0;
		py = 1;
		pz = 2;

		sys = new Insect();

		bounds(new IteratedMapSimulator(sys));

		rend = new GrainRender();
		// rend.setAxisRanges(ax, ay, az);
		ax.setMax(ax.getMax() + 0.1f);
		ay.setMax(ay.getMax() + 0.1f);
		ax.setMin(ax.getMin() - 0.1f);
		ay.setMin(ay.getMin() - 0.1f);
		rend.setAxisRanges(ax, ay);
		// rend.setVarpos(new int[] { px, py, pz });
		// rend.setMode(Render._2D);
		// rend.setEye(750, 750, 1500);
		// rend.setRotation(0.15, 0.15, .50);
		// rend.setRotation(Math.PI/3,Math.PI/4,Math.PI/6);

		dyn = new IteratedMapSimulator(sys, rend);

		test();
	}

	public void test() throws DynSimException {
		dyn.setSkip(0);
		// dyn.setItersMax(2000000);
		dyn.setItersMax(50000000);

		dyn.compute();

		display.setImage(rend.getImage());

		rend.flush();
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
