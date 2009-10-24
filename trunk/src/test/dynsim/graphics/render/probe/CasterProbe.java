package dynsim.graphics.render.probe;

import java.awt.BorderLayout;
import java.awt.Color;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.render.Renderer;
import dynsim.graphics.render.impl.CasterRenderer;
import dynsim.graphics.render.util.DisplayImage;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.ode.system.impl.Crispy;
import dynsim.simulator.system.DynamicalSystem;
import dynsim.ui.AppFrame;

public class CasterProbe extends AppFrame {
	private static final long serialVersionUID = -4767399338932470546L;

	Simulator dyn;

	Renderer rend;

	private DisplayImage display;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CasterProbe p = new CasterProbe();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		p.init();

		try {
			p.doIt();
		} catch (DynSimException e) {
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
		DynamicalSystem sys;

		sys = new Crispy();

		rend = new CasterRenderer();
		dyn = SimulatorFactory.createSimulator(sys, rend);

		rend.setAutoAxisRanges();
		// rend = new DensityRenderer();
		// ((DensityRenderer)rend).setTransparency(0.55f);
		// ((DensityRenderer)rend).setExposure(2f);
		// rend.setAxisRanges(ax, ay, az);
		// rend.setVarpos(new int[] { 1, 3, 2 });
		// rend.setRotation(0f, 0f, 0.25f);

//		 rend.setGamma(1.25f);
//		 rend.setScale(1.5f);

		// rend.setGamma(0.75f);
		// rend.setScale(1f);
		// rend.setDetail(0.005f);
		
		 rend.setGamma(1.25f);
		 rend.setScale(3.5f);
//		rend.setDetail(0.015f);
		
		rend.setDetail(0.00025f);

		// detail 0.015f
		
		test();
		rend.flush();
	}

	public void test() throws DynSimException {
		dyn.setSkip(10000);
		dyn.setItersMax(15000000);
//		dyn.setItersMax(10000000);

		dyn.compute();

		display.setImage(rend.getImage());
		display.repaint();

		System.out.println(CasterProbe.class.getName() + " end");
	}

}
