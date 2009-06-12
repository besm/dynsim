package dynsim.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

public class DrawingSurface extends Component {
	private static final Dimension MIN_SIZE = new Dimension(100, 100);

	private static final long serialVersionUID = -5474802891501411346L;

	protected BufferedImage offscr;

	protected Graphics2D g2;

	private final Line2D.Float line;

	private final Rectangle2D.Float rect;

	private int pw; // pref size

	private int ph;

	private int aw; // actual size (for resize)

	private int ah;

	private boolean stroke, fill, gradientStroke;

	protected Color strokeColor;

	private Color fillColor;

	private GradientPaint strokeGradient;

	private float strokeWeight;

	private final Logger log = Logger.getLogger(DrawingSurface.class.getName());

	private final Dimension preferredDimension;

	public DrawingSurface(int width, int height) {
		this.line = new Line2D.Float();
		this.rect = new Rectangle2D.Float();
		this.offscr = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		this.stroke = true;
		this.fill = true;
		this.gradientStroke = false;
		this.ph = height;
		this.pw = width;
		this.ah = ph;
		this.aw = pw;

		preferredDimension = new Dimension(pw, ph);

		updateGraphics();
	}

	private void updateGraphics() {
		this.g2 = offscr.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public void fillRect(final int x, final int y, final int width, final int height, final Color color) {
		g2.setColor(color);
		g2.fillRect(x, y, width, height);
	}

	protected void drawShape(final Shape s) {
		if (gradientStroke) {
			g2.setPaint(strokeGradient);
			g2.draw(s);
		} else if (stroke) {
			g2.setColor(strokeColor);
			g2.draw(s);
		}
	}

	public void drawPoint(final float x, final float y) {
		// drawLine(x, y, x, y);
		fillRect((int) x, (int) y, 1, 1, strokeColor);
	}

	public void drawLine(final float x1, final float y1, final float x2, final float y2) {
		line.setLine(x1, y1, x2, y2);
		drawShape(line);
	}

	public void drawRect(final int x, final int y, final int width, final int height, final Color color) {
		g2.setColor(color);
		g2.drawRect(x, y, width, height);
	}

	public void drawCircle(final int x, final int y, final int radius, final Color color) {
		g2.setColor(color);
		g2.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
	}

	public void fillCircle(final int x, final int y, final int radius, final Color color) {
		g2.setColor(color);
		g2.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
	}

	protected void fillShape(final Shape s) {
		if (fill) {
			g2.setColor(fillColor);
			g2.fill(s);
		}
		if (stroke) {
			g2.setColor(strokeColor);
			g2.draw(s);
		}
	}

	protected boolean isResized() {
		return (getWidth() != aw || getHeight() != ah);
	}

	protected void checkResize() {
		if (isResized()) {
			log.info("Resize " + getWidth() + "x" + getHeight());
			offscr = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
			updateGraphics();

			aw = getWidth();
			ah = getHeight();
		}
	}

	public void paint(final Graphics g) {
		super.paint(g);
		final Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(offscr, null, 0, 0);
	}

	public void strokeWeight(final float weight) {
		strokeWeight = weight;
		strokeNormal();
	}

	public void strokeDashed() {
		g2.setStroke(new BasicStroke(strokeWeight, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.0f,
				new float[] { 3.5f }, 0.0f));
	}

	public void strokeNormal() {
		g2.setStroke(new BasicStroke(strokeWeight, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
	}

	public void strokeColor(final Color c) {
		strokeColor = c;
	}

	public void strokeGradient(final GradientPaint g) {
		strokeGradient = g;
		gradientStroke = true;
	}

	public void disableGradient() {
		gradientStroke = false;
	}

	public void disableFill() {
		fill = false;
	}

	public void drawRect(final float x, final float y, final float w, final float h) {
		rect.setFrame(x, y, w - x, h - y);
		fillShape(rect);
	}

	public void fillColor(final Color c) {
		fillColor = c;
	}

	/**
	 * The preferred size.
	 */
	public Dimension getPreferredSize() {
		return preferredDimension;
	}

	/**
	 * The minimum size.
	 */
	public Dimension getMinimumSize() {
		return MIN_SIZE;
	}
}
