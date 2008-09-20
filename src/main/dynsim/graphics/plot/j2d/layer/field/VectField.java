package dynsim.graphics.plot.j2d.layer.field;

import java.awt.Color;
import java.awt.GradientPaint;

import dynsim.graphics.plot.j2d.Grapher2D;
import dynsim.graphics.plot.j2d.layer.GrapherLayer;

public interface VectField extends GrapherLayer {
	public static final int ARROW = 0;

	public static final int LINE = 1;

	public static final int GRAD_LINE = 2;

	public abstract void setIterResolution(double r);

	public abstract void setSpacing(double px);

	public abstract void setStyle(int style);

	public abstract void setColor(Color color);

	public abstract void setGradient(GradientPaint color);

	public abstract double getScale();

	public abstract void setScale(double scale);

	public abstract double[][] getResults();

	public abstract void init(Grapher2D grapher);
}