package dynsim.graphics.plot.j2d.layer.impl;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.text.NumberFormat;

import dynsim.graphics.ScreenMap;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.axis.AxesUpdater;
import dynsim.graphics.plot.j2d.axis.Axis;
import dynsim.graphics.plot.j2d.layer.AbstractGrapherLayer;
import dynsim.math.util.MathUtils;

public class AxesLayer extends AbstractGrapherLayer {
	public boolean isDrawable(GrapherConfig config) {
		return fireRule(config);
	}

	public void draw(Grapher2D g) {
		Axis yAxis = g.getAxisY();
		Axis xAxis = g.getAxisX();
		if (AxesUpdater.badAxes(yAxis, xAxis)) {
			return;
		}

		GrapherConfig config = g.getGrapherConfig();
		// TODO axisline weight
		g.strokeWeight(1);

		if (config.isEnabled(GrapherConfig2D.DRAW_AXIS_X)) {
			g.plotLine(xAxis.getStart(), 0, xAxis.getEnd(), 0, g.getForeground());
		}
		if (config.isEnabled(GrapherConfig2D.DRAW_AXIS_Y)) {
			g.plotLine(0, yAxis.getStart(), 0, yAxis.getEnd(), g.getForeground());
		}

		if (config.isEnabled(GrapherConfig2D.DRAW_AXIS_TEXT_X) || config.isEnabled(GrapherConfig2D.DRAW_AXIS_TEXT_Y)) {
			drawScalesText(yAxis, xAxis, config, g);
		}
	}

	private void drawScalesText(Axis yAxis, Axis xAxis, GrapherConfig config, Grapher2D g) {
		if (AxesUpdater.badAxes(yAxis, xAxis)) {
			return;
		}

		Graphics2D g2 = g.getOffscrGraphics();

		g2.setFont(g.getFont());
		FontMetrics fm = g.getFontMetrics(g2.getFont());
		float mw = g.getHalfMW();
		float mh = g.getHalfMH();
		if (config.isEnabled(GrapherConfig2D.DRAW_AXIS_TEXT_Y)) {
			double tickStep = yAxis.getTickStep();

			// TODO log, logx
			int parts = 10;
			double dx = tickStep / parts;

			if (yAxis.isLog()) {
				for (double i = yAxis.getStart(); i <= yAxis.getEnd(); i += tickStep) {

					int ni = 0;

					for (double n = 0; n < tickStep; n += dx) {
						ni++;
						if (n <= 0)
							continue;

						double log = Math.log10(n) + i;

						float p = ScreenMap.toScreenFloat(log, (int) (g.getHeight() - config.getMarginH()), yAxis
								.getEnd(), yAxis.getStart());

						if (log <= yAxis.getEnd() && log >= yAxis.getStart()) {
							if (ni == parts + 1) {
								String s = NumberFormat.getInstance().format(MathUtils.trunc(i * (ni - 1), 1));
								g2.drawString(s, mw - fm.stringWidth(s) - (fm.charWidth('.') << 1), p + mh
										+ (fm.getHeight() >> 1));
								g.drawLine(mw + config.getTickLen() * 2, mh + p, mw, mh + p);
							} else {
								g.drawLine(mw + config.getTickLen(), mh + p, mw, mh + p);
							}
						}
						// Font f = getFont();
						// s = NumberFormat.getInstance().format(
						// MathUtils.trunc(e, 1));
						// e++;
						// g2.setFont(new Font("Arial", Font.PLAIN, 8));
						// g2.drawString(s, mw - (fm.charWidth('.') << 1), p
						// + mh - (fm.getHeight() >> 3));
						// g2.setFont(f);
						// }
					}
				}
			} else {
				// Y
				double start = yAxis.getStart();

				if (config.isEnabled(GrapherConfig2D.DRAW_AXIS_TEXT_X)) {
					start += tickStep; // skip first
				}

				for (double i = start; i <= yAxis.getEnd(); i += tickStep) {
					float p = ScreenMap.toScreenFloat(i, (int) (g.getHeight() - config.getMarginH()), yAxis.getEnd(),
							yAxis.getStart());
					String s = NumberFormat.getInstance().format(MathUtils.trunc(i, 1));
					g2.drawString(s, mw - fm.stringWidth(s) - fm.charWidth('.'), p + mh + (fm.getHeight() >> 1));
					if (i != yAxis.getStart() && i + tickStep <= yAxis.getEnd()) {
						g.drawLine(mw + config.getTickLen(), mh + p, mw, mh + p);
					}
				}
			}

		}

		if (config.isEnabled(GrapherConfig2D.DRAW_AXIS_TEXT_X)) {

			if (xAxis.isLog()) {
				// TODO impl xAxis Log... en métodos distintos refactor
			} else {
				// X
				for (double i = xAxis.getStart(); i <= xAxis.getEnd(); i += xAxis.getTickStep()) {
					float p = ScreenMap.toScreenFloat(i, (int) (g.getWidth() - config.getMarginW()), xAxis.getStart(),
							xAxis.getEnd());
					String s = NumberFormat.getInstance().format(MathUtils.trunc(i, 1));
					g2.drawString(s, (p + mw) - (fm.stringWidth(s) >> 1), (g.getHeight() - mh) + fm.getHeight());
					// + (fm.getHeight() >> 1 )

					if (i != xAxis.getStart() && i + xAxis.getTickStep() <= xAxis.getEnd()) {
						g.drawLine(mw + p, g.getHeight() - mh - config.getTickLen(), mw + p, g.getHeight() - mh);
					}
				}
			}
		}
	}

	public static boolean fireRule(GrapherConfig config) {
		return config.isEnabled(GrapherConfig2D.DRAW_AXIS_X) || config.isEnabled(GrapherConfig2D.DRAW_AXIS_Y);
	}
}
