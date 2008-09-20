package dynsim.graphics.plot.j2d.mng;

import dynsim.graphics.plot.j2d.Grapher2D;

public abstract class GrapherManager {
	protected Grapher2D g;

	public GrapherManager(Grapher2D grapher) {
		this.g = grapher;
	}

	public void process(GrapherAction action) {
		action.execute(this);
	}
}
