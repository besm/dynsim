package dynsim.math.analysis.global;

import java.awt.Color;
import java.awt.image.BufferedImage;

import dynsim.graphics.ScreenMap;
import dynsim.graphics.plot.j2d.RasterGrapher2D;

//TODO ||x(i+n)-x(i)|| < e / n=k*n0 / k = period
// si el sistema forzado
//n0= número de muestras por periodo de forzado fundamental
// else
//n0= media del histograma de tiempo de recurrencia 
// [ver Lathrop y Kostelich]
// Para series divididas por el valor máximo de la serie
// e=0.005 
// En ventanas n+1,n+2... del mismo periodo =>
// media entre puntos o mínimo e
public class BifurcationPlot extends RasterGrapher2D implements BifurcationPlotDelegate {
	private static final long serialVersionUID = 2458236850897074084L;

	BifurcationDiagram1D bd;

	BufferedImage[] layers;

	int dotWeight;

	boolean onlyLayers;

	int[] palette;

	public BifurcationPlot(int w, int h) {
		super(w, h);
		dotWeight = 1;
		onlyLayers = false;
		palette = new int[] { Color.yellow.getRGB(), Color.orange.getRGB(), Color.red.getRGB(), Color.green.getRGB(),
				Color.blue.getRGB(), Color.cyan.getRGB(), Color.white.getRGB() };
	}

	public BifurcationDiagram1D createBifurcationDiagram() {
		bd = new BifurcationDiagram1D(this);
		bd.setItersNum(getHeight());
		bd.setParamItersNum(getWidth());
		return bd;
	}

	public void compute() {
		bd.compute();

		if (!isOnlyLayers()) {
			end();
		}
	}

	public void putpx(double x, double y, int period) {
		if (notInAxisRange((float) x, (float) y)) {
			return;
		}

		x = ScreenMap.toScreenFloat(x, getWidth() - getMarginW(), getAxisX().getStart(), getAxisX().getEnd());

		// Nota: max. Y es 0 en coordenadas de pantalla => necesario invertirlo
		y = ScreenMap.toScreenFloat(y, getHeight() - getMarginH(), getAxisY().getEnd(), getAxisY().getStart());

		x += getHalfMW();
		y += getHalfMH();

		int ix = (int) x;
		int iy = (int) y;

		if (checkBounds((float) x, (float) y)) {
			// if (bd.getWarm() == 0) {
			// if (layers == null) {
			// layers = new BufferedImage[bd.getMaxPeriod()];
			// }
			// if (layers[period] == null) {
			// GraphicsEnvironment ge = GraphicsEnvironment
			// .getLocalGraphicsEnvironment();
			// GraphicsConfiguration gc = ge.getDefaultScreenDevice()
			// .getDefaultConfiguration();
			//
			// layers[period] = gc.createCompatibleImage(getWidth()
			// - getMarginW(), getHeight() - getMarginH(),
			// Transparency.TRANSLUCENT);
			// }
			// Graphics2D g = layers[period].createGraphics();
			//
			// g.setClip(ix, iy - 2, 2, 6);
			// g.setColor(getBackground());
			// g.fill(g.getClipBounds());
			//
			// g.setColor(getColor(period));
			// g.fillRect(ix, iy, dotWeight, dotWeight);
			//
			// // g.fillOval(ix - radius, iy - radius, 2*radius, 2*radius);
			// // g.fillRect(ix-1, iy-1, 2, 2);
			//
			// } else {
			this.fillRect(ix, iy, dotWeight, dotWeight, getColor(period));
			// this.fillCircle(ix, iy, 1, getColor(period));
			// }
		}
	}

	public void end() {
		if (layers == null) {
			return;
		}

		// Dibujamos las capas empezando desde el final
		for (int v = layers.length - 1; v > -1; v--) {
			if (layers[v] != null) {
				// Composite orig = g2.getComposite();
				// g2.setComposite(makeComposite(0.5f));
				g2.drawImage(layers[v], 0, 0, null);
				// g2.setComposite(orig);
			}
		}
	}

	// private AlphaComposite makeComposite(float alpha) {
	// int type = AlphaComposite.SRC_OVER;
	// return (AlphaComposite.getInstance(type, alpha));
	// }

	private Color getColor(int n) {
		if (palette.length - 1 < n) {
			return Color.gray;
		}

		return new Color(palette[n]);
	}

	public int getDotWeight() {
		return dotWeight;
	}

	public void setDotWeight(int dotWeight) {
		this.dotWeight = dotWeight;
	}

	public BufferedImage[] getLayers() {
		return layers;
	}

	public boolean isOnlyLayers() {
		return onlyLayers;
	}

	public void setOnlyLayers(boolean onlyLayers) {
		this.onlyLayers = onlyLayers;
	}

	public int[] getPalette() {
		return palette;
	}

	public void setPalette(int[] palette) {
		this.palette = palette;
	}

}
