package dynsim.graphics.plot.j2d.axis;

import java.util.Iterator;

import dynsim.data.DataSlot;
import dynsim.data.Storage;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.Grapher2DUpdater;
import dynsim.graphics.plot.j2d.layer.Marker;
import dynsim.math.util.MathUtils;

/**
 * @author maf
 * 
 */
public class AxesUpdater implements Grapher2DUpdater {
	boolean checkAxes(Axis y, Axis x) {
		boolean c1 = badAxes(y, x);
		boolean c2 = !y.isLog();

		return c1 && c2;
	}

	public static boolean badAxes(Axis y, Axis x) {
		boolean c1 = (y.getEnd() == y.getStart()) || (x.getEnd() == x.getStart());
		return c1;
	}

	public void update(Grapher2D g) {
		GrapherConfig config = g.getGrapherConfig();

		if (checkAxes(g.getAxisY(), g.getAxisX())) {

			if (g.hasDataSlots()) {

				Iterator<DataSlot> itor = g.getSlotsIterator();
				double txmin, txmax, tymin, tymax;
				double xmin = Double.MAX_VALUE, xmax = -Double.MAX_VALUE, ymin = xmin, ymax = xmax;
				Storage tmp;

				boolean ok = false;

				while (itor.hasNext()) {
					DataSlot slot = itor.next();
					tmp = slot.getData();

					if (!tmp.hasMaxMin()) {
						continue;
					}

					txmin = tmp.getMin(config.getPlotVarX());
					txmax = tmp.getMax(config.getPlotVarX());

					if (txmin < xmin) {
						xmin = txmin;
					}
					if (txmax > xmax) {
						xmax = txmax;
					}

					tymin = tmp.getMin(config.getPlotVarY());
					tymax = tmp.getMax(config.getPlotVarY());

					if (tymin < ymin) {
						ymin = tymin;
					}
					if (tymax > ymax) {
						ymax = tymax;
					}

					ok = true;
				}

				if (!ok)
					return;

				checkAxesBounds(xmax, xmin, g);
				checkAxesBounds(ymax, ymin, g);

			} else {
				Storage data = g.getCurrentData();
				if (data.hasMaxMin()) {
					checkAxesBounds(data.getMin(config.getPlotVarX()), data.getMin(config.getPlotVarY()), g);
					checkAxesBounds(data.getMax(config.getPlotVarX()), data.getMax(config.getPlotVarY()), g);
				}
			}

			if (g.hasMarkerPoints()) {
				Iterator<Marker> fit = g.getMarkersIterator();
				while (fit.hasNext()) {
					Marker fp = fit.next();
					checkAxesBounds(fp.getCoord(config.getPlotVarX()), fp.getCoord(config.getPlotVarY()), g);
				}
			}
		}

		if (!g.hasDataSlots()) {
			config.setAutoAdjust(false);
		}
		g.setNeedRepaint(false); // don't need repaint this time
	}

	public void checkAxesBounds(double x, double y, Grapher2D g) {
		GrapherConfig config = g.getGrapherConfig();
		Axis yAxis = g.getAxisY();
		Axis xAxis = g.getAxisX();

		if (!config.isAutoAdjust())
			return;

		if (!(checkDouble(x) && checkDouble(y)))
			return;

		// TODO check...
		if (yAxis.isLog()) {
			y = Math.log10(y);
		}

		double maxX = xAxis.getEnd(), maxY = yAxis.getEnd();
		double minX = xAxis.getStart(), minY = yAxis.getStart();

		boolean grow = false;

		if (x > maxX) {
			maxX = x;
			grow = true;
		}
		if (y > maxY) {
			maxY = y;
			grow = true;
		}

		if (x < minX) {
			minX = x;
			grow = true;
		}
		if (y < minY) {
			minY = y;
			grow = true;
		}

		if (grow) {
			growAxis(yAxis, xAxis, maxX, maxY, minX, minY);
			g.setNeedRepaint(true);
		}
	}

	private boolean checkDouble(double x) {
		return !(Double.isNaN(x) || Double.isInfinite(x));
	}

	private void growAxis(Axis yAxis, Axis xAxis, double maxX, double maxY, double minX, double minY) {
		if (minX < xAxis.getStart()) {
			double n = minX / xAxis.getTickStep();
			n = MathUtils.trunc(n, 0);
			xAxis.setStart((n - 1) * xAxis.getTickStep());
		}
		if (minY < yAxis.getStart()) {
			double n = minY / yAxis.getTickStep();
			n = MathUtils.trunc(n, 0);
			yAxis.setStart((n - 1) * yAxis.getTickStep());
		}
		if (maxX > xAxis.getEnd()) {
			double n = maxX / xAxis.getTickStep();
			n = MathUtils.trunc(n, 0);
			xAxis.setEnd((n + 1) * xAxis.getTickStep());
		}
		if (maxY > yAxis.getEnd()) {
			double n = maxY / yAxis.getTickStep();
			n = MathUtils.trunc(n, 0);
			yAxis.setEnd((n + 1) * yAxis.getTickStep());
		}
	}

}
