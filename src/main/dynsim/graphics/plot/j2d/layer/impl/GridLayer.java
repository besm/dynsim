package dynsim.graphics.plot.j2d.layer.impl;

import java.awt.Color;

import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.axis.Axis;
import dynsim.graphics.plot.j2d.layer.AbstractGrapherLayer;

public class GridLayer extends AbstractGrapherLayer {

	public void draw(Grapher2D g) {

		Axis ax = g.getAxisX();
		Axis ay = g.getAxisY();

		if (ax.getStart() == ax.getEnd() || ay.getStart() == ay.getEnd()) {
			return;
		}

		g.strokeColor(Color.GRAY);// g.getForeground());
		g.strokeDashed();

		if (ay.isLog()) {
			// TODO only for test!
			int parts = 10;
			double dx = ay.getTickStep() / parts;

			for (double i = ay.getStart(); i < ay.getEnd(); i += ay.getTickStep()) {
				for (double n = 0; n < ay.getTickStep(); n += dx) {
					if (n <= 0)
						continue;

					double log = Math.log10(n) + i;
					ay.setLog(false);
					g.plotLine(ax.getStart(), log, ax.getEnd(), log);
					ay.setLog(true);
				}
			}
			for (double i = ay.getEnd(); i > ay.getStart(); i -= ay.getTickStep()) {
				for (double n = 0; n < ay.getTickStep(); n += dx) {
					if (n <= 0)
						continue;

					double log = Math.log10(n) + i;
					ay.setLog(false);
					g.plotLine(ax.getStart(), log, ax.getEnd(), log);
					ay.setLog(true);
				}
			}

		} else {
			for (double i = 0; i < ay.getEnd(); i += ay.getTickStep()) {
				g.plotLine(ax.getStart(), i, ax.getEnd(), i);
			}

			for (double i = 0; i > ay.getStart(); i -= ay.getTickStep()) {
				g.plotLine(ax.getStart(), i, ax.getEnd(), i);
			}
		}

		for (double i = 0; i < ax.getEnd(); i += ax.getTickStep()) {
			g.plotLine(i, ay.getStart(), i, ay.getEnd());
		}

		for (double i = 0; i > ax.getStart(); i -= ax.getTickStep()) {
			g.plotLine(i, ay.getStart(), i, ay.getEnd());
		}

		g.strokeNormal();
	}

	public static boolean fireRule(GrapherConfig config) {
		return config.isEnabled(GrapherConfig2D.DRAW_GRID);
	}

	public boolean isDrawable(GrapherConfig config) {
		return fireRule(config);
	}
}
