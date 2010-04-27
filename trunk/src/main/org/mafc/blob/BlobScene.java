package org.mafc.blob;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import dynsim.graphics.animation.AnimationComponent;
import dynsim.graphics.color.Palette;

public class BlobScene extends AnimationComponent {
	private static final double G = 667428 * 10E-11;

	private static final long serialVersionUID = 4810370655016984259L;

	public static final int BARNESHUT = 0;

	public static final int NAIVE = 1;

	private ArrayList<Blob> blobs;

	private BufferedImage scaledImage;

	private Palette pal;

	private int pixelRes;

	private Quadtree quadtree;

	private BoundRect bounds;

	private double max_error;

	private BoundRect scaledBounds;

	private String fpsLabel;

	private NumberFormat nf;

	private int mode;

	public BlobScene(int w, int h) {
		pixelRes = 3;
		setSize(w, h);
		bounds = new BoundRect(-pixelRes, -pixelRes, w + pixelRes, h + pixelRes);
		mode = BARNESHUT;
		fpsLabel = "";
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		init();
	}

	public void setBufferStrategy(BufferStrategy bufferStrategy) {
		super.setBufferStrategy(bufferStrategy);
		offImage = createCompatibleImage(getWidth(), getHeight());
	}

	public void init() {
		if (mode == BARNESHUT) {
			// Create an empty quadtree.
			quadtree = new Quadtree(this, bounds);
			max_error = 0.5;
		}

		// pal = new Palette();
		pal = new Palette(0.0f, 0.5f); // 0.35, 0.95
		blobs = new ArrayList<Blob>();

		int pxr2 = pixelRes / 2;
		scaledBounds = new BoundRect(-pxr2, -pxr2, (getWidth() / pixelRes) + pxr2, (getHeight() / pixelRes) + pxr2);
		scaledImage = createCompatibleImage(getWidth() / pixelRes, getHeight() / pixelRes);
	}

	public void timeStep(float deltatime) {
		Blob b;

		for (int i = 0; i < blobs.size(); i++) {
			b = blobs.get(i);
			b.update(deltatime);
		}

		if (mode == BARNESHUT) {
			computeAccel();
			recreateQuadtree();
		}

		updateColors();
	}

	// public void updateColors2(Graphics g) {
	// for (int y = 0; y < offImage.getHeight(); y += pixelRes) {
	// for (int x = 0; x < offImage.getWidth(); x += pixelRes) {
	//
	// int color = getColor(y, x);
	// g.setColor(new Color(color));
	//
	// if (pixelRes <= 1) {
	// g.drawLine(x, y, x, y);
	// } else {
	// g.fillRect(x, getHeight() - pixelRes - y, pixelRes,
	// pixelRes);
	// }
	// }
	// }
	// }

	public void updateColors() {
		for (int y = 0; y < scaledImage.getHeight(); y++) {
			for (int x = 0; x < scaledImage.getWidth(); x++) {

				int color = getColor(y, x);
				scaledImage.setRGB(x, y, color);

			}
		}
	}

	private void interpolateToScreen() {
		AffineTransform tx = new AffineTransform();
		tx.scale(pixelRes + 0.1, pixelRes + 0.1);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		op.filter(scaledImage, offImage);
	}

	// private void updateForces(Blob b, int i) {
	// Blob b2;
	// for (int n = 0; n < blobs.size(); n++) {
	// if (n == i)
	// continue;
	// b2 = blobs.get(n);
	// double a = -G * ((b2.mass * b.mass) / (b.dist(b2) * b.dist(b2)));
	//
	// double dx = b.loc.x - b2.loc.x;
	// double dy = b.loc.y - b2.loc.y;
	//
	// Vector2D t = new Vector2D(dx, dy);
	// t = t.norm();
	//
	// b.acc.add(new Vector2D(a * t.x, a * t.y));
	// }
	// }

	public void paint(Graphics g) {
		interpolateToScreen();
		g.drawImage(offImage, 0, 0, this);

		drawFPS(g);
	}

