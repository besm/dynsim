package dynsim.graphics.plot.j2d;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Iterator;

import dynsim.data.DataSlot;
import dynsim.data.DataSlotList;
import dynsim.data.Storage;
import dynsim.exceptions.DynSimException;
import dynsim.graphics.DrawingSurface;
import dynsim.graphics.ScreenMap;
import dynsim.graphics.plot.Grapher;
import dynsim.graphics.plot.GrapherUtils;
import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.axis.AxesUpdater;
import dynsim.graphics.plot.j2d.axis.Axis;
import dynsim.graphics.plot.j2d.layer.Marker;
import dynsim.graphics.plot.j2d.layer.field.VectField;
import dynsim.graphics.plot.j2d.layer.impl.MarkerLayer;
import dynsim.graphics.plot.j2d.mng.impl.LayerManager;
import dynsim.graphics.plot.spline.SplineFactory;
import dynsim.math.analysis.local.CPoint;
import dynsim.math.util.MathUtils;
import dynsim.simulator.color.ColoringStrategy;

/**
 * @author maf
 * 
 */
public class Grapher2D extends DrawingSurface implements Grapher {
	private static final long serialVersionUID = 2607019252509669696L;

	// TODO type = 2D...
	private Axis xAxis;

	private Axis yAxis;

	protected boolean needRepaint;

	private boolean painting;

	private DataSlotList dataSlots;

	private GrapherConfig config;

	private AxesUpdater axesUpdater;

	protected LayerManager layerMan;

	private MarkerLayer markerLayer;

	// TODO según rango de valores actualizar tickstep
	// auto. a una cantidad suficiente; incluir potencias.
	// subticks...acabar log scale

	public Grapher2D(final int w, final int h) {
		this(w, h, null, null, null);
	}

	public Grapher2D(final int w, final int h, final Axis xa, final Axis ya) {
		this(w, h, xa, ya, null);
	}

	public Grapher2D(final int w, final int h, final Axis xa, final Axis ya, final GrapherConfig conf) {
		super(w, h);

		if (conf != null) {
			this.config = conf;
		} else {
			this.config = new GrapherConfig2D();
		}

		initAxis(xa, ya);

		init();
	}

