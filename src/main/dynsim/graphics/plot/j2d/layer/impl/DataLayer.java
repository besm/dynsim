package dynsim.graphics.plot.j2d.layer.impl;

import java.util.Iterator;

import dynsim.data.DataSlot;
import dynsim.data.DataSlotList;
import dynsim.graphics.plot.GrapherUtils;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.layer.AbstractGrapherLayer;

public class DataLayer extends AbstractGrapherLayer {
	private static final int CHUNK_SIZE = 512;

	private Grapher2D g;

	private GrapherConfig config;

	private DataSlotList dataSlots;

	public static boolean fireRule(GrapherConfig config) {
		return true;
	}

	@Override
	public boolean isDrawable(GrapherConfig config) {
		return fireRule(config);
	}

	public void draw(Grapher2D grapher) {
		this.g = grapher;

		config = g.getGrapherConfig();
		dataSlots = g.getDataSlots();

		if (g.hasDataSlots()) {
			Iterator<DataSlot> itor = g.getSlotsIterator();
			int i = 0;

			int csiz = 1;

			// TODO gestionar caso: slot con color plano!
			if (g.hasColorings()) {
				// csiz = colorings.length;
			} else {
				// default colors from config
				csiz = config.getColorsNum();
			}

			while (itor.hasNext()) {
				if (g.hasColorings()) {
					// curColoring = i;
				} else {
					// default colors from config
					g.strokeColor(config.getColor(i));
					i++;
					i %= csiz; // rotate colors
				}

				dataSlots.setCurrentSlot(itor.next());
				streamDraw();
			}
		} else {
			// Only one slot
			if (dataSlots.hasCurrentSlot()) {
				g.strokeColor(config.getMainColor());
				streamDraw();
			}
		}
	}

	// TODO DataStreamer
	protected void streamDraw() {
		int max = dataSlots.getCurrentDataRowsNum();

		int parts = max / CHUNK_SIZE;

		int resto = max % CHUNK_SIZE;

		if (parts > 0) {
			streamDrawParts(parts);
		}

		if (resto > 0) {
			streamDrawLast(max, parts, resto);
		}
	}

	private void streamDrawLast(int max, int parts, int resto) {
		double[] tmpx = new double[resto];
		double[] tmpy = new double[resto];
		int[] tmpc = new int[resto];
		for (int i = 0; i < resto; i++) {
			i = GrapherUtils.loadPart(g, parts, (max - resto), tmpx, tmpy, i);
		}

		plotPart(max - resto, resto, tmpx, tmpy, tmpc);
	}

	private void streamDrawParts(int parts) {
		for (int p = 0; p < parts; p++) {
			double[] tmpx = new double[CHUNK_SIZE];
			double[] tmpy = new double[CHUNK_SIZE];
			int[] tmpc = new int[CHUNK_SIZE];
			int partIndex = (p * CHUNK_SIZE);

			for (int i = 0; i < CHUNK_SIZE; i++) {
				i = GrapherUtils.loadPart(g, p, partIndex, tmpx, tmpy, i);
			}

			plotPart(partIndex, CHUNK_SIZE, tmpx, tmpy, tmpc);
		}
	}

	private void plotPart(int from, int to, double[] tmpx, double[] tmpy, int[] tmpc) {
		if (config.isAutoAdjust()) {
			for (int j = 0; j < tmpx.length; j++) {
				g.checkAxesBounds(tmpx[j], tmpy[j]);
			}
		} else {
			if (g.hasColorings()) {
				System.arraycopy(dataSlots.getCurrentColoring(), from, tmpc, 0, to);
				g.plot(tmpx, tmpy, tmpc);
			} else {
				g.plot(tmpx, tmpy);
			}
		}
	}

}
