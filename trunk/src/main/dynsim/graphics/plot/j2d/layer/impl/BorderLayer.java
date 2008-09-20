package dynsim.graphics.plot.j2d.layer.impl;

import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.layer.AbstractGrapherLayer;

public class BorderLayer extends AbstractGrapherLayer {

	public static boolean fireRule(GrapherConfig config) {
		return config.isEnabled(GrapherConfig2D.DRAW_BORDER);
	}

	public void draw(Grapher2D g) {
		GrapherConfig config = g.getGrapherConfig();
		g.strokeColor(g.getForeground());
		g.strokeWeight(config.getBorderWeight());

		g.disableFill();
		int mw = g.getHalfMW();
		int mh = g.getHalfMH();

		g.drawRect(mw, mh, g.getWidth() - mw, g.getHeight() - mh);
	}

	@Override
	public boolean isDrawable(GrapherConfig config) {
		return fireRule(config);
	}
}
