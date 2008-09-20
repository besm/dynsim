package dynsim.graphics.render.probe;

import java.util.logging.Level;
import java.util.logging.Logger;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.render.Renderer;
import dynsim.graphics.render.util.DisplayImage;
import dynsim.graphics.render.util.FloatRange;
import dynsim.simulator.Simulator;
import dynsim.simulator.system.DynamicalSystem;

//TODO generalizar con Walker...
public class WSIterator {

	private static final int DELAY = 20000;

	private static Logger logger = Logger.getLogger(WSIterator.class.getPackage().getName());

	public static void iterate(Simulator sim, FloatRange xr, FloatRange yr, FloatRange zr, double step,
			DisplayImage display) {
		// sim.setSkip(0);
		// // dyn.setItersMax(100); // grado estructura
		// //
		// // double inc = 0.15; // parrilla
		// //
		// // for (double x = -6; x < 6; x += inc) {
		// // for (double y = -3; y < 3; y += inc) {
		// // for (double z = 5; z < 8.4; z += inc) {
		//	
		// sim.setItersMax(struct); // grado estructura

		double inc = step; // parrilla 0.075

		DynamicalSystem sys = sim.getSystem();
		Renderer rend = (Renderer) sim.getDelegate();

		boolean hasDisplay = (display == null) ? false : true;

		for (double x = xr.getMin(); x < xr.getMax(); x += inc) {
			for (double y = yr.getMin(); y < yr.getMax(); y += inc) {
				// se pone z fijo para cortes en lor ej 23, 10 black, 40..
				// for (double z = 0; z < 45; z += inc) {
				double[] initc = new double[] { 0, x, y, 5 };

				sys.setInitialConditions(initc);

				try {
					sim.compute();
				} catch (DynSimException e) {
					logger.log(Level.SEVERE, e.getLocalizedMessage());
				}

				sim.reset();

			}
			// }

			if (hasDisplay) {
				display.setImage(rend.getImage());
				display.repaint(DELAY);
			}
		}

		try {
			rend.flush();
		} catch (DynSimException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage());
		}

		logger.fine("End Of Computation. Repainting...");

		if (hasDisplay) {
			display.repaint();
		}
	}
}
