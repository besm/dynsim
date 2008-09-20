package dynsim.graphics.plot.j3d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import javax.media.j3d.Group;
import javax.media.j3d.Node;

import dynsim.data.DataSlot;
import dynsim.data.DataSlotList;
import dynsim.data.Storage;
import dynsim.graphics.plot.Grapher;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig3D;
import dynsim.graphics.plot.j2d.layer.Marker;
import dynsim.graphics.plot.j3d.axis.AxisBuilder;
import dynsim.graphics.plot.j3d.axis.AxisX;
import dynsim.graphics.plot.j3d.axis.AxisY;
import dynsim.graphics.plot.j3d.axis.AxisZ;
import dynsim.math.analysis.local.CPoint;
import dynsim.simulator.color.ColoringStrategy;

public class Grapher3D extends Base3D implements Grapher {
	private static final long serialVersionUID = 2536339785200187669L;

	private Storage data;

	private Grapher3DBuilder builder;

	private Node plot;

	private AxisBuilder xAxis;

	private AxisBuilder yAxis;

	private AxisBuilder zAxis;

	private String xAxisLabel = "X";

	private String yAxisLabel = "Y";

	private String zAxisLabel = "Z";

	private double xmin;

	private double xmax;

	private double ymin;

	private double ymax;

	private double zmin;

	private double zmax;

	protected int[] coloring;

	protected Color[] colors;

	private GrapherConfig config;

	public Grapher3D() {
		super();
		config = new GrapherConfig3D();
		config.setPlotVarX("x");
		config.setPlotVarY("y");
		config.setPlotVarZ("z");

		this.setForeground(Color.DARK_GRAY);
		this.setBackground(Color.BLACK);
		this.setColors(new Color[] { Color.RED, Color.GREEN });
	}

	public void setData(Storage data) {
		this.data = data;
		if (data.getMin(config.getPlotVarX()) != xmin || data.getMax(config.getPlotVarX()) != xmax) {
			xmin = data.getMin(config.getPlotVarX());
			xmax = data.getMax(config.getPlotVarX());
		}
		if (data.getMin(config.getPlotVarY()) != ymin || data.getMax(config.getPlotVarY()) != ymax) {
			ymin = data.getMin(config.getPlotVarY());
			ymax = data.getMax(config.getPlotVarY());
		}
		if (data.getMin(config.getPlotVarZ()) != zmin || data.getMax(config.getPlotVarZ()) != zmax) {
			zmin = data.getMin(config.getPlotVarZ());
			zmax = data.getMax(config.getPlotVarZ());
		}
	}

	public String getXAxisLabel() {
		return xAxisLabel;
	}

	public void setXAxisLabel(String s) {
		xAxisLabel = s;
		xAxis.setLabel(s);
		xAxis.apply();
	}

	public String getYAxisLabel() {
		return yAxisLabel;
	}

	public void setYAxisLabel(String s) {
		yAxisLabel = s;
		yAxis.setLabel(s);
		yAxis.apply();
	}

	public String getZAxisLabel() {
		return zAxisLabel;
	}

	public void setZAxisLabel(String s) {
		zAxisLabel = s;
		zAxis.setLabel(s);
		zAxis.apply();
	}

	protected Node createPlot() {
		builder = new Grapher3DBuilder(this);
		Group g = new Group();

		if (config.isEnabled(GrapherConfig3D.DRAW_BORDER)) {
			Node box = builder.buildOutsideBox();
			g.addChild(box);
		}

		// Node plane = builder.buildPlane(new Point3f(0.0f, -0.5f, 0.0f), new
		// Point3f(0.0f, 0.5f, 0.0f), new Point3f(0.0f, 0.5f, 0.5f), new
		// Point3f(0.0f, -0.5f, 0.5f));
		//
		// g.addChild(plane);

		plot = builder.buildContent2(data);

		buildAxes(g);

		g.addChild(plot);

		return g;
	}

	private void buildAxes(Group g) {
		if (config.isEnabled(GrapherConfig3D.DRAW_AXIS_X) || config.isEnabled(GrapherConfig3D.DRAW_AXIS_Y)
				|| config.isEnabled(GrapherConfig3D.DRAW_AXIS_Z)) {
			double[] tick = { 0, .1, .2, .3, .4, .5, .6, .7, .8, .9, 1.0 };
			String[] labels = { "0.0", "0.2", "0.4", "0.6", "0.8", "1.0" };

			if (config.isEnabled(GrapherConfig3D.DRAW_AXIS_X)) {
				xAxis = new AxisX(xAxisLabel, labels, tick);
				xAxis.createLabelsNTicks(xmin, xmax);
				xAxis.apply();
				g.addChild(xAxis.getNode());
			}

			if (config.isEnabled(GrapherConfig3D.DRAW_AXIS_Y)) {
				yAxis = new AxisY(yAxisLabel, labels, tick);
				yAxis.createLabelsNTicks(ymin, ymax);
				yAxis.apply();
				g.addChild(yAxis.getNode());
			}

			if (config.isEnabled(GrapherConfig3D.DRAW_AXIS_Z)) {
				zAxis = new AxisZ(zAxisLabel, labels, tick);
				zAxis.createLabelsNTicks(zmin, zmax);
				zAxis.apply();
				g.addChild(zAxis.getNode());
			}
		}
	}

	public void setGrapherConfig(GrapherConfig pconf) {
		this.config = pconf;
	}

	public GrapherConfig getGrapherConfig() {
		return config;
	}

	public void setPlotStyle(int style) {
		config.setPlotStyle(style);
	}

	public void setPlotVarX(String varname) {
		config.setPlotVarX(varname);
	}

	public void setPlotVarY(String varname) {
		config.setPlotVarY(varname);
	}

	public void setPlotVarZ(String varname) {
		config.setPlotVarZ(varname);
	}

	protected Color[] getColors() {
		return colors;
	}

	protected void setColors(Color[] colors) {
		this.colors = colors;
	}

	public boolean hasColorings() {
		return coloring != null;
	}

	public void setMainColoring(int[] coloring) {
		this.coloring = coloring;
	}

	public void addData(Storage data) {
		// TODO Auto-generated method stub

	}

	public void addData(Storage data, ColoringStrategy colors) {
		// TODO Auto-generated method stub

	}

	public void addMarker(CPoint fp) {
		// TODO Auto-generated method stub

	}

	public void addSlot(DataSlot slot) {
		// TODO Auto-generated method stub

	}

	public void checkAxesBounds(double x, double y) {
		// TODO Auto-generated method stub

	}

	public boolean checkBounds(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	public Storage getCurrentData() {
		// TODO Auto-generated method stub
		return null;
	}

	public DataSlotList getDataSlots() {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator<Marker> getMarkersIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public Graphics2D getOffscrGraphics() {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator<DataSlot> getSlotsIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public Color getStrokeColor() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasDataSlots() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasMarkerPoints() {
		// TODO Auto-generated method stub
		return false;
	}
}
