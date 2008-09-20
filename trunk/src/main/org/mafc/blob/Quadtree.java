package org.mafc.blob;

//
// Module: Quadtree.java
//
// Purpose: This file will contain your implementation of the Quadtree
//          class.
//

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

// IMPLEMENTATION ROADMAP:
//
// Here is the order in which I would suggest that you implement this
// project.  This is most definitely not the only way to approach this.
//
// 1) Create your Node data type.  
//
//    You will want to read carefully through the lecture slides on
//    Quadtrees as well as the comments written here.  Think
//    carefully about your design!
//
// 2) Implement the constructor and insert.
//
//    You should test your implementation, perhaps by writing a 
//    function which prints out the contents of your tree to the 
//    console.  In the NBody applet, insert new planets by clicking
//    in the simulation window.  
//
//    Note that each time you insert a new body with the simulator
//    insert will either be called just once to add the new body, or
//    multiple times.  In the second case the tree gets recreated 
//    and all of the bodies reinserted.  This is necessary
//    because we need to have a bounding rectangle for all of the
//    points that will be inserted: since a newly inserted point is
//    not necessarily in the old bounding rectangle, we might recreate
//    the tree with a new bounding rectangle.
//
// 3) Implement drawGrid.
//
//    With this, you can press the "grid on" button and see quadrants
//    of your Quadtree.  This will give you a very good picture of
//    what your tree looks like, so that you can verify it is working
//    correctly.
//
// 4) Implement rangeQuery.
//
//    This will be one of the more difficult parts of this assignment.
//
//    Once you have finished range query, you can use the "Highlight"
//    feature to test it.  Drag the mouse over the region you want to
//    highlight.  Letting go of the mouse performs a range query on the
//    given rectangle and then highlights all of the bodies which were
//    returned by your rangeQuery function.
//
// 5) Implement remove.
//
//    Once you have finished this part, you can use the "Delete"
//    button to test your implementation.  Highlight a bunch of 
//    nodes and then hit "delete".  The program will delete
//    them individually with your remove function.
//
// 6) Implement computeAccel
//
//    This will also be one of the more difficult parts of this
//    assignment.  However, once you have completed it, you are done!
//    At this point, the N-Body simulation program is ready to go.
//    Turn on "Quadtree physics" by the "quad on" button.  This means
//    that the simulation will use the quadtree's computeAccel to
//    perform the simulation, rather than the naive O(n^2) method.
//    Turn on the "stats" to see whether you are reducing the number
//    of force calculations.  Hopefully, you will have a fun time 
//    playing around with it and answering the questions for your 
//    documentation.
//
// 7) Pat yourself on the back!  (Heck, you should have done this
//    with each step-- pat your back 7x)
//

class Quadtree {

	/** ************************************************************** */
	/** INSTANCE VARIABLES */

	// Declare your Quadtree Node data type. In addition to the
	// normal fields of a Quadtree node, it should contain the
	// following information about all of the bodies in the subtree
	// rooted at that node:
	//
	// 1) the center of mass of those bodies (a Vector)
	// 2) the total mass of those bodies (a double)
	// 3) the total number of bodies in this subtree (an int)
	// 4) a number R such that the circle centered at the center of
	// mass with radius R contains all of the nodes in the subtree.
	//
	// Each of these values is necessary either for performing the
	// approximate force calculations or determining the error in the
	// force calculations.
	// put your node's instance variables here:
	// Simulation sim;
	BoundRect bounds;

	BlobScene scene;

	int numBodies;

	double mass;

	Vector2D center;

	double radius;

	Blob mBody;

	Quadtree Q[];

	boolean marked;

	/** ************************************************************** */
	/** CONSTRUCTOR */

	// Create an empty Quadtree. The S parameter is a pointer to a
	// Simulation object which contains all of the information about
	// the current state of this simulation (the planet's masses,
	// positions, colors, et cetera). The bounds parameter
	// is a rectangle for which no inserted bodies will fall outside.
	Quadtree(BlobScene sc, BoundRect r) {

		// We will need to keep track of the simulation object so that we
		// can access the simulation computation routines.
		scene = sc;

		// Why is this bounding rectangle necessary? There is a good
		// reason.
		bounds = r;

		// YOUR INITIALIZATIONS HERE:
		// ...
		numBodies = 0;
		mBody = null;
		Q = null;

		center = new Vector2D();
		mass = 0.0;
		radius = 0.0;

		marked = false;
	}

