package dynsim.graphics.plot.j2d.layer;

import dynsim.exceptions.DynSimException;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.j2d.Grapher2D;

public abstract class AbstractGrapherLayer implements GrapherLayer {

	public void init(Grapher2D g) {
		// TODO Auto-generated method stub
	}

	public void compute() throws DynSimException {
		// TODO Auto-generated method stub

	}

	public abstract boolean isDrawable(GrapherConfig config);
}