	private void drawFPS(Graphics g) {
		g.setColor(Color.YELLOW);
		g.drawChars(fpsLabel.toCharArray(), 0, fpsLabel.length(), 3, getHeight() - 3);
	}

	private int getColor(int y, int x) {
		Blob b, b2;
		// float fc = 0;
		// float fact = 0.5f/blobs.size();
		//
		// for (int i = 0; i < blobs.size(); i++) {

		b = blobs.get(0);
		double f = b.volume / b.pxDist1P(x, y);

		for (int n = 1; n < blobs.size(); n++) {
			// if (n == i)
			// continue;
			b2 = blobs.get(n);
			f += b2.volume / b2.pxDist1P(x, y);

			// Elongaci�n
			// f *= (b2.mass + b.mass) / (b.pxDist(x, y) * b2.pxDist(x, y));
			f *= (b2.volume + b.volume) / (b.pxDist(x, y) * b2.pxDist(x, y));
		}

		// fc += f*fact;
		// }

		return pal.getColor(f);
	}

	// private float[] toHSB(Color aColor) {
	// return Color.RGBtoHSB(aColor.getRed(), aColor.getGreen(), aColor
	// .getBlue(), null);
	// }
	//
	// private int mix(int c1, int c2) {
	// float[] cc1 = toHSB(new Color(c1));
	// float[] cc2 = toHSB(new Color(c2));
	//
	// return Color.HSBtoRGB(msub(cc1[0], cc2[0]), cc2[1], cc2[2]);
	// }
	//
	// private float msub(float f1, float f2) {
	// float f = .5f * (f1 + f2);
	// if (f > 1)
	// f = 1;
	// return f;
	// }

	// private double dist(Blob b, double x, double y) {
	// double dx = b.loc.x - x;
	// double dy = b.loc.y - y;
	// return Math.abs(dx * dx + dy * dy);
	// }

	public Graphics getOffGraphics() {
		return offImage.createGraphics();
	}

