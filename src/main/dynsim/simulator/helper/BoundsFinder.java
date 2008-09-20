/**
 * 
 */
package dynsim.simulator.helper;

import java.util.logging.Logger;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.render.util.FloatRange;
import dynsim.math.util.MathUtils;
import dynsim.simulator.Simulator;

/**
 * @author maf83
 * 
 */
public class BoundsFinder {
	private static Logger log = Logger.getLogger(BoundsFinder.class.getName());

	public static FloatRange[] getBounds(Simulator sim) throws DynSimException {
		sim.setSkip(1000);
		sim.compute();
		double[][] rs = sim.getRawResults();

		int dim = rs.length;

		FloatRange[] frs = new FloatRange[dim];

		for (int i = 0; i < dim; i++) {
			FloatRange tmp;

			double[] mm = MathUtils.minMax(rs[i]);

			tmp = new FloatRange((float) mm[0], (float) mm[1]);

			frs[i] = tmp;

			log.info("v(" + i + "):" + tmp);
		}

		return frs;
	}
}