	/** ************************************************************** */
	/** INSERT */
	// Insert the given body into the Quadtree. The number given is
	// the index we can pass to the simulation in order to request the
	// data about this body (its position, velocity, mass, et cetera).
	void insert(Blob body) {

		// When inserting the given body, we will need to update the extra
		// fields in each of the internal nodes. Those four fields can be
		// updated in the following manner.

		// Let C, M, R, and N denote the center of mass, total mass,
		// radius, and number of bodies, respectively. Let C', M', R',
		// and N' denote their new values after the insert. Then the new
		// values are related to the current values in the following
		// fashion.
		//
		// (1) N' = N + 1
		// (2) M' = M + mass(body)
		// (3) C' = (1 / M') * (M * C + mass(body) * position(body))
		// (4) R' = max(|position(body) - C'|, |C - C'| + R)
		//
		// (Note: the expression |V|, where V is some vector, represents
		// the norm of that vector.)
		//
		// Certainly, (1) and (2) are straightforward. (3) follows
		// immediately from the definition of the center of mass. (4) is
		// a bit tricky. There is actually no fast way for us to update R
		// exactly. Equation (4) creates R' only so that it is still an
		// upper-bound on the distance any body in this subtree could be
		// from the center of mass.

		// Look in Vector.java to see a complete listing of the operations
		// supported by our Vector type. Note in particular, that we have
		// functions for finding the norm of a vector and for finding the
		// distance between two vectors.

		// YOUR IMPLEMENTATION HERE:
		// ...
		if (numBodies == 0) {
			mBody = body;
			// totalMass = planet.mass;
			// centerMass.set(planet.pos);
			// ballRadius = 0.0;
		} else if (numBodies == 1) {
			split();
			Q[quadrant(mBody)].insert(mBody);
			mBody = null;
			Q[quadrant(body)].insert(body);
		} else {
			Q[quadrant(body)].insert(body);
		}
		numBodies++;
		insertFix(body);
	}

	void split() {
		double splitx = (bounds.x_min + bounds.x_max) / 2.0;
		double splity = (bounds.y_min + bounds.y_max) / 2.0;
		Q = new Quadtree[4];
		Q[0] = new Quadtree(scene, new BoundRect(bounds.x_min, bounds.y_min, splitx, splity));
		Q[1] = new Quadtree(scene, new BoundRect(splitx, bounds.y_min, bounds.x_max, splity));
		Q[2] = new Quadtree(scene, new BoundRect(bounds.x_min, splity, splitx, bounds.y_max));
		Q[3] = new Quadtree(scene, new BoundRect(splitx, splity, bounds.x_max, bounds.y_max));
	}

	int quadrant(Blob p) {
		double splitx = (bounds.x_min + bounds.x_max) / 2.0;
		double splity = (bounds.y_min + bounds.y_max) / 2.0;
		int q = 0;
		if (p.loc.x > splitx)
			q++;
		if (p.loc.y > splity)
			q += 2;
		return q;
	}

	void insertFix(Blob p) {
		Vector2D oldCenter = new Vector2D(center);
		center.mul(mass);
		center.add(p.loc.times(p.mass));
		mass += p.mass;
		center.mul(1.0 / mass);
		// (4) R' = max(|position(body) - C'|, |C - C'| + R)
		double r1 = center.dist(p.loc);
		double r2 = center.dist(oldCenter) + radius;
		radius = r1 > r2 ? r1 : r2;
	}

	/** ************************************************************** */
	/** REMOVE */
	// Remove the given body from the tree.
	void remove(Blob body) {
		// In this case as well, we need to worry about updating the
		// fields of our internal nodes. Let the symbols C, M, R, N, et
		// cetera have the same meaning as they did above (in the
		// description of insert). Then we can define the new values of
		// the fields of our internal nodes as follows.
		//
		// (1) N' = N - 1
		// (2) M' = M - mass(body)
		// (3) C' = (1 / M') * (M * C - mass(body) * position(body))
		// (4) R' = R
		//  
		// Equations (1-3) are the same as before except that '+'s have
		// been replaced by '-'s. Equation (4) says that we are just
		// going to leave the radius unchanged. This will not hurt us,
		// because R' = R will still be an upper bound if it was an upper
		// bound before.

		// YOUR IMPLEMENTATION HERE:
		// ...
		if (numBodies == 1) {
			if (mBody == body) {
				numBodies--;
				mBody = null;
				removeFix(body);
			}
		} else {
			Q[quadrant(body)].remove(body);
			numBodies = 0;
			for (int i = 0; i < 4; i++)
				numBodies += Q[i].numBodies;
			if (numBodies == 1) {
				for (int i = 0; i < 4; i++) {
					if (Q[i].numBodies > 0) {
						mBody = Q[i].mBody;
						Q = null;
						removeFix(body);
						break;
					}
				}
			}
		}
	}

	void removeFix(Blob p) {
		center.mul(mass);
		center.sub(p.loc.times(p.mass));
		mass -= p.mass;
		center.mul(1.0 / mass);
	}

	/** ************************************************************** */
	/** RANGE QUERY */
	// Add to the given set all items which fall in the given range.
	void rangeQuery(ArrayList<Blob> set, BoundRect range) {
		// You will want to use the add function of the PlanetSet class to
		// insert the bodies you find into the set. For example,
		//
		// set.add(b);
		//
		// Will make set contain the body b in addition to the ones
		// it contained before.

		// YOUR IMPLEMENTATION HERE:
		// ...
		if (bounds.intersects(range)) {
			if (numBodies == 1) {
				if (range.contains(mBody.loc)) {
					set.add(mBody);
				}
			} else if (numBodies > 1) {
				for (int i = 0; i < 4; i++) {
					Q[i].rangeQuery(set, range);
				}
			}
		}
	}

