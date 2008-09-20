package dynsim.graphics.plot.j2d;

import java.awt.image.BufferedImage;

import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.j2d.axis.Axis;

//TODO extraer una interfaz de servicio decente...
// GrapherService? una fachada + obj config con todos los parámetros??
public class Grapher2DService extends Grapher2D {

	private static final long serialVersionUID = -234242173306321548L;

	private BufferedImage img;

	public Grapher2DService(int w, int h, Axis xa, Axis ya, GrapherConfig conf) {
		super(w, h, xa, ya, conf);
		setSize(w, h);
	}

	public Grapher2DService(int w, int h, Axis xa, Axis ya) {
		this(w, h, xa, ya, null);
	}

	public Grapher2DService(int w, int h, GrapherConfig conf) {
		this(w, h, null, null, conf);
	}

	public Grapher2DService(int w, int h) {
		this(w, h, null, null, null);
	}

	public void render() {
		img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		super.paint(img.getGraphics());
	}

	public BufferedImage getImage() {
		return img;
	}
}
