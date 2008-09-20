package dynsim.graphics.plot.j2d;

import dynsim.exceptions.DynSimException;
import dynsim.math.numeric.Computable;

public abstract class RasterGrapher2D extends Grapher2D implements Computable {
	private static final long serialVersionUID = -63985702831316796L;

	public RasterGrapher2D(int w, int h) {
		super(w, h);

	}

	@Override
	protected void beforeDraw() throws DynSimException {
		compute();
	}

}
