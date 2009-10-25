package dynsim.graphics.plot.j3d;

import javax.media.j3d.*;
import javax.vecmath.*;

/**
 * This is a base class for all PlotBuilders.
 * 
 * Note that all of its build* methods return BranchGraphs which can be added to
 * a 3D display.
 * 
 * @author Joy Kyriakopulos (joyk@fnal.gov)
 * @version $Id$
 */
public abstract class AbstractPlotBuilder {

	public Node buildOutsideBox() {
		// Allocate line array for wire-frame cube - 8 vertices, 24 coordinates
		// (i.e. size of array)
		IndexedLineArray xCube = new IndexedLineArray(8, IndexedLineArray.COORDINATES, 24);

		// Set coordinates for the cube //
		xCube.setCoordinate(0, new Point3d(-0.5, +0.5, 0.0));
		xCube.setCoordinate(1, new Point3d(+0.5, +0.5, 0.0));
		xCube.setCoordinate(2, new Point3d(+0.5, -0.5, 0.0));
		xCube.setCoordinate(3, new Point3d(-0.5, -0.5, 0.0));
		xCube.setCoordinate(4, new Point3d(-0.5, +0.5, 1.0));
		xCube.setCoordinate(5, new Point3d(+0.5, +0.5, 1.0));
		xCube.setCoordinate(6, new Point3d(+0.5, -0.5, 1.0));
		xCube.setCoordinate(7, new Point3d(-0.5, -0.5, 1.0));

		// Construct the vertical //
		xCube.setCoordinateIndex(0, 0);
		xCube.setCoordinateIndex(1, 1);
		xCube.setCoordinateIndex(2, 3);
		xCube.setCoordinateIndex(3, 2);
		xCube.setCoordinateIndex(4, 4);
		xCube.setCoordinateIndex(5, 5);
		xCube.setCoordinateIndex(6, 7);
		xCube.setCoordinateIndex(7, 6);

		// Construct the lower side //
		xCube.setCoordinateIndex(8, 0);
		xCube.setCoordinateIndex(9, 4);
		xCube.setCoordinateIndex(10, 4);
		xCube.setCoordinateIndex(11, 7);
		xCube.setCoordinateIndex(12, 7);
		xCube.setCoordinateIndex(13, 3);
		xCube.setCoordinateIndex(14, 3);
		xCube.setCoordinateIndex(15, 0);

		// Construct the upper side //
		xCube.setCoordinateIndex(16, 1);
		xCube.setCoordinateIndex(17, 5);
		xCube.setCoordinateIndex(18, 5);
		xCube.setCoordinateIndex(19, 6);
		xCube.setCoordinateIndex(20, 6);
		xCube.setCoordinateIndex(21, 2);
		xCube.setCoordinateIndex(22, 2);
		xCube.setCoordinateIndex(23, 1);

		return new Shape3D(xCube);
	}

	public Node buildPlane(Point3f A, Point3f B, Point3f C, Point3f D) {

		QuadArray plane = new QuadArray(4, GeometryArray.COORDINATES | GeometryArray.NORMALS);

		plane.setCoordinate(0, A);
		plane.setCoordinate(1, B);
		plane.setCoordinate(2, C);
		plane.setCoordinate(3, D);

		Vector3f a = new Vector3f(A.x - B.x, A.y - B.y, A.z - B.z);
		Vector3f b = new Vector3f(C.x - B.x, C.y - B.y, C.z - B.z);
		Vector3f n = new Vector3f();
		n.cross(b, a);

		n.normalize();

		plane.setNormal(0, n);
		plane.setNormal(1, n);
		plane.setNormal(2, n);
		plane.setNormal(3, n);

		System.out.println(n);

		return new Shape3D(plane);
	}
}
