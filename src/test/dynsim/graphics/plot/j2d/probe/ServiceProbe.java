package dynsim.graphics.plot.j2d.probe;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2DService;
import dynsim.simulator.color.MultirampColors;
import dynsim.simulator.ode.OdeSimulator;
import dynsim.simulator.ode.system.OdeSystem;
import dynsim.simulator.ode.system.impl.*;

public class ServiceProbe {
	public static void main(String[] args) {
		ServiceProbe p = new ServiceProbe();
		p.init();
	}

	private Grapher2DService svc;

	private OdeSystem sys;

	private OdeSimulator dyn;

	private Storage data;

	private void init() {
		GrapherConfig conf = new GrapherConfig2D();
		conf.setDrawConfig(GrapherConfig2D.DRAW_AXES);
		svc = new Grapher2DService(400, 400, conf);
		sim();
	}

	private void sim() {
		// sys = new SphericalRep();
		sys = new Lorentz();
		dyn = new OdeSimulator(sys);
		dyn.setSkip(2000);
		dyn.setItersMax(15000);
		try {
			dyn.compute();
		} catch (DynSimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data = dyn.getStateSeries();

		svc.addData(data, new MultirampColors());

		svc.setTickStep(2, 2);
		svc.getGrapherConfig().setPlotStyle(GrapherConfig2D.CURVES);

		svc.render();

		BufferedImage img = svc.getImage();

		try {
			writeToDisk("serv", "png", "data/images", img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void writeToDisk(String fname, String ext, String dir, BufferedImage img) throws IOException {
		File f = File.createTempFile(fname, "." + ext, new File(dir));
		ImageIO.write(img, ext, f);
	}

}
