package org.mafc.blob;

public class Blob {
	public Vector2D loc;

	public Vector2D acc;

	public Vector2D vel;

	public double density, mass, volume;

	public double radius;

	private BoundRect bounds;

	private double maxVel;

	public Blob(double r, Vector2D l, Vector2D a, BoundRect b, double d, double mv) {
		this.radius = r;
		loc = new Vector2D(l.x, l.y);
		acc = new Vector2D(a.x, a.y);
		vel = new Vector2D(0., 0.);
		volume = 4 * Math.PI * (radius * radius * radius);
		volume /= 3;
		density = d;
		mass = volume * density;
		maxVel = mv;
		bounds = b;

		System.out.println(this);
	}

	public Blob(double r, Vector2D l, Vector2D a, BoundRect b, double d) {
		this(r, l, a, b, d, 150);
	}

	public Blob(double r, Vector2D l, Vector2D a, double d) {
		this(r, l, a, null, d);
	}

	public void update(float deltatime) {
		if (Math.abs(vel.x) >= maxVel) {
			vel.x = Math.signum(vel.x) * maxVel;
		}

		if (Math.abs(vel.y) >= maxVel) {
			vel.y = Math.signum(vel.y) * maxVel;
		}

		// Euler (fast enough)
		Vector2D oVel = vel.copy();
		oVel.add(acc.times(deltatime));
		vel.add(acc);
		loc.add(oVel.mul(deltatime).mul(0.5));

		// oscilador
		// loc.x += 80*Math.sin(vel.x*Math.PI/180);
		// loc.y += 70*Math.sin(vel.y*Math.PI/180);

		acc.clear();

		checkBounds();
	}

	private void checkBounds() {
		if (loc.x <= bounds.x_min) {
			loc.x = bounds.x_min;
			vel.x = -vel.x;
		}
		if (loc.y <= bounds.y_min) {
			loc.y = bounds.y_min;
			vel.y = -vel.y;
		}
		if (loc.x >= bounds.x_max) {
			loc.x = bounds.x_max;
			vel.x = -vel.x;
		}
		if (loc.y >= bounds.y_max) {
			loc.y = bounds.y_max;
			vel.y = -vel.y;
		}
	}

	public String toString() {
		String s = "Blob [";
		s += " r: " + radius;
		s += " V: " + volume;
		s += " D: " + density;
		s += " M: " + mass;
		s += " maxv: " + maxVel;
		s += " bounds: " + bounds;
		s += "]";
		return s;
	}

	public double dist(Blob b) {
		double dx = b.loc.x - loc.x;
		double dy = b.loc.y - loc.y;

		return Math.sqrt((dx * dx) + (dy * dy));
	}

	public double pxDist(double x, double y) {
		double dx = x - loc.x;
		double dy = y - loc.y;

		return Math.sqrt((dx * dx) + (dy * dy));
	}

	public double dist1P(Blob b) {
		double dx = b.loc.x - loc.x;
		double dy = b.loc.y - loc.y;

		return Math.abs((dx * dx) + (dy * dy));
	}

	public double pxDist1P(int x, int y) {
		double dx = x - loc.x;
		double dy = y - loc.y;

		return Math.abs((dx * dx) + (dy * dy));
	}

	public BoundRect getBounds() {
		return bounds;
	}

	public void setBounds(BoundRect bounds) {
		this.bounds = bounds;
	}
}
