package org.mafc.blob;

class BoundRect {
	double x_min; // Left

	double x_max; // Right

	double y_min; // Bottom

	double y_max; // Top

	BoundRect(double xlo, double ylo, double xhi, double yhi) {
		x_min = xlo;
		x_max = xhi;
		y_min = ylo;
		y_max = yhi;
	}

	Vector2D minCorner() {
		return new Vector2D(x_min, y_min);
	}

	Vector2D maxCorner() {
		return new Vector2D(x_max, y_max);
	}

	boolean contains(Vector2D v) {
		return contains(v.x, v.y);
	}

	boolean contains(double x, double y) {
		return (x >= x_min) && (x < x_max) && (y >= y_min) && (y < y_max);
	}

	void print() {
		System.out.println("(" + x_min + ":" + y_min + "):(" + x_max + "," + y_max + ")");
	}

	boolean intersects(BoundRect r) {
		boolean result = true;
		if (x_max <= r.x_min)
			result = false;
		if (x_min >= r.x_max)
			result = false;
		if (y_max <= r.y_min)
			result = false;
		if (y_min >= r.y_max)
			result = false;
		return result;
	}
}