	public void writeSnapshot() {
		try {
			File f = File.createTempFile("render", ".jpg", new File("img/"));
			ImageIO.write(offImage, "jpg", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void insert(Blob p) {
		p.setBounds(scaledBounds);
		blobs.add(p);

		// If the newly inserted point is in the current bounding
		// rectangle, then we can just insert it. Otherwise, we must
		// recreate the tree.
		if (bounds.contains(p.loc)) {
			quadtree.insert(p);
		} else {
			recreateQuadtree();
		}
	}

	// /

	// Compute some approximate measure of the error that would be
	// produced in computing the acceleration on the given body if we
	// approximated a group of points by their center of mass. The
	// radius input is an upper bound on the radius of a bounding sphere
	// for those points whose center is situated at the center of mass.
	double approxError(Blob p, Vector2D center, double radius) {
		// This error should grow as radius grows, and it should shrink as
		// the distance between body and the center of mass grows. It
		// should also grow as the mass of those points grows (since more
		// mass means more acceleration which means more error in the
		// calculation).

		// First, compute the squared distance between the given body and
		// the center of mass.
		double d2 = p.loc.minus(center).sqrMag();

		// If d2 is too small, we cannot divide by it, so say that the
		// error is infinite.
		if (d2 <= Double.MIN_VALUE)
			return Double.MAX_VALUE;

		// Otherwise, the following is a good appoximation of the error.
		return ((radius * radius) / d2);
	}

	// Compute the approximate acceleration on the given body due to a
	// group of bodies whose center of mass and total mass are as
	// given. This acceleration is added onto the given acceleration
	// parameter.
	void approxAccel(Blob p, Vector2D center, double mass, Vector2D accel) {
		// Find a vector pointing in the direction of the acceleration.
		// Vector2D R = p.loc.minus(center);

		// Find the distance between those points, which is simply the
		// length of the vector that stretches between them.
		// double r = R.mag();

		// If r is reallly small... well, bad things will happen.
		// we'll just ignore the interaction in that icky case.
		//
		// Otherwise, just compute the acceleration using Newton's brain
		Vector2D d = p.loc.minus(center);
		double r = d.mag();

		if (r > Double.MIN_VALUE) {
			// Add the appropriate acceleration of p1 due to p2
			double a = -G * (mass / (r * r));
			d = d.div(r); // unit vect
			accel.add(d.times(a));
			// accel.add(d.times(-(G * mass) / (r * r * r)));
		}
	}

	// Compute the approximate acceleration on the given body due to a
	// group of bodies whose center of mass and total mass are as
	// given. This acceleration is added onto the given acceleration
	// parameter.
	void computeAccel(Blob p1, Blob p2, Vector2D accel) {
		approxAccel(p1, p2.loc, p2.mass, accel);
	}

	// Compute the accelerations of every planet due to the gravity of
	// every other planet.
	void computeAccel() {
		// forceCount = 0;
		// Compute the acceleration of each planet.

		// if (_use_quad) {
		//
		// do it with style
		//
		for (int i = 0; i < blobs.size(); i++) {

			Blob p = blobs.get(i);

			// p.acc.zero();
			quadtree.computeAccel(p, p.acc, max_error);
		}
		// } else {
		//	
		// //
		// // do it the ol' fashioned O(n^2) way
		// //
		// for (PlanetIter Pptr = _planets.first();
		// !Pptr.isPastEnd();
		// Pptr.advance()) {
		//
		// Planet p = Pptr.element();
		//
		// p.acc.zero();
		//
		// for (PlanetIter Qptr = _planets.first();
		// !Qptr.isPastEnd();
		// Qptr.advance()) {
		//	
		// Planet q = Qptr.element();
		// if (p != q) {
		// computeAccel(p,q,p.acc);
		// }
		//		
		// }
		// }
		// }
	}

	// Find a bounding rectangle for all of the bodies currently in
	// this simulation.
	void computeBoundingRect() {
		// Start with an empty rectangle.
		bounds.x_min = bounds.y_min = Double.MAX_VALUE;
		bounds.x_max = bounds.y_max = -Double.MAX_VALUE;

		// Check the components of each planet to see if they are outside
		// of the current rectangle.
		for (int i = 0; i < blobs.size(); i++) {

			Blob p = blobs.get(i);
			if (p.loc.x < bounds.x_min)
				bounds.x_min = p.loc.x;
			if (p.loc.x > bounds.x_max)
				bounds.x_max = p.loc.x;
			if (p.loc.y < bounds.y_min)
				bounds.y_min = p.loc.y;
			if (p.loc.y > bounds.y_max)
				bounds.y_max = p.loc.y;
		}
	}

	// Recreate the quadtree with the new positions of the bodies.
	void recreateQuadtree() {
		// Recompute the bounding rectangle for these points.
		computeBoundingRect();

		// Create a new quadtree to store the points points that we just
		// loaded. First, we must free the other one.
		quadtree = new Quadtree(this, bounds);

		// Now, insert the planets into the Quadtree.
		for (int i = 0; i < blobs.size(); i++) {
			Blob p = blobs.get(i);
			quadtree.insert(p);
		}
	}

	public void drawRectangle(BoundRect bounds) {
		// TODO Auto-generated method stub

	}

	// remove all the bodies from the simulation
	void clear() {
		// delete each body found.
		for (int i = 0; i < blobs.size(); i++) {

			Blob p = blobs.get(i);

			quadtree.remove(p);
		}
		blobs = new ArrayList<Blob>();
	}

	// Delete bodies from the given set
	void delete(ArrayList<Blob> Ps) {
		// delete each body found.
		for (int i = 0; i < Ps.size(); i++) {
			Blob p = Ps.get(i);

			// Remove the body from our collection and the quadtree
			blobs.remove(p);
			quadtree.remove(p);
		}
	}

	public void rateChanged(double framerate) {
		fpsLabel = "fps: " + nf.format(framerate);
	}

	@Override
	public String toString() {
		return "NB: " + blobs.size();
	}

}
