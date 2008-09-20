package dynsim.graphics.plot.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public class FloatRect {
	private static final long serialVersionUID = -3937075319700676669L;

	private Rectangle rect;

	FloatRect(Point p, Dimension d) {
		rect = new Rectangle(p, d);
	}

	public float getHeight() {
		return (float) rect.getHeight();
	}

	public float getWidth() {
		return (float) rect.getWidth();
	}

	public float getX() {
		return (float) rect.getX();
	}

	public float getY() {
		return (float) rect.getY();
	}

	public float getCenterX() {
		return (float) rect.getCenterX();
	}

	public float getCenterY() {
		return (float) rect.getCenterY();
	}

	public float getMaxX() {
		return (float) rect.getMaxX();
	}

	public float getMaxY() {
		return (float) rect.getMaxY();
	}

	public float getMinX() {
		return (float) rect.getMinX();
	}

	public float getMinY() {
		return (float) rect.getMinY();
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}
}