	public Grapher2D(final int w, final int h, final GrapherConfig conf) {
		this(w, h, null, null, conf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#addData(dynsim.data.Storage)
	 */
	public void addData(final Storage data) {
		dataSlots.addStorage(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#addData(dynsim.data.Storage,
	 * dynsim.simulator.color.ColoringStrategy)
	 */
	public void addData(final Storage data, final ColoringStrategy colors) {
		final DataSlot slot = new DataSlot(data);
		slot.setColoring(colors.getColors(data));
		addSlot(slot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dynsim.graphics.plot.GrapherI#addMarker(dynsim.math.analysis.local.CPoint
	 * )
	 */
	public void addMarker(final CPoint fp) {
		markerLayer.addMarker(fp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#addSlot(dynsim.data.DataSlot)
	 */
	public void addSlot(final DataSlot slot) {
		dataSlots.addDataSlot(slot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#checkAxesBounds(double, double)
	 */
	public void checkAxesBounds(final double x, final double y) {
		axesUpdater.checkAxesBounds(x, y, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#checkBounds(float, float)
	 */
	public boolean checkBounds(final float x, final float y) {
		final Rectangle bounds = new Rectangle(getHalfMW(), getHalfMH(), getWidth() - getHalfMW(), getHeight() - getHalfMH());

		return bounds.contains(x, y);
	}

	public Axis getAxisX() {
		return xAxis;
	}

	public Axis getAxisY() {
		return yAxis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#getCurrentData()
	 */
	public Storage getCurrentData() {
		return dataSlots.getCurrentData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#getDataSlots()
	 */
	public DataSlotList getDataSlots() {
		return dataSlots;
	}

	public AxesUpdater getGrapherAxis() {
		return axesUpdater;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#getGrapherConfig()
	 */
	public GrapherConfig getGrapherConfig() {
		return config;
	}

	public int getHalfMH() {
		return config.getMarginH() >> 1;
	}

	public int getHalfMW() {
		return config.getMarginW() >> 1;
	}

	public int getMarginH() {
		return config.getMarginH();
	}

	public int getMarginW() {
		return config.getMarginW();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#getMarkersIterator()
	 */
	public Iterator<Marker> getMarkersIterator() {
		return markerLayer.getMarkersIterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#getOffscrGraphics()
	 */
	public Graphics2D getOffscrGraphics() {
		return g2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#getSlotsIterator()
	 */
	public Iterator<DataSlot> getSlotsIterator() {
		return dataSlots.dataSlotIterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#getStrokeColor()
	 */
	public Color getStrokeColor() {
		return strokeColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#hasColorings()
	 */
	public boolean hasColorings() {
		return dataSlots.hasColorings();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#hasDataSlots()
	 */
	public boolean hasDataSlots() {
		return dataSlots.hasDataSlots();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dynsim.graphics.plot.GrapherI#hasMarkerPoints()
	 */
	public boolean hasMarkerPoints() {
		return markerLayer.hasMarkerPoints();
	}

	public boolean notInAxisRange(final float x, final float y) {
		return (!yAxis.inAxisRange(y) || !xAxis.inAxisRange(x));
	}

	public void paint(final Graphics g) {
		if (painting) {
			try {
				before();

				layerMan.process(layerMan.newDrawAction());

				after();
			} catch (DynSimException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		super.paint(g);

		if (config.isAutoAdjust()) {
			// ajustado
			config.setAutoAdjust(false);
		}
	}

	public void plot(final double[] x, final double[] y) {
		switch (config.getPlotStyle()) {
		case GrapherConfig.LINES:
			plotLines(x, y);
			break;
		case GrapherConfig.CURVES:
			plotCurves(x, y);
			break;
		default:
			plotPoints(x, y);
			break;
		}
	}

	public void plot(final double[] x, final double[] y, final int[] c) {
		switch (config.getPlotStyle()) {
		case GrapherConfig.LINES:
			plotLines(x, y, c);
			break;
		case GrapherConfig.CURVES:
			plotCurves(x, y, c);
			break;
		default:
			plotPoints(x, y, c);
			break;
		}
	}

	public void plotCurve(double[] control) {
		plotCurve(control, null, -1);
	}

	public void plotCurve(double[] control, int[] c, int basec) {
		int parts = config.getSplineParts();
		double[] pts = SplineFactory.createCatmullRom(control, parts);
		int ci = basec;

		for (int i = 0; i < pts.length - 6; i += 3) {
			if (hasColorings()) {
				if ((i / 3) % parts == 0) {
					ci++;
				}
				Color color = new Color(c[ci]);
				plotLine(pts[i], pts[i + 1], pts[i + 3], pts[i + 4], color);
			} else {
				plotLine(pts[i], pts[i + 1], pts[i + 3], pts[i + 4]);
			}
		}
	}

	public void plotLine(double x1, double y1, double x2, double y2) {
		plotLine(x1, y1, x2, y2, strokeColor);
	}

	public void plotLine(double x1, double y1, double x2, double y2, Color c) {
		float[] lin = preLine(x1, y1, x2, y2);

		strokeColor(c);
		drawLine(lin[0], lin[1], lin[2], lin[3]);
	}

	public void plotLine(double x1, double y1, double x2, double y2, GradientPaint lg) {
		float[] lin = preLine(x1, y1, x2, y2);

		lg = new GradientPaint(lin[0], lin[1], lg.getColor1(), lin[2], lin[3], lg.getColor2(), false);
		strokeGradient(lg);
		drawLine(lin[0], lin[1], lin[2], lin[3]);
		disableGradient();
	}

	public void plotPoint(double x, double y, Color c) {
		plotPoint((float) x, (float) y, c);
	}

	public void plotPoint(float x, float y, Color c) {
		strokeWeight(config.getPointWeight());
		strokeColor(c);

		if (notInAxisRange(x, y)) {
			return;
		}

		x = ScreenMap.toScreenFloat(x, getWidth() - getMarginW(), xAxis.getStart(), xAxis.getEnd());

		// Nota: max. Y es 0 en coordenadas de pantalla => necesario invertirlo
		y = ScreenMap.toScreenFloat(y, getHeight() - getMarginH(), yAxis.getEnd(), yAxis.getStart());

		x += getHalfMW();
		y += getHalfMH();

		if (checkBounds(x, y)) {
			drawPoint(x, y);
		}
	}

	public void repaint() {
		if (isResized()) {
			painting = true;
			checkResize();
		}

		super.repaint();
	}

	public void setAxisX(Axis axis) {
		xAxis = axis;
	}

	public void setAxisY(Axis axis) {
		yAxis = axis;
	}

	public void setDataSlots(final DataSlotList slots) {
		this.dataSlots = slots;
	}

	public void setGrapherConfig(final GrapherConfig pconf) {
		this.config = pconf;
	}

	public void setNeedRepaint(final boolean needRepaint) {
		this.needRepaint = needRepaint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enectic.dynamics.graphics.plot.Plottable#setStrokeColor(java.awt.
	 * Color)
	 */
	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}

	public void setTickStep(float ts) {
		this.xAxis.setTickStep(ts);
		this.yAxis.setTickStep(ts);
	}

	public void setTickStep(float x, float y) {
		this.xAxis.setTickStep(x);
		this.yAxis.setTickStep(y);
	}

	public void setVectField(VectField vectorField) {
		layerMan.addLayer(vectorField);
	}

	protected void after() throws DynSimException {
		afterDraw();

		procStates();
	}

	protected void afterDraw() throws DynSimException {
		// callback method
	}

	protected void before() throws DynSimException {
		update();

		clear();

		beforeDraw(); // Callback
	}

	protected void beforeDraw() throws DynSimException {
		// callback method
	}

	protected void clear() {
		fillRect(0, 0, getWidth(), getHeight(), getBackground());
	}

	protected void init() {
		this.needRepaint = false;
		this.painting = true;
		this.setForeground(Color.DARK_GRAY);
		this.setBackground(Color.BLACK);
		this.setFont(new Font("Arial", Font.PLAIN, 12));

		// Create
		this.dataSlots = new DataSlotList();
		this.axesUpdater = new AxesUpdater();
		this.layerMan = new LayerManager(this);
		this.markerLayer = new MarkerLayer();
		layerMan.addLayer(markerLayer);

		// Add listeners
		addMouseListener(new Grapher2DMouseListener());
	}

	protected void plotCurves(final double[] x, final double[] y) {
		final int controlPointsNum = config.getCurveControlPointsNum();
		double[] ct = new double[controlPointsNum * 3];
		final int ch = x.length / controlPointsNum;

		if (ch > 0) {
			for (int i = 0; i < ch; i++) {
				GrapherUtils.loadPart3Array(i, x, y, i * controlPointsNum, ct);
				plotCurve(ct);
			}
		}

		final int rs = x.length % controlPointsNum;

		if (rs > 0) {
			ct = new double[rs * 3];
			GrapherUtils.loadPart3Array(ch, x, y, (x.length - rs), ct);
			plotCurve(ct);
		}
	}

	protected void plotCurves(final double[] x, final double[] y, final int[] c) {
		final int controlPointsNum = config.getCurveControlPointsNum();
		double[] ct = new double[controlPointsNum * 3];
		final int ch = x.length / controlPointsNum;

		if (ch > 0) {
			for (int i = 0; i < ch; i++) {
				GrapherUtils.loadPart3Array(i, x, y, i * controlPointsNum, ct);
				plotCurve(ct, c, (i * controlPointsNum));
			}
		}

		int rs = x.length % controlPointsNum;

		if (rs > 0) {
			ct = new double[rs * 3];
			GrapherUtils.loadPart3Array(ch, x, y, (x.length - rs), ct);

			plotCurve(ct, c, (x.length - rs));
		}
	}

	protected void plotLines(final double[] x, final double[] y) {
		plotLines(x, y, getStrokeColor());
	}

	// TODO Refactor
	protected void plotLines(final double[] x, final double[] y, final Color c) {
		double lx = x[0];
		double ly = y[0];

		for (int i = 0; i < x.length; i += 2) {
			plotLine(lx, ly, x[i + 1], y[i + 1], c);
			lx = x[i + 1];
			ly = y[i + 1];
		}
	}

	protected void plotLines(final double[] x, final double[] y, final int[] c) {
		double lx = x[0];
		double ly = y[0];

		for (int i = 0; i < x.length; i += 2) {
			plotLine(lx, ly, x[i + 1], y[i + 1], new Color(c[i]));
			lx = x[i + 1];
			ly = y[i + 1];
		}
	}

	protected void plotPoint(final double x, final double y) {
		plotPoint((float) x, (float) y, getStrokeColor());
	}

	protected void plotPoints(final double[] x, final double[] y) {
		plotPoints(x, y, strokeColor);
	}

	protected void plotPoints(final double[] x, final double[] y, final Color c) {
		for (int i = 0; i < x.length; i++) {
			plotPoint(x[i], y[i], c);
		}
	}

	protected void plotPoints(final double[] x, final double[] y, final int[] c) {
		for (int i = 0; i < x.length; i++) {
			plotPoint(x[i], y[i], new Color(c[i]));
		}
	}

	protected void update() {
		updateMargins();

		updateAxes();
	}

	private void initAxis(final Axis xa, final Axis ya) {
		if (xa == null) {
			xAxis = new Axis();
		} else {
			xAxis = xa;
		}
		if (ya == null) {
			yAxis = new Axis();
		} else {
			yAxis = ya;
		}
	}

	private float[] preLine(double x1, double y1, double x2, double y2) {
		int mw = getHalfMW();
		int mh = getHalfMH();

		// TODO esto lo tiene que hacer el axis según tipo?
		if (yAxis.isLog()) {
			y1 = Math.log10(y1);
			y2 = Math.log10(y2);
		}

		x1 = ScreenMap.toScreenFloat(x1, getWidth() - getMarginW(), xAxis.getStart(), xAxis.getEnd());

		x2 = ScreenMap.toScreenFloat(x2, getWidth() - getMarginW(), xAxis.getStart(), xAxis.getEnd());

		y1 = ScreenMap.toScreenFloat(y1, getHeight() - getMarginH(), yAxis.getEnd(), yAxis.getStart());

		y2 = ScreenMap.toScreenFloat(y2, getHeight() - getMarginH(), yAxis.getEnd(), yAxis.getStart());

		y1 += mh;
		y2 += mh;
		x1 += mw;
		x2 += mw;

		return new float[] { trimX(x1), trimY(y1), trimX(x2), trimY(y2) };
	}

	private void procStates() {
		if (needRepaint) {
			repaint();
			needRepaint = false;
		} else {
			painting = false;
		}
	}

	private float trimX(final double x) {
		final int mw = getMarginW() >> 1;

		if (x > getWidth() - mw) {
			return getWidth() - mw;
		}

		if (x < mw) {
			return mw;
		}

		return (float) x;
	}

	private float trimY(final double y) {
		final int mh = getMarginH() >> 1;

		if (y > getHeight() - mh) {
			return getHeight() - mh;
		}

		if (y < mh) {
			return mh;
		}

		return (float) y;
	}

	private void updateAxes() {
		if (config.isAutoAdjust() && dataSlots.hasCurrentSlot()) {
			axesUpdater.update(this);
		}
	}

	private void updateMargins() {
		config.setMarginW((int) MathUtils.percent(new Double(getWidth()).doubleValue(), config.getMarginWPct()));
		config.setMarginH((int) MathUtils.percent(new Double(getHeight()).doubleValue(), config.getMarginHPct()));
	}
}