	/** ************************************************************** */
	/** COMPUTE ACCELERATION */
	// Calculate the acceleration on the given body due to the gravity
	// of every other body in this quadtree. We may approximate
	// these bodies by their center of mass provided that the error
	// involved is less than max_error.
	void computeAccel(Blob body, Vector2D accel, double maxError) {
		// The algorithm for performing this computation is simply the
		// following. If we are at a leaf node, perform the calculation
		// in the normal way. If we are at an internal node, first find
		// the error that we would endure by approximating all of the
		// bodies in the subtree rooted at this node by their center of
		// mass. If this error is less than max_error, then go ahead and
		// perform the approximate calculation. Otherwise, we must
		// recursively compute the acceleration from each of our subtrees.

		// All of the detailed physics behind this are taken care of by
		// functions in the Simulation class. The following are the ones
		// you will probably need to use.
		//
		// sim.computeAccel: this does normal acceleration
		// computation. It produces the acceleration of one body due to
		// the gravity of another body and adds that acceleration into
		// a vector you pass in.
		//
		// sim.approxError: computes the approximate error in
		// representing a group of bodies by their center of mass for
		// the purposes of calculating the acceleration on some other
		// body.
		//
		// sim.approxAccel: this does approximate acceleration
		// computation. It produces the acceleration of one body due to
		// a group of other bodies whose center of mass and total mass
		// are given. It adds the resulting acceleration into a
		// parameter that you pass in.
		//
		// To see a complete listing of the parameters of these functions,
		// look in Simulation.java.

		// YOUR IMPLEMENTATION HERE:
		// ...
		if (numBodies == 1) {
			if (mBody != body) {
				scene.computeAccel(body, mBody, accel);
			}
		} else if (numBodies > 1) {
			if (!bounds.contains(body.loc) && scene.approxError(body, center, radius) < maxError) {
				scene.approxAccel(body, center, mass, accel);
			} else {
				for (int i = 0; i < 4; i++) {
					Q[i].computeAccel(body, accel, maxError);
				}
			}
		}
	}

	void unmark() {
		marked = false;
		if (numBodies > 1) {
			for (int i = 0; i < 4; i++) {
				Q[i].unmark();
			}
		}
	}

	void mark(Blob body) {
		if (bounds.contains(body.loc) && numBodies >= 1) {
			if (numBodies == 1) {
				if (mBody == body) {
					marked = true;
				}
			} else {
				for (int i = 0; i < 4; i++) {
					Q[i].mark(body);
				}
			}
		}
	}

	// Draw the splitting lines (grid) of this Quadtree.
	void drawGrid() {
		// We want to draw each quadrant of the Quadtree onto the screen.
		// We can use the draw_rectangle function of our simulation object
		// in order to do this. If R is the rectangle that we want to
		// draw,
		//
		// sim.drawRectangle(R);
		//
		// Will cause R to be drawn on the screen. This function will
		// handle all of the details of translating from the coordinate
		// system in our simulation into the coordinate system on the
		// screen.

		// YOUR IMPLEMENTATION HERE:
		// ...
		if (numBodies > 1) {
			for (int i = 0; i < 4; i++) {
				Q[i].drawGrid();
			}
		} else {
			scene.drawRectangle(bounds);
		}
	}

	// Draw the splitting lines (grid) of this Quadtree.
	void drawTree(Graphics gc, int left, int depth, int dx, int dy) {
		if (numBodies >= 1) {
			if (numBodies > 1) {
				int offset = left;
				for (int i = 0; i < 4; i++) {
					if (Q[i].size() >= 1) {
						gc.setColor(Color.white);
						gc.drawLine(left, depth, offset, depth + dy);
						Q[i].drawTree(gc, offset, depth + dy, dx, dy);
						offset += dx * Q[i].size();
					}
				}
			} else {
				int r = (int) (mBody.radius * dx / 20);
				if (r < 4)
					r = 4;
				gc.setColor(/* planet.color */Color.CYAN);
				gc.fillOval(left - r / 2, depth - r / 2, r, r);
				if (marked) {
					gc.setColor(Color.red);
					gc.drawOval(left - dx / 2 - 2, depth - dx / 2 - 2, dx + 4, dx + 4);
				}
			}
		}
	}

	int size() {
		return numBodies;
	}

	int depth() {
		if (numBodies > 1) {
			int max = 0;
			for (int i = 0; i < 4; i++) {
				int d = Q[i].depth();
				if (d > max)
					max = d;
			}
			return max + 1;
		} else {
			return 1;
		}
	}

	// Draw the splitting lines (grid) of this Quadtree.
	void drawGravGrid(Blob body, double maxError) {
		if (numBodies > 1) {
			if (!bounds.contains(body.loc) && scene.approxError(body, center, radius) < maxError) {
				scene.drawRectangle(bounds);
			} else {
				for (int i = 0; i < 4; i++) {
					Q[i].drawGravGrid(body, maxError);
				}
			}
		} else {
			if (numBodies == 1) {
				scene.drawRectangle(bounds);
			}
		}
	}

}
