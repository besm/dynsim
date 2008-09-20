package dynsim.graphics.plot.j2d.layer;

import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.math.numeric.Computable;

public interface GrapherLayer extends Computable {
	public void draw(Grapher2D g);

	public void init(Grapher2D g);

	public abstract boolean isDrawable(GrapherConfig config);
}
