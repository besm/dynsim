package dynsim.graphics.render.probe;

import java.awt.BorderLayout;
import java.awt.Color;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.render.Renderer;
import dynsim.graphics.render.impl.CasterRenderer;
import dynsim.graphics.render.util.DisplayImage;
import dynsim.simulator.Parameters;
import dynsim.simulator.Simulator;
import dynsim.simulator.SimulatorFactory;
import dynsim.simulator.ode.system.impl.Crispy;
import dynsim.simulator.system.DynamicalSystem;
import dynsim.ui.AppFrame;

public class MovieMakerProbe extends AppFrame {
	private static final long serialVersionUID = -4767399338932470546L;

	Simulator dyn;

	Renderer rend;

	private DisplayImage display;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MovieMakerProbe p = new MovieMakerProbe();
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
		DynamicalSystem sys = newSys();

		Parameters ps = sys.getParameters();
		double lastA = ps.getValue(Crispy.PARAM_A);
		int maxFrames = 200;
		for (int i = 0; i < maxFrames; i++) {
			sys.setParameter(Crispy.PARAM_A, lastA);
			dyn.compute();

			display.setImage(rend.getImage());
			display.repaint();

			System.out.println(CasterProbe.class.getName() + " end");
			rend.setFilename(i+"-");
			rend.flush();
			lastA += 0.0005;
			if(i<maxFrames)
			sys = newSys();
			
			System.out.println(lastA);
		}

	}

	private DynamicalSystem newSys() throws DynSimException {
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

		// rend.setGamma(1.25f);
		// rend.setScale(1.5f);

		// rend.setGamma(0.75f);
		// rend.setScale(1f);
		// rend.setDetail(0.005f);

		// rend.setGamma(1.25f);
		// rend.setScale(1.5f);
		// rend.setDetail(0.015f);

		rend.setDetail(0.055f);

		// detail 0.015f
		rend.setDirectory("data/images/anim");

		dyn.setSkip(10000);
		dyn.setItersMax(1000000);
		// dyn.setItersMax(10000000);
		return sys;
	}

}
