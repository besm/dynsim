package dynsim.graphics.plot.j2d.layer.field;

import java.awt.Color;
import java.awt.GradientPaint;

import dynsim.graphics.plot.config.GrapherConfig;
import dynsim.graphics.plot.config.GrapherConfig2D;
import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.axis.Axis;
import dynsim.graphics.plot.j2d.layer.AbstractGrapherLayer;
import dynsim.math.util.MathUtils;
import dynsim.math.vector.Vector2D;

public abstract class AbstractVectField extends AbstractGrapherLayer implements VectField {
	/**
         * 
         */
	private static final int RATIO = 3;

	protected double iterResolution;

	private int n;

	protected int nmax, smy, smx;

	protected double[][] pos;

	protected double scale;

	protected double xmin;

	protected double xmax;

	protected double ymax;

	protected double ymin;

	protected int style;

	protected Color lineColor;

	protected GradientPaint lineGradient;

	private double margin;

	// TODO Generalizar a 3D
	// parametros RATIO iterResolution y ¿scale?

	public AbstractVectField() {
		style = LINE;
		iterResolution = 1d / RATIO;
		margin = iterResolution / 10;
		scale = iterResolution / RATIO;
	}

	protected abstract Vector2D func(Vector2D v);

	public void init(Grapher2D g) {
		Axis ax = g.getAxisX();
		Axis ay = g.getAxisY();

		xmin = MathUtils.trunc(ax.getStart(), 0);
		xmax = MathUtils.trunc(ax.getEnd(), 0);
		ymin = MathUtils.trunc(ay.getStart(), 0);
		ymax = MathUtils.trunc(ay.getEnd(), 0);

		smy = (int) MathUtils.trunc((ymax - ymin) / iterResolution, 0);
		smx = (int) MathUtils.trunc((xmax - xmin) / iterResolution, 0);
		nmax = (smy + 1) * (smx + 1);
		pos = new double[4][nmax];
		if (lineColor == null)
			lineColor = g.getForeground();
		n = 0;
	}

	public void compute() {
		double x, y;

		y = ymin;
		for (int i = 0; i <= smy; i++) {
			x = xmin;
			for (int j = 0; j <= smx; j++) {
				procPos(x, y);
				x = x + iterResolution;
			}
			y = y + iterResolution;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enectic.dynamics.simulator.analysis.VectorField#setIterResolution
	 * (double)
	 */
	public void setIterResolution(double r) {
		iterResolution = r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enectic.dynamics.simulator.analysis.VectorField#setSpacing(double)
	 */
	public void setSpacing(double px) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enectic.dynamics.simulator.analysis.VectorField#setStyle(int)
	 */
	public void setStyle(int style) {
		this.style = style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enectic.dynamics.simulator.analysis.VectorField#setColor(java.awt
	 * .Color)
	 */
	public void setColor(Color color) {
		this.lineColor = color;
	}

	public void setGradient(GradientPaint grad) {
		this.lineGradient = grad;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enectic.dynamics.simulator.analysis.VectorField#getScale()
	 */
	public double getScale() {
		return scale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enectic.dynamics.simulator.analysis.VectorField#setScale(double)
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enectic.dynamics.simulator.analysis.VectorField#getResults()
	 */
	public double[][] getResults() {
		return pos;
	}

	// TODO generalizar a vectores m-dim
	protected void procPos(double xc, double yc) {
		Vector2D v, to;
		double step = (iterResolution - margin);
		double ox = xc;
		double oy = yc;
		to = new Vector2D(ox, oy);
		v = func(to);

		// TODO distancia mult step+dist
		v.normalize();

		v.multiply(step);
		to.add(v);

		addPositions(ox, oy, to.getX(), to.getY());
	}

	protected void addPositions(double x1, double y1, double x2, double y2) {
		pos[0][n] = x1;
		pos[1][n] = y1;
		pos[2][n] = x2;
		pos[3][n] = y2;
		n++;
	}

	public void draw(Grapher2D g) {
		GrapherConfig conf = g.getGrapherConfig();
		boolean b = conf.isAutoAdjust();
		conf.setAutoAdjust(false);

		for (int i = 0; i < nmax; i++) {
			switch (style) {
			case VectField.GRAD_LINE:
				g.plotLine(pos[0][i], pos[1][i], pos[2][i], pos[3][i], lineGradient);
				break;
			default:
				g.plotLine(pos[0][i], pos[1][i], pos[2][i], pos[3][i], lineColor);
				if (style == VectField.ARROW) {
					// arrows
					double d1 = (pos[2][i] - pos[0][i]) / 5;
					double d2 = (pos[3][i] - pos[1][i]) / 5;

					g.plotLine(pos[2][i] + d2 - d1, pos[3][i] - d1 - d2, pos[2][i], pos[3][i], lineColor);
					g.plotLine(pos[2][i] - d1 - d2, pos[3][i] + d1 - d2, pos[2][i], pos[3][i], lineColor);
				}
				break;
			}
		}

		conf.setAutoAdjust(b);
	}

	public boolean isDrawable(GrapherConfig config) {
		boolean r = config.isEnabled(GrapherConfig2D.DRAW_VECTFIELD);
		return r;
	}
}
