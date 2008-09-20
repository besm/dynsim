package dynsim.graphics.plot.j3d;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.LineArray;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PointArray;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import dynsim.data.Storage;
import dynsim.graphics.plot.config.GrapherConfig;

public class Grapher3DBuilder extends AbstractPlotBuilder {
	private Shape3D shape;
	private Grapher3D graph;

	public Grapher3DBuilder(Grapher3D graph) {
		this.graph = graph;
	}

	public Node buildContent(Storage data) {
		shape = createShape();
		shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);

		GrapherConfig config = graph.getGrapherConfig();

		Color c = graph.colors[0];

		String xs = config.getPlotVarX();
		String ys = config.getPlotVarY();
		String zs = config.getPlotVarZ();

		PointArray pa = new PointArray(data.getRowsNum(), PointArray.COORDINATES | PointArray.COLOR_3);
		double x, y, z;
		double ratiox = 1d / (data.getMax(xs) - data.getMin(xs));
		double ratioy = 1d / (data.getMax(ys) - data.getMin(ys));
		double ratioz = 1d / (data.getMax(zs) - data.getMin(zs));

		for (int i = 0; i < data.getRowsNum(); i++) {
			x = ratiox * (data.get(xs, i) - data.getMax(xs)) + 0.5d;
			y = ratioy * (data.get(ys, i) - data.getMax(ys)) + 0.5d;
			z = ratioz * (data.get(zs, i) - data.getMax(zs)) + 1d;
			Vector3d v = new Vector3d(x, y, z);
			pa.setCoordinate(i, new Point3d(v));

			if (graph.hasColorings()) {
				c = new Color(graph.coloring[i]);
			}

			pa.setColor(i, new Color3f(c));
		}

		shape.setGeometry(pa);

		return shape;
	}

	public void updatePlot(Storage data) {
		shape.setGeometry(buildGeometry(data));
	}

	// TODO esto será LineArrayStrip !!
	public Node buildContent2(Storage data) {
		shape = createShape();
		shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);

		GrapherConfig config = graph.getGrapherConfig();

		Color c = graph.colors[0];

		String xs = config.getPlotVarX();
		String ys = config.getPlotVarY();
		String zs = config.getPlotVarZ();

		LineArray qa = new LineArray(data.getRowsNum() * 2, LineArray.COORDINATES | LineArray.COLOR_3);
		double x, y, z;
		double ratiox = 1d / (data.getMax(xs) - data.getMin(xs));
		double ratioy = 1d / (data.getMax(ys) - data.getMin(ys));
		double ratioz = 1d / (data.getMax(zs) - data.getMin(zs));

		int j = 0;

		for (int i = 0; i < data.getRowsNum() - 1; i++) {
			x = ratiox * (data.get(xs, i) - data.getMax(xs)) + 0.5d;
			y = ratioy * (data.get(ys, i) - data.getMax(ys)) + 0.5d;
			z = ratioz * (data.get(zs, i) - data.getMax(zs)) + 1d;

			Vector3d v = new Vector3d(x, y, z);
			qa.setCoordinate(j, new Point3d(v));

			if (graph.hasColorings()) {
				c = new Color(graph.coloring[i]);
			}

			qa.setColor(j, new Color3f(c));

			j++;

			x = ratiox * (data.get(xs, i + 1) - data.getMax(xs)) + 0.5d;
			y = ratioy * (data.get(ys, i + 1) - data.getMax(ys)) + 0.5d;
			z = ratioz * (data.get(zs, i + 1) - data.getMax(zs)) + 1d;

			v = new Vector3d(x, y, z);
			qa.setCoordinate(j, new Point3d(v));

			if (graph.hasColorings()) {
				c = new Color(graph.coloring[i + 1]);
			}

			qa.setColor(j, new Color3f(c));

			j++;
		}

		shape.setGeometry(qa);

		// Appearance app = new Appearance();
		// RenderingAttributes ra = new RenderingAttributes();
		// ra.setDepthBufferEnable(true);
		// app.setRenderingAttributes(ra);
		// ColoringAttributes ca = new ColoringAttributes();
		// ca.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
		// app.setColoringAttributes(ca);
		// TransparencyAttributes ta = new TransparencyAttributes();
		// ta.setTransparency(1.25f);
		// app.setTransparencyAttributes(ta);
		// LineAttributes lat = new LineAttributes();
		// lat.setLineAntialiasingEnable(true);
		// lat.setLineWidth(10f);
		// app.setLineAttributes(lat);
		// shape.setAppearance(app);

		return shape;
	}

	private Geometry buildGeometry(Storage data) {
		return null;
		//
		// // We make a GeometryInfo object so that normals can be generated
		// // for us and geometry stripified for us. J3D documentation
		// // says it's best to do these two steps in this order.
		//
		// GeometryInfo geom = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
		//
		// // geom.setNormals(normals);
		// geom.setCoordinates(bcoord);
		// geom.setColors(bcolor);
		// NormalGenerator ng = new NormalGenerator();
		// ng.generateNormals(geom);
		//
		// // Make normals conform to our "handedness" of z direction
		// // i.e. change their sign
		// Vector3f normals[] = geom.getNormals();
		// for (i = 0; i < normals.length; ++i) {
		// normals[i].x = -normals[i].x;
		// normals[i].y = -normals[i].y;
		// normals[i].z = -normals[i].z;
		// }
		// geom.setNormals(normals);
		//
		// Stripifier st = new Stripifier();
		// st.stripify(geom);
		// geom.recomputeIndices();
		//
		// System.out.print("Surface geometry done.\n");
		// timeStamp.print("finished, now finalizing, point count = " + bcur);
		// return geom.getGeometryArray();
	}

	Shape3D createShape() {
		Shape3D surface = new Shape3D();
		surface.setAppearance(createMaterialAppearance());

		return surface;
	}

	private Appearance createMaterialAppearance() {
		Appearance materialAppear = new Appearance();
		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
		materialAppear.setPolygonAttributes(polyAttrib);

		Material material = new Material();
		// set diffuse color to red (this color will only be used
		// if lighting disabled - per-vertex color overrides)
		material.setDiffuseColor(new Color3f(1.0f, 0.0f, 0.0f));
		materialAppear.setMaterial(material);

		return materialAppear;
	}
}
