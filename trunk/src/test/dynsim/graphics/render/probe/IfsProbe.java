/**
 * 
 */
package dynsim.graphics.render.probe;

import java.awt.BorderLayout;
import java.awt.Color;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.render.RenderConfig;
import dynsim.graphics.render.impl.DensityRenderer;
import dynsim.graphics.render.util.DisplayImage;
import dynsim.graphics.render.util.FloatRange;
import dynsim.simulator.Simulator;
import dynsim.simulator.helper.BoundsFinder;
import dynsim.simulator.ifs.IfsSimulator;
import dynsim.simulator.ifs.system.IteratedFunctionSystem;
import dynsim.simulator.ifs.system.impl.StochasticIfs;
import dynsim.simulator.ifs.system.loader.IfsDatum;
import dynsim.simulator.ifs.system.loader.IfsLoader;
import dynsim.ui.AppFrame;

/**
 * @author maf83
 * 
 */
public class IfsProbe extends AppFrame {
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
		IfsProbe p = new IfsProbe();
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
		IteratedFunctionSystem sys;
		px = 0;
		py = 1;
		pz = 2;

		String fname = "data/test/ifs/ifs.xml";

		IfsLoader il = new IfsLoader();
		il.loadFromFile(fname);
		String sn = "dragon";
		// sn = "farn";
		sn = "swirl";
		// sn="fuckup";
		// sn="Schere";
		// sn="Koch";
		// sn="Seltsam";
		// sn = "coral";
		IfsDatum data = il.getIfsById(sn);
		IteratedFunctionSystem ifs = new StochasticIfs(data);

		sys = ifs;

		bounds(new IfsSimulator(sys));

		rend = new DensityRenderer();
		RenderConfig cfg = new RenderConfig(RenderConfig.MODE_2D);
		rend.setConfig(cfg);
		rend.setAxisRanges(ax, ay, az);
		rend.setVarpos(new int[] { px, py, pz });
		rend.setScale(2f);
		rend.setDefaultCorrection();
		rend.setGamma(0.5f);

		dyn = new IfsSimulator(sys, rend);

		test();
		rend.flush();
	}

	public void test() throws DynSimException {
		dyn.setSkip(100000);
		// dyn.setItersMax(2000000);
		dyn.setItersMax(5000000);

		dyn.compute();

		display.setImage(rend.getImage());
		display.repaint();

		System.out.println(IfsProbe.class.getName() + " end");
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
